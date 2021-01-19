package gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;

public class Settings {

    @FXML
    private CheckBox hiddenElementsCheckbox;

    // private vars
    private boolean ignoreHiddenElements;


    // constructor
    public Settings(CheckBox hiddenElementsCheckbox) {
        this.hiddenElementsCheckbox = hiddenElementsCheckbox;

        ignoreHiddenElements = false;

        hiddenElementsCheckbox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                ignoreHiddenElements = newValue;
            }
        });
    }

    // getter
    public boolean getIgnoreHiddenElements() {
        return ignoreHiddenElements;
    }

    // setter
}
