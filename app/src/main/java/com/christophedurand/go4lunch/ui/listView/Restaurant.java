package com.christophedurand.go4lunch.ui.listView;


class Restaurant {
    private String id;
    private String name;
    private String address;
    private String businessStatus;

    public Restaurant(String id, String name, String address, String businessStatus) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.businessStatus = businessStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBusinessStatus() {
        return businessStatus;
    }

    public void setBusinessStatus(String businessStatus) {
        this.businessStatus = businessStatus;
    }
}