package com.hotel.daos;

import com.hotel.models.Building;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BuildingDAO extends JpaRepository<Building, Integer> {

    List<Building> findAllByIdIn(Iterable<Integer> ids);
}
