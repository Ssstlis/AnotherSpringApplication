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

//    @OneToMany(fetch = FetchType.LAZY)
//    @JoinColumn(name = "building_id")
//    public Set<Floor> flours;

    @Transient
    public Long floors;
}
