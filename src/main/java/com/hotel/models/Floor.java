package com.hotel.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "flour")
@JsonRootName("flour")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Flour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @Column(name = "building_id")
    @JsonProperty("building_id")
    public Integer buildingId;

    @OneToOne
    @JoinColumn(name = "id")
    public Building building;

//    @OneToMany
//    @JoinColumn(name = "flour_id")
//    public Set<OrganizationOnFlour> organizationsOnFlour;

//    @OneToMany
//    @JoinColumn(name = "flour_id")
//    public Set<Room> rooms;
//
//    @ManyToMany
//    @JoinTable(
//            name = "organization_on_flour",
//            joinColumns = { @JoinColumn(name = "flour_id") },
//            inverseJoinColumns = { @JoinColumn(name = "organization_id") }
//    )
//    public Set<Organization> organizations;
}