package com.hotel.services;

import com.hotel.daos.UserDAO;
import com.hotel.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserDAO userDAO;

    @Autowired
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User save(User user) {
        return userDAO.save(user);
    }

    public Integer countByLogin(String login) {
        return userDAO.countByLogin(login);
    }

    public Optional<User> findByLogin(String login) {
        return userDAO.findByLogin(login);
    }
}