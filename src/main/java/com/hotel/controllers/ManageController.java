package com.hotel.controllers;

import com.hotel.forms.BuildingForm;
import com.hotel.forms.FloorForm;
import com.hotel.forms.ServiceForm;
import com.hotel.models.*;
import com.hotel.services.*;
import com.hotel.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
public class ManageController {
    private final AuthUtils authUtils;
    private final BuildingService buildingService;
    private final FloorService floorService;
    private final OrganizationService organizationService;
    private final TicketService ticketService;
    private final ServiceService serviceService;

    @Autowired
    public ManageController(
            AuthUtils authUtils,
            BuildingService buildingService,
            FloorService floorService,
            OrganizationService organizationService,
            TicketService ticketService,
            ServiceService serviceService) {
        this.authUtils = authUtils;
        this.buildingService = buildingService;
        this.floorService = floorService;
        this.organizationService = organizationService;
        this.ticketService = ticketService;
        this.serviceService = serviceService;
    }

    @RequestMapping(value = "/manage", method = RequestMethod.GET)
    public ModelAndView manage(HttpServletRequest request, HttpServletResponse response) {
        return authUtils.authOrganizationOrAdminAndThen(request, response, (user, modelMap) -> {
            return new ModelAndView("manage", modelMap, HttpStatus.OK);
        });
    }

    @RequestMapping(value = "/buildings", method = RequestMethod.GET)
    public ModelAndView buildings(HttpServletRequest request, HttpServletResponse response) {
        return authUtils.authAdminAndThen(request, response, (user, modelMap) -> {
            List<Building> buildings = buildingService.buildingsWithFloorCount();
            modelMap.put("buildings", buildings);
            return new ModelAndView("manage", modelMap, HttpStatus.OK);
        });
    }

    @RequestMapping(value = "/add_building", method = RequestMethod.GET)
    public ModelAndView addBuilding(HttpServletRequest request, HttpServletResponse response) {
        return authUtils.authAdminAndThen(request, response, (user, modelMap) -> {
            modelMap.put("buildingForm", new BuildingForm());
            return new ModelAndView("manage", modelMap, HttpStatus.OK);
        });
    }

    @RequestMapping(value = "/add_building", method = RequestMethod.POST)
    public ModelAndView addBuilding(@ModelAttribute("buildingForm") BuildingForm buildingForm, HttpServletRequest request, HttpServletResponse response) {

        return authUtils.authAdminAndThen(request, response, (user, modelMap) -> {
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
            return new ModelAndView("manage", modelMap, HttpStatus.OK);
        });
    }

    @RequestMapping(value = "/services", method = RequestMethod.GET)
    public ModelAndView services(HttpServletRequest request, HttpServletResponse response) {
        return authUtils.authAdminAndThen(request, response, (user, modelMap) -> {
            List<Service> services = serviceService.all();
            modelMap.put("services", services);
            return new ModelAndView("manage", modelMap, HttpStatus.OK);
        });
    }

    @RequestMapping(value = "/add_service", method = RequestMethod.GET)
    public ModelAndView addService(HttpServletRequest request, HttpServletResponse response) {
        return authUtils.authAdminAndThen(request, response, (user, modelMap) -> {
            modelMap.put("serviceForm", new ServiceForm());
            return new ModelAndView("manage", modelMap, HttpStatus.OK);
        });
    }

    @RequestMapping(value = "/add_service", method = RequestMethod.POST)
    public ModelAndView addService(@ModelAttribute("serviceForm") ServiceForm serviceForm, HttpServletRequest request, HttpServletResponse response) {
        return authUtils.authAdminAndThen(request, response, (user, modelMap) -> {
            return new ModelAndView("manage", modelMap, HttpStatus.OK);
        });
    }

    @RequestMapping(value = "/organizations", method = RequestMethod.GET)
    public ModelAndView organizations(HttpServletRequest request, HttpServletResponse response) {
        return authUtils.authAdminAndThen(request, response, (user, modelMap) -> {
            List<Organization> organizations = organizationService.all();
            modelMap.put("organizations", organizations);
            return new ModelAndView("manage", modelMap, HttpStatus.OK);
        });
    }

    @RequestMapping(value = "/money_become", method = RequestMethod.GET)
    public ModelAndView moneyBecome(HttpServletRequest request, HttpServletResponse response) {
        return authUtils.authOrganizationOrAdminAndThen(request, response, (user, modelMap) -> {
            List<Ticket> tickets = ticketService.all();
            modelMap.put("tickets", tickets);
            return new ModelAndView("manage", modelMap, HttpStatus.OK);
        });
    }

    @RequestMapping(value = "/money_spent", method = RequestMethod.GET)
    public ModelAndView moneySpent(HttpServletRequest request, HttpServletResponse response) {
        return authUtils.authOrganizationOrAdminAndThen(request, response, (user, modelMap) -> {
            List<Ticket> tickets = ticketService.all();
            modelMap.put("tickets", tickets);
            return new ModelAndView("manage", modelMap, HttpStatus.OK);
        });
    }

    @RequestMapping(value = "/tickets", method = RequestMethod.GET)
    public ModelAndView tickets(HttpServletRequest request, HttpServletResponse response) {
        return authUtils.authOrganizationOrAdminAndThen(request, response, (user, modelMap) -> {
            List<Ticket> tickets = ticketService.all();
            modelMap.put("tickets", tickets);
            return new ModelAndView("manage", modelMap, HttpStatus.OK);
        });
    }

    @RequestMapping(value = "/service_policy", method = RequestMethod.GET)
    public ModelAndView servicePolicy(HttpServletRequest request, HttpServletResponse response) {
        return authUtils.authOrganizationAndThen(request, response, (user, modelMap) -> {
            List<Organization> organizations = organizationService.all();
            return new ModelAndView("manage", modelMap, HttpStatus.OK);
        });
    }

    @RequestMapping(value = "/add_flour", method = RequestMethod.GET)
    public ModelAndView addFlour(HttpServletRequest request, HttpServletResponse response) {
        return authUtils.authOrganizationOrAdminAndThen(request, response, (user, modelMap) -> {
            modelMap.put("floorForm", new FloorForm());
            return new ModelAndView("manage", modelMap, HttpStatus.OK);
        });
    }

    @RequestMapping(value = "/add_flour", method = RequestMethod.POST)
    public ModelAndView addFlour(@ModelAttribute("floorForm") FloorForm floorForm, HttpServletRequest request, HttpServletResponse response) {
        return authUtils.authOrganizationOrAdminAndThen(request, response, (user, modelMap) -> {
            return new ModelAndView("manage", modelMap, HttpStatus.OK);
        });
    }

}