package com.jkkniugmail.rubel.tourfriend.models;

/**
 * Created by islan on 12/24/2016.
 */

public class User {
    private int id;
    private String first_name;
    private String last_name;
    private String phone_no;
    private String email;
    private String password;

    public User(int id, String first_name, String last_name, String phone_no, String email, String password) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone_no = phone_no;
        this.email = email;
        this.password = password;
    }

    public User(String first_name, String last_name, String phone_no, String email, String password) {
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone_no = phone_no;
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }
}
