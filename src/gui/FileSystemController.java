package gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import scanner.CheckAvailableFileSystems;
import helper.TableContent;

import java.io.File;
import java.util.List;

public class FileSystemController {

    @FXML
    private ChoiceBox fileSdropdown;

    // private vars
    private List<TableContent> actualTable;
    private String actualPath;
    private String diskPath;
    private File partitions[];

    // other gui classes
    private PathController pathController;

    public void init(GUIController guiController, ChoiceBox fileSdropdown) {
        this.fileSdropdown = fileSdropdown;


        this.pathController = guiController.getPathController();

        initFileSDropDown();

        // add dropdown listener
        fileSdropdown.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                diskPath = (String)fileSdropdown.getItems().get((Integer) number2);
                pathController.setDisk(diskPath);
            }
        });
    }



    // private functions
    private void initFileSDropDown() {
        CheckAvailableFileSystems fileSystems = new CheckAvailableFileSystems();

        File partitions[] = fileSystems.checkFileSystems();
        String partitionPaths[] = new String[partitions.length];

        for( int i=0; i<partitions.length; i++) {
            partitionPaths[i] = partitions[i].getPath();
        }

        fileSdropdown.setItems(FXCollections.observableArrayList(partitionPaths));
    }
}
