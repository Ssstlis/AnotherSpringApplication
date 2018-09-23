package com.hotel.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "ticket")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName("ticket")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;
    @Column(name = "timestamp_buy")
    public Long timestampBuy;
    @Column(name = "timestamp_start")
    public Long timestampStart;
    @Column(name = "timestamp_end")
    public Long timestampEnd;
    public Long price;

    @Transient
    public DateTime dateBuy;

    @Transient
    public DateTime dateStart;

    @Transient
    public DateTime dateEnd;

    @Column(name = "user_id")
    public Integer userId;

    @Column(name = "ticket_id")
    public Integer ticketId;

//    @ManyToOne
//    @JoinColumn(name = "id")
//    public User user;

//    @OneToMany
//    @JoinColumn(name = "ticket_id")
//    public Set<ClientOnTicket> clientsOnTicket;

    @ManyToMany
    @JoinTable(
            name = "client_on_ticket",
            joinColumns = {@JoinColumn(name = "ticket_id")},
            inverseJoinColumns = { @JoinColumn(name = "client_id") }
    )
    public Set<Client> clients;

    public void initTime() {
        dateBuy = new DateTime(timestampBuy);
        dateEnd = new DateTime(timestampEnd);
        dateStart =  new DateTime(timestampStart);
    }

    public void initTime(DateTime startTime, DateTime endTime) {
        timestampBuy = DateTime.now().getMillis();
        timestampStart = DateTime.now().getMillis();
        timestampEnd = DateTime.now().getMillis();
    }
}