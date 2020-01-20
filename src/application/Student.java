package application;

import javafx.scene.image.ImageView;
//inheritance
public class Student extends ImageView {
//encapsulation
	private String firstName;
	private String lastName;
	private String description;
	private String email;
	private int id;

	public Student(String firstName, String lastName, int id, String path) {
		super(path);
		this.firstName = firstName;
		this.lastName = lastName;
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getDescription() {
		return description;
	}

	public String getEmail() {
		return email;
	}

	public int getStudentId() {
		return id;
	}

	public void setAdditionalInfo(String description, String email) {
		this.description = description;
		this.email = email;
	}
}
