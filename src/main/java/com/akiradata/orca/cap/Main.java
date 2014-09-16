package com.akiradata.orca.cap;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		Scene root = FXMLLoader.load(getClass().getResource("root.fxml"));
		stage.setScene(root);		
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
