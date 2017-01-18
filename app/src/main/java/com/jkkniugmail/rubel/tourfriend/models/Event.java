package com.jkkniugmail.rubel.tourfriend.models;

/**
 * Created by islan on 12/27/2016.
 */

public class Event {
    private int id;
    private String title;
    private String description;
    private String location;
    private String starting_date;
    private float budget;

    public Event(int id, String title, String description, String location, String starting_date, float budget) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.starting_date = starting_date;
        this.budget = budget;
    }

    public Event(String title, String description, String location, String starting_date, float budget) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.starting_date = starting_date;
        this.budget = budget;
    }

    public Event() {
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getStarting_date() {
        return starting_date;
    }

    public float getBudget() {
        return budget;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setStarting_date(String starting_date) {
        this.starting_date = starting_date;
    }

    public void setBudget(float budget) {
        this.budget = budget;
    }
}
