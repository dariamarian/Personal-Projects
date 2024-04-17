package com.example.gamificationapp.service;

import com.example.gamificationapp.domain.Player;
import com.example.gamificationapp.domain.PlayerQuests;
import com.example.gamificationapp.domain.Quest;
import com.example.gamificationapp.domain.validators.Validator;
import com.example.gamificationapp.exceptions.RepoException;
import com.example.gamificationapp.exceptions.ValidationException;
import com.example.gamificationapp.repository.RepoPlayer;
import com.example.gamificationapp.repository.RepoPlayerQuests;
import com.example.gamificationapp.repository.RepoQuest;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class Service implements IService{
    private final Validator<Player> playerValidator;
    private final Validator<Quest> questValidator;
    private final RepoPlayer playerRepo;
    private final RepoQuest questRepo;
    private final RepoPlayerQuests playerQuestRepo;
    public Service(Validator<Player> playerValidator, Validator<Quest> questValidator, RepoPlayer playerRepo, RepoQuest questRepo, RepoPlayerQuests playerQuestRepo)
    {
        this.playerValidator=playerValidator;
        this.questValidator=questValidator;
        this.playerRepo=playerRepo;
        this.questRepo=questRepo;
        this.playerQuestRepo=playerQuestRepo;
    }
    @Override
    public void addPlayer(String username, String password) throws ValidationException {
        Player toAdd = new Player(username, password);
        playerValidator.validate(toAdd);
        playerRepo.add(toAdd);
    }

    @Override
    public void removePlayer(Integer id){
        questRepo.removeQuestForUser(id);
        playerQuestRepo.removePlayerQuestForUser(id);
        playerRepo.remove(id);
    }

    @Override
    public Player getPlayer(Integer id){
        return playerRepo.findElement(id);
    }

    @Override
    public Iterable<Player> getAllPlayers() {
        return playerRepo.getAll();
    }

    @Override
    public Player getPlayerByUsername(String username) {
        Iterable<Player> players = getAllPlayers();
        for (Player player : players) {
            String username2 = player.getUsername();
            if (Objects.equals(username, username2))
                return player;
        }
        return null;
    }

    public PlayerQuests existsAnotherPendingQuest(Player p) {
        List<PlayerQuests> playerQuests =
                StreamSupport.stream(playerQuestRepo.getAll().spliterator(), false).toList();
        Optional<PlayerQuests> optionalPlayerQuest = playerQuests.stream()
                .filter(playerQuest -> playerQuest.getIdPlayer() == p.getId() && Objects.equals(playerQuest.getStatus(), "ACCEPTED"))
                .findFirst();

        boolean isPresent = optionalPlayerQuest.isPresent();
        if (isPresent)
            return optionalPlayerQuest.get();
        else return null;
    }

    public PlayerQuests existsPendingQuest(PlayerQuests pq) {
        List<PlayerQuests> playerQuests =
                StreamSupport.stream(playerQuestRepo.getAll().spliterator(), false).toList();
        Optional<PlayerQuests> optionalPlayerQuest = playerQuests.stream()
                .filter(playerQuest -> playerQuest.getIdPlayer() == pq.getIdPlayer() && playerQuest.getIdQuest() == pq.getIdQuest()&& Objects.equals(playerQuest.getStatus(), "ACCEPTED"))
                .findFirst();

        boolean isPresent = optionalPlayerQuest.isPresent();
        if (isPresent)
            return optionalPlayerQuest.get();
        else return null;
    }

    public PlayerQuests existsCompletedQuest(PlayerQuests pq) {
        List<PlayerQuests> playerQuests =
                StreamSupport.stream(playerQuestRepo.getAll().spliterator(), false).toList();
        Optional<PlayerQuests> optionalPlayerQuest = playerQuests.stream()
                .filter(playerQuest -> playerQuest.getIdPlayer() == pq.getIdPlayer() && playerQuest.getIdQuest() == pq.getIdQuest() && Objects.equals(playerQuest.getStatus(), "COMPLETED"))
                .findFirst();

        boolean isPresent = optionalPlayerQuest.isPresent();
        if (isPresent)
            return optionalPlayerQuest.get();
        else return null;
    }

    @Override
    public void addQuest(String description, Integer nrTokens, Integer idPlayer, Integer nrOfGamesNeeded) throws ValidationException, RepoException {
        Player player=getPlayer(idPlayer);
        if(player.getNrOfTokens()>=nrTokens)
        {
            Quest toAdd = new Quest(description,nrTokens,idPlayer,nrOfGamesNeeded);
            questValidator.validate(toAdd);
            questRepo.add(toAdd);
            playerRepo.updateRemoveTokens(player,nrTokens);
        }
        else
        {
            throw new RepoException("You don't have enough tokens");
        }
    }

    @Override
    public Quest getQuest(Integer id){
        return questRepo.findElement(id);
    }

    @Override
    public Iterable<Quest> getAllQuests() {
        return questRepo.getAll();
    }

    @Override
    public void addPlayerQuest(Integer idPlayer, Integer idQuest) throws RepoException {
        PlayerQuests toAdd=new PlayerQuests(idPlayer,idQuest);
        Player player=getPlayer(idPlayer);
        PlayerQuests playerQuest = existsPendingQuest(toAdd);
        PlayerQuests playerQuest2 = existsCompletedQuest(toAdd);
        PlayerQuests playerQuest3 = existsAnotherPendingQuest(player);
        if(playerQuest==null&&playerQuest2==null&&playerQuest3==null)
        {
            playerQuestRepo.add(toAdd);
        }
        else if(playerQuest!=null)
        {
            throw new RepoException("You have already accepted this quest");
        }
        else if (playerQuest2!=null)
        {
            throw new RepoException("You have already completed this quest");
        }
        else {
            throw new RepoException("You can only accept one quest at once");
        }
    }

    @Override
    public void completeQuest(Integer idPlayer, Integer idQuest) throws RepoException {
        Player player=getPlayer(idPlayer);
        Quest quest=getQuest(idQuest);
        PlayerQuests toComplete=new PlayerQuests(idPlayer,idQuest);
        PlayerQuests playerQuest = existsPendingQuest(toComplete);
        PlayerQuests playerQuest2 = existsCompletedQuest(toComplete);
        if(playerQuest2==null&&playerQuest!=null) {
            playerQuestRepo.completePlayerQuest(toComplete);
            playerRepo.updateAddTokens(player, quest.getNrOfWonTokens());
        }
        else if(playerQuest2!=null)
        {
            throw new RepoException("You have already completed this quest");
        }
        else {
            throw new RepoException("You have to accept the quest first");
        }
    }
    @Override
    public void logNewGameForAcceptedQuest(Player player, PlayerQuests playerQuest) {
        playerRepo.updateGames(player);
        playerQuestRepo.updateGameNumber(playerQuest);
    }
    @Override
    public void logNewGame(Player player) {
        playerRepo.updateGames(player);
    }
}
