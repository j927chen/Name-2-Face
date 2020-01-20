package application;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

public class OpeningController {
	public ImageView imageView1, imageView2;

	public void initialize() {
		imageView1.setImage(new Image("https://www.clipartqueen.com/image-files/young-male-headsilhouette.png"));
		imageView2.setImage(new Image("https://loading.io/spinners/wave/lg.wave-ball-preloader.gif"));
	}

	public void toSearch(ActionEvent event) {
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("search.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		Scene searchMenu = new Scene(root, 750, 450);
		searchMenu.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		stage.setScene(searchMenu);
		stage.show();
	}

	public void toStatistics(ActionEvent event) {
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("statistics.fxml"));
		} catch (IOException e) {
			return;
		}
		Scene statistics = new Scene(root, 750, 450);
		statistics.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		stage.setScene(statistics);
		stage.show();
	}

	public void toUpload(ActionEvent event) {
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("upload.fxml"));
		} catch (IOException e) {
			return;
		}
		Scene upload = new Scene(root, 750, 450);
		upload.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		stage.setScene(upload);
		stage.show();
	}
}
