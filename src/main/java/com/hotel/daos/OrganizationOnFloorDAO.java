package com.hotel.daos;

import com.hotel.models.OrganizationOnFloor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationOnFlourDAO extends JpaRepository<OrganizationOnFloor, Integer> {
}
