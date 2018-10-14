package com.hotel.daos;

import com.hotel.models.OrganizationOnFloor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrganizationOnFloorDAO extends JpaRepository<OrganizationOnFloor, Integer> {

    List<OrganizationOnFloor> findAllByOrganizationIdIsNotContaining(Integer organizationId);

    List<OrganizationOnFloor> findDistinctByOrganizationIdIsNot(Integer organizationId);

    List<OrganizationOnFloor> findAllByOrganizationId(Integer organizationId);
}
