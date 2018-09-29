package com.hotel.daos;

import com.hotel.models.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketDAO extends JpaRepository<Ticket, Integer> {
}
