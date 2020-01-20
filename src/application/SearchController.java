package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class SearchController {
	public ListView<ImageView> listView = new ListView<ImageView>();
	public ListView<String> classListView = new ListView<String>();
	public TextField textField;
	static final String allClasses = "All Classes";

	public void initialize() {
		getAllStudents();
		ArrayList<String> classes = Database.getClassCodes();
		classes.add(allClasses);
		classListView.setItems(FXCollections.observableArrayList(classes));
		classListView.getSelectionModel().selectLast();
	}

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

	public void selectedImage(MouseEvent event) {
		Student student;
		try {
			student = Database.getAllInfo((Student) listView.getSelectionModel().getSelectedItem());
		} catch (NullPointerException e) {
			return;
		}
		if (student == null)
			return;
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("studentProfile.fxml"));
		Parent root;
		try {
			root = loader.load();
		} catch (IOException e) {
			return;
		}
		Scene studentProfile = new Scene(root, 750, 450);
		studentProfile.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		stage.setScene(studentProfile);
		StudentProfileController controller = loader.getController();
		// object-oriented design makes the passing of information across scenes
		// efficient
		controller.initializeProfile(student.getFirstName(), student.getLastName(), student.getImage(),
				student.getStudentId(), student.getDescription(), student.getEmail());
		stage.show();
	}

	public void search(Event event) {
		if (textField.getText().isEmpty()) {
			// polymorphism: listView is <ImageView> parameterized. The method
			// sortStudents(List<Student> list) returns an array list of Student (which
			// inherits from ImageView)
			listView.setItems(FXCollections.observableArrayList(resizeImages(
					sortStudents(Database.getStudent(classListView.getSelectionModel().getSelectedItem())))));
		} else {
			try {
				ArrayList<Student> student = new ArrayList<Student>();
				student.add(Database.getStudent(Integer.parseInt(textField.getText().trim()),
						classListView.getSelectionModel().getSelectedItem()));
				if (student.get(0) != null)
					listView.setItems(FXCollections.observableArrayList(resizeImages(student)));
			} catch (NumberFormatException numberFormatException) {
				try {
					listView.setItems(FXCollections
							.observableArrayList(sortStudents(resizeImages(Database.getStudent(((textField.getText())),
									classListView.getSelectionModel().getSelectedItem())))));
				} catch (NullPointerException nullPointerException) {
					listView.setItems(null);
				}
			}
		}
	}

	private void getAllStudents() {
		listView.setItems(FXCollections.observableArrayList(sortStudents(resizeImages(Database.getAllStudents()))));
	}

	private ArrayList<Student> resizeImages(ArrayList<Student> student) {
		final int DEFAULT_IMAGE_WIDTH = 150;
		for (int i = 0; i < student.size(); i++) {
			student.get(i).setFitWidth(DEFAULT_IMAGE_WIDTH);
			student.get(i).setPreserveRatio(true);
		}
		return student;
	}

	/*
	 * Recursive quicksort algorithm on list data structures to sort the students
	 * based on last name, first name, student number priority.
	 */
	private ArrayList<Student> sortStudents(List<Student> list) {
		if (list.size() <= 1)
			return (ArrayList<Student>) list;
		ArrayList<Student> left = new ArrayList<Student>();
		ArrayList<Student> right = new ArrayList<Student>();
		for (int i = 0; i < list.size(); i++) {
			if (i == list.size() / 2)
				continue;
			if (inOrder(((Student) list.get(i)).getFirstName(), ((Student) list.get(list.size() / 2)).getFirstName(),
					((Student) list.get(i)).getLastName(), ((Student) list.get(list.size() / 2)).getLastName(),
					((Student) list.get(i)).getStudentId(), ((Student) list.get(list.size() / 2)).getStudentId())) {
				left.add(list.get(i));
			} else {
				right.add(list.get(i));
			}
		}
		left = sortStudents(left);
		left.add(list.get(list.size() / 2));

		left.addAll(sortStudents(right)); // merging two sorted data structures
		return left;
	}

	/*
	 * Recursive function that compares the alphanumeric order of (the information
	 * of) two students.
	 */
	private boolean inOrder(String firstName1, String firstName2, String lastName1, String lastName2, int id1,
			int id2) {
		if (lastName1.isEmpty() && !lastName2.isEmpty())
			return true;
		else if (!lastName1.isEmpty() && lastName2.isEmpty())
			return false;
		else if (lastName1.isEmpty() && lastName2.isEmpty()) {
			if (firstName1.isEmpty() && !firstName2.isEmpty())
				return true;
			else if (!firstName1.isEmpty() && firstName2.isEmpty())
				return false;
			else if (firstName1.isEmpty() && firstName2.isEmpty())
				return id1 < id2;
			else if (firstName1.charAt(0) < firstName2.charAt(0))
				return true;
			else if (firstName1.charAt(0) > firstName2.charAt(0))
				return false;
			return inOrder(firstName1.substring(1), firstName2.substring(1), lastName1, lastName2, id1, id2);
		} else if (lastName1.charAt(0) < lastName2.charAt(0))
			return true;
		else if (lastName1.charAt(0) > lastName2.charAt(0))
			return false;
		return inOrder(firstName1, firstName2, lastName1.substring(1), lastName2.substring(1), id1, id2);
	}
}
