package gui;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import scanner.CheckAvailableFileSystems;
import scanner.DirectoryScanner;
import scanner.PathInformation;
import scanner.TableContent;

import java.io.File;
import java.util.*;

public class GUIController {
    private String VERSION = "v0.20";
    public boolean DEBUG = true;


    private String partitionPaths[];
    private DirectoryScanner ds;



    // gui controller
    private TableController tableController;
    private SizeController sizeController;
    private FileSystemController fileSystemController;
    private Settings settings;
    private PathController pathController;

    // scanner specific
    private Map<String, PathInformation> scannedDirs;

    @FXML
    private ChoiceBox fileSdropdown;
    @FXML
    private TableView table;
    @FXML
    private TableColumn nameColumn;
    @FXML
    private TableColumn sizeColumn;
    @FXML
    private TableColumn filesColumn;
    @FXML
    private TableColumn dirsColumn;
    @FXML
    private ChoiceBox sizedropdown;
    @FXML
    private TextField pathTextField;
    @FXML
    private Text versionText;
    @FXML
    private CheckBox hiddenElementsCheckbox;



    @FXML
    public void initialize() {
        // init fxml objects
        versionText.setText(VERSION);

        // init objects
        scannedDirs = new HashMap<>();

        tableController = new TableController(this, table, nameColumn, sizeColumn, filesColumn, dirsColumn);
        sizeController = new SizeController(sizedropdown, this);
        fileSystemController = new FileSystemController(fileSdropdown, this);
        settings = new Settings(hiddenElementsCheckbox);
        pathController = new PathController(this, pathTextField);
    }

    @FXML
    private void reloadButtonPressed() {
        tableController.reloadTable();
    }

    @FXML
    private void backButtonPressed() {
        pathController.leaveDirectory();
    }

    @FXML
    private void homeButtonPressed() {
        pathController.goToHomeDirectory();
    }

    @FXML
    private void reloadFileSystemPressed() {
        // TODO initFileSDropDown();
    }


    // getter
    public TableController getTableController() {
        return tableController;
    }

    public SizeController getSizeController() {
        return sizeController;
    }

    public FileSystemController getFileSystemController() {
        return fileSystemController;
    }

    public Settings getSettings() {
        return settings;
    }

    public PathController getPathController() {
        return  pathController;
    }
}
