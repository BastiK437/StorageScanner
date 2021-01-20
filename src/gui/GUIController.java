package gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import scanner.CheckAvailableFileSystems;
import scanner.DirectoryScanner;

import java.io.File;
import java.util.*;

public class GUIController {
    private String VERSION = "v0.20";
    public boolean DEBUG = true;


    private String partitionPaths[];
    private DirectoryScanner ds;



    // gui controller
    public TableController tableController;
    public SizeController sizeController;
    public FileSystemController fileSystemController;
    public Settings settings;
    public PathController pathController;

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

    private String diskPath;



    @FXML
    public void initialize() {
        // init fxml objects
        versionText.setText(VERSION);

        settings = new Settings();
        tableController = new TableController();
        sizeController = new SizeController();
        pathController = new PathController();
        fileSystemController = new FileSystemController();

        settings.init(this, hiddenElementsCheckbox);
        tableController.init(this, table, nameColumn, sizeColumn, filesColumn, dirsColumn);
        sizeController .init(this, sizedropdown);
        pathController.init(this, pathTextField);
        fileSystemController.init(this, fileSdropdown);
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

    @FXML
    private void dropdownClicked() {

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
