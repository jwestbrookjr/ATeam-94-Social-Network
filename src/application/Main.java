package application;

import java.util.List;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

@SuppressWarnings("unused")
public class Main extends Application {
	// Store any command-line arguments that were entered.
	// NOTE: this.getParameters().getRaw() will get these also.
	private List<String> args;

	private static final int WINDOW_WIDTH = 300;
	private static final int WINDOW_HEIGHT = 200;
	private static final String APP_TITLE = "";

	@Override
	public void start(Stage primaryStage) throws Exception {
		// Save args example.
		args = this.getParameters().getRaw();

		// Main layout is BorderPane example (top, left, center, right, bottom).
		BorderPane root = new BorderPane();

		Scene mainScene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

		// Add the stuff and set the primary stage
		primaryStage.setTitle(APP_TITLE);
		primaryStage.setScene(mainScene);
		primaryStage.show();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}
}