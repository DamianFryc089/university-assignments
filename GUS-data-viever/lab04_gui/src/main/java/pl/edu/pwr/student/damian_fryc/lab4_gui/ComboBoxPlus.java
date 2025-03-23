package pl.edu.pwr.student.damian_fryc.lab4_gui;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import java.util.List;
import java.util.Objects;

public class ComboBoxPlus extends ComboBox {
    public String promptText;
    private String previousText = null;
    private Button button;
    private List<ComboBoxPlus> comboBoxesBelow;
    public ComboBoxPlus(String promptText, Button button, List<ComboBoxPlus> comboBoxesToBelow){
        this.promptText = promptText;
        this.button = button;
        this.comboBoxesBelow = comboBoxesToBelow;

        this.showingProperty().addListener((obs, wasShowing, isNowShowing) -> {
            if (isNowShowing) {
                previousText = this.getValue().toString();
                this.getItems().remove(promptText);
            }
            if (wasShowing) {
                // data changed, so remove all inputs below
                if (!Objects.equals(this.getValue().toString(), previousText)) {
                    clearAllBelow();
                }
                this.getStyleClass().remove("error");
            }

        });
        clearAndHide();
    }
    public void showPromptText(){
        if (this.getValue() == promptText)
            this.setValue(null);
        if (this.getValue() == null) {
            this.getItems().add(promptText);
            this.setValue(promptText);
        }
    }
    public void changePromptText(String newText){
        promptText = newText;
        this.setValue(promptText);
    }
    public void clearAndHide(){
        this.getItems().clear();
        this.setValue(null);
        this.visibleProperty().set(false);
        button.visibleProperty().set(false);
    }
    public void clearAllBelow(){
        for (ComboBoxPlus comboBoxPlus : this.comboBoxesBelow) {
            comboBoxPlus.clearAndHide();
        }
    }
    public void appear(){
        showPromptText();
        this.visibleProperty().set(true);
        button.visibleProperty().set(true);
    }
    public boolean isEmpty(){
        if (this.getValue() == promptText || this.getValue() == null) {
            if (!this.getStyleClass().contains("error"))
                this.getStyleClass().add("error");
            return true;
        }
        this.getStyleClass().remove("error");
        return false;
    }
}
