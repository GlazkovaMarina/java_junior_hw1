package ru.gb.lesson3;

public class Student{
    private long id;
    private String firstName;
    private String secondName;
    private long age;

    public Student(long id, String firstName, String secondName, long age) {
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", age=" + age +
                '}';
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public long getAge() {
        return age;
    }
}
