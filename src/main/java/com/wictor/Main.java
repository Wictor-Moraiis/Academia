package com.wictor;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Application;
import com.wictor.dao.UserDao;


public class Main extends Application {
    public void start(Stage stage) throws Exception {
        UserDao dao = new UserDao();
        if(dao.existeUser()){
            Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
            Scene scene = new Scene(root);
            stage.setTitle("Login");
            stage.setScene(scene);
            stage.show();

        }else {
        Parent root = FXMLLoader.load(getClass().getResource("/view/cadUser.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Minha Nova Tela");
        stage.setScene(scene);
        stage.show();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}