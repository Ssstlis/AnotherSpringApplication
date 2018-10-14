package com.hotel.forms;

public class ServiceForm {
    private String description;
    private Integer basicPrice;

    public ServiceForm(){}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getBasicPrice() {
        return basicPrice;
    }

    public void setBasicPrice(Integer basicPrice) {
        this.basicPrice = basicPrice;
    }
}
