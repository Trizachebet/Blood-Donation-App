package com.example.bda.Model;

public class User {
    String bloodgroups, email, id, idnumber, name, phonenumber, search, type, profilepicurl;

    public User() {
    }

    public User(String bloodgroups, String email, String id, String idnumber, String name, String phonenumber, String search, String type, String profilepicurl) {
        this.bloodgroups = bloodgroups;
        this.email = email;
        this.id = id;
        this.idnumber = idnumber;
        this.name = name;
        this.phonenumber = phonenumber;
        this.search = search;
        this.type = type;
        this.profilepicurl = profilepicurl;
    }

    public String getBloodgroups() {
        return bloodgroups;
    }

    public void setBloodgroups(String bloodgroups) {
        this.bloodgroups = bloodgroups;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdnumber() {
        return idnumber;
    }

    public void setIdnumber(String idnumber) {
        this.idnumber = idnumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProfilepicurl() {
        return profilepicurl;
    }

    public void setProfilepicurl(String profilepicurl) {
        this.profilepicurl = profilepicurl;
    }
}

