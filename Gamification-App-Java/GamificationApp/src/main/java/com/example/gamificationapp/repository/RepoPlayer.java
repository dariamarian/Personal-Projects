package com.example.gamificationapp.repository;

import com.example.gamificationapp.domain.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RepoPlayer implements IRepository<Player, Integer> {
    private static final Logger logger= LogManager.getLogger();
    private final JDBCUtils jdbcUtils;

    public RepoPlayer(Properties properties) {
        logger.info("Initializing RepoPlayer with properties: {} ",properties);
        jdbcUtils=new JDBCUtils(properties);
    }
    @Override
    public void add(Player entity) {
        logger.traceEntry("Saving player {}", entity);
        String query = "INSERT INTO Players(username,password,rank,nr_of_tokens,nr_of_games) VALUES(?,?,?,?,?)";
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setString(1, entity.getUsername());
            statement.setString(2, entity.getPassword());
            statement.setString(3, entity.getRank());
            statement.setInt(4, entity.getNrOfTokens());
            statement.setInt(5, entity.getNrOfGames());

            int result=statement.executeUpdate();
            logger.trace("Saved {} instances", result);
        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
        logger.traceExit();
    }

    @Override
    public void remove(Integer id) {
        logger.traceEntry("Deleting player {}", id);
        String query = "DELETE FROM Players WHERE id_player = ?";

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
    public Player findElement(Integer id) {
        logger.traceEntry();
        String query = "SELECT * FROM Players WHERE id_player=?";
        Player player = null;
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int idPlayer=resultSet.getInt("id_player");
                String username = resultSet.getString("username");
                String pass = resultSet.getString("password");
                String rank = resultSet.getString("rank");
                int nrTokens = resultSet.getInt("nr_of_tokens");
                int nrGames = resultSet.getInt("nr_of_games");
                player=new Player(username,pass);
                player.setId(idPlayer);
                player.setRank(rank);
                player.setNrOfTokens(nrTokens);
                player.setNrOfGames(nrGames);
            }
        } catch (SQLException e) {
            logger.error(e);
            e.printStackTrace();
        }
        logger.traceExit();
        return player;
    }

    @Override
    public Iterable<Player> getAll() {
        logger.traceEntry();
        List<Player> players = new ArrayList<>();

        String query = "SELECT * from Players";
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                int idPlayer=resultSet.getInt("id_player");
                String username = resultSet.getString("username");
                String pass = resultSet.getString("password");
                String rank = resultSet.getString("rank");
                int nrTokens = resultSet.getInt("nr_of_tokens");
                int nrGames = resultSet.getInt("nr_of_games");
                Player player=new Player(username,pass);
                player.setId(idPlayer);
                player.setRank(rank);
                player.setNrOfTokens(nrTokens);
                player.setNrOfGames(nrGames);
                players.add(player);
            }
        } catch (SQLException e) {
            logger.error(e);
            e.printStackTrace();
        }
        logger.traceExit();
        return players;
    }

    public void updateRemoveTokens(Player player, int nrTokens) {
        logger.traceEntry("Updating player {}", player.getId());
        String query = "UPDATE Players SET nr_of_tokens=? WHERE id_player = ?";

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setInt(1, player.getNrOfTokens()-nrTokens);
            statement.setInt(2,player.getId());
            int result=statement.executeUpdate();
            logger.trace("Updated {} instance", result);
        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
        logger.traceExit();
    }

    public void updateAddTokens(Player player, int nrTokens) {
        logger.traceEntry("Updating player {}", player.getId());
        String query = "UPDATE Players SET nr_of_tokens=? WHERE id_player = ?";

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setInt(1, player.getNrOfTokens()+nrTokens);
            statement.setInt(2,player.getId());
            int result=statement.executeUpdate();
            logger.trace("Updated {} instance", result);
        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
        logger.traceExit();
    }

    public void updateGames(Player player) {
        logger.traceEntry("Updating player {}", player.getId());
        String query = "UPDATE Players SET nr_of_games=? WHERE id_player = ?";

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setInt(1, player.getNrOfGames()+1);
            statement.setInt(2, player.getId());
            int result=statement.executeUpdate();
            logger.trace("Updated {} instance", result);
        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
        logger.traceExit();
    }
}
