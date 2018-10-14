package com.hotel.models;

import javax.persistence.*;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @Column(name = "first_name")
    public String firstName;
    @Column(name = "last_name")
    public String lastName;
    public String login;
    public String password;
    public Integer privilegies;
    public Integer activity;

    public User() {
    }
}
