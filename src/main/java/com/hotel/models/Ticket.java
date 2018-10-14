package com.hotel.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.joda.time.DateTime;

import javax.persistence.*;

import static java.lang.Math.toIntExact;

@Entity
@Table(name = "ticket")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName("ticket")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;
    @Column(name = "timestamp_buy")
    public Integer timestampBuy;
    @Column(name = "timestamp_start")
    public Integer timestampStart;
    @Column(name = "timestamp_end")
    public Integer timestampEnd;
    public Integer price;

    @Transient
    public DateTime dateBuy;

    @Transient
    public DateTime dateStart;

    @Transient
    public DateTime dateEnd;

    @Column(name = "user_id")
    public Integer userId;

    @Column(name = "room_id")
    public Integer roomId;

    public void initTime() {
        dateBuy = new DateTime(timestampBuy);
        dateEnd = new DateTime(timestampEnd);
        dateStart =  new DateTime(timestampStart);
    }

    public void initTime(DateTime startTime, DateTime endTime) {
        timestampBuy = toIntExact(DateTime.now().getMillis());
        timestampStart = toIntExact(startTime.getMillis());
        timestampEnd = toIntExact(endTime.getMillis());
    }
}