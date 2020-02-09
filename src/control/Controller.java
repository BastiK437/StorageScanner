package control;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import supportClasses.CheckAvailableFileSystems;
import supportClasses.DirectoryScanner;
import supportClasses.TableContent;

import java.io.File;
import java.util.*;

public class Controller {
    private File partitions[];
    private String partitionPaths[];
    private boolean checkboxInitialized = false;
    private static int selectedPartition = 0;
    private DirectoryScanner ds;
    private Controller controller = this;
    private int selectedSize = 0; // 0=bytes, 1=kB, 2=MB, 3=GB
    private List<TableContent> actualTable;
    private String actualPath;
    private String startPath;
    private Thread searchThread;
    private boolean tableReady = true;

    @FXML
    private ChoiceBox fileSdropdown;
    @FXML
    private TableView table;
    @FXML
    private TableColumn nameColumn;
    @FXML
    private TableColumn sizeColumn;
    @FXML
    private ChoiceBox sizedropdown;
    @FXML
    private TextField pathTextField;

    @FXML
    public void dropdownClicked() {
        initCheckbox();

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
        sizeColumn.setText("Size (bytes)");

        sizedropdown.setItems(FXCollections.observableArrayList(
                "Size (bytes)", "Size (kB)", "Size (MB)", "Size (GB)")
        );
        sizedropdown.getSelectionModel().select(0);

        sizedropdown.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                updateSizeConvertion((int) number2 );
            }
        });

        fileSdropdown.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                table.getItems().clear();

                //startPath = (String)fileSdropdown.getItems().get((Integer) number2);
                startPath = "/home/basti/Studium_MEGA";
                getNewTable(startPath);

            }
        });

        table.setRowFactory( tv -> {
            TableRow<TableContent> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    //supportClasses.TableContent rowData = row.getItem().getName();
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
                        getNewTable(actualPath);
                    }
                }
            });
            return row ;
        });
    }

    @FXML
    private void backButtonPressed() {
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
        getNewTable(upperPath);

    }

    @FXML
    private void homeButtonPressed() {
        if(searchThread != null && searchThread.isAlive()) {
            try {
                searchThread.interrupt();
                searchThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        getNewTable(startPath);
    }

    private void getNewTable(String path) {
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
        ds = new DirectoryScanner(actualPath, controller);

        searchThread = new Thread(ds);
        searchThread.start();
    }

    private void updatePathTextField(String path) {
        pathTextField.setText(path);
    }

    private void updateSizeConvertion(int size) {
        selectedSize = size;
        if(actualTable != null) {
            for(int i=0; i<actualTable.size(); i++) {
                actualTable.get(i).setSizeConvertion(size);
            }
            updateTable(actualTable);
        }
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

    private void initCheckbox() {
        CheckAvailableFileSystems fileSystems = new CheckAvailableFileSystems();

        //System.out.printf("Davor: %s\n", ZonedDateTime.now().toString() );
        partitions = fileSystems.checkFileSystems();

        partitionPaths = new String[partitions.length];

        for( int i=0; i<partitions.length; i++) {
            partitionPaths[i] = partitions[i].getPath();
        }

        fileSdropdown.setItems(FXCollections.observableArrayList(partitionPaths));
        System.out.printf("Items set\n");
    }

    public int getselectedSize() {
        return selectedSize;
    }
}
