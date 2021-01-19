package gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import scanner.CheckAvailableFileSystems;

public class SizeController {


    @FXML
    private ChoiceBox sizedropdown;

    // private vars
    private int selectedSize = 2; // 0=bytes, 1=kB, 2=MB, 3=GB

    // other gui classes
    private TableController tableController;



    // constructor
    public SizeController(ChoiceBox sizedropdown, GUIController guiController) {
        this.sizedropdown = sizedropdown;
        this.tableController = guiController.getTableController();

        sizedropdown.setItems(FXCollections.observableArrayList(
                "Size (bytes)", "Size (kB)", "Size (MB)", "Size (GB)")
        );

        sizedropdown.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                updateSizeConvertion((int) number2 );
            }
        });

        sizedropdown.getSelectionModel().select(2);
    }



    // private functions
    private void updateSizeConvertion(int size) {
        selectedSize = size;
        // TODO
        /*
        if(actualTable != null) {
            for(int i=0; i<actualTable.size(); i++) {
                actualTable.get(i).setSizeConvertion(size);
            }
            updateTable(actualTable);
        }
         */
    }


    // getter
    // TODO
    /*
    public int getselectedSize() {
        return selectedSize;
    }

     */
}
