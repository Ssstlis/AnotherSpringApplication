package com.hotel.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import javax.persistence.*;

@Entity
@Table(name = "floor")
@JsonRootName("floor")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Floor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @Column(name = "building_id")
    @JsonProperty("building_id")
    public Integer buildingId;

    @Transient
    public Integer oneRooms;

    @Transient
    public Integer twoRooms;

    @Transient
    public Integer threeRooms;

}