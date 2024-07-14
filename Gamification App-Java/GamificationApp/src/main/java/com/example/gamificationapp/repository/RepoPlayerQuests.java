package com.example.gamificationapp.repository;

import com.example.gamificationapp.domain.PlayerQuests;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RepoPlayerQuests implements IRepository<PlayerQuests,Integer>{
    private static final Logger logger = LogManager.getLogger();
    private final JDBCUtils jdbcUtils;

    public RepoPlayerQuests(Properties properties) {
        logger.info("Initializing RepoPlayerQuests with properties: {} ", properties);
        jdbcUtils = new JDBCUtils(properties);
    }

    @Override
    public void add(PlayerQuests entity) {
        logger.traceEntry("Saving player badge {}", entity);
        String query = "INSERT INTO PlayerQuests(id_player,id_quest,status,nr_of_games) VALUES(?,?,?,?)";
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setInt(1, entity.getIdPlayer());
            statement.setInt(2, entity.getIdQuest());
            statement.setString(3,entity.getStatus());
            statement.setInt(4,entity.getNrOfGames());
            int result = statement.executeUpdate();
            logger.trace("Saved {} instances", result);
        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
        logger.traceExit();
    }

    @Override
    public void remove(Integer id) {}

    public void removePlayerQuestForUser(Integer id) {
        logger.traceEntry("Deleting player quest {}", id);
        String query = "DELETE FROM PlayerQuests WHERE id_player = ?";

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setInt(1, id);
            int result=statement.executeUpdate();
            logger.trace("Deleted {} instance", result);
        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
        logger.traceExit();
    }

    @Override
    public PlayerQuests findElement(Integer id) { return null;}

    @Override
    public Iterable<PlayerQuests> getAll() {
        logger.traceEntry();
        List<PlayerQuests> playerQuests = new ArrayList<>();

        String query = "SELECT * from PlayerQuests";
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                int idPlayer = resultSet.getInt("id_player");
                int idQuest = resultSet.getInt("id_quest");
                String status=resultSet.getString("status");
                int nrOfGames=resultSet.getInt("nr_of_games");
                PlayerQuests playerQuest=new PlayerQuests(idPlayer,idQuest);
                playerQuest.setStatus(status);
                playerQuest.setNrOfGames(nrOfGames);
                playerQuests.add(playerQuest);
            }
        } catch (SQLException e) {
            logger.error(e);
            e.printStackTrace();
        }
        logger.traceExit();
        return playerQuests;
    }

    public void completePlayerQuest(PlayerQuests toComplete) {
        logger.traceEntry("Updating player quest {}", toComplete);
        String query = "UPDATE PlayerQuests SET status=? WHERE id_player = ? and id_quest = ?";

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            toComplete.completePlayerQuest();
            statement.setString(1,toComplete.getStatus());
            statement.setInt(2, toComplete.getIdPlayer());
            statement.setInt(3,toComplete.getIdQuest());
            int result=statement.executeUpdate();
            logger.trace("Updated {} instance", result);
        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
        logger.traceExit();
    }

    public void updateGameNumber(PlayerQuests toComplete)
    {
        logger.traceEntry("Updating player quest {}", toComplete);
        String query = "UPDATE PlayerQuests SET nr_of_games=? WHERE id_player = ? and id_quest = ?";

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            toComplete.completePlayerQuest();
            statement.setInt(1,toComplete.getNrOfGames()+1);
            statement.setInt(2, toComplete.getIdPlayer());
            statement.setInt(3,toComplete.getIdQuest());
            int result=statement.executeUpdate();
            logger.trace("Updated {} instance", result);
        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
        logger.traceExit();
    }
}
