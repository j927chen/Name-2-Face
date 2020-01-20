package application;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class StudentProfileController {
	public ImageView image;
	public Label id;
	public TextField firstName, lastName, email;
	public TextArea description;
	public ListView<String> listView;

	public void initializeProfile(String firstName, String lastName, Image image, int id, String description,
			String email) {
		this.firstName.setText(firstName);
		this.lastName.setText(lastName);
		this.email.setText(email);
		this.description.setText(description);
		this.id.setText(id + "");
		this.image.setImage(image);
		ArrayList<String> classCodes = Database.getClassCodes();
		listView.setItems(FXCollections.observableArrayList(classCodes));
		listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		for (int i = 0; i < classCodes.size(); i++) {
			if (Database.getStudent(id, classCodes.get(i)) != null)
				listView.getSelectionModel().select(i);
		}
	}

	public void back(ActionEvent event) {
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("search.fxml"));
		Parent root;
		try {
			root = loader.load();
		} catch (IOException e) {
			return;
		}
		Scene search = new Scene(root, 750, 450);
		search.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		stage.setScene(search);
	}

	public void updateStudentInfo(ActionEvent event) {
		Database.updateStudentInfo(Integer.parseInt(id.getText()), firstName.getText().toLowerCase().trim(),
				lastName.getText().toLowerCase().trim(), description.getText(), email.getText());
		Database.unenrollStudent(Integer.parseInt(id.getText()));
		for (int i = 0; i < listView.getSelectionModel().getSelectedItems().size(); i++) {
			Database.enrollStudent(Integer.parseInt(id.getText()),
					listView.getSelectionModel().getSelectedItems().get(i));
		}
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Updated");
		alert.setContentText("Information has been updated.");
		alert.showAndWait();
	}

	public void updateImage(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		File selectedFile = fileChooser.showOpenDialog(null);
		if (selectedFile != null) // Error handling
		{
			String extension = selectedFile.getName().substring(selectedFile.getName().lastIndexOf(".") + 1,
					selectedFile.getName().length());
			if (extension.contentEquals("jpg") || extension.contentEquals("jpeg")) {
				Image image = new Image(selectedFile.toURI().toString()); // reading file (image)
				String format = "jpg";
				File file = null;
				try {
					file = new File((Database.STUDENT_IMAGES_PATHWAY + id.getText() + ".jpg").replace("file:", ""));
					ImageIO.write(SwingFXUtils.fromFXImage(image, null), format, file); // writing file (image)
					this.image.setImage(image);
				} catch (IOException e) {
				}
			} else {
				// Informative error message
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Error!");
				alert.setContentText("Not a valid file! Choose .jpg or .jpeg files.");
				alert.showAndWait();
			}
		}
	}

	public void deleteStudent(ActionEvent event) {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.showAndWait();
		if (alert.getResult() == alert.getButtonTypes().get(0)) {
			Database.deleteStudent(Integer.parseInt(id.getText()));
			back(event);
		}
	}
}
