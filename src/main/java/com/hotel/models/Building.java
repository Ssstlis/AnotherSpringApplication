package com.hotel.models;

import javax.persistence.*;

@Entity
@Table(name = "building")
public class Building {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    public String description;

    public Integer stars;

    @Transient
    public Long floors;
}
