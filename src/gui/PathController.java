package gui;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.File;

public class PathController {

    // fxml objects
    @FXML
    private TextField pathTextField;

    // private vars
    private String homePath;
    private String path;

    // other gui classes
    private TableController tableController;


    // constructor
    public PathController(GUIController guiController, TextField pathTextField) {
        this.pathTextField = pathTextField;

        guiController.getTableController();
    }


    // TODO
    // pfad selber eingeben

    public void setDisk(String disk) {
        this.path = disk;
        this.homePath = disk;

        // TODO
        tableController.clearTable();
        tableController.setPath(path);
        updatePathTextField();
    }

    public void enterDirectory(String directory) {
        String tmpPath;
        if(path.equals("/")) {
            tmpPath  = path + directory;
        }else {
            tmpPath = path + "/" + directory;
        }

        File f = new File(tmpPath);

        if(f.exists() && f.isDirectory()) {
            path = tmpPath;
            tableController.setPath(path);
            updatePathTextField();
        }
    }

    public void leaveDirectory() {
        if(path == null) {
            System.out.printf("path not set yet\n");
            return;
        }

        int lastDirIndex = 0;

        for(int i=0; i<path.length(); i++) {
            if(path.charAt(i) == '/') {
                lastDirIndex = i;
            }
        }

        path = path.substring(0, lastDirIndex);

        tableController.setPath(path);
        updatePathTextField();
    }

    public void goToHomeDirectory() {
        if(homePath == null) {
            return;
        }
        path = homePath;
        tableController.setPath(homePath);
        updatePathTextField();
    }

    public String getPath() {
        return path;
    }

    private void updatePathTextField() {
        pathTextField.setText(path);
    }
}
