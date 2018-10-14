package com.hotel.controllers;

import com.hotel.forms.EditProfileForm;
import com.hotel.forms.LoginForm;
import com.hotel.forms.RegisterForm;
import com.hotel.forms.RegisterOrganizationForm;
import com.hotel.models.Organization;
import com.hotel.models.Session;
import com.hotel.models.User;
import com.hotel.services.OrganizationService;
import com.hotel.services.ServiceService;
import com.hotel.services.UserService;
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

import static com.hotel.utils.AuthUtils.base64Encode;
import static com.hotel.utils.AuthUtils.cleanCookies;

@Controller
public class UserController {
    private final AuthUtils authUtils;
    private final UserService userService;
    private final OrganizationService organizationService;
    private final ServiceService serviceService;

    @Autowired
    public UserController(
            AuthUtils authUtils,
            UserService userService,
            OrganizationService organizationService,
            ServiceService serviceService
    ) {
        this.authUtils = authUtils;
        this.userService = userService;
        this.organizationService = organizationService;
        this.serviceService = serviceService;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(@ModelAttribute("loginForm") LoginForm loginForm, HttpServletResponse response) {
        return authUtils.loginAuthAndThen(loginForm, response, "index", (user, modelMap) -> {
        });
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login() {
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("loginForm", new LoginForm());
        return new ModelAndView("login", modelMap);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView register(Integer mode, @ModelAttribute("registerForm") RegisterForm registerForm, HttpServletResponse response) {
        if (!userService.findByLogin(registerForm.getLogin()).isPresent()) {
            User user = new User();
            user.privilegies = mode;
            user.password = registerForm.getPassword();
            user.lastName = registerForm.getLastName();
            user.firstName = registerForm.getFirstName();
            user.login = registerForm.getLogin();
            user.password = base64Encode(user.password);
            user.activity = 1;
            user = userService.save(user);
            Session session = authUtils.newSession(user);
            response.addCookie(session.cookie);
            Map<String, Object> modelMap = new HashMap<>();
            modelMap.put("user", user);
            return new ModelAndView("index", modelMap, HttpStatus.OK);
        } else {
            return new ModelAndView("index", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/register_organization", method = RequestMethod.POST)
    public ModelAndView register(Integer mode, @ModelAttribute("organizationForm") RegisterOrganizationForm registerOrganizationForm, HttpServletResponse response) {
        if (!userService.findByLogin(registerOrganizationForm.getLogin()).isPresent()) {
            User user = new User();
            user.privilegies = mode;
            user.password = registerOrganizationForm.getPassword();
            user.lastName = registerOrganizationForm.getLastName();
            user.firstName = registerOrganizationForm.getFirstName();
            user.login = registerOrganizationForm.getLogin();
            Integer serviceId = registerOrganizationForm.getServiceId();
            user.password = base64Encode(user.password);
            user.activity = 1;
            user = userService.save(user);
            Organization organization = new Organization();
            organization.serviceId = serviceId;
            organization.userId = user.id;
            organizationService.save(organization);
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
        if (mode != 2) {
            modelMap.put("registerForm", new RegisterForm());
        } else {
            modelMap.put("services", serviceService.all());
            modelMap.put("organizationForm", new RegisterOrganizationForm());
        }
        modelMap.put("mode", mode);
        return new ModelAndView("register", modelMap);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
        cleanCookies(request, response);
        if (authUtils.logoutAction(request)) {
            Map<String, Object> modelMap = new HashMap<>();
            modelMap.put("loginForm", new LoginForm());
            return new ModelAndView("login", modelMap, HttpStatus.OK);
        } else {
            return new ModelAndView("index", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/edit_profile", method = RequestMethod.GET)
    public ModelAndView editProfile(HttpServletRequest request, HttpServletResponse response) {
        return authUtils.authAndThen(request, response, "editProfile", (user, modelMap) -> {
            modelMap.put("editProfileForm", new EditProfileForm());
        });
    }

    @RequestMapping(value = "/edit_profile", method = RequestMethod.POST)
    public ModelAndView editProfile(@ModelAttribute("editProfileForm") EditProfileForm editProfileForm, HttpServletRequest request, HttpServletResponse response) {
        return authUtils.authAndThen(request, response, "editProfile", (user, modelMap) -> {
            if (userService.countByLogin(editProfileForm.getLogin()) == 0 || !editProfileForm.getLogin().equals(user.login)) {
                user = userService.save(editProfileForm.reGenUser(user));
                cleanCookies(request, response);
                response.addCookie(authUtils.newSession(user).cookie);
            }
            modelMap.put("user", user);
            modelMap.put("editProfileForm", new EditProfileForm());
        });
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
}