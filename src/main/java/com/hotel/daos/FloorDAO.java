package com.hotel.daos;

import com.hotel.models.Floor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FloorDAO extends JpaRepository<Floor, Integer> {

    List<Floor> findByIdNotIn(List<Integer> ids);

    List<Floor> findByIdIn(List<Integer> ids);
}