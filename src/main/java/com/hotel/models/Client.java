package com.hotel.models;

import javax.persistence.*;

@Entity
@Table(name = "client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;
    public Integer credit;

//    @ManyToMany
//    @JoinTable(
//            name = "client_on_ticket",
//            joinColumns = {@JoinColumn(name = "client_id")},
//            inverseJoinColumns = { @JoinColumn(name = "ticket_id") }
//    )
//    public Set<Ticket> tickets;
}
