package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Database {
	private static Connection connection = connect();
	static final String STUDENT_IMAGES_PATHWAY = Database.class.getResource("database.db").getPath().toString()
			.replace("ia.jar!/application/database.db", "studentImages/");

	private static Connection connect() {
		try {
			// connecting to database
			java.lang.Class.forName("org.sqlite.JDBC");
			return DriverManager.getConnection("jdbc:sqlite::resource:" + Database.class.getResource("database.db"));
		} catch (ClassNotFoundException | SQLException e) {
			return null;
		}
	}

	/*
	 * Retrieving data
	 */
	public static ArrayList<Student> getAllStudents() {
		ArrayList<Student> student = new ArrayList<Student>();
		String sql = "SELECT id, firstName, lastName FROM student";
		Statement statement;
		try {
			statement = connection.createStatement();

			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				student.add(new Student(resultSet.getString("firstName"), resultSet.getString("lastName"),
						resultSet.getInt("id"), STUDENT_IMAGES_PATHWAY + resultSet.getInt("id") + ".jpg"));
			}
		} catch (SQLException e) {
		}
		return student;
	}

	/*
	 * Searching for specified data
	 */
	public static ArrayList<Student> getStudent(String name, String classCode) {
		// breaks searched name into a first name and (possibly) a last name
		Scanner scanner = new Scanner(name);
		String name1 = null;
		String name2 = null;
		try {
			name1 = scanner.next().toLowerCase();
			name2 = scanner.next().toLowerCase();
		} catch (NoSuchElementException e) {
		}
		ArrayList<Student> student = new ArrayList<Student>();
		String sql = "SELECT id, firstName, lastName FROM student";
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				if (resultSet.getString("firstName").equals(name1)) {
					if (name2 == null || name2.equals(resultSet.getString("lastName"))) {

						student.add(getStudent(resultSet.getInt("id"), classCode));
					}
				} else if (resultSet.getString("lastName").equals(name1)) {
					if (name2 == null || name2.equals(resultSet.getString("firstName"))) {
						student.add(getStudent(resultSet.getInt("id"), classCode));
					}
				}
			}
		} catch (SQLException e) {
		}
		return student;
	}

	public static ArrayList<Student> getStudent(String classCode) {
		if (classCode.equals(SearchController.allClasses))
			return getAllStudents();
		ArrayList<Student> student = new ArrayList<Student>();
		String sql = "SELECT class, student FROM enrollment";
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			{
				while (resultSet.next()) {
					if (classCode.equals(resultSet.getString("class"))) {
						student.add(getStudent(resultSet.getInt("student")));
					}
				}
			}
		} catch (SQLException e) {
		}
		return student;
	}

	public static Student getStudent(int id) {
		String sql = "SELECT id, firstName, lastName FROM student";
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				if (id == resultSet.getInt("id")) {
					return new Student(resultSet.getString("firstName"), resultSet.getString("lastName"), id,
							STUDENT_IMAGES_PATHWAY + resultSet.getInt("id") + ".jpg");
				}
			}
		} catch (SQLException e) {
		}
		return null;
	}

	public static Student getStudent(int id, String classCode) {
		if (classCode.equals(SearchController.allClasses))
			return getStudent(id);
		String sql = "SELECT student, class FROM enrollment";
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				if (id == resultSet.getInt("student") && classCode.equals(resultSet.getString("class"))) {
					return getStudent(id);
				}
			}
		} catch (SQLException e) {
		}
		return null;
	}

	public static ArrayList<Class> getClasses() {
		ArrayList<Class> classes = new ArrayList<Class>();
		String sql = "SELECT code FROM Class";
		Statement statement;
		try {
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				classes.add(new Class(resultSet.getString("code"),
						getNumberOfStudentsInClass(resultSet.getString("code"))));
			}
		} catch (SQLException e) {
		}
		return classes;
	}

	public static ArrayList<String> getClassCodes() {
		ArrayList<String> classes = new ArrayList<String>();
		String sql = "SELECT code FROM Class";
		Statement statement;
		try {
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				classes.add(resultSet.getString("code"));
			}
		} catch (SQLException e) {
		}
		return classes;
	}

	public static Student getAllInfo(Student student) {
		String sql = "SELECT id, description, email FROM student";
		Statement statement;
		try {
			statement = connection.createStatement();

			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				if (resultSet.getInt("id") == student.getStudentId()) {
					student.setAdditionalInfo(resultSet.getString("description"), resultSet.getString("email"));
				}
			}
		} catch (SQLException e) {
		}
		return student;
	}

	/*
	 * Updating data
	 */
	public static void updateStudentInfo(int id, String firstName, String lastName, String description, String email) {
		String sql = "UPDATE student SET firstName = ? , " + "lastName = ? , " + "description = ? , " + "email = ? "
				+ "WHERE id = ?";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, firstName);
			preparedStatement.setString(2, lastName);
			preparedStatement.setString(3, description);
			preparedStatement.setString(4, email);
			preparedStatement.setInt(5, id);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
		}
	}

	public static boolean isUniqueId(int id) {
		String sql = "SELECT id FROM student";
		Statement statement;
		try {
			statement = connection.createStatement();

			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				if (resultSet.getInt("id") == id)
					return false;
			}
		} catch (SQLException e) {
		}
		return true;
	}

	/*
	 * Inserting data
	 */
	public static void newStudent(int id, String firstName, String lastName, String description, String email) {
		String sql = "INSERT INTO student(id, firstName, lastName, description, email) VALUES(?,?,?,?,?)";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			preparedStatement.setString(2, firstName);
			preparedStatement.setString(3, lastName);
			preparedStatement.setString(4, description);
			preparedStatement.setString(5, email);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
		}
	}

	public static void enrollStudent(int id, String classCode) {
		String sql = "INSERT INTO enrollment(student, class) VALUES(?,?)";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			preparedStatement.setString(2, classCode);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
		}
	}

	public static void unenrollStudent(int id)

	{
		String sql = "DELETE FROM enrollment WHERE student = ?";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
		}
	}

	/*
	 * Deleting data
	 */
	public static void deleteStudent(int id) {
		String sql = "DELETE FROM student WHERE id = ?";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();
			sql = "DELETE FROM enrollment WHERE student = ?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
		}
	}

	public static void newClass(String code) {
		String sql = "INSERT INTO class(code) VALUES(?)";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, code);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
		}
	}

	public static boolean isUniqueCode(String code) {
		String sql = "SELECT code FROM class";
		Statement statement;

		try {
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				if (resultSet.getString("code").contentEquals(code))
					return false;
			}
		} catch (SQLException e) {
		}
		return true;
	}

	/*
	 * Analyzing data
	 */
	public static int getNumberOfStudentsInClass(String code) {
		String sql = "SELECT class from enrollment";
		int count = 0;
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				if (code.equals(resultSet.getString("class")))
					count++;
			}
		} catch (SQLException e) {
		}
		return count;
	}
}
