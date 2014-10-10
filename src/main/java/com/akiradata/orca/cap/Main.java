package com.akiradata.orca.cap;

import java.io.File;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Main extends Application {

	File projectFile = null;
	
	@Override
	public void start(Stage stage) throws Exception {
		VBox rootVbox = FXMLLoader.load(getClass().getResource("root.fxml"));		
		Scene root = new Scene(rootVbox);
		stage.setMaximized(true);
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

	@FXML public void newProjectClicked() throws Exception {
		FileChooser fileChooser = new FileChooser();
		File newFile = fileChooser.showSaveDialog(null);
		if (newFile != null){
			this.projectFile = newFile;
			
			
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
