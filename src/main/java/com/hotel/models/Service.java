package com.hotel.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import javax.persistence.*;

@Entity
@Table(name = "service")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName("service")
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;
    public String description;

    @Column(name = "basic_price")
    @JsonProperty("basic_price")
    public Integer basicPrice;

//    @OneToMany
//    @JoinColumn(name = "service_id")
//    public Set<Organization> organizations;
}