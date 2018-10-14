package com.hotel.controllers;

import com.hotel.daos.UserDAO;
import com.hotel.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/rest")
public class RestController {

    private final AuthUtils authUtils;

    private final UserDAO userDAO;

    @Autowired
    public RestController(AuthUtils authUtils, UserDAO userDAO) {
        this.authUtils = authUtils;
        this.userDAO = userDAO;
    }
}
