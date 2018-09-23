package com.hotel.controllers;

import com.hotel.daos.UserDAO;
import com.hotel.forms.Login;
import com.hotel.forms.Register;
import com.hotel.models.Session;
import com.hotel.models.User;
import com.hotel.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/rest")
public class RestController {

    @Autowired
    private AuthUtils authUtils;

    @Autowired
    private UserDAO userDAO;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test(HttpServletRequest request, HttpServletResponse response) {
        Session session = authUtils.authAction(request);
        if (session == null) {
            return "fail";
        } else {
            response.addCookie(session.cookie);
            return session.user.password;
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestBody Login login, HttpServletRequest request, HttpServletResponse response) {
        Session session = authUtils.authAction(login);
        response.addCookie(session.cookie);
        return "";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView register(@ModelAttribute("registerForm") Register register, HttpServletRequest request, HttpServletResponse response) {
        User user = new User();
        user.password = register.getPassword();
        user.lastName = register.getLastName();
        user.firstName = register.getFirstName();
        user.login = register.getLogin();
        if (userDAO.findByLogin(user.login).isPresent()) {
            user = userDAO.save(user);
            Session session = authUtils.newSession(user);
            response.addCookie(session.cookie);
            return new ModelAndView("index", HttpStatus.OK);
        } else {
            return new ModelAndView("index", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/reg", method = RequestMethod.GET)
    public ModelAndView reg(Model model) {
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("registerForm", new Register());
        return new ModelAndView("register", modelMap);
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
}
