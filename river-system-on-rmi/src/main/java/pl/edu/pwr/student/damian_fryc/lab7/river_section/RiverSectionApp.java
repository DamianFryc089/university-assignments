package pl.edu.pwr.student.damian_fryc.lab7.river_section;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.rmi.RemoteException;
import java.util.Objects;

public class RiverSectionApp extends Application{
		private RiverSection riverSection;
		private final StackPane mainPane = new StackPane();
		private Label[] waterFragments;

		@Override
		public void start(Stage stage) {
			Scene scene = new Scene(mainPane, 300, 250);
			riverSectionInput(stage);

			stage.setTitle("River Section");
			stage.setScene(scene);
			stage.show();
		}

		private void riverSectionInput(Stage stage){
			mainPane.getChildren().clear();

			Text text = new Text("River Section");
			TextField size = new TextField("5");
			size.setTextFormatter(new TextFormatter<>(change -> {
				String newText = change.getText();
				if (newText.matches("\\d*"))
					return change;
				return null;
			}));

            TextField nameInput = new TextField("River Section 1");
            HBox nameBox = new HBox(new Text("     Name: "), nameInput);
            nameBox.alignmentProperty().set(Pos.CENTER);

			HBox volumeBox = new HBox(new Text("Size: "), size);
			volumeBox.alignmentProperty().set(Pos.CENTER);

			TextField portInput = new TextField("0");
			portInput.setTextFormatter(new TextFormatter<>(change -> {
				String newText = change.getText();
				if (newText.matches("\\d*"))
					return change;
				return null;
			}));
			HBox portBox = new HBox(new Text("Port: "), portInput);
			portBox.alignmentProperty().set(Pos.CENTER);

			Button acceptButton = new Button("Set");
			acceptButton.setOnAction(event -> {
                try {
                    riverSection = new RiverSection(Integer.parseInt(size.getText()), Integer.parseInt(portInput.getText()), nameInput.getText());
					stage.setOnCloseRequest(event2 -> {
						try {
							riverSection.closeServer();
						} catch (RemoteException ignored) {}
					});
				} catch (RemoteException e) {
                    throw new RuntimeException(e);
                }

                waterFragments = new Label[riverSection.waterFragments.length];
				for (int i = 0; i < waterFragments.length; i++)
					waterFragments[i] = new Label();

				String portSegment = Objects.equals(portInput.getText(), "0") ? "" : ":" + Integer.parseInt(portInput.getText());
				stage.setTitle("River Section - " + nameInput.getText() + portSegment);
				tailorInput();
			});

			VBox vbox = new VBox(5, text, nameBox, volumeBox, portBox, acceptButton);
			vbox.alignmentProperty().set(Pos.CENTER);
			mainPane.getChildren().add(vbox);
		}

		private void tailorInput(){
			mainPane.getChildren().clear();

			Text text = new Text("Tailor");

			TextField nameInput = new TextField("Tailor");
			HBox nameBox = new HBox(new Text("Name: "), nameInput);
			nameBox.alignmentProperty().set(Pos.CENTER);

			TextField hostInput = new TextField("localhost");
			HBox hostBox = new HBox(new Text("Host: "), hostInput);
			hostBox.alignmentProperty().set(Pos.CENTER);

			TextField portInput = new TextField("2000");
			portInput.setTextFormatter(new TextFormatter<>(change -> {
				String newText = change.getText();
				if (newText.matches("\\d*"))
					return change;
				return null;
			}));

			HBox portBox = new HBox(new Text("Port: "), portInput);
			portBox.alignmentProperty().set(Pos.CENTER);

			Button acceptButton = new Button("Set");
			acceptButton.setOnAction(event -> {
				boolean connected = riverSection.registerToTailor(nameInput.getText(), hostInput.getText(), Integer.parseInt(portInput.getText()));
				if (connected){
					startInput();
				}
			});
			VBox vbox = new VBox(5, text, nameBox, hostBox, portBox, acceptButton);
			vbox.alignmentProperty().set(Pos.CENTER);
			mainPane.getChildren().add(vbox);
		}

		private void startInput(){
			mainPane.getChildren().clear();
			Button acceptButton = new Button("START");
			acceptButton.setOnAction(event -> {
				showFillingPercentageColumn();

				Thread updateThread = new Thread(() -> {
					try {
						while (true) {
							riverSection.riverLogic();
							updateWaterFragments();
							Thread.sleep(1000);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                });
				updateThread.setDaemon(true);
				updateThread.start();
			});

			mainPane.getChildren().add(acceptButton);
		}

		private void showFillingPercentageColumn(){
			mainPane.getChildren().clear();
			VBox vbox = new VBox(waterFragments);
			vbox.setOnMouseClicked(event -> showFillingPercentageRow());
			vbox.setStyle(
					"-fx-border-color: #2c2c2c;" +
					"-fx-border-width: 1;" +
					"-fx-border-radius: 5;" +
					"-fx-padding: 2;" +
					"-fx-background-color: #f9f9f9;"
			);

			vbox.alignmentProperty().set(Pos.CENTER);
			mainPane.getChildren().add(vbox);
			updateWaterFragments();
		}
		private void showFillingPercentageRow(){
			mainPane.getChildren().clear();
			HBox hbox = new HBox(waterFragments);
			hbox.setOnMouseClicked(event -> showFillingPercentageColumn());
			hbox.setStyle(
					"-fx-border-color: #2c2c2c;" +
					"-fx-border-width: 1;" +
					"-fx-border-radius: 5;" +
					"-fx-padding: 2;" +
					"-fx-background-color: #f9f9f9;"
			);
			hbox.alignmentProperty().set(Pos.CENTER);
			mainPane.getChildren().add(hbox);
			updateWaterFragments();
		}

		private void updateWaterFragments() {
			javafx.application.Platform.runLater(() -> {
				for (int i = 0; i < waterFragments.length; i++) {
					waterFragments[i].setText(riverSection.waterFragments[i]+"");

					waterFragments[i].setStyle(
							"-fx-border-color: #3a3a3a;" +
							"-fx-border-width: 1;" +
							"-fx-padding: 2;" +
							"-fx-background-color: #f9f9f9;" +
							"-fx-font-size: "+ Math.max(10, 100 / waterFragments.length) + ";"
					);
				}
			});
		}

		public static void main(String[] args) {
			launch(args);
		}
	}
