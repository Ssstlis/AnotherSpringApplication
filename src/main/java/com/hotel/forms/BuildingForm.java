package com.hotel.forms;

public class BuildingForm {
    public BuildingForm() {

    }

    public BuildingForm(String description, Integer stars) {
        this.description = description;
        this.stars = stars;
    }

    private String description;
    private Integer stars;

    public Integer getFloors() {
        return floors;
    }

    public void setFloors(Integer floors) {
        this.floors = floors;
    }

    private Integer floors;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }
}
