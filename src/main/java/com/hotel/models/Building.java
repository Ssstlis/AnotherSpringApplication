package com.hotel.models;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "building")
public class Building {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    public String description;

    public Integer stars;

    @OneToMany
    @JoinColumn(name = "building_id")
    public Set<Flour> flours;
}
