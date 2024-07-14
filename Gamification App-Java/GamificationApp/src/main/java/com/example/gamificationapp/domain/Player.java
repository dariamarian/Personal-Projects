package com.example.gamificationapp.domain;

public class Player extends Entity<Integer>{

    private String username;
    private String password;
    private String rank;
    private int nrOfTokens;
    private int nrOfGames;
    public Player(String username, String password)
    {
        this.username=username;
        this.password=password;
        this.rank="BRONZE";
        this.nrOfTokens=0;
        this.nrOfGames=0;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public int getNrOfTokens() {
        return nrOfTokens;
    }

    public void setNrOfTokens(int nrOfTokens) {
        this.nrOfTokens = nrOfTokens;
    }

    public int getNrOfGames() {
        return nrOfGames;
    }

    public void setNrOfGames(int nrOfGames) {
        this.nrOfGames = nrOfGames;
    }
}
