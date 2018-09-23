package com.hotel.models;

import javax.persistence.*;
import java.util.Set;

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

//    @OneToMany(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    public Set<Ticket> tickets;
//
//    @OneToOne
//    @JoinColumn(name = "organization_id")
//    public Organization organization;

    public User() {}
}
