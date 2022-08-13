package com.example.es.model;


import lombok.Data;

@Data
public class User {

    private String name;
    private int age;

    public User(String name, int age) {
        this.age = age;
        this.name = name;
    }
}
