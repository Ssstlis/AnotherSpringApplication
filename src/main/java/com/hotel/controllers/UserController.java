package com.hotel.controllers;

import com.hotel.daos.ServiceDAO;
import com.hotel.daos.UserDAO;
import com.hotel.forms.EditProfile;
import com.hotel.forms.Login;
import com.hotel.forms.Register;
import com.hotel.models.Session;
import com.hotel.models.User;
import com.hotel.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {
    private final AuthUtils authUtils;
    private final UserDAO userDAO;
    private final ServiceDAO serviceDAO;

    @Autowired
    public UserController(AuthUtils authUtils, UserDAO userDAO, ServiceDAO serviceDAO) {
        this.authUtils = authUtils;
        this.userDAO = userDAO;
        this.serviceDAO = serviceDAO;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(@ModelAttribute("loginForm") Login loginForm, HttpServletResponse response) {
        return authUtils.loginAuthAndThen(loginForm, response, (user) -> {
            Map<String, Object> modelMap = new HashMap<>();
            modelMap.put("user", user);
            return new ModelAndView("index", modelMap, HttpStatus.OK);
        });
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login() {
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("loginForm", new Login());
        return new ModelAndView("login", modelMap);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView register(Integer mode, @ModelAttribute("registerForm") Register registerForm, HttpServletResponse response) {
        if (!userDAO.findByLogin(registerForm.getLogin()).isPresent()) {
            User user = new User();
            user.privilegies = mode;
            user.password = registerForm.getPassword();
            user.lastName = registerForm.getLastName();
            user.firstName = registerForm.getFirstName();
            user.login = registerForm.getLogin();
            user.password = AuthUtils.base64Encode(user.password);
            user.activity = 1;
            user = userDAO.save(user);
            Session session = authUtils.newSession(user);
            response.addCookie(session.cookie);
            Map<String, Object> modelMap = new HashMap<>();
            modelMap.put("user", user);
            return new ModelAndView("index", modelMap, HttpStatus.OK);
        } else {
            return new ModelAndView("index", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView reg(Integer mode) {
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("registerForm", new Register());
        modelMap.put("mode", mode);
        return new ModelAndView("register", modelMap);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
        AuthUtils.cleanCookies(request, response);
        if (authUtils.logoutAction(request)) {
            Map<String, Object> modelMap = new HashMap<>();
            modelMap.put("loginForm", new Login());
            return new ModelAndView("login", modelMap, HttpStatus.OK);
        } else {
            return new ModelAndView("index", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/edit_profile", method = RequestMethod.GET)
    public ModelAndView editProfile(HttpServletRequest request, HttpServletResponse response) {
        return authUtils.authAndThen(request, response, (user) -> {
            Map<String, Object> modelMap = new HashMap<>();
            modelMap.put("user", user);
            modelMap.put("editProfileForm", new EditProfile());
            return new ModelAndView("editProfile", modelMap, HttpStatus.OK);
        });
    }

    @RequestMapping(value = "/edit_profile", method = RequestMethod.POST)
    public ModelAndView editProfile(@ModelAttribute("editProfileForm") EditProfile editProfile, HttpServletRequest request, HttpServletResponse response) {
        return authUtils.authAndThen(request, response, (user) -> {
            if (userDAO.countByLogin(editProfile.getLogin()) == 0 || editProfile.getLogin().equals(user.login)) {
                user.lastName = editProfile.getLastName();
                user.firstName = editProfile.getFirstName();
                user.login = editProfile.getLogin();
                user.password = AuthUtils.base64Encode(editProfile.getPassword());
                user = userDAO.save(user);
                Session newSession = authUtils.newSession(user);
                AuthUtils.cleanCookies(request, response);
                response.addCookie(newSession.cookie);
            }
            Map<String, Object> modelMap = new HashMap<>();
            modelMap.put("user", user);
            modelMap.put("editProfileForm", new EditProfile());
            return new ModelAndView("editProfile", modelMap, HttpStatus.OK);
        });
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
}