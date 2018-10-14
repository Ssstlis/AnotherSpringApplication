package com.hotel.daos;

import com.hotel.models.Floor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FloorDAO extends JpaRepository<Floor, Integer> {
}
