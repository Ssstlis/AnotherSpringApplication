package com.hotel.services;

import com.hotel.daos.OrganizationOnFloorDAO;
import com.hotel.models.OrganizationOnFloor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrganizationOnFloorService {
    private final OrganizationOnFloorDAO organizationOnFloorDAO;

    @Autowired
    public OrganizationOnFloorService(OrganizationOnFloorDAO organizationOnFloorDAO) {
        this.organizationOnFloorDAO = organizationOnFloorDAO;
    }

    public List<Integer> findByOrganizationIdIsNot(Integer organizationId) {
        return organizationOnFloorDAO.findDistinctByOrganizationIdIsNot(organizationId)
                .stream()
                .map(o -> o.floorId)
                .distinct()
                .collect(Collectors.toList());
    }

    public void save(OrganizationOnFloor organizationOnFloor) {
        organizationOnFloorDAO.save(organizationOnFloor);
    }

    public void saveAll(Iterable<OrganizationOnFloor> organizationOnFloors) {
        organizationOnFloorDAO.saveAll(organizationOnFloors);
    }

    public List<OrganizationOnFloor> findAllBydOrganizationId(Integer organizationId) {
        return organizationOnFloorDAO.findAllByOrganizationId(organizationId);
    }
}