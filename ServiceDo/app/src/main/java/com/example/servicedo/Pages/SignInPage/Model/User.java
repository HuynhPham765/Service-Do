package com.example.servicedo.Pages.SignInPage.Model;

public class User {

    public enum EnumSex{ Male, Female, Other;}

    private String userId;
    private String userName;
    private String email;
    private String phone;
    private EnumSex sex;
    private String avatar;

    public User(String userId, String email) {
        this.userId = userId;
        this.userName = email.split("@")[0];
        this.email = email;
        this.phone = "";
        this.sex = EnumSex.Other;
        this.avatar = "";
    }

    public User(String userId, String userName, String email, String phone, EnumSex sex, String avatar) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.phone = phone;
        this.sex = sex;
        this.avatar = avatar;
    }

    public User(String userName, String email, String phone, EnumSex sex, String avatar) {
        this.userName = userName;
        this.email = email;
        this.phone = phone;
        this.sex = sex;
        this.avatar = avatar;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public EnumSex getSex() {
        return sex;
    }

    public void setSex(EnumSex sex) {
        this.sex = sex;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", sex=" + sex +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
