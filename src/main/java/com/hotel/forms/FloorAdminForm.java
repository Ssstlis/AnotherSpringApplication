package com.hotel.forms;

public class FloorAdminForm {
    public Integer buildingId;
    public Integer oneRooms;
    public Integer twoRooms;
    public Integer threeRooms;


    public FloorAdminForm(){}

    public void setBuildingId(Integer buildingId) {
        this.buildingId = buildingId;
    }

    public Integer getBuildingId() {
        return buildingId;
    }

    public Integer getOneRooms() {
        return oneRooms;
    }

    public void setOneRooms(Integer oneRooms) {
        this.oneRooms = oneRooms;
    }

    public Integer getTwoRooms() {
        return twoRooms;
    }

    public void setTwoRooms(Integer twoRooms) {
        this.twoRooms = twoRooms;
    }

    public Integer getThreeRooms() {
        return threeRooms;
    }

    public void setThreeRooms(Integer threeRooms) {
        this.threeRooms = threeRooms;
    }
}