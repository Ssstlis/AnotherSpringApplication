package com.hotel.services;

import com.hotel.daos.OrganizationOnFloorDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrganizationOnFloorService {
    private final OrganizationOnFloorDAO organizationOnFloorDAO;

    @Autowired
    public OrganizationOnFloorService(OrganizationOnFloorDAO organizationOnFloorDAO) {
        this.organizationOnFloorDAO = organizationOnFloorDAO;
    }
}