package com.example.gamificationapp.controllers;

import com.example.gamificationapp.domain.Player;
import com.example.gamificationapp.domain.PlayerQuests;
import com.example.gamificationapp.domain.Quest;
import com.example.gamificationapp.exceptions.RepoException;
import com.example.gamificationapp.service.IService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class TicTacToeController implements Initializable {

    IService service;
    private Player loggedPlayer=null;

    @FXML
    private Button button1;

    @FXML
    private Button button2;

    @FXML
    private Button button3;

    @FXML
    private Button button4;

    @FXML
    private Button button5;

    @FXML
    private Button button6;

    @FXML
    private Button button7;

    @FXML
    private Button button8;

    @FXML
    private Button button9;

    @FXML
    private Text winnerText;

    private int playerTurn = 0;

    ArrayList<Button> buttons;

    public void SetService(IService service, Player player) {
        this.service = service;
        this.loggedPlayer = player;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttons = new ArrayList<>(Arrays.asList(button1,button2,button3,button4,button5,button6,button7,button8,button9));

        buttons.forEach(button ->{
            setupButton(button);
            button.setFocusTraversable(false);
        });
    }

    @FXML
    void restartGame() {
        buttons.forEach(this::resetButton);
        winnerText.setText("Tic-Tac-Toe");
    }

    public void resetButton(Button button){
        button.setDisable(false);
        button.setText("");
    }

    private void setupButton(Button button) {
        button.setOnMouseClicked(mouseEvent -> {
            setPlayerSymbol(button);
            button.setDisable(true);
            try {
                checkIfGameIsOver();
            } catch (RepoException | SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void setPlayerSymbol(Button button){
        if(playerTurn % 2 == 0){
            button.setText("X");
            playerTurn = 1;
        } else{
            button.setText("O");
            playerTurn = 0;
        }
    }

    public void checkIfGameIsOver() throws RepoException, SQLException {
        for (int a = 0; a < 8; a++) {
            String line = switch (a) {
                case 0 -> button1.getText() + button2.getText() + button3.getText();
                case 1 -> button4.getText() + button5.getText() + button6.getText();
                case 2 -> button7.getText() + button8.getText() + button9.getText();
                case 3 -> button1.getText() + button5.getText() + button9.getText();
                case 4 -> button3.getText() + button5.getText() + button7.getText();
                case 5 -> button1.getText() + button4.getText() + button7.getText();
                case 6 -> button2.getText() + button5.getText() + button8.getText();
                case 7 -> button3.getText() + button6.getText() + button9.getText();
                default -> null;
            };

            //X winner
            if (line.equals("XXX")) {
                winnerText.setText("X won!");
                button1.setDisable(true);
                button2.setDisable(true);
                button3.setDisable(true);
                button4.setDisable(true);
                button5.setDisable(true);
                button6.setDisable(true);
                button7.setDisable(true);
                button8.setDisable(true);
                button9.setDisable(true);
                PlayerQuests playerQuest = service.existsAnotherPendingQuest(loggedPlayer);
                if(playerQuest!=null)
                {
                    Quest quest=service.getQuest(playerQuest.getIdQuest());
                    int actualNrOfGames=playerQuest.getNrOfGames();
                    int nrOfGamesNeeded=quest.getNrOfGamesNeeded();
                    if(actualNrOfGames+1==nrOfGamesNeeded)
                    {
                        service.logNewGameForAcceptedQuest(loggedPlayer,playerQuest);
                        loggedPlayer.setNrOfGames(loggedPlayer.getNrOfGames()+1);
                        service.completeQuest(loggedPlayer.getId(),quest.getId());
                        loggedPlayer.setNrOfTokens(loggedPlayer.getNrOfTokens()+quest.getNrOfWonTokens());
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Quest completed! Congrats!", ButtonType.OK);
                        alert.show();
                        return;
                    }
                    service.logNewGameForAcceptedQuest(loggedPlayer,playerQuest);
                    loggedPlayer.setNrOfGames(loggedPlayer.getNrOfGames()+1);
                }
                else
                {
                    service.logNewGame(loggedPlayer);
                    loggedPlayer.setNrOfGames(loggedPlayer.getNrOfGames()+1);
                }
            }

            //O winner
            else if (line.equals("OOO")) {
                winnerText.setText("O won!");
                button1.setDisable(true);
                button2.setDisable(true);
                button3.setDisable(true);
                button4.setDisable(true);
                button5.setDisable(true);
                button6.setDisable(true);
                button7.setDisable(true);
                button8.setDisable(true);
                button9.setDisable(true);
            }
        }
    }
}