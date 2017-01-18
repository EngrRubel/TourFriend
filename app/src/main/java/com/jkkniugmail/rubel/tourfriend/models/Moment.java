package com.jkkniugmail.rubel.tourfriend.models;

/**
 * Created by islan on 12/27/2016.
 */

public class Moment {
    private int id;
    private String caption;
    private String date_time;
    private String image_link;

    public Moment() {

    }

    public Moment(int id, String caption, String date_time, String image_link) {
        this.id = id;
        this.caption = caption;
        this.date_time = date_time;
        this.image_link = image_link;
    }

    public Moment(String caption, String date_time, String image_link) {
        this.caption = caption;
        this.date_time = date_time;
        this.image_link = image_link;
    }

    public int getId() {
        return id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getImage_link() {
        return image_link;
    }

    public void setImage_link(String image_link) {
        this.image_link = image_link;
    }
}
