package com.hotel.daos;

import com.hotel.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDAO extends JpaRepository<User, Integer> {

    Optional<User> findByIdAndPassword(Integer id, String password);

    Optional<User> findByLoginAndPassword(String login, String password);

    Optional<User> findByLogin(String login);
}
