package gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ProgressBar;

public class Settings {

    @FXML
    private CheckBox hiddenElementsCheckbox;
    @FXML
    private ProgressBar progressBar;

    // private vars
    private boolean ignoreHiddenElements;


    // constructor
    public void init(GUIController guiController, CheckBox hiddenElementsCheckbox, ProgressBar progressBar) {
        this.hiddenElementsCheckbox = hiddenElementsCheckbox;
        this.progressBar = progressBar;

        ignoreHiddenElements = false;

        hiddenElementsCheckbox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                ignoreHiddenElements = newValue;
            }
        });
    }

    public void setProgress(double procent) {
        progressBar.setProgress(procent);
    }

    // getter
    public boolean getIgnoreHiddenElements() {
        return ignoreHiddenElements;
    }

    // setter
}
