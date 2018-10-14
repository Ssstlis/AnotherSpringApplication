package com.hotel.daos;

import com.hotel.models.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrganizationDAO extends JpaRepository<Organization, Integer> {

    Optional<Organization> findByUserId(Integer integer);
}
