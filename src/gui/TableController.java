package gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import scanner.DirectoryScanner;
import scanner.TableContent;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TableController {

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

    public TableController(TableView table, TableColumn nameColumn, TableColumn sizeColumn, TableColumn filesColumn, TableColumn dirsColumn) {
        this.table = table;
        this.nameColumn = nameColumn;
        this.sizeColumn = sizeColumn;
        this.filesColumn = filesColumn;
        this.dirsColumn = dirsColumn;

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
        filesColumn.setCellValueFactory(new PropertyValueFactory<>("files"));
        dirsColumn.setCellValueFactory(new PropertyValueFactory<>("dirs"));

        table.setRowFactory( tv -> {
            TableRow<TableContent> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    //supportClasses.scanner.TableContent rowData = row.getItem().getName();
                    //System.out.println(row.getItem().getName());
                    String tmpPath;
                    if(actualPath.equals("/")) {
                        tmpPath  = actualPath+row.getItem().getName();
                    }else {
                        tmpPath = actualPath+"/"+row.getItem().getName();
                    }

                    File f = new File(tmpPath);

                    if(f.exists() && f.isDirectory()) {
                        actualPath = tmpPath;
                        getNewTable(actualPath, false);
                    }
                }
            });
            return row ;
        });
    }


    private void getNewTable(String path, boolean reload) {
        if(searchThread != null && searchThread.isAlive()) {
            try {
                searchThread.interrupt();
                searchThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        actualPath = path;
        updatePathTextField(actualPath);
        ds = new DirectoryScanner(actualPath, controller, reload, ignoreHiddenElements);

        searchThread = new Thread(ds);
        searchThread.start();
    }

    public void updateTable(List<TableContent> tc) {
        //Arrays.sort(tc, (newContent, oldContent) -> (int)(newContent.getSizeLong() - oldContent.getSizeLong()));
        actualTable = new ArrayList<>(tc);

        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                while(!tableReady){}
                tableReady = false;
                List<TableContent> tmpList = new ArrayList<>(actualTable);
                table.getItems().clear();
                for (TableContent content: tmpList) {
                    if(content == null) {
                        break;
                    }else{
                        table.getItems().add(content);
                    }
                }
                tableReady = true;
                //table.scrollTo(actualTable.size()-1);
            }
        });

    }

    public void sortTable() {
        Collections.sort(actualTable);
        updateTable(actualTable);
    }
}
