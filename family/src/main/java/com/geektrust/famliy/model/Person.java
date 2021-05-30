package com.geektrust.famliy.model;

import lombok.Data;

@Data
public class Person {
    private boolean child;
    private String name;
    private Gender gender;

    public Person(String name, Gender gender) {
        this.name = name;
        this.gender = gender;
        this.child = false;
    }

    public Person(String name, Gender gender, Boolean child) {
        this.name = name;
        this.gender = gender;
        this.child = child;
    }
}

