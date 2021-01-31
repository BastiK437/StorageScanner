package gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import scanner.DirectoryManager;
import helper.TableContent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TableController {

    // fxml elements
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


    // other gui classes
    private PathController pathController;
    private Settings settings;
    private SizeController sizeController;

    // private classes
    private TableUpdate tableUpdateThread;
    private List<TableContent> currentTable;
    private DirectoryManager directoryManager;

    public void init(GUIController guiController, TableView table, TableColumn nameColumn, TableColumn sizeColumn, TableColumn filesColumn, TableColumn dirsColumn) {
        this.table = table;
        this.nameColumn = nameColumn;
        this.sizeColumn = sizeColumn;
        this.filesColumn = filesColumn;
        this.dirsColumn = dirsColumn;

        this.pathController = guiController.getPathController();
        this.settings = guiController.getSettings();
        this.sizeController = guiController.getSizeController();

        directoryManager = new DirectoryManager(this, settings);


        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
        filesColumn.setCellValueFactory(new PropertyValueFactory<>("files"));
        dirsColumn.setCellValueFactory(new PropertyValueFactory<>("dirs"));

        // set event handler if a double click on a row happens
        table.setRowFactory( tv -> {
            TableRow<TableContent> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    pathController.enterDirectory(row.getItem().getName());
                }
            });
            return row ;
        });
    }



    // public functions
    public void setPath(String path) {
        getTable(path);
    }

    public void reloadTable(boolean completeReload) {
        if(completeReload) {
            getNewTable(pathController.getPath());
        } else {
            getTable(pathController.getPath());
        }
    }
    
    // get called by directory manager
    public void setTableContent(List<TableContent> tableContent) {
        updateTable(tableContent);
    }



    // private functions
    private void getTable(String path) {
        if(path == null) {
            return;
        }
        directoryManager.getTableToPath(path, settings.getIgnoreHiddenElements());
    }

    
    private void getNewTable(String path) {
        if(path == null) {
            return;
        }
        // get settings
        directoryManager.createTreeToPath(path, settings.getIgnoreHiddenElements());
    }

    private void updateTable(List<TableContent> tc) {
        currentTable = new ArrayList<>(tc);

        if(tableUpdateThread != null) {
            tableUpdateThread.interrupt();
            tableUpdateThread.join();
        }

        tableUpdateThread = new TableUpdate();
        Platform.runLater(tableUpdateThread);
    }







    // private classes
    class TableUpdate implements Runnable {
        private boolean interrupt;
        private boolean running;

        @Override
        public void run() {
            running = true;
            interrupt = false;

            Collections.sort(currentTable);
            sizeController.manipulateSizeOfContent(currentTable);

            List<TableContent> tmpList = new ArrayList<>(currentTable);
            table.getItems().clear();

            for (TableContent content: tmpList) {
                if(interrupt) {
                    break;
                }
                if(content == null) {
                    break;
                }else{
                    table.getItems().add(content);
                }
            }

            running = false;
        }

        public void interrupt() {
            interrupt = true;
        }

        public void join() {
            while(running) {
                try {
                    Thread.currentThread().wait(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
