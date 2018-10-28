package com.hotel.daos;

import com.hotel.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomDAO extends JpaRepository<Room, Integer> {

    public List<Room> findAllByFloorId(Integer floorId);

    public List<Room> findAllByFloorIdIn(List<Integer> ids);
}
