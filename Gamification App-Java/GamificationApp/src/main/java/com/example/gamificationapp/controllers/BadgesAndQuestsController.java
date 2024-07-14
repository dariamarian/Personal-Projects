package com.example.gamificationapp.controllers;

import com.example.gamificationapp.Main;
import com.example.gamificationapp.domain.Player;
import com.example.gamificationapp.domain.Quest;
import com.example.gamificationapp.service.IService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BadgesAndQuestsController {
    IService service;
    private Player loggedPlayer=null;
    @FXML
    private ImageView threeGamesImage;
    @FXML
    private ImageView tenGamesImage;
    @FXML
    private ImageView thirtyGamesImage;
    @FXML
    private ImageView hundredGamesImage;
    @FXML
    private ImageView tenTokensImage;
    @FXML
    private ImageView hundredTokensImage;
    @FXML
    private ImageView thousandTokensImage;
    @FXML
    private ImageView fivethousandTokensImage;
    ObservableList<Quest> questsList = FXCollections.observableArrayList();
    @FXML
    private TableView<Quest> questsTable;
    @FXML
    TableColumn<Quest,String> descriptionColumn;
    @FXML
    TableColumn<Quest,Integer> tokensColumn;
    @FXML
    TableColumn<Quest,Integer> gamesColumn;
    @FXML
    private TextField descriptionTextField;
    @FXML
    private TextField tokensTextField;
    @FXML
    private TextField gamesTextField;
    @FXML
    public Button addQuestButton;
    @FXML
    public Button acceptQuestButton;
    @FXML
    public Button BackButton;
    public void SetService(IService service, Player player) {
        this.service = service;
        this.loggedPlayer = player;
        initializeBadges();
        init_lists();
        initializeQuests();
    }
    @FXML
    public void initializeBadges()
    {
        threeGamesImage.setOpacity(0.5);
        tenGamesImage.setOpacity(0.5);
        thirtyGamesImage.setOpacity(0.5);
        hundredGamesImage.setOpacity(0.5);
        tenTokensImage.setOpacity(0.5);
        hundredTokensImage.setOpacity(0.5);
        thousandTokensImage.setOpacity(0.5);
        fivethousandTokensImage.setOpacity(0.5);
        if(loggedPlayer.getNrOfGames()>3)
            threeGamesImage.setOpacity(1);
        if(loggedPlayer.getNrOfGames()>10)
            tenGamesImage.setOpacity(1);
        if(loggedPlayer.getNrOfGames()>30)
            thirtyGamesImage.setOpacity(1);
        if(loggedPlayer.getNrOfGames()>100)
            hundredGamesImage.setOpacity(1);
        if(loggedPlayer.getNrOfTokens()>10)
            tenTokensImage.setOpacity(1);
        if(loggedPlayer.getNrOfTokens()>100)
            hundredTokensImage.setOpacity(1);
        if(loggedPlayer.getNrOfTokens()>1000)
            thousandTokensImage.setOpacity(1);
        if(loggedPlayer.getNrOfTokens()>5000)
            fivethousandTokensImage.setOpacity(1);
    }

    @FXML
    public void init_lists() {
        Iterable<Quest> quests = service.getAllQuests();
        List<Quest> questsTemp = new ArrayList<>();
        for (Quest quest : quests) {
            if(quest.getIdPlayer()!=loggedPlayer.getId())
                questsTemp.add(quest);
        }
        questsList.setAll(questsTemp);
    }
    @FXML
    public void initializeQuests() {
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        tokensColumn.setCellValueFactory(new PropertyValueFactory<>("nrOfWonTokens"));
        gamesColumn.setCellValueFactory(new PropertyValueFactory<>("nrOfGamesNeeded"));
        questsTable.setItems(questsList);
    }
    @FXML
    public void onAddQuestButtonAction()
    {
        try {
            String description=descriptionTextField.getText();
            int tokens=Integer.parseInt(tokensTextField.getText());
            int nrOfGamesNeeded=Integer.parseInt(gamesTextField.getText());
            service.addQuest(description,tokens,loggedPlayer.getId(),nrOfGamesNeeded);
            loggedPlayer.setNrOfTokens(loggedPlayer.getNrOfTokens()-tokens);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Quest added successfully", ButtonType.OK);
            alert.show();
        } catch (NullPointerException npe) {
            Alert alert = new Alert(Alert.AlertType.WARNING, npe.getMessage(), ButtonType.OK);
            alert.show();
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
            alert.show();
        }
    }
    @FXML
    public void onAcceptQuestButtonAction()
    {
        try
        {
            Quest quest = questsTable.getSelectionModel().getSelectedItem();
            service.addPlayerQuest(loggedPlayer.getId(), quest.getId());
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Quest accepted successfully", ButtonType.OK);
            alert.show();
        } catch (NullPointerException npe) {
            Alert alert = new Alert(Alert.AlertType.WARNING, npe.getMessage(), ButtonType.OK);
            alert.show();
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
            alert.show();
        }
    }
    @FXML
    public void onBackButtonAction(){
        FXMLLoader loader=new FXMLLoader(Main.class.getResource("MainInterface.fxml"));
        Scene scene;
        try{
            scene = new Scene(loader.load(), 1000, 600);
            scene.getStylesheets().addAll(Objects.requireNonNull(Main.class.getResource("style.css")).toExternalForm());
            MainController controller = loader.getController();
            controller.SetService(service,loggedPlayer);

            Stage currentStage=(Stage) BackButton.getScene().getWindow();

            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.setResizable(false);
            newStage.setTitle("Main");
            currentStage.close();
            newStage.show();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
