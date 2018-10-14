package com.hotel.utils;

import com.hotel.daos.UserDAO;
import com.hotel.forms.LoginForm;
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
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AuthUtils {

    private UserDAO userDAO;

    @Autowired
    public AuthUtils(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    private Function<Map<String, Object>, ModelAndView> loginView = (modelMap) -> {
//        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("loginForm", new LoginForm());
        return new ModelAndView("login", modelMap, HttpStatus.BAD_REQUEST);
    };

    private Function<Map<String, Object>, ModelAndView> indexView = (modelMap) -> {
//        Map<String, Object> modelMap = new HashMap<>();
        return new ModelAndView("index", modelMap, HttpStatus.BAD_REQUEST);
    };

//    private <T> ModelAndView andThen(Function<T, Session> auth, T t, HttpServletResponse response, String name, BiConsumer<User, Map<String, Object>> f) {
//        return auth.andThen(session -> {
//            if (session != null && session.user != null) {
//                response.addCookie(session.cookie);
//                Map<String, Object> modelMap = new HashMap<>();
//                modelMap.put("user", session.user);
//                f.accept(session.user, modelMap);
//                return new ModelAndView(name, modelMap, HttpStatus.OK);
//            } else {
//                return loginView();
//            }
//        }).apply(t);
//    }

    private <T> ModelAndView andThenWith(Function<T, Session> auth, T t, HttpServletResponse response, String name, BiConsumer<User, Map<String, Object>> f, Predicate<User> p) {
        return auth.andThen(session -> {
            Map<String, Object> modelMap = new HashMap<>();
            if (session != null && session.user != null) {
                response.addCookie(session.cookie);
                modelMap.put("user", session.user);
                if (p.test(session.user)) {
                    try {
                        f.accept(session.user, modelMap);
                        return new ModelAndView(name, modelMap, HttpStatus.OK);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        return indexView.apply(modelMap);
                    }
                } else {
                    return indexView.apply(modelMap);
                }
            } else {
                return loginView.apply(modelMap);
            }
        }).apply(t);
    }

    public ModelAndView loginAuthAndThen(LoginForm loginForm, HttpServletResponse response, String name, BiConsumer<User, Map<String, Object>> f) {
        return andThenWith(this::getSession, loginForm, response, name, f, (user) -> true);
    }

    public ModelAndView authAndThen(HttpServletRequest request, HttpServletResponse response, String name, BiConsumer<User, Map<String, Object>> f) {
        return andThenWith(this::authAction, request, response, name, f, (user) -> true);
    }

    public ModelAndView authOrganizationOrAdminAndThen(HttpServletRequest request, HttpServletResponse response, String name, BiConsumer<User, Map<String, Object>> f) {
        return andThenWith(this::authAction, request, response, name, f, (user) -> user.privilegies > 1);
    }

    public ModelAndView authOrganizationAndThen(HttpServletRequest request, HttpServletResponse response, String name, BiConsumer<User, Map<String, Object>> f) {
        return andThenWith(this::authAction, request, response, name, f, (user) -> user.privilegies == 2);
    }

    public ModelAndView authAdminAndThen(HttpServletRequest request, HttpServletResponse response, String name, BiConsumer<User, Map<String, Object>> f) {
        return andThenWith(this::authAction, request, response, name, f, (user) -> user.privilegies == 3);
    }
//
//    public ModelAndView authOrganizationOrAdminAndThen(HttpServletRequest request, HttpServletResponse response, BiFunction<User, Map<String, Object>, ModelAndView> f) {
//        return andThenWith(authAction(request), response, f, (user) -> user.privilegies > 1);
//    }
//
//    public ModelAndView authAdminAndThen(HttpServletRequest request, HttpServletResponse response, BiFunction<User, Map<String, Object>, ModelAndView> f) {
//        return andThenWith(authAction(request), response, f, (user) -> user.privilegies > 2);
//    }

    public Session newSession(User user) {
        if (user == null) {
            return null;
        } else {
            String value = user.id.toString() + "-" + user.password;
            String base64 = base64Encode(value);
            return new Session(user, new Cookie("session", base64));
        }
    }

    private Session getSession(LoginForm loginForm) {
        if (loginForm == null) {
            return null;
        } else {
            String pass = base64Encode(loginForm.getPassword());
            User user = userDAO.findByLoginAndPassword(loginForm.getLogin(), pass).orElse(null);
            if (user != null && user.activity == 0) {
                user.activity = 1;
                user = userDAO.save(user);
            }
            return newSession(user);
        }
    }

    private Session authAction(HttpServletRequest request) {
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
            Cookie cookie = null;
            if (request.getCookies() != null) {
                cookie = (new ArrayList<>(Arrays.asList(request.getCookies())))
                        .stream()
                        .filter(c -> c.getName().equals("session"))
                        .findFirst()
                        .orElse(null);
            }
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