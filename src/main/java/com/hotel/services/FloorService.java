package com.hotel.services;

import com.hotel.daos.FloorDAO;
import com.hotel.daos.RoomDAO;
import com.hotel.models.Floor;
import com.hotel.models.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FloorService {
    private final FloorDAO floorDAO;
    private final RoomDAO roomDAO;

    @Autowired
    public FloorService(FloorDAO floorDAO, RoomDAO roomDAO) {
        this.floorDAO = floorDAO;
        this.roomDAO = roomDAO;
    }

    public List<Floor> all() {
        return floorDAO.findAll();
    }

    public void saveAll(Iterable<Floor> floors) {
        floorDAO.saveAll(floors);
    }

    public List<Floor> findByIdsNotIn(List<Integer> ids) {
        if (ids.isEmpty()) {
            return all();
        } else {
            return floorDAO.findByIdNotIn(ids);
        }
    }

    public Floor save(Floor floor) { return floorDAO.save(floor); }

    public List<Floor> findAllByIds(List<Integer> ids) {
        Map<Integer, List<Room>> roomsByFloorId = roomDAO.findAllByFloorIdIn(ids)
                .stream()
                .collect(Collectors.groupingBy(r -> r.floorId));

        Map<Integer, Floor> byFloorIds = ids
                .stream()
                .collect(Collectors.toMap(id -> id, id -> {
                    Stream<Room> rooms = roomsByFloorId.get(id).stream();
                    Floor floor = new Floor();
                    floor.oneRooms = (int) rooms.filter(r -> r.capacity == 1).count();
                    floor.twoRooms = (int) rooms.filter(r -> r.capacity == 2).count();
                    floor.threeRooms = (int) rooms.filter(r -> r.capacity == 3).count();
                    return floor;
                }));

        return floorDAO.findByIdIn(ids)
                .stream()
                .peek(f -> {
                    Floor floor = byFloorIds.get(f.id);
                    f.oneRooms = floor.oneRooms;
                    f.twoRooms = floor.twoRooms;
                    f.threeRooms = floor.threeRooms;
                }).collect(Collectors.toList());
    }
}