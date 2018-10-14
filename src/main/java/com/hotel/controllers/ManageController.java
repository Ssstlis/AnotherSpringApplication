package com.hotel.controllers;

import com.hotel.forms.*;
import com.hotel.models.*;
import com.hotel.services.*;
import com.hotel.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class ManageController {
    private final AuthUtils authUtils;
    private final BuildingService buildingService;
    private final FloorService floorService;
    private final OrganizationService organizationService;
    private final TicketService ticketService;
    private final ServiceService serviceService;
    private final OrganizationOnFloorService organizationOnFloorService;

    @Autowired
    public ManageController(
            AuthUtils authUtils,
            BuildingService buildingService,
            FloorService floorService,
            OrganizationService organizationService,
            TicketService ticketService,
            ServiceService serviceService,
            OrganizationOnFloorService organizationOnFloorService) {
        this.authUtils = authUtils;
        this.buildingService = buildingService;
        this.floorService = floorService;
        this.organizationService = organizationService;
        this.ticketService = ticketService;
        this.serviceService = serviceService;
        this.organizationOnFloorService = organizationOnFloorService;
    }

    @RequestMapping(value = "/manage", method = RequestMethod.GET)
    public ModelAndView manage(HttpServletRequest request, HttpServletResponse response) {
        return authUtils.authOrganizationOrAdminAndThen(request, response, "manage", (user, modelMap) -> {
        });
    }

    @RequestMapping(value = "/buildings", method = RequestMethod.GET)
    public ModelAndView buildings(HttpServletRequest request, HttpServletResponse response) {
        return authUtils.authAdminAndThen(request, response, "manage", (user, modelMap) -> {
            List<Building> buildings = buildingService.buildingsWithFloorCount();
            modelMap.put("buildings", buildings);
        });
    }

    @RequestMapping(value = "/add_building", method = RequestMethod.GET)
    public ModelAndView addBuilding(HttpServletRequest request, HttpServletResponse response) {
        return authUtils.authAdminAndThen(request, response, "manage", (user, modelMap) -> {
            modelMap.put("buildingForm", new BuildingForm());
        });
    }

    @RequestMapping(value = "/add_building", method = RequestMethod.POST)
    public ModelAndView addBuilding(@ModelAttribute("buildingForm") BuildingForm buildingForm, HttpServletRequest request, HttpServletResponse response) {
        return authUtils.authAdminAndThen(request, response, "manage", (user, modelMap) -> {
            Building building = new Building();
            building.description = buildingForm.getDescription();
            building.stars = buildingForm.getStars();
            Integer buildingId = buildingService.save(building).id;

            Set<Floor> floorSet = new HashSet<>();
            for (int i = 1; i <= buildingForm.getFloors(); i++) {
                Floor floor = new Floor();
                floor.buildingId = buildingId;
                floorSet.add(floor);
            }
            floorService.saveAll(floorSet);
            List<Building> buildings = buildingService.buildingsWithFloorCount();
            modelMap.put("buildings", buildings);
            modelMap.put("buildingForm", null);
        });
    }

    @RequestMapping(value = "/services", method = RequestMethod.GET)
    public ModelAndView services(HttpServletRequest request, HttpServletResponse response) {
        return authUtils.authAdminAndThen(request, response, "manage", (user, modelMap) -> {
            List<Service> services = serviceService.all();
            modelMap.put("services", services);
        });
    }

    @RequestMapping(value = "/add_service", method = RequestMethod.GET)
    public ModelAndView addService(HttpServletRequest request, HttpServletResponse response) {
        return authUtils.authAdminAndThen(request, response, "manage", (user, modelMap) -> {
            modelMap.put("serviceForm", new ServiceForm());
        });
    }

    @RequestMapping(value = "/add_service", method = RequestMethod.POST)
    public ModelAndView addService(@ModelAttribute("serviceForm") ServiceForm serviceForm, HttpServletRequest request, HttpServletResponse response) {
        return authUtils.authAdminAndThen(request, response, "manage", (user, modelMap) -> {
            Service service = new Service();
            service.description = serviceForm.getDescription();
            service.basicPrice = serviceForm.getBasicPrice();
            serviceService.save(service);
            List<Service> services = serviceService.all();
            modelMap.put("services", services);
            modelMap.put("serviceForm", null);
        });
    }

    @RequestMapping(value = "/organizations", method = RequestMethod.GET)
    public ModelAndView organizations(HttpServletRequest request, HttpServletResponse response) {
        return authUtils.authAdminAndThen(request, response, "manage", (user, modelMap) -> {
            List<Organization> organizations = organizationService.all();
            modelMap.put("organizations", organizations);
        });
    }

    @RequestMapping(value = "/money_become", method = RequestMethod.GET)
    public ModelAndView moneyBecome(HttpServletRequest request, HttpServletResponse response) {
        return authUtils.authOrganizationOrAdminAndThen(request, response, "manage", (user, modelMap) -> {
            List<Ticket> tickets = ticketService.all();
            modelMap.put("tickets", tickets);
        });
    }

    @RequestMapping(value = "/money_spent", method = RequestMethod.GET)
    public ModelAndView moneySpent(HttpServletRequest request, HttpServletResponse response) {
        return authUtils.authOrganizationOrAdminAndThen(request, response, "manage", (user, modelMap) -> {
            List<Ticket> tickets = ticketService.all();
            modelMap.put("tickets", tickets);
        });
    }

    @RequestMapping(value = "/tickets", method = RequestMethod.GET)
    public ModelAndView tickets(HttpServletRequest request, HttpServletResponse response) {
        return authUtils.authOrganizationOrAdminAndThen(request, response, "manage", (user, modelMap) -> {
            List<Ticket> tickets = ticketService.all();
            modelMap.put("tickets", tickets);
        });
    }

    @RequestMapping(value = "/service_policy", method = RequestMethod.GET)
    public ModelAndView servicePolicy(HttpServletRequest request, HttpServletResponse response) {
        return authUtils.authOrganizationAndThen(request, response, "manage", (user, modelMap) -> {
            Organization organization = organizationService.findByUserId(user.id);
            modelMap.put("servicePolicyForm", new ServicePolicyForm());
            modelMap.put("serviceId", organization.serviceId);
            modelMap.put("services_form", serviceService.all().stream().collect(Collectors.toMap(s -> s, s -> s.id.equals(organization.serviceId))));
        });
    }

    @RequestMapping(value = "/service_policy", method = RequestMethod.POST)
    public ModelAndView servicePolicy(@ModelAttribute("servicePolicyForm") ServicePolicyForm servicePolicyForm, HttpServletRequest request, HttpServletResponse response) {
        return authUtils.authOrganizationAndThen(request, response, "manage", (user, modelMap) -> {
            Integer serviceId = servicePolicyForm.serviceId;
            Organization organization = organizationService.findByUserId(user.id);
            organization.serviceId = serviceId;
            organizationService.save(organization);
            modelMap.put("servicePolicyForm", null);
        });
    }

    @RequestMapping(value = "/add_flour", method = RequestMethod.GET)
    public ModelAndView addFlour(HttpServletRequest request, HttpServletResponse response) {
        return authUtils.authOrganizationOrAdminAndThen(request, response, "manage", (user, modelMap) -> {
            if (user.privilegies == 2) {
                Organization organization = organizationService.findByUserId(user.id);
                //TODO query
                List<Integer> floorIds = organizationOnFloorService.findByOrganizationIdIsNot(organization.id);
                List<Floor> floorServiceByIdsNotIn = floorService.findByIdsNotIn(floorIds);
                Map<Building, List<Floor>> unavailableBuildings = buildingService.unavailableBuildings(floorServiceByIdsNotIn);
                modelMap.put("floors_form", unavailableBuildings);
                modelMap.put("floorOrganizationForm", new FloorOrganizationForm());
            } else {
                modelMap.put("buildings_form", buildingService.all());
                modelMap.put("floorAdminForm", new FloorAdminForm());
            }
        });
    }

    @RequestMapping(value = "/add_flour_admin", method = RequestMethod.POST)
    public ModelAndView addFlour(@ModelAttribute("floorAdminForm") FloorAdminForm floorAdminForm, HttpServletRequest request, HttpServletResponse response) {
        return authUtils.authOrganizationOrAdminAndThen(request, response, "manage", (user, modelMap) -> {
            Integer buildingId = floorAdminForm.buildingId;
            Floor floor = new Floor();
            floor.buildingId = buildingId;
            floorService.save(floor);
            List<Building> buildings = buildingService.buildingsWithFloorCount();
            modelMap.put("buildings", buildings);
            modelMap.put("floorAdminForm", null);
        });
    }

    @RequestMapping(value = "/add_flour_organization", method = RequestMethod.POST)
    public ModelAndView addFlour(@ModelAttribute("floorOrganizationForm") FloorOrganizationForm floorOrganizationForm, HttpServletRequest request, HttpServletResponse response) {
        return authUtils.authOrganizationOrAdminAndThen(request, response, "manage", (user, modelMap) -> {
            Organization organization = organizationService.findByUserId(user.id);
            Integer floorId = floorOrganizationForm.floorId;
            Float multipler = (float) (floorOrganizationForm.multipler / 100);
            OrganizationOnFloor organizationOnFloor = new OrganizationOnFloor();
            organizationOnFloor.floorId = floorId;
            organizationOnFloor.multipler = multipler;
            organizationOnFloor.organizationId = organization.id;
            organizationOnFloorService.save(organizationOnFloor);
            List<Service> services = serviceService.all();
            modelMap.put("services", services);
            modelMap.put("floorOrganizationForm", null);
        });
    }
}