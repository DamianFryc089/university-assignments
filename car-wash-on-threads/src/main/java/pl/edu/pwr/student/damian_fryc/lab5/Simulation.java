package pl.edu.pwr.student.damian_fryc.lab5;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.util.Locale;
import java.util.Objects;


public class Simulation extends Application {
	private SimulationController simulationController;

	private double carSpeedScale = 1;
	private double controllerSpeedScale = 1;
	private double washerSpeedScale = 1;
	@Override
	public void start(Stage stage) {

		Pane drawArea = new Pane();
		drawArea.setPrefSize(10000, 3500);


		Text carAmount = new Text();
		simulationController = new SimulationController(drawArea, carAmount);

		HBox menuPanel = new HBox(10);
		menuPanel.getChildren().add(createCarSpeedBox());
		menuPanel.getChildren().add(createControllerSpeedBox());
		menuPanel.getChildren().add(createWasherSpeedBox());
		menuPanel.getChildren().add(createCarsMenu(carAmount));
		menuPanel.getChildren().add(createResetMenu(drawArea, carAmount));

		menuPanel.setId("MenuPanel");
		menuPanel.setAlignment(Pos.CENTER);

		ScrollPane scrollPane = new ScrollPane(drawArea);
		scrollPane.setPrefSize(600, 600);
		scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
		scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

		VBox main = new VBox(scrollPane, menuPanel);
		Scene scene = new Scene(main, 1000, 500);

		File cssFile = new File("./styles.css");
		scene.getStylesheets().add(cssFile.toURI().toString());

		stage.setScene(scene);
		stage.setTitle("Car Wash On Threads");
		stage.show();
	}

	private VBox createCarSpeedBox(){
		VBox vBox = new VBox(4);
		vBox.setAlignment(Pos.CENTER);
		HBox hBox = new HBox(5);
		hBox.setAlignment(Pos.CENTER);

		carSpeedScale = simulationController.getCarSpeedScale();
		Text valueText = new Text(String.format(Locale.US,"%.3f", carSpeedScale));

		StackPane textRegion = new StackPane(valueText);

		Button minusButton = new Button("-");
		minusButton.setOnAction(event -> {
			carSpeedScale = simulationController.changeCarSpeedScale(carSpeedScale * 9 / 10);
			valueText.setText(String.format(Locale.US,"%.3f", carSpeedScale));
		});

		Button plusButton = new Button("+");
		plusButton.setOnAction(event -> {
			carSpeedScale = simulationController.changeCarSpeedScale(carSpeedScale * 10 / 9);
			valueText.setText(String.format(Locale.US,"%.3f", carSpeedScale));
		});

		hBox.getChildren().addAll(minusButton, textRegion, plusButton);
		vBox.getChildren().addAll(new Text("Car speed scale"), hBox);
		return vBox;
	}
	private VBox createControllerSpeedBox(){
		VBox vBox = new VBox(4);
		vBox.setAlignment(Pos.CENTER);
		HBox hBox = new HBox(5);
		hBox.setAlignment(Pos.CENTER);

		controllerSpeedScale = simulationController.getControllerSpeedScale();
		Text valueText = new Text(String.format(Locale.US,"%.3f", controllerSpeedScale));

		StackPane textRegion = new StackPane(valueText);

		Button minusButton = new Button("-");
		minusButton.setOnAction(event -> {
			controllerSpeedScale = simulationController.changeControllerSpeedScale(controllerSpeedScale * 9 / 10);
			valueText.setText(String.format(Locale.US,"%.3f", controllerSpeedScale));
		});

		Button plusButton = new Button("+");
		plusButton.setOnAction(event -> {
			controllerSpeedScale = simulationController.changeControllerSpeedScale(controllerSpeedScale * 10 / 9);
			valueText.setText(String.format(Locale.US,"%.3f", controllerSpeedScale));
		});


		hBox.getChildren().addAll(minusButton, textRegion, plusButton);
		vBox.getChildren().addAll(new Text("Controller speed scale"), hBox);
		return vBox;
	}
	private VBox createWasherSpeedBox(){
		VBox vBox = new VBox(4);
		vBox.setAlignment(Pos.CENTER);
		HBox hBox = new HBox(5);
		hBox.setAlignment(Pos.CENTER);

		washerSpeedScale = simulationController.getWasherSpeedScale();
		Text valueText = new Text(String.format(Locale.US, "%.3f", washerSpeedScale));

		StackPane textRegion = new StackPane(valueText);

		Button minusButton = new Button("-");
		minusButton.setOnAction(event -> {
			washerSpeedScale = simulationController.changeWasherSpeedScale(washerSpeedScale * 9 / 10);
			valueText.setText(String.format(Locale.US, "%.3f", washerSpeedScale));
		});

		Button plusButton = new Button("+");
		plusButton.setOnAction(event -> {
			washerSpeedScale = simulationController.changeWasherSpeedScale(washerSpeedScale * 10 / 9);
			valueText.setText(String.format(Locale.US, "%.3f", washerSpeedScale));
		});


		hBox.getChildren().addAll(minusButton, textRegion, plusButton);
		vBox.getChildren().addAll(new Text("Washer speed scale"), hBox);
		return vBox;
	}
	private VBox createCarsMenu(Text carAmount){
		VBox vBox = new VBox(4);
		vBox.setAlignment(Pos.CENTER);
		HBox hBox = new HBox(5);
		hBox.setAlignment(Pos.CENTER);
		StackPane textRegion = new StackPane(carAmount);

		Button addButton = new Button("add");
		addButton.setOnAction(event -> simulationController.addCars(1));

		Button loopButton = new Button("loop: on");
		loopButton.setOnAction(event -> {
			String mode = simulationController.toggleCarLoop() ? "on" : "off";
			loopButton.setText("loop: " + mode);
		});

		hBox.getChildren().addAll(addButton, textRegion, loopButton);
		vBox.getChildren().addAll(new Text("Cars"), hBox);
		return vBox;
	}
	private VBox createResetMenu(Pane drawArea, Text carAmount){
		VBox vBox = new VBox(2);

		HBox queueMenu = new HBox(4);
		queueMenu.getChildren().add(new Text("     Queues: "));
		TextField queues = new TextField();
		queues.setTextFormatter(new TextFormatter<>(change -> {
			String newText = change.getText();
			if (newText.matches("\\d*"))
				return change;
			return null;
		}));
		queueMenu.getChildren().add(queues);


		HBox washBaysMenu = new HBox(4);
		washBaysMenu.getChildren().add(new Text("Wash bays: "));
		TextField washBays = new TextField();
		washBays.setTextFormatter(new TextFormatter<>(change -> {
			String newText = change.getText();
			if (newText.matches("\\d*"))
				return change;
			return null;
		}));
		washBaysMenu.getChildren().add(washBays);

		Button resetButton = new Button("RESET");
		resetButton.setOnAction(event -> {
			if(Objects.equals(queues.getText(), "") || Objects.equals(washBays.getText(), "")) return;
			int queuesCount = Integer.parseInt(queues.getText());
			int washBaysCount = Integer.parseInt(washBays.getText());

			simulationController.removeSimulationController();
			simulationController = new SimulationController(drawArea, carAmount, queuesCount, washBaysCount, carSpeedScale, controllerSpeedScale, washerSpeedScale);
		});


		vBox.getChildren().addAll(queueMenu, washBaysMenu, resetButton);
		return vBox;
	}

	public static void main(String[] args) {
		launch(args);
		System.exit(0);
	}
}
