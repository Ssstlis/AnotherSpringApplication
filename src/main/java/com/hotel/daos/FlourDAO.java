package com.hotel.daos;

import com.hotel.models.Flour;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlourDAO extends JpaRepository<Flour, Integer> {
}
