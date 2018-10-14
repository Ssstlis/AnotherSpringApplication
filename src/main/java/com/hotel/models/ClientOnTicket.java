package com.hotel.models;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import javax.persistence.*;

@Entity
@Table(name = "client_on_ticket")
@JsonRootName("client_on_ticket")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClientOnTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @Column(name = "ticket_id")
    @JsonProperty("ticket_id")
    public Integer ticketId;

    @Column(name = "client_id")
    @JsonProperty("client_id")
    public Integer clientId;

//    @OneToMany()
//    @JoinColumn(name = "id")
//    public Set<Client> clients;
//
//    @ManyToOne()
//    @JoinColumn(name = "id")
//    public Ticket ticket;
}