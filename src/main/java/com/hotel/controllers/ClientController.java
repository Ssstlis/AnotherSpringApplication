package com.hotel.controllers;

import com.hotel.daos.UserDAO;
import com.hotel.utils.AuthUtils;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

@Controller
public class ClientController {

    private final AuthUtils authUtils;

    private final UserDAO userDAO;

    @Autowired
    public ClientController(AuthUtils authUtils, UserDAO userDAO) {
        this.authUtils = authUtils;
        this.userDAO = userDAO;
    }

    @RequestMapping(value = "/bin", method = RequestMethod.GET)
    public ModelAndView clientBin(HttpServletRequest request, HttpServletResponse response) {
        return authUtils.authAndThen(request, response, (user, modelMap) -> {
            return new ModelAndView("userBin", modelMap, HttpStatus.OK);
        });
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
}
