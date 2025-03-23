package pl.edu.pwr.student.damian_fryc.lab4_gui;

import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import pl.edu.pwr.student.damian_fryc.lab4_client.ApiClient;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Main extends Application {
	private final TableView<Map<String, Object>> table = new TableView<>();
	private final ApiClient apiClient = new ApiClient();

	private JSONObject jsonData;

	private final Button bCategories = new Button("Get Areas");
	private final Button bAreas = new Button("Get Variables");
	private final Button bVariables = new Button("Get Sections");
	private final Button bSections = new Button("Get Periods");
	private final Button bPeriods = new Button("Get Data");

	private final ComboBoxPlus cbPeriods = new ComboBoxPlus("Period", bPeriods, new ArrayList<>(Arrays.asList()));
	private final ComboBoxPlus cbSections = new ComboBoxPlus("Section", bSections, new ArrayList<>(Arrays.asList(cbPeriods)));
	private final ComboBoxPlus cbVariables = new ComboBoxPlus("Variable", bVariables, new ArrayList<>(Arrays.asList(cbSections, cbPeriods)));
	private final ComboBoxPlus cbAreas = new ComboBoxPlus("Area", bAreas, new ArrayList<>(Arrays.asList(cbVariables, cbSections, cbPeriods)));
	private final ComboBoxPlus cbCategories = new ComboBoxPlus("Category", bCategories, new ArrayList<>(Arrays.asList(cbAreas, cbVariables, cbSections, cbPeriods)));
	private final TextField tfYear = new TextField();
	private final ComboBox cbLanguages = new ComboBox();
	private final Button bFilters = new Button("Filters");
	private final TextArea taFilters = new TextArea();

	@Override
	public void start(Stage primaryStage) {
		tfYear.setPromptText("Year");

		bCategories.setOnAction(e -> updateAreas());
		bAreas.setOnAction(e -> updateVariables());
		bVariables.setOnAction(e -> updateSections());
		bSections.setOnAction(e -> updatePeriods());
		bPeriods.setOnAction(e -> updateData());

		table.setId("Table");

		cbLanguages.setId("LangComboBox");
		cbLanguages.getItems().addAll("pl","en");
		cbLanguages.setValue("pl");
		cbLanguages.valueProperty().addListener((observable, oldValue, newValue) -> setLanguage());
		setLanguage();

		updateCategories();

		VBox buttonBox = new VBox(4, bCategories, bAreas, bVariables, bSections, bPeriods);
		buttonBox.setId("ButtonBox");

		VBox listBox = new VBox(4, cbCategories, cbAreas, cbVariables, cbSections, cbPeriods);
		listBox.setId("ListBox");

		HBox leftBox = new HBox(buttonBox, listBox);
		taFilters.setPrefSize(400, 200);

		// showing and hiding filters text area
		taFilters.setVisible(false);
		bFilters.setOnAction(event -> {
			leftBox.setVisible(!leftBox.isVisible());
			taFilters.setVisible(!taFilters.isVisible());
		});

		StackPane stackPane = new StackPane(leftBox, taFilters);
		stackPane.setId("LeftBox");

		VBox rightBox = new VBox(15, tfYear, bFilters, cbLanguages);
		rightBox.setAlignment(Pos.BOTTOM_CENTER);
		rightBox.setId("RightBox");

		HBox inputBox = new HBox(stackPane, rightBox);
		inputBox.setId("BottomBox");

		VBox mainLayout = new VBox(10, table, inputBox);

		Scene scene = new Scene(mainLayout, 800, 600);

		File cssFile = new File("./styles.css");
		scene.getStylesheets().add(cssFile.toURI().toString());

		primaryStage.setScene(scene);
		primaryStage.setTitle("GUS data viewer");
		primaryStage.show();
	}

	private void updateCategories(){
		JSONArray dataJson = apiClient.getCategories();
		ArrayList categories = new ArrayList<>();
		for (int i = 0; i < dataJson.length(); i++) {
			JSONObject jsonObject = dataJson.getJSONObject(i);
			categories.add(new ComboItem(jsonObject.getInt("id"), jsonObject.getString("nazwa")));
		}
		cbCategories.getItems().setAll(categories);
		cbCategories.appear();
		cbCategories.clearAllBelow();
	}
	private void updateAreas(){
		if(cbCategories.isEmpty())
			return;

		JSONArray dataJson = apiClient.getAreas( ((ComboItem)cbCategories.getValue()).id );
		ArrayList areas = new ArrayList<>();
		for (int i = 0; i < dataJson.length(); i++) {
			JSONObject jsonObject = dataJson.getJSONObject(i);
			areas.add(new ComboItem(jsonObject.getInt("id"), jsonObject.getString("nazwa")));
		}

		cbAreas.getItems().setAll(areas);
		cbAreas.appear();
		cbAreas.clearAllBelow();
	}
	private void updateVariables(){
		if(cbAreas.isEmpty())
			return;
		JSONArray dataJson = apiClient.getVariables( ((ComboItem)cbAreas.getValue()).id );
		ArrayList Variables = new ArrayList<>();
		for (int i = 0; i < dataJson.length(); i++) {
			JSONObject jsonObject = dataJson.getJSONObject(i);
			Variables.add(new ComboItem(jsonObject.getInt("id"), jsonObject.getString("nazwa")));
		}
		cbVariables.getItems().setAll(Variables);
		cbVariables.appear();
		cbVariables.clearAllBelow();
	}
	private void updateSections(){
		if(cbVariables.isEmpty())
			return;

		JSONArray dataJson = apiClient.getSections( ((ComboItem)cbVariables.getValue()).id );
		ArrayList sections = new ArrayList<>();
		for (int i = 0; i < dataJson.length(); i++) {
			JSONObject jsonObject = dataJson.getJSONObject(i);
			sections.add(new ComboItem(jsonObject.getInt("id"), jsonObject.getString("nazwa")));
		}
		cbSections.getItems().setAll(sections);
		cbSections.appear();
		cbSections.clearAllBelow();
	}
	private void updatePeriods(){
		if(cbSections.isEmpty() || cbVariables.isEmpty())
			return;

		JSONArray dataJson = apiClient.getPeriods( ((ComboItem)cbSections.getValue()).id, ((ComboItem)cbVariables.getValue()).id );
		ArrayList periods = new ArrayList<>();
		for (int i = 0; i < dataJson.length(); i++) {
			JSONObject jsonObject = dataJson.getJSONObject(i);
			periods.add(new ComboItem(jsonObject.getInt("id"), jsonObject.getString("nazwa")));
		}
		cbPeriods.getItems().setAll(periods);
		cbPeriods.appear();
		cbPeriods.clearAllBelow();

	}
	private void updateData() {
		if(cbAreas.isEmpty()
				|| cbVariables.isEmpty()
				|| cbSections.isEmpty()
				|| cbPeriods.isEmpty()
		) return;
		int yearId = 0;
		try {
			yearId = Integer.parseInt(tfYear.getText());
			if (yearId < 1900 || yearId > 2050)
				throw new NumberFormatException("Wrong year");
		}
		catch (NumberFormatException e) {
			if (!tfYear.getStyleClass().contains("error"))
				tfYear.getStyleClass().add("error");
			return;
		}

		tfYear.getStyleClass().remove("error");
		int variableId = ((ComboItem)cbVariables.getValue()).id;
		int sectionId = ((ComboItem)cbSections.getValue()).id;
		int periodId = ((ComboItem)cbPeriods.getValue()).id;
		jsonData = apiClient.getData(variableId, sectionId, yearId, periodId, taFilters.getText());
		updateTable();
	}
	private void updateTable() {
		// if no data show year
		if(jsonData == null) {
			table.getColumns().clear();
			table.setPlaceholder(new Text("--" + tfYear.getText() + "--"));
			return;
		}

		// create columns
		JSONArray headers = jsonData.getJSONArray("headers");
		table.getColumns().clear();

		for (int i = 0; i < headers.length(); i++) {
			String header = headers.getString(i);
			TableColumn<Map<String, Object>, Object> column = new TableColumn<>(header);
			column.setCellValueFactory(param -> {
				Object value = param.getValue().get(header);
				return new SimpleObjectProperty<>(value);
			});
			table.getColumns().add(column);
		}

		// update data
		JSONArray data = jsonData.getJSONArray("data");
		ObservableList<Map<String, Object>> rows = FXCollections.observableArrayList();
		for (int i = 0; i < data.length(); i++) {
			JSONArray rowArray = data.getJSONArray(i);
			Map<String, Object> row = new HashMap<>();
			for (int j = 0; j < rowArray.length(); j++) {
				String columnName = jsonData.getJSONArray("headers").getString(j);
				Object value = rowArray.get(j);
				row.put(columnName, value);
			}
			rows.add(row);
		}

		table.setItems(rows);
	}
	private void setLanguage(){

		switch ((String) cbLanguages.getValue()){
			case "en":
				cbCategories.changePromptText("Category");
				cbAreas.changePromptText("Area");
				cbVariables.changePromptText("Variable");
				cbSections.changePromptText("Section");
				cbPeriods.changePromptText("Period");

				bCategories.setText("Get Areas");
				bAreas.setText("Get Variables");
				bVariables.setText("Get Sections");
				bSections.setText("Get Periods");
				bPeriods.setText("Get Data");
				bFilters.setText("Filters");

				table.setPlaceholder(new Label("No data available"));
				tfYear.setPromptText("Year");
				taFilters.setPromptText("Syntax: nr_column|operator|comparison_value, nr_column starts at 0, ignore '|' symbols, example: 0>=5");
				break;
			default:
				cbCategories.changePromptText( "Kategoria");
				cbAreas.changePromptText("Obszar");
				cbVariables.changePromptText("Zmienna");
				cbSections.changePromptText("Sekcja");
				cbPeriods.changePromptText("Okres");

				bCategories.setText("Pobierz obszary");
				bAreas.setText("Pobierz Zmienne");
				bVariables.setText("Pobierz Sekcje");
				bSections.setText("Pobierz Okresy");
				bPeriods.setText("Pobierz Dane");
				bFilters.setText("Filtry");

				table.setPlaceholder(new Label("Brak danych do wyświetlenia"));
				tfYear.setPromptText("Rok");
				taFilters.setPromptText("Składnia: nr_kolumny|operator|wartość_porównania, nr_kolumny zaczyna się od 0, ignoruj symbole „|”, przykład: 0>=5");
				break;

		}

		apiClient.setLanguage((String) cbLanguages.getValue());
		cbCategories.clearAllBelow();
		updateCategories();
	}

	public static void main(String[] args) {
		launch(args);
	}
}