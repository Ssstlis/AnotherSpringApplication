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

    public void saveAll(Iterable<Floor> floors) {
        floorDAO.saveAll(floors);
    }

    public List<Floor> findByIdsNotIn(List<Integer> ids) {
        return floorDAO.findByIdNotIn(ids);
    }

    public Floor save(Floor floor) { return floorDAO.save(floor); }
}