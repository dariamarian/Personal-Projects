package com.example.gamificationapp.domain;

public class PlayerQuests{
    private final int idPlayer;
    private final int idQuest;
    private String status;
    private int nrOfGames;
    public PlayerQuests(int idPlayer, int idQuest)
    {
        this.idPlayer=idPlayer;
        this.idQuest=idQuest;
        this.status="ACCEPTED";
        this.nrOfGames=0;
    }

    public int getIdPlayer() {
        return idPlayer;
    }

    public int getIdQuest() {
        return idQuest;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void completePlayerQuest()
    {
        this.status="COMPLETED";
    }

    public int getNrOfGames() {
        return nrOfGames;
    }

    public void setNrOfGames(int nrOfGames) {
        this.nrOfGames = nrOfGames;
    }
}
