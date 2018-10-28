package com.hotel.services;

import com.hotel.daos.RoomDAO;
import com.hotel.models.Floor;
import com.hotel.models.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RoomService {
    private final RoomDAO roomDAO;
    private final FloorService floorService;

    @Autowired
    public RoomService(RoomDAO roomDAO, FloorService floorService) {
        this.roomDAO = roomDAO;
        this.floorService = floorService;
    }

    public void saveAll(Iterable<Room> rooms) { roomDAO.saveAll(rooms); }

    public List<Room> findByFloorId(Integer id) { return roomDAO.findAllByFloorId(id); }

    public Map<Integer, List<Floor>> allAsFloors() {
        Map<Integer, Floor> floorMap = floorService.all()
                .stream()
                .collect(Collectors.toMap(f -> f.id, f -> f));

        return roomDAO.findAll()
                .stream()
                .collect(Collectors.groupingBy(r -> r.floorId))
                .entrySet()
                .stream()
                .map(entry -> {
                    Integer floorId = entry.getKey();
                    List<Room> rooms = entry.getValue();
                    Floor floor = floorMap.get(floorId);
                    floor.oneRooms = (int) rooms.stream().filter(r -> r.capacity == 1).count();
                    floor.twoRooms = (int) rooms.stream().filter(r -> r.capacity == 2).count();
                    floor.threeRooms = (int) rooms.stream().filter(r -> r.capacity == 3).count();
                    return floor;
                }).collect(Collectors.groupingBy(f -> f.buildingId));
    }
}