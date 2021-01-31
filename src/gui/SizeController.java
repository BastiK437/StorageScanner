package gui;

import helper.TableContent;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import scanner.CheckAvailableFileSystems;

import java.util.List;

public class SizeController {


    @FXML
    private ChoiceBox sizedropdown;

    // private vars
    private int selectedSize = 2; // 0=bytes, 1=kB, 2=MB, 3=GB

    // other gui classes
    private TableController tableController;



    // constructor
    public void init(GUIController guiController, ChoiceBox sizedropdown) {
        this.sizedropdown = sizedropdown;
        this.tableController = guiController.getTableController();

        sizedropdown.setItems(FXCollections.observableArrayList(
                "Size (bytes)", "Size (kB)", "Size (MB)", "Size (GB)")
        );

        sizedropdown.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                updateSizeConvertion();
                selectedSize = (int) number2;
            }
        });

        sizedropdown.getSelectionModel().select(2);
    }


    public void manipulateSizeOfContent(List<TableContent> table) {
        int manipulator = 0;

        for(TableContent tc: table) {
            switch (selectedSize) {
                case 0:
                    tc.setSize(String.format("%,.2f", (double)tc.getSizeRaw()) );
                    break;
                case 1:
                    tc.setSize(String.format("%,.2f", (double)tc.getSizeRaw() / 1024.0) );
                    break;
                case 2:
                    tc.setSize(String.format("%,.2f", (double)tc.getSizeRaw() / 1048576.0) );
                    break;
                case 3:
                    tc.setSize(String.format("%,.2f", (double)tc.getSizeRaw() / 1073741824.0) );
                    break;
            }
        }
    }

    private void updateSizeConvertion() {
        tableController.reloadTable(false);
    }
}
