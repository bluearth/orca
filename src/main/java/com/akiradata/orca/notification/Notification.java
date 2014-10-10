package com.akiradata.orca.notification;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.JDialog;

public class Notification extends JDialog {
	
	private static final long serialVersionUID = 1L;
	
	public static final int INFO = 0;
	public static final int SUCCESS = 1;
	public static final int WARNING = 2;
	public static final int ERROR = 3;
	static final String IMAGE_DIRECTORY = "/com/akiradata/orca/notification/";

	@FXML private Label fxLabelTitle, fxLabelMessage; 
	@FXML private ImageView fxImageViewLogo;
	
	VBox vbox;
	
	public Notification(Stage stage, int type) {
		new Notification(stage, type, "");
	}
	
	public Notification(Stage stage, int type, String message) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Notification.fxml"));
	        fxmlLoader.setController(this);
	        vbox = (VBox) fxmlLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		if (type == INFO) {
			fxLabelTitle.setText("Information");
			fxImageViewLogo.setImage(new Image(vbox.getClass().getResource(IMAGE_DIRECTORY + "info.png").toExternalForm()));
		} else if (type == SUCCESS) {
			fxLabelTitle.setText("Success");
			fxImageViewLogo.setImage(new Image(vbox.getClass().getResource(IMAGE_DIRECTORY + "success.png").toExternalForm()));
		} else if (type == WARNING) {
			fxLabelTitle.setText("Warning");
			fxImageViewLogo.setImage(new Image(vbox.getClass().getResource(IMAGE_DIRECTORY + "warning.png").toExternalForm()));
		} else if (type == ERROR) {
			fxLabelTitle.setText("Error");
			fxImageViewLogo.setImage(new Image(vbox.getClass().getResource(IMAGE_DIRECTORY + "error.png").toExternalForm()));
		}
		
		if (!message.equals("")) {
			fxLabelMessage.setText(message);
		}

		show(stage);
	}

	private void show(Stage stage) {
		
		final Scene scene = new Scene(vbox);
		final JFXPanel fxPanel = new JFXPanel();
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				fxPanel.setScene(scene);
			}
		});
		
		FadeTransition fadeIn = new FadeTransition(Duration.millis(1000), vbox);
		fadeIn.setFromValue(0.0);
		fadeIn.setToValue(1.0);
		fadeIn.play();
		
		new Timer().schedule(new TimerTask() {
		    public void run () { 
		    	FadeTransition fadeOut = new FadeTransition(Duration.millis(1000), vbox);
				fadeOut.setFromValue(1.0);
				fadeOut.setToValue(0.0);
				fadeOut.play();
				
		    }
		}, 2000); 
		
		new Timer().schedule(new TimerTask() {
		    public void run () { 
		    	System.gc();
				dispose();
		    }
		}, 3000); 

		add(fxPanel);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setSize(200, 100);
		setLocation((int) (stage.getX() + stage.getWidth() - 200d - 25d), (int) (stage.getY() + 75));
		setUndecorated(true);
		setOpacity(0.8f);
		setAlwaysOnTop(true);
		setVisible(true);
	}
	
	
}
