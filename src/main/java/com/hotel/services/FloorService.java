package com.hotel.services;

import com.hotel.daos.FloorDAO;
import com.hotel.models.Floor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FloorService {
    private final FloorDAO floorDAO;

    @Autowired
    public FloorService(FloorDAO floorDAO) {
        this.floorDAO = floorDAO;
    }

    public List<Floor> all() {
        return floorDAO.findAll();
    }

    public List<Floor> saveAll(Iterable<Floor> floors) {
        return floorDAO.saveAll(floors);
    }
}