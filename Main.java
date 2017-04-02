package assignment5;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

import com.sun.javafx.geom.Rectangle;

import java.io.*;

public class Main extends Application{

	private static String myPackage;	// package of Critter file.  Critter cannot be in default pkg.
	
	static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }
	
	public static void main(String[] args) {
		 launch(args);
	}
	
	@Override
	public void start(Stage primaryStage){
		GridPane overall = new GridPane();
		GridPane grid = new GridPane();
		GridPane nonStats = new GridPane();
		primaryStage.setScene(new Scene(overall, 1000, 600));
		//grid.setGridLinesVisible(true);
		final int numCols = 5;
		final int numRows = 5;
		
		RowConstraints menuConstraints = new RowConstraints();
		menuConstraints.setPercentHeight(90.0);
		overall.getRowConstraints().add(menuConstraints);
		RowConstraints statsConstraints = new RowConstraints();
		statsConstraints.setPercentHeight(10.0);
		overall.getRowConstraints().add(statsConstraints);
		
		ColumnConstraints menuCol = new ColumnConstraints();
		menuCol.setPercentWidth(40.0);
		nonStats.getColumnConstraints().add(menuCol);
		ColumnConstraints gridCol = new ColumnConstraints();
		gridCol.setPercentWidth(60.0);
		nonStats.getColumnConstraints().add(gridCol);
		
		RowConstraints nonStatsRow = new RowConstraints();
		nonStatsRow.setPercentHeight(100.0);
		nonStats.getRowConstraints().add(nonStatsRow);
		
		overall.add(nonStats, 0, 0);
		nonStats.add(grid, 0, 0);
		
		
		for(int i = 0; i < numCols; i++) {
			ColumnConstraints colConst = new ColumnConstraints();
			colConst.setPercentWidth(100.0 / numCols);
			grid.getColumnConstraints().add(colConst);
		}
		for(int i = 0; i < numRows; i++) {
			RowConstraints rowConst = new RowConstraints();
			rowConst.setPercentHeight(100.0 / numRows);
			grid.getRowConstraints().add(rowConst);
		}
				
		Button timeStep = new Button();
		timeStep.setText("Step");
		ObservableList<String> timeStepOptions = FXCollections.observableArrayList(
				"1", "10", "100", "other");
		ComboBox<String> timeStepBox = new ComboBox<String>(timeStepOptions);
		timeStepBox.setValue("");
		TextField amountField = new TextField();
		timeStepBox.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(timeStepBox.getValue().equals("other")) 
					grid.add(amountField, 4, 0);
				else
					grid.getChildren().remove(amountField);
			}
		});
		timeStep.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(!timeStepBox.getValue().equals("")) {
					int steps = 0;
					if(timeStepBox.getValue().equals("other")) {
						try {
							steps = Integer.valueOf(amountField.getText());
						} catch(Exception exception) {
							error(amountField.getText());
						}
					}
					else
						steps = Integer.valueOf(timeStepBox.getValue());
					timeStepBox.setValue("");
					amountField.clear();
					grid.getChildren().remove(amountField);
					for(int i = 0; i < steps; i++)
						Critter.worldTimeStep();
				}
			}
		});
		
		Button quit = new Button();
		quit.setText("Quit");
		quit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				System.exit(0);
			}
		});
		
		Button show = new Button();
		show.setText("Show");
		show.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Critter.displayWorld(nonStats);
			}
		});
		
		Button seed = new Button();
		seed.setText("Seed");
		TextField seedField = new TextField();
		seed.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(!seedField.getText().equals("")) {
					try {
						Critter.setSeed(Integer.valueOf(seedField.getText()));
						seedField.setText("");
					} catch(Exception exception) {
						error(seedField.getText());
						seedField.setText("");
					}
					
				}
			}
		});
		
		Button stats = new Button();
		stats.setText("Stats");
		ArrayList<String> classes = validClasses();
		ObservableList<String> critterOptions = FXCollections.observableArrayList(classes);
		ComboBox<String> statsBox = new ComboBox<String>(critterOptions);
		statsBox.setValue("");
		GridPane statsPane = new GridPane();
		Label statsLabel = new Label();
		stats.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(!statsBox.getValue().equals("")) {
					try {
						List<Critter> list = Critter.getInstances(statsBox.getValue());
						Class<?> c;
						Method method;
						c = Class.forName(myPackage + "." + statsBox.getValue());
						statsBox.setValue("");
						method = c.getMethod("runStats", List.class);
						String result = (String) method.invoke(null, list);
						result = "\n" + result;
						statsLabel.setText(result);
						statsPane.add(statsLabel, 0, 0);
						overall.add(statsPane, 0, 1);
					} catch(Exception e) {
						
					}
				}
				
			}
		});
		
		Button make = new Button();
		make.setText("Make");
		ComboBox<String> makeBox = new ComboBox<String>(critterOptions);
		makeBox.setValue("");
		TextField makeField = new TextField();
		makeBox.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				grid.getChildren().remove(makeField);
				if(!makeBox.getValue().equals(""))
					grid.add(makeField, 4, 3);
			}
		});
		make.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(!makeBox.getValue().equals("")) {
					try {
						int amount = 1;
						if(!makeField.getText().equals(""))
							amount = Integer.valueOf(makeField.getText());
						makeField.clear();
						grid.getChildren().remove(makeField);
						for(int i = 0; i < amount; i++)
							Critter.makeCritter(makeBox.getValue());
						makeBox.setValue("");
					}
					catch(Exception exception) {
						error(makeField.getText());
						makeField.clear();
						grid.getChildren().remove(makeField);
						makeBox.setValue("");
					}
				}
			}
		});
		
		grid.add(timeStep, 0, 0);
		grid.add(timeStepBox, 2, 0);
		grid.add(show, 0, 1);
		grid.add(seed, 0, 4);
		grid.add(seedField, 2, 4);
		grid.add(stats, 0, 2);
		grid.add(statsBox, 2, 2);
		grid.add(make, 0, 3);
		grid.add(makeBox, 2, 3);
		grid.add(quit, numCols - 1, numRows - 1);
		
		primaryStage.setTitle("Critters");
		primaryStage.show();
	}
	
	private ArrayList<String> validClasses() {
		ArrayList<String> classes = new ArrayList<String>();
		File dir = new File("bin/assignment5");
		File[] classFiles = dir.listFiles();
		for(File f: classFiles) {
			String file = f.getName().substring(0, f.getName().indexOf('.'));
			Class<?> myCritter = null;
			Class<?> critter = Critter.class;
			
			try {
				myCritter = Class.forName(myPackage + "." + file);  // Class object of specified name
				if(critter.isAssignableFrom(myCritter) && !file.equals("Critter") && !file.equals("Critter$TestCritter"))
					classes.add(file);
			} catch(Exception e) {
				
			}
		}
		return classes;
	}
	
	private void error(String err) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error Processing");
		alert.setContentText("Error processing: " + err);
		alert.showAndWait();
	}
}
