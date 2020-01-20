package application;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.imageio.ImageIO;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class UploadController {

	// intuitive GUI
	public TextField id, firstName, lastName, code, email, description;
	public Label fileName;
	private File file;

	public void back(ActionEvent event) {
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("opening.fxml"));
		Parent root;
		try {
			root = loader.load();
		} catch (IOException e) {
			return;
		}
		Scene opening = new Scene(root, 750, 450);
		opening.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		stage.setScene(opening);
	}

	public void upload(ActionEvent event) {

		FileChooser fileChooser = new FileChooser();
		file = fileChooser.showOpenDialog(null);
		if (file != null) {
			String extension = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length());
			if (extension.contentEquals("jpg") || extension.contentEquals("jpeg")) {
				fileName.setText(file.getName());
			} else {
				file = null;
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Error!");
				alert.setContentText("Not a valid file! Choose .jpg or .jpeg files.");
				alert.showAndWait();
			}
		}
	}

	public void save(ActionEvent event) throws URISyntaxException {
		Alert alert = new Alert(AlertType.INFORMATION);
		try {
			if (Database.isUniqueId(Integer.parseInt(id.getText()))) {
				if (file != null) {
					Database.newStudent(Integer.parseInt(id.getText()), firstName.getText().toLowerCase().trim(),
							lastName.getText().toLowerCase().trim(), description.getText(), email.getText());
					try {
						BufferedImage image = ImageIO.read(file);
						String format = "jpg";
						File outputFile = new File(
								(Database.STUDENT_IMAGES_PATHWAY + id.getText() + ".jpg").replace("file:", ""));
						ImageIO.write(image, format, outputFile);
						alert.setTitle("Success!");
						alert.setContentText("A new student has been registered.");
						alert.showAndWait();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					alert.setTitle("Error!");
					alert.setContentText("Must choose valid file!");
					alert.showAndWait();
				}
			} else {
				alert.setTitle("Error!");
				alert.setContentText("Student ID number already taken");
				alert.showAndWait();
			}
		} catch (NumberFormatException e) {
			alert.setTitle("Error!");
			alert.setContentText("Student ID number not valid!");
			alert.showAndWait();
		}
	}

	public void newClass(ActionEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION);
		if (!code.getText().isEmpty()) {
			if (Database.isUniqueCode(code.getText())) {
				Database.newClass(code.getText());
				alert.setTitle("Success!");
				alert.setContentText("New class created!");

				alert.showAndWait();
			} else {
				alert.setTitle("Error!");
				alert.setContentText("Class code aleady taken!");
				alert.showAndWait();
			}
		} else {
			alert.setTitle("Error!");
			alert.setContentText("Class code not valid!");
			alert.showAndWait();
		}
	}
}
