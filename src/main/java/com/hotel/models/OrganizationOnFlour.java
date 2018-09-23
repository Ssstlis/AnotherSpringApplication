package com.hotel.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import javax.persistence.*;

@Entity
@Table(name = "organization_on_flour")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName("organization_on_flour")
public class OrganizationOnFlour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;
    public Float multipler;

    @Column(name = "organization_id")
    @JsonProperty("organization_id")
    public Integer organizationId;

    @Column(name = "flour_id")
    @JsonProperty("flour_id")
    public Integer flourId;
}
