package com.ggz.cowsino;

public class Profile {
    private String username;
    private long money;

    Profile() {
        this.money = 100;
        this.username = "PlayerOne";
    }
    Profile(String username) {
        this.username = username;
        this.money = 100;
    }
    Profile(long money) {
        this.money = money;
        this.username = "PlayerOne";
    }
    Profile(String username, long money) {
        this.username = username;
        this.money = money;
    }

    public String moneyToString() {
        return "$" + String.valueOf(money);
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public long getMoney() {
        return money;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
