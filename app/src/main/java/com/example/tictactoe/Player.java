package com.example.tictactoe;

public class Player {
    String email;
    String name;
    String userId;
    String status;
    String opponentId;

    public Player(){
        //default constructor
    }

    public Player(String email, String name, String userId) {
        this.email = email;
        this.name = name;
        this.userId = userId;
    }

    public Player(String email, String username, String userId, String status, String opponentId) {
        this.email = email;
        this.name = username;
        this.userId = userId;
        this.status = status;
        this.opponentId = opponentId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOpponentId() {
        return opponentId;
    }

    public void setOpponentId(String opponentId) {
        this.opponentId = opponentId;
    }
}
