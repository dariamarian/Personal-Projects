package com.example.gamificationapp.domain;

public class Quest extends Entity<Integer>{
    private String description;
    private int nrOfWonTokens;
    private int idPlayer;
    private int nrOfGamesNeeded;

    public Quest(String description, int nrOfWonTokens, int idPlayer, int nrOfGamesNeeded)
    {
        this.description=description;
        this.nrOfWonTokens=nrOfWonTokens;
        this.idPlayer=idPlayer;
        this.nrOfGamesNeeded=nrOfGamesNeeded;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNrOfWonTokens() {
        return nrOfWonTokens;
    }

    public void setNrOfWonTokens(int nrOfWonTokens) {
        this.nrOfWonTokens = nrOfWonTokens;
    }

    public int getIdPlayer() {
        return idPlayer;
    }

    public void setIdPlayer(int idPlayer) {
        this.idPlayer = idPlayer;
    }

    public int getNrOfGamesNeeded() {
        return nrOfGamesNeeded;
    }

    public void setNrOfGamesNeeded(int nrOfGamesNeeded) {
        this.nrOfGamesNeeded = nrOfGamesNeeded;
    }

}
