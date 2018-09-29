package com.hotel.daos;

import com.hotel.models.Building;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildingDAO extends JpaRepository<Building, Integer> {
}
