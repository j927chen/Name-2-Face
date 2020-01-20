package application;

public class Class {
	
	private String code;
	private int numberOfStudents;

	public Class(String code, int numberOfStudents) {
		this.code = code;
		this.numberOfStudents = numberOfStudents;
	}

	public String getCode() {
		return code;
	}

	public int getNumberOfStudents() {
		return numberOfStudents;
	}
}