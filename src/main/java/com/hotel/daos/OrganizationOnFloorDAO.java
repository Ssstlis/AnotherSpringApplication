package com.hotel.daos;

import com.hotel.models.OrganizationOnFloor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationOnFloorDAO extends JpaRepository<OrganizationOnFloor, Integer> {
}
