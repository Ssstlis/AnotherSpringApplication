package com.hotel.utils;

import com.hotel.daos.UserDAO;
import com.hotel.forms.Login;
import com.hotel.models.Session;
import com.hotel.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AuthUtils {

    private UserDAO userDAO;

    @Autowired
    public AuthUtils(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public Supplier<ModelAndView> loginView = () -> {
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("loginForm", new Login());
        return new ModelAndView("login", modelMap, HttpStatus.BAD_REQUEST);
    };

    private ModelAndView andThen(Session session, HttpServletResponse response, Function<User, ModelAndView> f) {
        if (session != null  && session.user != null) {
            response.addCookie(session.cookie);
            return f.apply(session.user);
        } else {
            return loginView.get();
        }
    }

    public ModelAndView loginAuthAndThen(Login login, HttpServletResponse response, Function<User, ModelAndView> f) {
        return andThen(getSession(login), response, f);
    }

    public ModelAndView authAndThen(HttpServletRequest request, HttpServletResponse response, Function<User, ModelAndView> f) {
        return andThen(authAction(request), response, f);
    }

    public Session newSession(User user) {
        if (user == null) {
            return null;
        } else {
            String value = user.id.toString() + "-" + user.password;
            String base64 = base64Encode(value);
            return new Session(user, new Cookie("session", base64));
        }
    }

    public Session getSession(Login login) {
        String pass = base64Encode(login.getPassword());
        User user = userDAO.findByLoginAndPassword(login.getLogin(), pass).orElse(null);
        if (user != null && user.activity == 0) {
            user.activity = 1;
            user = userDAO.save(user);
        }
        return newSession(user);
    }

    public Session authAction(HttpServletRequest request) {
        if (request == null || request.getCookies() == null) {
            return null;
        } else {
            Cookie cookie = (new ArrayList<>(Arrays.asList(request.getCookies())))
                    .stream()
                    .filter(c -> c.getName().equals("session"))
                    .findFirst()
                    .orElse(null);
            if (cookie == null) {
                return null;
            } else {
                String token = cookie.getValue();
                Tuple<Integer, String> tuple = parseToken(token);
                if (tuple == null) {
                    return null;
                } else {
                    User user = userDAO.findByIdAndPasswordAndActivity(tuple.x, tuple.y, 1).orElse(null);
                    return new Session(user, cookie);
                }
            }
        }
    }

    public Boolean logoutAction(HttpServletRequest request) {
        if (request == null) {
            return false;
        } else {
            Cookie cookie = (new ArrayList<>(Arrays.asList(request.getCookies())))
                    .stream()
                    .filter(c -> c.getName().equals("session"))
                    .findFirst()
                    .orElse(null);
            if (cookie == null) {
                return false;
            } else {
                String token = cookie.getValue();
                Tuple<Integer, String> tuple = parseToken(token);
                if (tuple == null) {
                    return false;
                } else {
                    User user = userDAO.findByIdAndPasswordAndActivity(tuple.x, tuple.y, 1).orElse(null);
                    if (user != null) {
                        user.activity = 0;
                        userDAO.save(user);
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }
    }

    public static String base64Encode(String input) {
        return Base64.getEncoder().encodeToString(input.getBytes());
    }

    public static String base64Decode(String input) {
        return new String(Base64.getDecoder().decode(input));
    }

    public static void cleanCookies(HttpServletRequest request, HttpServletResponse response) {
        if (request != null && request.getCookies() != null) {
            (new ArrayList<>(Arrays.asList(request.getCookies()))).forEach(c -> {
                c.setMaxAge(0);
                response.addCookie(c);
            });
        }
    }

    private static Tuple<Integer, String> parseToken(String token) {
        String info = AuthUtils.base64Decode(token);
        Pattern pattern = Pattern.compile("(\\d*)-(.+)");
        Matcher matcher = pattern.matcher(info);
        if (matcher.matches()) {
            String s1 = matcher.group(1);
            Integer id = Integer.valueOf(matcher.group(1));
            String password = matcher.group(2);
            return new Tuple<>(id, password);
        } else {
            return null;
        }
    }

    public static class Tuple<X, Y> {
        public final X x;
        public final Y y;

        public Tuple(X x, Y y) {
            this.x = x;
            this.y = y;
        }
    }
}