package dmartynov.com.dao;

import dmartynov.com.model.Groups;
import dmartynov.com.model.Student;
import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgreStorage {
    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Connection connection;
    private int id = 1;
    private int groupId = 1;
    private int addId = 1;


    public PostgreStorage() throws SQLException, IOException {
        FileInputStream inputStream = new FileInputStream("password.txt");
        String everything = IOUtils.toString(inputStream);
        connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/test", "postgres", everything);
        maybeCreateUsersTable();
        maybeCreateUsersGroupsTable();
        maybeCreateGroupsTable();
    }

    private void maybeCreateUsersTable() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS groups (\n" +
                    "_id int PRIMARY KEY,\n" +
                    "name varchar(100)\n" +
                    ");");
        }
    }

    private void maybeCreateUsersGroupsTable() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS groups_students (\n" +
                    "count int PRIMARY KEY,\n" +
                    "student_id int ,\n" +
                    "group_id int \n" +
                    ");");
        }
    }

    private void maybeCreateGroupsTable() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS students (\n" +
                    "_id SERIAL PRIMARY KEY,\n" +
                    "name varchar(100),\n" +
                    "age int\n" +
                    ");");
        }
    }


    public void removeAll() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM students;");
            statement.executeUpdate("DELETE FROM groups;");
            statement.executeUpdate("DELETE FROM groups_students;");


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void removeStudent(int id) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM students WHERE _id = '" + id + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void removeStudentByName(String name) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM students WHERE name = '" + name + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void insertGroup(Groups group) throws SQLException {
        group.setId(groupId++);
        try (Statement statement = connection.createStatement()) {
            String request = String.format("INSERT INTO groups VALUES ('%s', '%s');", group.getId(), group.getName());
            statement.execute(request);
            for (Student student : group.getStudents()) {
                insertStudent(student);
                insertStudentGroups(student, group.getId());
            }
        }
    }


    private void insertStudent(Student student) throws SQLException {
        student.setId(addId++);
        try (Statement statement = connection.createStatement()) {
            String request1 = String.format("INSERT INTO students VALUES ('%s', '%s', '%d');", student.getId(), student.getName(), student.getAge());
            statement.execute(request1);
        }
    }


    private void insertStudentGroups(Student student, int groupId) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String request = String.format("INSERT INTO groups_students VALUES ('%s', '%s', '%s');", id, student.getId(), groupId);
            statement.execute(request);
            id++;
        }
    }

    public void updateStudent(Student student) {
        try (Statement statement = connection.createStatement()) {
            String request = String.format("UPDATE students SET name = '%s', age = '%d' WHERE _id = '%d'", student.getName(), student.getAge(), student.getId());
            statement.executeUpdate(request);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Student getStudent(int id) {
        try (Statement statement = connection.createStatement()) {
            String request = String.format("SELECT * FROM students WHERE _id = '%d';", id);
            ResultSet resultSet = statement.executeQuery(request);
            if (resultSet.next()) {
                int studentId = resultSet.getInt("_id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                return new Student(studentId, name, age);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public List< Student > getAllStudent() {
        List< Student > students = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            String request = "SELECT * FROM students";
            ResultSet resultSet = statement.executeQuery(request);
            while (resultSet.next()) {
                int userId = resultSet.getInt("_id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                students.add(new Student(userId, name, age));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }


    public List< Student > getUsersByGroupName(String groupName) throws SQLException {
        List< Student > students = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            String request = String.format("SELECT students._id, students.name,students.age " +
                    "FROM students, groups, groups_students WHERE groups.name = '%s' " +
                    "AND groups._id = groups_students.group_id " +
                    "AND groups_students.student_id = students._id;", groupName);
            ResultSet resultSet = statement.executeQuery(request);
            while (resultSet.next()) {
                int id = resultSet.getInt("_id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                students.add(new Student(id, name, age));
            }
        }
        return students;
    }

    public List< Groups > getGroupsByUserName(String userName) throws SQLException {
        List< Groups > groups = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            String request = String.format("SELECT groups._id, groups.name " +
                    "FROM students, groups, groups_students WHERE students.name = '%s' " +
                    "AND students._id = groups_students.student_id " +
                    "AND groups_students.group_id = groups._id;", userName);
            ResultSet resultSet = statement.executeQuery(request);
            while (resultSet.next()) {
                int id = resultSet.getInt("_id");
                String name = resultSet.getString("name");
                groups.add(new Groups(id, name));
            }
        }
        return groups;
    }
}

