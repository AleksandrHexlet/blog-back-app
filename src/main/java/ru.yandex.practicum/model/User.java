package ru.yandex.practicum.model;

import lombok.Setter;

@Setter
public class User {

    private Long id;
    private String firstName;
    private String lastName;
    private int age;
    private boolean active;


    public User() {}

    public User(Long id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(String firstName, String lastName, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    public User(String firstName, String lastName, int age, boolean active) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.active = active;
    }

    public User(Long id, String firstName, String lastName,
                int age, boolean active) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.active = active;
    }
    public Long getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public int getAge() { return age; }
    public boolean isActive() { return active; }

}