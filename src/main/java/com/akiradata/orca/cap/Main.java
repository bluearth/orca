package com.akiradata.orca.cap;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		Scene root = FXMLLoader.load(getClass().getResource("root.fxml"));
		stage.setTitle("ORCA v0.1");
		stage.setScene(root);		
		stage.show();
	}
	
	public void onAction() throws IOException {
       Scene secondScene = FXMLLoader.load(getClass().getResource("CreateNewProject.fxml"));

       Stage secondStage = new Stage();
       secondStage.setTitle("Create New Project");
       secondStage.setScene(secondScene);
        
       //Set position of second window, related to primary window.
       secondStage.setX(100 + 250);
       secondStage.setY(100 + 100);

       secondStage.show();
    }

	public static void main(String[] args) {
		launch(args);
	}
}
