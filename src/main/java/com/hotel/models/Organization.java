package com.hotel.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import javax.persistence.*;

@Entity
@Table(name = "organization")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName("organization")
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @Column(name = "service_id")
    @JsonProperty("service_id")
    public Integer serviceId;

    @Column(name = "user_id")
    @JsonProperty("user_id")
    public Integer userId;

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "id")
//    public User user;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "id")
//    public Service service;

    //    @OneToMany
//    @JoinColumn(name = "organization_id")
//    public Set<OrganizationOnFloor> organizationOnFlours;
//    @ManyToMany
//    @JoinTable(
//            name = "organization_on_flour",
//            joinColumns = {@JoinColumn(name = "organization_id")},
//            inverseJoinColumns = {@JoinColumn(name = "flour_id")}
//    )
//    public Set<Floor> organizations;
}
