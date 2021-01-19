package gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import scanner.CheckAvailableFileSystems;
import scanner.TableContent;

import java.io.File;
import java.util.List;

public class FileSystemController {

    @FXML
    private ChoiceBox fileSdropdown;

    // private vars
    private List<TableContent> actualTable;
    private String actualPath;
    private String startPath;
    private File partitions[];

    public FileSystemController(ChoiceBox fileSdropdown) {
        this.fileSdropdown = fileSdropdown;

        initFileSDropDown();

        fileSdropdown.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                // TODO table.getItems().clear();

                startPath = (String)fileSdropdown.getItems().get((Integer) number2);
                // TODO getNewTable(startPath, false);
            }
        });
    }

    private void initFileSDropDown() {
        CheckAvailableFileSystems fileSystems = new CheckAvailableFileSystems();

        File partitions[] = fileSystems.checkFileSystems();

        String partitionPaths[] = new String[partitions.length];

        for( int i=0; i<partitions.length; i++) {
            partitionPaths[i] = partitions[i].getPath();
        }

        fileSdropdown.setItems(FXCollections.observableArrayList(partitionPaths));
        // System.out.printf("Items set\n");
    }
}
