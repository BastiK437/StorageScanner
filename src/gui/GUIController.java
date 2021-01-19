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
    private GUIController controller = this;

    private Thread searchThread;
    private boolean tableReady = true;

    // gui controller
    private TableController tableController;
    private SizeController sizeController;
    private FileSystemController fileSystemController;
    private Settings settings;

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

        tableController = new TableController(table, nameColumn, sizeColumn, filesColumn, dirsColumn);
        sizeController = new SizeController(sizedropdown, this);
        fileSystemController = new FileSystemController(fileSdropdown);
        settings = new Settings(hiddenElementsCheckbox);
    }

    @FXML
    private void reloadButtonPressed() {
        getNewTable(actualPath, true);
    }

    @FXML
    private void backButtonPressed() {
        if(startPath == null) {
            System.out.printf("Start Path not set yet\n");
            return;
        }
        if(actualPath == null) {
            System.out.printf("Actual Path not set yet\n");
            return;
        }

        String upperPath = "";
        int maxDirs = 0;
        int tmpCnt = 0;

        for(int i=0; i<actualPath.length(); i++) {
            if(actualPath.charAt(i) == '/') {
                maxDirs++;
            }
        }

        if(maxDirs == 1) {
            upperPath = "/";
        }else {
            for (int i = 0; i < actualPath.length(); i++) {
                if (actualPath.charAt(i) == '/') {
                    tmpCnt++;
                }
                if (tmpCnt < maxDirs) {
                    upperPath += actualPath.charAt(i);
                } else {
                    break;
                }
            }
        }
        getNewTable(upperPath, false);

    }

    @FXML
    private void homeButtonPressed() {
        if(startPath == null) {
            System.out.printf("Start Path not set yet\n");
            return;
        }
        if(searchThread != null && searchThread.isAlive()) {
            try {
                searchThread.interrupt();
                searchThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        getNewTable(startPath, false);
    }

    @FXML
    private void reloadFileSystemPressed() {
        initFileSDropDown();
    }



    private void updatePathTextField(String path) {
        pathTextField.setText(path);
    }




    public void putScannedDirs(String path, PathInformation information ){
        if( !scannedDirs.containsKey(path) ){
            scannedDirs.put(path, information);
        }
    }

    public void printMap() {
        for(Map.Entry<String, PathInformation> entry : scannedDirs.entrySet()) {
            String key = entry.getKey();
            PathInformation value = entry.getValue();

            System.out.printf("Key: %s, Value: %d\n", key, value);
        }

        System.out.printf("Map size: %d\n", scannedDirs.size());
    }


    public boolean containsKey( String key ){
        return scannedDirs.containsKey(key);
    }

    // getter
    public PathInformation getKeyInformation(String key ) {
        return scannedDirs.get(key);
    }

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
}
