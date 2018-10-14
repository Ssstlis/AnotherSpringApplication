package com.hotel.forms;

public class RegisterOrganizationForm extends RegisterForm {
    protected Integer serviceId;

    public RegisterOrganizationForm(){}

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }
}
