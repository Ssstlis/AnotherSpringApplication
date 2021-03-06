package com.hotel.forms;

import com.hotel.models.User;

import static com.hotel.utils.AuthUtils.base64Encode;

public class EditProfileForm {
    private Integer Id;
    private String firstName;
    private String lastName;
    private String login;
    private String password;
    public EditProfileForm() {}

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public User reGenUser(User user) {
        user.lastName  = lastName;
        user.firstName = firstName;
        user.login     = login;
        user.password  = base64Encode(password);
        return user;
    }
}