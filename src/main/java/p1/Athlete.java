package p1;

import java.util.List;

public class Athlete{
    private int id;
    private String name;
    private String nationality;
    private int age;
    private String gender;
    private List<SportEvent> events;
    public Athlete(int id,String name ,String nationality,int age ,String gender,List<SportEvent> events){
        this.id = id;
        this.name = name;
        this.nationality = nationality;
        this.age=age;
        this.gender=gender;
        this.events = events;
    }
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNationality() {
        return nationality;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public List<SportEvent> getEvents() {
        return events;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setEvents(List<SportEvent> events) {
        this.events = events;
    }
}
