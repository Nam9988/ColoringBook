package com.example.dd.model;

import java.io.Serializable;

public class UserDTO implements Serializable {
    private String name;
    private String email;
    private String password;
    private String gender;
    private String imgPath;
    private String dob;

    public UserDTO(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public UserDTO(String name, String email, String password, String gender, String imgPath, String dob) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.imgPath = imgPath;
        this.dob = dob;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
}
