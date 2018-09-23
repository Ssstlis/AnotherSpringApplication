package com.hotel.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonValue;

import javax.persistence.*;
import java.util.Set;

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
//    public Flour flour;

    @OneToMany
    @JoinColumn(name = "room_id")
    public Set<Ticket> tickets;
}