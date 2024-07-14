package com.example.gamificationapp.repository;

import com.example.gamificationapp.domain.Quest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RepoQuest implements IRepository<Quest, Integer>{
    private static final Logger logger= LogManager.getLogger();
    private final JDBCUtils jdbcUtils;

    public RepoQuest(Properties properties) {
        logger.info("Initializing RepoQuest with properties: {} ",properties);
        jdbcUtils=new JDBCUtils(properties);
    }
    @Override
    public void add(Quest entity) {
        logger.traceEntry("Saving quest {}", entity);
        String query = "INSERT INTO Quests(description,nr_of_won_tokens,id_player,nr_of_games_needed) VALUES(?,?,?,?)";
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setString(1, entity.getDescription());
            statement.setInt(2, entity.getNrOfWonTokens());
            statement.setInt(3, entity.getIdPlayer());
            statement.setInt(4, entity.getNrOfGamesNeeded());

            int result=statement.executeUpdate();
            logger.trace("Saved {} instances", result);
        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
        logger.traceExit();
    }

    @Override
    public void remove(Integer id){}

    public void removeQuestForUser(Integer id) {
        logger.traceEntry("Deleting quest{}", id);
        String query = "DELETE FROM Quests WHERE id_player = ?";

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
    public Quest findElement(Integer id) {
        logger.traceEntry();
        String query = "SELECT * FROM Quests WHERE id_quest=?";
        Quest quest = null;
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int idQuest=resultSet.getInt("id_quest");
                String description = resultSet.getString("description");
                int nrTokens = resultSet.getInt("nr_of_won_tokens");
                int idPlayer = resultSet.getInt("id_player");
                int nrGames = resultSet.getInt("nr_of_games_needed");
                quest=new Quest(description,nrTokens,idPlayer,nrGames);
                quest.setId(idQuest);
            }
        } catch (SQLException e) {
            logger.error(e);
            e.printStackTrace();
        }
        logger.traceExit();
        return quest;
    }

    @Override
    public Iterable<Quest> getAll() {
        logger.traceEntry();
        List<Quest> quests = new ArrayList<>();

        String query = "SELECT * from Quests";
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                int idQuest=resultSet.getInt("id_quest");
                String description = resultSet.getString("description");
                int nrTokens = resultSet.getInt("nr_of_won_tokens");
                int idPlayer = resultSet.getInt("id_player");
                int nrGames = resultSet.getInt("nr_of_games_needed");
                Quest quest=new Quest(description,nrTokens,idPlayer,nrGames);
                quest.setId(idQuest);
                quests.add(quest);
            }
        } catch (SQLException e) {
            logger.error(e);
            e.printStackTrace();
        }
        logger.traceExit();
        return quests;
    }
}
