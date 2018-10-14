package com.hotel.services;

import com.hotel.daos.OrganizationDAO;
import com.hotel.models.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizationService {
    private final OrganizationDAO organizationDAO;

    @Autowired
    public OrganizationService(OrganizationDAO organizationDAO) {
        this.organizationDAO = organizationDAO;
    }

    public List<Organization> all() {
        return organizationDAO.findAll();
    }
}