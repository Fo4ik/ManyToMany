package dmartynov.com;

import dmartynov.com.dao.PostgreStorage;
import dmartynov.com.model.Groups;
import dmartynov.com.model.Student;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, SQLException {


        Groups java = new Groups("Java", "25.04.19");
        Groups javaScript = new Groups("JavaScript", "21.04.19");

        Student javaStudent1 = new Student(25, "Vanya");
        java.getStudents().add(javaStudent1);

        Student javaStudent2 = new Student(20, "Sanya");
        java.getStudents().add(javaStudent2);

        Student jsStudent1 = new Student(27, "Dima");
        javaScript.getStudents().add(jsStudent1);

        Student jsStudent2 = new Student(19, "Snegana");
        javaScript.getStudents().add(jsStudent2);

        Student max = new Student(29, "Max");
        java.getStudents().add(max);
        javaScript.getStudents().add(max);

        PostgreStorage storageDao = new PostgreStorage();
        storageDao.removeAll();
            storageDao.insertGroup(java);
            storageDao.insertGroup(javaScript);
            List<Student> students = storageDao.getAllStudent();
        List<Student> usersFromGroup = storageDao.getUsersByGroupName("Java");
        System.out.println(usersFromGroup);
        List<Groups> groupsByStudentName = storageDao.getGroupsByUserName("Max");
        System.out.println(groupsByStudentName);
        System.out.println(students);


    }
}
