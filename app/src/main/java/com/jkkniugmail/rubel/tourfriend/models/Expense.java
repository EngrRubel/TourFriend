package com.jkkniugmail.rubel.tourfriend.models;

/**
 * Created by islan on 12/27/2016.
 */

public class Expense {
    private int id;
    private String detail;
    private float cost;
    private String date_time;

    public Expense() {
    }

    public Expense(int id, String detail, float cost, String date_time) {
        this.id = id;
        this.detail = detail;
        this.cost = cost;
        this.date_time = date_time;
    }

    public Expense(String detail, float cost, String date_time) {

        this.detail = detail;
        this.cost = cost;
        this.date_time = date_time;
    }

    public int getId() {
        return id;
    }

    public String getDetail() {
        return detail;
    }

    public float getCost() {
        return cost;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }
}
