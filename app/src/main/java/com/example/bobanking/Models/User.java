package com.example.bobanking.Models;

public class User {
    private String id,name,nickname,email;
    private int balance;

    public User() {
    }

    public User(String id, String name, String nickname, String email, int balance) {
        this.id = id;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.balance = balance;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

}
