package com.example.gamificationapp.service;

import com.example.gamificationapp.domain.Player;
import com.example.gamificationapp.domain.PlayerQuests;
import com.example.gamificationapp.domain.Quest;
import com.example.gamificationapp.exceptions.RepoException;
import com.example.gamificationapp.exceptions.ValidationException;

import java.sql.SQLException;

public interface IService {

    void addPlayer(String username, String password) throws ValidationException, RepoException;
    void removePlayer(Integer id) throws RepoException;
    Player getPlayer(Integer id) throws RepoException;
    Player getPlayerByUsername(String username) throws RepoException;
    Iterable<Player> getAllPlayers();
    void addQuest(String description, Integer nrTokens, Integer idPlayer, Integer nrOfGamesNeeded) throws ValidationException, RepoException;
    Quest getQuest(Integer id) throws RepoException;
    Iterable<Quest> getAllQuests();
    void addPlayerQuest(Integer idPlayer, Integer idQuest) throws RepoException;
    void completeQuest(Integer idPlayer, Integer idQuest) throws SQLException, RepoException;
    void logNewGame(Player player) throws SQLException, RepoException;
    void logNewGameForAcceptedQuest(Player player, PlayerQuests playerQuest) throws SQLException, RepoException;
    PlayerQuests existsAnotherPendingQuest(Player loggedPlayer);
}

