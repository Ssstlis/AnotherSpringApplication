package com.hotel.services;

import com.hotel.daos.BuildingDAO;
import com.hotel.models.Building;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BuildingService {
    private final BuildingDAO buildingDAO;
    private final FloorService floorService;

    @Autowired
    public BuildingService(BuildingDAO buildingDAO, FloorService floorService) {
        this.buildingDAO = buildingDAO;
        this.floorService = floorService;
    }

    public List<Building> all() {
        return buildingDAO.findAll();
    }

    public List<Building> buildingsWithFloorCount() {
        Map<Integer, Long> floursByBuilding = floorService.all()
                .stream()
                .collect(Collectors.groupingBy(f -> f.buildingId, Collectors.counting()));
        return buildingDAO.findAll()
                .stream()
                .peek(b -> b.floors = floursByBuilding.getOrDefault(b.id, 0L))
                .sorted(Comparator.comparingInt(b -> b.id))
                .collect(Collectors.toList());
//                .collect(Collectors.toMap(b -> b, b -> floursByBuilding.getOrDefault(b.id, 0L)));
    }

    public Building save(Building building) {
        return buildingDAO.save(building);
    }
}