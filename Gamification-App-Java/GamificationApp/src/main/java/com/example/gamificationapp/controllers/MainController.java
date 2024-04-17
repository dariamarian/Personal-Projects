package com.example.gamificationapp.controllers;

import com.example.gamificationapp.Main;
import com.example.gamificationapp.domain.Player;
import com.example.gamificationapp.service.IService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.Objects;


public class MainController {
    IService service;
    private Player loggedPlayer=null;
    @FXML
    public Label loggedPlayerUsername;
    @FXML
    public Label loggedPlayerTokens;
    @FXML
    public Label loggedPlayerRank;
    @FXML
    public Button LogOutButton;
    @FXML
    public Button StartGameButton;
    @FXML
    public Button SeeBadgesAndQuestsButton;
    @FXML
    public Button deleteAccountButton;

    public void SetService(IService service, Player player) {
        this.service = service;
        this.loggedPlayer = player;
        init();
        loggedPlayerUsername.setText(loggedPlayer.getUsername());
        loggedPlayerTokens.setText(String.valueOf(loggedPlayer.getNrOfTokens()));
        loggedPlayerRank.setText(loggedPlayer.getRank());
    }
    @FXML
    public void init()
    {
        if(loggedPlayer.getNrOfTokens()>100)
            loggedPlayer.setRank("SILVER");
        if(loggedPlayer.getNrOfTokens()>500)
            loggedPlayer.setRank("GOLD");
        if(loggedPlayer.getNrOfTokens()>1000)
            loggedPlayer.setRank("PLATINUM");
    }
    @FXML
    public void onLogoutButtonAction(){
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("LogInSignUp.fxml"));
        Scene scene;
        try{
            scene = new Scene(loader.load(), 210, 500);
        }
        catch (Exception ex){
            ex.printStackTrace();
            return;
        }
        LogInSignupController controller=loader.getController();
        controller.SetService(service);
        Stage currentStage= (Stage) LogOutButton.getScene().getWindow();

        Stage newStage = new Stage();
        newStage.setScene(scene);
        newStage.setResizable(false);
        newStage.setTitle("LogIn");
        currentStage.close();
        newStage.show();
    }

    @FXML
    public void onSeeBadgesAndQuestsButtonAction() {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("BadgesAndQuests.fxml"));
        Scene scene;
        try{
            scene = new Scene(loader.load(), 675, 430);
            scene.getStylesheets().addAll(Objects.requireNonNull(Main.class.getResource("badgesStyle.css")).toExternalForm());
        }
        catch (Exception ex){
            ex.printStackTrace();
            return;
        }
        BadgesAndQuestsController controller=loader.getController();
        controller.SetService(service,loggedPlayer);
        Stage currentStage= (Stage) SeeBadgesAndQuestsButton.getScene().getWindow();

        Stage newStage = new Stage();
        newStage.setScene(scene);
        newStage.setResizable(false);
        newStage.setTitle("Badges and Quests");
        currentStage.close();
        newStage.show();
    }
    @FXML
    public void onStartGameButtonAction() {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("TicTacToe.fxml"));
        Scene scene;
        try{
            scene = new Scene(loader.load(), 675, 430);
        }
        catch (Exception ex){
            ex.printStackTrace();
            return;
        }
        TicTacToeController controller=loader.getController();
        controller.SetService(service,loggedPlayer);

        Stage newStage = new Stage();
        newStage.setScene(scene);
        newStage.setResizable(false);
        newStage.setTitle("TicTacToe");
        newStage.show();
    }

    @FXML
    public void onDeleteAccountButtonAction() {
        try {
            service.removePlayer(loggedPlayer.getId());
            onLogoutButtonAction();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Account deleted successfully", ButtonType.OK);
            alert.show();

        } catch (NullPointerException npe) {
            Alert alert = new Alert(Alert.AlertType.WARNING, npe.getMessage(), ButtonType.OK);
            alert.show();
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
            alert.show();
        }
    }
}