import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.time.ZonedDateTime;
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

                //ds = new DirectoryScanner((String)fileSdropdown.getItems().get((Integer) number2), controller);
                ds = new DirectoryScanner("/home/basti/Studium_MEGA", controller);

                Thread t = new Thread(ds);
                t.start();
            }
        });

        table.setRowFactory( tv -> {
            TableRow<TableContent> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    TableContent rowData = row.getItem();
                    System.out.println(rowData);
                }
            });
            return row ;
        });
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

                table.getItems().clear();
                for (TableContent content: actualTable) {
                    if(content == null) {
                        break;
                    }else{
                        table.getItems().add(content);
                    }
                }
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
