package application;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class LoadingScreenController {
	
	public ImageView imageView1, imageView2;

	public void initialize() {
		imageView1.setImage(new Image("https://www.clipartqueen.com/image-files/face-silhouette-teenagegirl.png"));
		imageView2.setImage(new Image("https://i.pinimg.com/originals/3f/2c/97/3f2c979b214d06e9caab8ba8326864f3.gif"));
	}

	public void toOpening(MouseEvent event) {
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("opening.fxml"));
		} catch (IOException e) {
			return;
		}
		Scene openingMenu = new Scene(root, 750, 450);
		openingMenu.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		stage.setScene(openingMenu);
		stage.show();
	}
}
