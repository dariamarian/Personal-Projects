package com.example.gamificationapp;

import com.example.gamificationapp.controllers.LogInSignupController;
import com.example.gamificationapp.domain.Player;
import com.example.gamificationapp.domain.Quest;
import com.example.gamificationapp.domain.validators.PlayerValidator;
import com.example.gamificationapp.domain.validators.QuestValidator;
import com.example.gamificationapp.domain.validators.Validator;
import com.example.gamificationapp.repository.RepoPlayer;
import com.example.gamificationapp.repository.RepoPlayerQuests;
import com.example.gamificationapp.repository.RepoQuest;
import com.example.gamificationapp.service.IService;
import com.example.gamificationapp.service.Service;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        Properties properties=new Properties();
        try{
            properties.load(new FileReader("bd.config"));

            Validator<Player> validatorP = PlayerValidator.getInstance();
            Validator<Quest> validatorQ = QuestValidator.getInstance();
            RepoPlayer repoPlayer =new RepoPlayer(properties);
            RepoQuest repoQuest =new RepoQuest(properties);
            RepoPlayerQuests repoPlayerQuest=new RepoPlayerQuests(properties);
            IService service = new Service(validatorP,validatorQ,repoPlayer,repoQuest,repoPlayerQuest);

            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("LogInSignUp.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 210, 500);
            LogInSignupController controller=fxmlLoader.getController();
            controller.SetService(service);
            stage.setTitle("LogInSignUp");
            stage.setScene(scene);
            stage.show();
        }
        catch(IOException ex) {
            System.out.println("Cannot find bd.config");
        }
    }

    public static void main(String[] args) {
        launch();
    }
}