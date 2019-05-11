package dmartynov.com.model;

import java.util.ArrayList;
import java.util.List;

public class Groups {
    private int id;
    private String name;
    private String startDate;
    private List< Student > students = new ArrayList<>();

    public Groups(String name, String startDate) {
        this.name = name;
        this.startDate = startDate;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStartDate() {
        return startDate;
    }

    public List< Student > getStudents() {
        return students;
    }

    public Groups(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Groups(int id, String name, String startDate) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Groups(int id, String name, String startDate, List< Student > students) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.students = students;
    }

    @Override
    public String toString() {
        return "Groups{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
