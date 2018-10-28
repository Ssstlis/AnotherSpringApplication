package com.hotel.services;

import com.hotel.daos.OrganizationDAO;
import com.hotel.models.Organization;
import com.hotel.models.Service;
import com.hotel.models.User;
import com.hotel.utils.AuthUtils.Tuple;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class OrganizationService {
    private final OrganizationDAO organizationDAO;
    private final ServiceService serviceService;
    private final UserService userService;

    @Autowired
    public OrganizationService(
            OrganizationDAO organizationDAO,
            ServiceService serviceService,
            UserService userService
    ) {
        this.organizationDAO = organizationDAO;
        this.serviceService = serviceService;
        this.userService = userService;
    }

    public List<Organization> all() {
        return organizationDAO.findAll();
    }

    public void save(Organization organization) {
        organizationDAO.save(organization);
    }

    public Organization findByUserId(Integer userId) {
        return organizationDAO.findByUserId(userId);
    }

    public Map<Organization, Tuple<Service, User>> organizationsWithUser() {

        Map<Integer, Service> serviceMap = serviceService.all()
                .stream()
                .collect(Collectors.toMap(s -> s.id, s -> s));

        Map<Integer, User> userMap = userService.all()
                .stream()
                .collect(Collectors.toMap(u -> u.id, u -> u));

        return organizationDAO.findAll()
                .stream()
                .collect(Collectors.toMap(o -> o, o -> {
                    User user = userMap.get(o.userId);
                    Service service = serviceMap.get(o.serviceId);
                    return new Tuple<>(service, user);
                }));
    }
}