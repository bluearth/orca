package com.akiradata.orca.cap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.imageio.ImageIO;
import javax.xml.bind.JAXBContext;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import com.akiradata.orca.notification.Notification;

public class ImageProcessorController implements Initializable {
	
	final String TEMP_IMAGE_LOCATION = "D:/temp.jpg"; 
	final double TOOLBAR_HEIGHT = 45;
	
	private ResizeDirection resizeDirection;
	private Stage stage;
	private Scene scene;
	private ImageView tempImageView;
	private double x, y, x1, y1, xAfterSetPosition, yAfterSetPosition;
	private Rectangle segmentation = null;
	private Label kordinat = new Label();
	private Label tracker = null;
	
	@FXML private AnchorPane fxAnchorPane;
	@FXML private FlowPane fxFlowPane;
	@FXML private ScrollPane fxScrollPane;
	@FXML private ImageView fxImageView;
	@FXML private TextArea fxTextAreaOcr, fxTextAreaTags, fxTextAreaComments;
	@FXML private TextField fxTextFieldRotate;
	@FXML private TitledPane fxTitledPane;
	@FXML private CheckBox fxCheckBoxIsConcat;
	@FXML private Button fxButtonCapture, fxButtonOk, fxButtonCancel;
	@FXML private ProgressBar fxProgressBar;
	@FXML private Label fxLabelStatus, fxLabelRotateQuestion;

	public ImageProcessorController() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ImageProcessor.fxml"));
        fxmlLoader.setController(this);
        
        BorderPane borderPane = (BorderPane) fxmlLoader.load();
        
		scene = new Scene(borderPane);
		
		scene.widthProperty().addListener(new ChangeListener<Number>() {
		    @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
		        setImagePosition();
		    }
		});
		
		scene.heightProperty().addListener(new ChangeListener<Number>() {
		    @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
		        setImagePosition();
		    }
		});
		
		stage = new Stage();
		stage.setScene(scene);
		
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				File file = new File(TEMP_IMAGE_LOCATION);
				if (file.exists()) {
					file.delete();
				}
			}
		});
		
    }
	
	public void show(Image image) throws IOException {
        fxImageView.setImage(image);
        fxImageView.setFitWidth(fxImageView.getImage().getWidth());
        fxImageView.setFitHeight(fxImageView.getImage().getHeight());
        stage.setTitle("Image Processing - " + image.impl_getUrl().substring(image.impl_getUrl().lastIndexOf("/") + 1)); 
		stage.show();
	}
	
	@Override
	public void initialize(URL url, ResourceBundle resourcebundle) {
		Date ewe =new Date();
		ewe.setDate(11);
		Calendar ewe2 = Calendar.getInstance();
		ewe2.setTime(ewe);
	}
	
	//TODO delete getScrollPaneInformation	, dibuat cuma buat debug. hasil tidak dapat dipertanggungjawabkan
	private void getScrollPaneInformation(ScrollPane scrollPane) {
		Set<Node> nodes = scrollPane.lookupAll(".scroll-bar");
        for (final Node node : nodes) {
            if (node instanceof ScrollBar) {
                ScrollBar sb = (ScrollBar) node;
                if (sb.getOrientation() == Orientation.HORIZONTAL) {
                    System.out.println("horizontal scrollbar visible = " + sb.isVisible());
                    System.out.println("width = " + sb.getWidth());
                    System.out.println("height = " + sb.getHeight());
                    System.out.println("LayoutX" + sb.getLayoutX()) ;
                    System.out.println("LayoutY" + sb.getLayoutY()) ;
                }
            }
        }
	}
	// ============================= [START] Toolbar Listener =========================
	public void rotate() {
		
		removeSegmentation();
		fxImageView.setRotate(new Double(fxTextFieldRotate.getText().equals("")? "0" : fxTextFieldRotate.getText()));
		createTempFile();
		fxScrollPane.setDisable(true);
		setEnabledRotateQuestion(true);
	}
	
	public void onActionButtonZoomIn() {
		removeSegmentation();
		
		Double width = fxImageView.getFitWidth() + 100;
		Double height = fxImageView.getFitHeight() + 100;
		
		fxImageView.setFitWidth(width);
		fxImageView.setFitHeight(height);
		
		setImagePosition();
		createTempFile();
	}
	
	public void onActionButtonZoomOut() {
		removeSegmentation();
		
		Double width = fxImageView.getFitWidth() - 100;
		Double height = fxImageView.getFitHeight() - 100;
		
		fxImageView.setFitWidth(width);
		fxImageView.setFitHeight(height);
		
		setImagePosition();
		createTempFile();
	}
	
	public void onActionButtonOk() {
		fxImageView.setImage(new Image("file:/" + TEMP_IMAGE_LOCATION));
		fxImageView.setRotate(0);
		fxTextFieldRotate.setText("");
		fxScrollPane.setDisable(false);
		setEnabledRotateQuestion(false);
	}
	
	public void onActionButtonCancel() {
		fxImageView.setRotate(0);
		fxTextFieldRotate.setText("");
		fxScrollPane.setDisable(false);
		setEnabledRotateQuestion(false);
	}
	
	public void capture() {
		fxButtonCapture.setDisable(true);
		fxProgressBar.setVisible(true);
		fxLabelStatus.setVisible(false);
		
		Task<?> copyWorker = createWorker();
		new Thread(copyWorker).start();
	}
	
	// ============================= [END] Toolbar Listener ===========================
	
	
	public void mosMoved(MouseEvent me) {
		//TODO: delete tracker, dibuat cuma buat debug. hasil tidak dapat dipertanggungjawabkan
		fxFlowPane.getChildren().remove(tracker);
		tracker = new Label(
				"(x: " + me.getX() + ", y: " + me.getY() + ") -- " + 
				"(sceneX: " + me.getSceneX() + ", sceneY: " + me.getSceneY() + ") -- " + 
				"(screenX: " + me.getScreenX() + ", screenY: " + me.getScreenY() + ")");
		fxFlowPane.getChildren().add(tracker);
		
		// TODO: 8 direction, klo ga males dan ada waktu
		if ((me.getX() <= x1 && me.getX() >= x) && (me.getY() <= y-2 && me.getY() >= y-4)) { // utara
			resizeDirection = ResizeDirection.NORTH;
			scene.setCursor(Cursor.N_RESIZE);
		} else if ((me.getY() <= y1 && me.getY() >= y) && (me.getX() <= x1+4 && me.getX() >= x1+2)) { //timur
			resizeDirection = ResizeDirection.EAST;
			scene.setCursor(Cursor.E_RESIZE);
		} else if ((me.getX() <= x1 && me.getX() >= x) && (me.getY() <= y1+4 && me.getY() >= y1+2)) { //selatan
			resizeDirection = ResizeDirection.SOUTH;
			scene.setCursor(Cursor.S_RESIZE);
		} else if ((me.getY() <= y1 && me.getY() >= y) && (me.getX() <= x-2 && me.getX() >= x-4)) { //barat
			resizeDirection = ResizeDirection.WEST;
			scene.setCursor(Cursor.W_RESIZE);
		} else {
			resizeDirection = ResizeDirection.NONE;
			scene.setCursor(Cursor.DEFAULT);
		}
	}
	
	public void mosPressed(MouseEvent me) {
		if (resizeDirection == 	ResizeDirection.NONE) {
			removeSegmentation();
			x = me.getX();
			y = me.getY();
		}		
		
		scene = fxAnchorPane.getScene();
		me.consume();
	}

	public void mosDragged(MouseEvent me) {
		removeSegmentation();
		
		switch (resizeDirection) {
			case NORTH 	: y  = me.getY(); break;
			case EAST 	: x1 = me.getX(); break;
			case SOUTH 	: y1 = me.getY(); break;
			case WEST 	: x  = me.getX(); break;
			case NONE 	: x1 = me.getX(); y1 = me.getY(); break;
		}
		
		if (me.getX() > fxImageView.getBoundsInParent().getWidth() + xAfterSetPosition) {
			x1 = fxImageView.getBoundsInParent().getWidth() + xAfterSetPosition;
		} else if (me.getX() <= 0 + xAfterSetPosition) {
			x1 = 0 + xAfterSetPosition;
		}
		
		if (me.getY() > fxImageView.getBoundsInParent().getHeight() + yAfterSetPosition) {
			y1 = fxImageView.getBoundsInParent().getHeight() + yAfterSetPosition;
		} else if (me.getY() <= 0 + yAfterSetPosition) {
			y1 = 0 + yAfterSetPosition;
		}
		
		if (me.getSceneX() > scene.getWidth()) {
			if (fxScrollPane.getHvalue() <=0.99) {
				fxScrollPane.setHvalue(fxScrollPane.getHvalue()+0.01);
			}
		} else if (me.getScreenX()-20 <= scene.getWindow().getX()) {
			if (fxScrollPane.getHvalue() >= 0.01) {
				fxScrollPane.setHvalue(fxScrollPane.getHvalue()-0.01);
			}
		}
		
		if (me.getSceneY() > scene.getHeight()) {
			if (fxScrollPane.getVvalue() <=0.99) {
				fxScrollPane.setVvalue(fxScrollPane.getVvalue()+0.01);
			}
		} else if (me.getScreenY()-20 <= scene.getWindow().getY()) {
			if (fxScrollPane.getVvalue() >= 0.01) {
				fxScrollPane.setVvalue(fxScrollPane.getVvalue()-0.01);
			}
		}
			
		segmentation = segmentation(x, y, x1, y1);

		kordinat.setText("(" + x + ", " + y + ", " + x1 + ", " + y1 + ")");
		kordinat.setLayoutX(x1);
		kordinat.setLayoutY(y1);

		fxAnchorPane.getChildren().add(kordinat);
		fxAnchorPane.getChildren().add(segmentation);
	}
	
	public void mosReleased(MouseEvent me) {
		//haha.. this workaround when user make segmentation not from NORTH_WEST to SOUTH_EAST
		double temp;
		if (x1<x) {
			temp=x; x=x1; x1=temp;
		}
		if (y1<y) {
			temp=y; y=y1; y1=temp;
		}
	}
	
	public Task<?> createWorker() {
		return new Task<Object>() {
			@Override
			protected Object call() throws Exception {
				doOCR();
				fxLabelStatus.setVisible(true);
				fxButtonCapture.setDisable(false);
				fxProgressBar.setVisible(false);
				return true;
			}
		};
	}

	public void doOCR() {
		//TODO: modif url gambar, ilangin deprecated
		File imageFile = null;
		
		if(tempImageView != null) {
			imageFile = new File(tempImageView.getImage().impl_getUrl().substring(6));
		} else {
			imageFile = new File(fxImageView.getImage().impl_getUrl().substring(6));
		}
		
		Tesseract instance = Tesseract.getInstance();
		instance.setLanguage("ind");
//		instance.setDatapath("C:\\barkah\\Tess4J");
		
		java.awt.Rectangle awtRectangle = null;
		try {
			if (segmentation != null) {
				awtRectangle = new java.awt.Rectangle(	(int) ((int)segmentation.getX()-xAfterSetPosition),
														(int) ((int)segmentation.getY()-yAfterSetPosition), 
														(int)segmentation.getWidth(), 
														(int)segmentation.getHeight());
				
				String result = instance.doOCR(imageFile, awtRectangle);
				if (fxCheckBoxIsConcat.isSelected()) {
					fxTextAreaOcr.setText(fxTextAreaOcr.getText() + result);
				} else {
					 fxTextAreaOcr.setText(result);
				}
			} else {
				fxTextAreaOcr.setText(instance.doOCR(imageFile));
			}
			fxTitledPane.setExpanded(true);
		} catch (TesseractException e) {
			System.err.println(e.getMessage());
		}
	}
	
	public void clear() {
		fxTextAreaOcr.setText("");
		new Notification(stage, Notification.SUCCESS, "homok itu indah!!!!!!");
	}
	
	public void save() {
		
		String ocr = fxTextAreaOcr.getText();
		String tags[] = fxTextAreaTags.getText().split(";");
		String comments = fxTextAreaComments.getText();
		
		//TODO : save kemana? tanya pak barmo
		System.out.println("pegimana urusannya ini?");
		new Notification(stage, Notification.WARNING, "save kemana? tanya pak barmo");
	}

	private Rectangle segmentation(double x, double y, double x1, double y1) {
		Rectangle rectangle = null;

		if (x <= x1 && y <= y1) {
			rectangle = new Rectangle(x, y, x1 - x, y1 - y);
		} else if (x <= x1 && y >= y1) {
			rectangle = new Rectangle(x, y1, x1 - x, y - y1);
		} else if (x >= x1 && y <= y1) {
			rectangle = new Rectangle(x1, y, x - x1, y1 - y);
		} else if (x >= x1 && y >= y1) {
			rectangle = new Rectangle(x1, y1, x - x1, y - y1);
		}

		rectangle.setFill(Color.web("#8799ff")); // <--- nullin biar tengahnya bolong
		rectangle.getStrokeDashArray().addAll(3d); // <--- dashed - garis putus2
		rectangle.setStroke(Color.BLACK);
		rectangle.setStrokeWidth(1);
		rectangle.setOpacity(0.3);

		return rectangle;
	}
	
	private void createTempFile() {
		WritableImage writeableImage = fxImageView.snapshot(null, null);
		BufferedImage image = SwingFXUtils.fromFXImage(writeableImage, null);
		BufferedImage imageRGB = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.OPAQUE);
		Graphics2D graphics = imageRGB.createGraphics();
		graphics.drawImage(image, 0, 0, null);
		
		try {
			//TODO: set urlnya biar dinamis, ambil dari resourcebundle?
			ImageIO.write(imageRGB, "jpg", new File(TEMP_IMAGE_LOCATION));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		graphics.dispose();
		tempImageView = new ImageView(new Image("file:/" + TEMP_IMAGE_LOCATION));
	}
	
	private void setImagePosition() {
		xAfterSetPosition = (scene.getWidth()/2) - (fxImageView.getFitWidth()/2) >= 0 ? (scene.getWidth()/2) - (fxImageView.getFitWidth()/2) : 0;
		yAfterSetPosition = (scene.getHeight()/2) - (fxImageView.getFitHeight()/2) - TOOLBAR_HEIGHT >= 0 ? (scene.getHeight()/2) - (fxImageView.getFitHeight()/2) - TOOLBAR_HEIGHT : 0;
		fxImageView.setX(xAfterSetPosition);
		fxImageView.setY(yAfterSetPosition);
	}
	
	
	private void removeSegmentation() {
		fxAnchorPane.getChildren().remove(kordinat);
		fxAnchorPane.getChildren().remove(segmentation);
		segmentation = null;
	}
	
	private void setEnabledRotateQuestion(boolean flag) {
		fxLabelRotateQuestion.setVisible(flag);
		fxButtonOk.setVisible(flag);
		fxButtonCancel.setVisible(flag);
	}
	
	public enum ResizeDirection {
	    NONE, NORTH, EAST, SOUTH, WEST
	}
	
	
}

// ngesave kemana?
// flow aplikasinya gmn sih? scanner naro hasil disuatu tempat, trus dimakan sama dms 
// bantu SDK