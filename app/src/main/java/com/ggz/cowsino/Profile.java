package com.ggz.cowsino;

public class Profile {
    private String username;
    private String password;
    private long money;
    private long workGains;
    private long coinFlipGains;
    private long coinFlipLoss;

    Profile(String username, String password, long money) {
        this.username = username;
        this.password = password;
        this.money = money;
    }

    public String profileToString() {
        return username + "\n"
                + password + "\n"
                + money + "\n"
                + workGains + "\n"
                + coinFlipGains + "\n"
                + coinFlipLoss;
    }

    public String moneyToString() {
        return "$" + money;
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

    public void setPassword(String password) { this.password = password; }

    public long getTotalGains() {
        return workGains + coinFlipGains;
    }

    public long getTotalLoss() {
        return coinFlipLoss;
    }

    public void setWorkGains(long gains) {
        this.workGains = gains;
    }

    public long getWorkGains() {
        return workGains;
    }

    public void setCoinFlipGains(long gains) {
        this.coinFlipGains = gains;
    }

    public long getCoinFlipGains() {
        return coinFlipGains;
    }

    public void setCoinFlipLoss(long loss) {
        this.coinFlipLoss = loss;
    }

    public long getCoinFlipLoss() {
        return coinFlipLoss;
    }
}
