package NEODatabase;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.util.Date;

public class NEOViewer {

    private NEODatabase neoDatabase;
    private String sortOption;

    private Button submitQueryButton;
    private TextField pageNumberInputField;
    private MenuButton sortingMenu;
    private Text status;
    private TableView<NearEarthObject> tableView;

    public NEOViewer() {
        // initialize database variables
        neoDatabase = new NEODatabase();
        sortOption = null;

        // initialize GUI components
        submitQueryButton = new Button("Submit Query");
        pageNumberInputField = new TextField();
        pageNumberInputField.setPromptText("Page Number");
        sortingMenu = new MenuButton("Sorting Options", null,
                new MenuItem("Reference ID"), new MenuItem("Approach Date"),
                new MenuItem("Average Diameter"), new MenuItem("Miss Distance"));
        status = new Text();
        tableView = new TableView<>();

        TableColumn<NearEarthObject, Integer> referenceIDCol = new TableColumn<>("Reference ID");
        referenceIDCol.setCellValueFactory(new PropertyValueFactory<>("referenceID"));
        referenceIDCol.setPrefWidth(125);

        TableColumn<NearEarthObject, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(200);

        TableColumn<NearEarthObject, String> orbitingBodyCol = new TableColumn<>("Orbiting Body");
        orbitingBodyCol.setCellValueFactory(new PropertyValueFactory<>("orbitingBody"));
        orbitingBodyCol.setPrefWidth(150);

        TableColumn<NearEarthObject, Double> absoluteMagnitudeCol = new TableColumn<>("Absolute Magnitude");
        absoluteMagnitudeCol.setCellValueFactory(new PropertyValueFactory<>("absoluteMagnitude"));
        absoluteMagnitudeCol.setPrefWidth(125);

        TableColumn<NearEarthObject, Double> averageDiameterCol = new TableColumn<>("Average Diameter");
        averageDiameterCol.setCellValueFactory(new PropertyValueFactory<>("averageDiameter"));
        averageDiameterCol.setPrefWidth(125);

        TableColumn<NearEarthObject, Double> missDistanceCol = new TableColumn<>("Miss Distance");
        missDistanceCol.setCellValueFactory(new PropertyValueFactory<>("missDistance"));
        missDistanceCol.setPrefWidth(100);

        TableColumn<NearEarthObject, Boolean> isDangerousCol = new TableColumn<>("Is Dangerous?");
        isDangerousCol.setCellValueFactory(new PropertyValueFactory<>("isDangerous"));
        isDangerousCol.setPrefWidth(100);

        TableColumn<NearEarthObject, Date> closestApproachDateCol = new TableColumn<>("Closest Approach Date");
        closestApproachDateCol.setCellValueFactory(new PropertyValueFactory<>("closestApproachDate"));
        closestApproachDateCol.setPrefWidth(225);

        tableView.getColumns().addAll(
                referenceIDCol, nameCol,
                orbitingBodyCol, absoluteMagnitudeCol, averageDiameterCol,
                missDistanceCol, isDangerousCol, closestApproachDateCol
        );
    }

    private void processQuery() {
        int pageNumber;
        try {
            pageNumber = Integer.parseInt(pageNumberInputField.getText());
        }
        catch (NumberFormatException numberFormatException) {
            status.setText("MUST ENTER A VALID PAGE NUMBER");
            return;
        }

        String queryURL;
        try {
            queryURL = neoDatabase.buildQueryURL(pageNumber);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            status.setText("MUST PICK A PAGE NUMBER IN THE INTERVAL [0, 715]");
            return;
        }

        try {
            neoDatabase.addAll(queryURL);

            switch (sortOption) {
                case "R" -> neoDatabase.sort(new ReferenceIDComparator());
                case "D" -> neoDatabase.sort(new DiameterComparator());
                case "A" -> neoDatabase.sort(new ApproachDateComparator());
                case "M" -> neoDatabase.sort(new MissDistanceComparator());
                default -> {
                    status.setText("MUST SELECT A SORTING OPTION");
                    return;
                }
            }

            buildTable();
        }
        catch (IllegalArgumentException illegalArgumentException) {
            status.setText("SOMETHING WENT WRONG WHEN BUILDING QUERY");
        }
    }

    private void buildTable() {
        NearEarthObject[] items = NEODatabase.castArray(new NearEarthObject[tableView.getItems().toArray().length],
                tableView.getItems().toArray());

        for (NearEarthObject currentNEO : items) {
            tableView.getItems().remove(currentNEO);
        }

        for (NearEarthObject newNEO : neoDatabase.getTable()) {
            tableView.getItems().add(newNEO);
        }

        System.out.println("Done");
    }

    public static void display() {
        Stage neoViewerStage = new Stage();
        neoViewerStage.setTitle("NEO Viewer");
        neoViewerStage.getIcons().add(new Image("file:neo-icon.png"));

        NEOViewer neoViewer = new NEOViewer();

        HBox hBox = new HBox(neoViewer.submitQueryButton, neoViewer.sortingMenu,
                neoViewer.pageNumberInputField);
        hBox.setAlignment(Pos.TOP_CENTER);

        VBox vBox = new VBox(hBox, neoViewer.status, neoViewer.tableView);
        vBox.setAlignment(Pos.CENTER);

        neoViewer.submitQueryButton.setOnAction(actionEvent -> neoViewer.processQuery());

        neoViewer.sortingMenu.getItems().get(0).setOnAction(actionEvent -> {
            neoViewer.sortOption = "R";
            neoViewer.sortingMenu.setText("Reference ID");
        });

        neoViewer.sortingMenu.getItems().get(1).setOnAction(actionEvent -> {
            neoViewer.sortOption = "A";
            neoViewer.sortingMenu.setText("Approach Date");
        });

        neoViewer.sortingMenu.getItems().get(2).setOnAction(actionEvent -> {
            neoViewer.sortOption = "D";
            neoViewer.sortingMenu.setText("Average Diameter");
        });

        neoViewer.sortingMenu.getItems().get(3).setOnAction(actionEvent -> {
            neoViewer.sortOption = "M";
            neoViewer.sortingMenu.setText("Miss Distance");
        });

        Scene neoViewerScene = new Scene(vBox, 1150, 620);
        neoViewerStage.setScene(neoViewerScene);
        neoViewerStage.showAndWait();
    }

}
