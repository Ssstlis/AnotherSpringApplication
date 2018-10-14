package com.hotel.services;

import com.hotel.daos.ServiceDAO;
import com.hotel.models.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@org.springframework.stereotype.Service
public class ServiceService {
    private final ServiceDAO serviceDAO;

    @Autowired
    public ServiceService(ServiceDAO serviceDAO) {
        this.serviceDAO = serviceDAO;
    }

    public List<Service> all() {
        return serviceDAO.findAll();
    }

    public Service save(Service service) {
        return serviceDAO.save(service);
    }
}