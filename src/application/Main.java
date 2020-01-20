package application;

import java.io.IOException;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

public class Main extends Application {
	@Override
	public void start(Stage stage) {
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("loadingScreen.fxml"));
		} catch (IOException e) {
			return;
		}
		Scene connectionPage = new Scene(root, 750, 450);
		connectionPage.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		stage.setScene(connectionPage);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
