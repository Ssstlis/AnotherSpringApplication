package com.hotel.services;

import com.hotel.daos.BuildingDAO;
import com.hotel.models.Building;
import com.hotel.models.Floor;
import com.hotel.utils.Container;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BuildingService {
    private final BuildingDAO buildingDAO;
    private final FloorService floorService;
    private final RoomService roomService;

    @Autowired
    public BuildingService(BuildingDAO buildingDAO, FloorService floorService, RoomService roomService) {
        this.buildingDAO = buildingDAO;
        this.floorService = floorService;
        this.roomService = roomService;
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
    }

    public Building save(Building building) {
        return buildingDAO.save(building);
    }

    public Map<Building, List<Floor>> unavailableBuildings(List<Floor> floors) {
        Map<Integer, List<Floor>> map = floors.stream().collect(Collectors.groupingBy(f -> f.buildingId));
        Set<Integer> ids = map.keySet();
        Map<Integer, Building> buildingMap = buildingDAO.findAllByIdIn(ids).stream().collect(Collectors.toMap(b -> b.id, b -> b));
        return map.entrySet()
                .stream()
                .collect(Collectors.toMap(entry -> buildingMap.get(entry.getKey()), Map.Entry::getValue));
    }

    public List<Building> a() {
        Map<Integer, List<Floor>> map = roomService.allAsFloors();

        return buildingDAO.findAll()
                .stream()
                .peek(b -> {
                            final Container<Integer> fl = new Container<>(1);
                            b.Floors = map.getOrDefault(b.id, new ArrayList<>())
                                    .stream()
                                    .sorted(Comparator.comparingInt(floor -> floor.id))
                                    .peek(f -> {
                                        f.id = fl.getVariable();
                                        fl.setVariable(fl.getVariable() + 1);
                                    })
                                    .collect(Collectors.toList());
                        }
                )
                .sorted(Comparator.comparingInt(b -> b.id))
                .collect(Collectors.toList());
    }
}