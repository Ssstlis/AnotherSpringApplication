package com.hotel.utils;

import com.hotel.daos.UserDAO;
import com.hotel.forms.Login;
import com.hotel.models.Session;
import com.hotel.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AuthUtils {

    private UserDAO userDAO;

    @Autowired
    public AuthUtils(UserDAO userDAO) {
        this.userDAO = userDAO;
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

    public Session authAction(Login login) {
        User user = userDAO.findByLoginAndPassword(login.login, login.password).orElse(null);
        return newSession(user);
    }

    public Session authAction(HttpServletRequest request) {
        if (request == null) {
            return null;
        } else {
            Cookie cookie = (new ArrayList<>(Arrays.asList(request.getCookies())))
                    .stream()
                    .filter((c) -> c.getName().equals("session"))
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
                    return new Session(userDAO.findByIdAndPassword(tuple.x, tuple.y).orElse(null), cookie);
                }
            }
        }
    }

    private static String base64Encode(String input) {
        return Base64.getEncoder().encodeToString(input.getBytes());
    }

    private static String base64Decode(String input) {
        return new String(Base64.getDecoder().decode(input));
    }

    private static Tuple<Integer, String> parseToken(String token) {
        Pattern pattern = Pattern.compile("(\\d+)-(.+)");
        Matcher matcher = pattern.matcher(token);
        if (matcher.matches()) {
            Integer id = Integer.getInteger(matcher.group(1));
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