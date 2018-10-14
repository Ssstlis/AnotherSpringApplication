package com.hotel.daos;

import com.hotel.models.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationDAO extends JpaRepository<Organization, Integer> {

    Organization findByUserId(Integer userId);
}
