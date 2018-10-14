package com.hotel.services;

import com.hotel.daos.ClientOnTicketDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientOnTicketService {
    private final ClientOnTicketDAO clientOnTicketDAO;

    @Autowired
    public ClientOnTicketService(ClientOnTicketDAO clientOnTicketDAO) {
        this.clientOnTicketDAO = clientOnTicketDAO;
    }
}