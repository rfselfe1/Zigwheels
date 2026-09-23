package com.hackathon.models;

public class Bike {

    private final String name;
    private final int price;
    private final String expectedLaunchDate;

    public Bike(String name, int price, String expectedLaunchDate) {
        this.name = name;
        this.price = price;
        this.expectedLaunchDate = expectedLaunchDate;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getExpectedLaunchDate() {
        return expectedLaunchDate;
    }

    @Override
    public String toString() {
        return "Bike{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", expectedLaunchDate='" + expectedLaunchDate + '\'' +
                '}';
    }
}