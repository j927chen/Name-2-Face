package application;

import java.io.IOException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class StatisticsController {

	public PieChart pieChart;
	public BarChart<?, ?> barChart;
	public Slider slider1, slider2;
	public RadioButton radioButton;
	private ArrayList<Class> classes;
	private static int NUMBER_OF_DISPLAYED_CLASSES = 1;
	private static final double MIN_DEGREE = 0;
	private static final double MAX_DEGREE = 360;

	public void initialize() {
		// pie chart
		ClassBinaryTree tree = new ClassBinaryTree();
		classes = tree.inOrder(tree.getRootNode());
		ArrayList<PieChart.Data> data = new ArrayList<PieChart.Data>();
		for (int i = 0; i < classes.size(); i++) {
			data.add(new PieChart.Data(classes.get(i).getCode(), classes.get(i).getNumberOfStudents()));
		}
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
		pieChartData.addAll(data);
		pieChart.setData(pieChartData);

		pieChart.setTitle("Relative Class Sizes");
		// slider1 configuration
		slider1.setMin(MIN_DEGREE);
		slider1.setMax(MAX_DEGREE);
		// slider2 configuration
		slider2.setMin(1);
		slider2.setMax(classes.size());
		setNumberOfDisplayedClasses(new MouseEvent(null, 0, 0, 0, 0, null, 0, false, false, false, false, false, false,
				false, false, false, false, null));
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

	public void rotatePieChart(MouseEvent event) {
		pieChart.setStartAngle(slider1.getValue());
	}

	public void setClockwise(ActionEvent event) {
		pieChart.setClockwise(!pieChart.isClockwise());
	}

	public void setNumberOfDisplayedClasses(MouseEvent event) {
		NUMBER_OF_DISPLAYED_CLASSES = (int)

		slider2.getValue();
		// bar chart
		barChart.getData().clear();
		XYChart.Series orderedData = new XYChart.Series<>();
		for (int i = 0; i < NUMBER_OF_DISPLAYED_CLASSES; i++) {
			orderedData.getData()
					.addAll(new XYChart.Data(classes.get(i).getCode(), classes.get(i).getNumberOfStudents()));
		}
		barChart.getData().addAll(orderedData);
		barChart.setLegendVisible(false);
		barChart.setTitle("Top " + NUMBER_OF_DISPLAYED_CLASSES + " Largest Classes");
	}
}
