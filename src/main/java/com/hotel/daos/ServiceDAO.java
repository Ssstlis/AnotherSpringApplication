package com.hotel.daos;

import com.hotel.models.Service;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceDAO extends JpaRepository<Service, Integer> {
}
