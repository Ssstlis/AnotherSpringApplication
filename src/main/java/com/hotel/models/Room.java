package com.hotel.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;

import javax.persistence.*;

@Entity
@Table(name = "room")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName("room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;
    public Integer capacity;
    public Integer price;

//    @ManyToOne
//    @JoinColumn(name = "id")
//    public Floor flour;

//    @OneToMany
//    @JoinColumn(name = "room_id")
//    public Set<Ticket> tickets;
}