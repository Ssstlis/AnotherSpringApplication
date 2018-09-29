package com.hotel.daos;

import com.hotel.models.ClientOnTicket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientOnTicketDAO extends JpaRepository<ClientOnTicket, Integer> {
}
