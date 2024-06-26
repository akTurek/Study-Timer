package sample.stimer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.util.Timer;

public class StartController {
    private SaveData db = new SaveData();
    private boolean addNewPredmetClicked = false;


    private String currentPredmet;
    private String[] listPredmeti;
    private int[] listPredmetovCas;

    private int [] arrayNapredka;
    private String napredek;

    PredmetHolder predmetHolder = PredmetHolder.getInstance();

    @FXML
    private TextField addNewPredmetField;
    @FXML
    private Button addNewPredmetButton;
    @FXML
    private Text casUcenja;
    @FXML
    private ListView<String> listView;
    @FXML
    private PieChart pieChart;
    @FXML
    private Button sceneChangeButton;
    @FXML
    private HBox  hBox;
    @FXML
    private CategoryAxis xAxis = new CategoryAxis();
    @FXML
    private NumberAxis yAxis = new NumberAxis();
    @FXML
    private BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);



    public StartController() throws IOException {

    }
        @FXML
        private void initialize() {
            listPredmeti = db.getArrayListPredmetov().toArray(new String[0]);

            listView.getItems().addAll(listPredmeti);
            listView.getSelectionModel().clearSelection();

            // Add a listener to handle selection changes in the ListView
            listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    currentPredmet = listView.getSelectionModel().getSelectedItem();
                    predmetHolder.setPredmet(currentPredmet);
                    try {
                        sceneChangeToPredmet();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            arrayNapredka = SaveDataToDo.opravljenaNeopravljenaPieChart(db.getArrayListPredmetov());
            updateNapredek();
            drawPieChart();
            drawBarChart();

        }

    @FXML
    private void addNewPredmet() {
        if (!addNewPredmetClicked) {
            // Enable the text field for adding a new predmet
            addNewPredmetClicked = true;
            addNewPredmetField.setVisible(true);
            addNewPredmetField.setDisable(false);
        } else {
            // Add the new predmet to the database and update UI
            String newPredmet = addNewPredmetField.getText();
            if (!newPredmet.equals("")){
                db.newPredmet(newPredmet);
                listView.getItems().add(newPredmet);
                System.out.println(newPredmet);
                addNewPredmetField.clear();
                addNewPredmetField.setVisible(false);
                addNewPredmetField.setDisable(true);
                addNewPredmetClicked = false;
            }

        }
    }



    @FXML
    private String updateTime(){
        String barCharNaslov;
        int [] casArrray = db.getArrayOfCas();
        int input = 0;
        for (int i = 0; i < db.getArrayOfCas().length; i++) {
            input += casArrray[i];
        }
        int numberOfDays = input / 86400;
        int numberOfHours = (input % 86400) / 3600 ;
        int numberOfMinutes = ((input % 86400) % 3600) / 60;
        int numberOfSeconds = ((input % 86400) % 3600) % 60;
        String formattedTime = String.format("Cas ucenja: \n%d dni, %d ur, %d min, %d sec",
                numberOfDays, numberOfHours, numberOfMinutes, numberOfSeconds);
        barCharNaslov = formattedTime;
        return barCharNaslov;
    }

    @FXML
    private void updateNapredek() {
        napredek = String.format("%d / %d", arrayNapredka[0], arrayNapredka[0] + arrayNapredka[1]);
    }



    @FXML
    private void sceneChangeToPredmet () throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("predmet-view.fxml"));
        Stage stage = (Stage) listView.getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add("style.css");
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void drawPieChart(){
        int [] PieChartData = arrayNapredka;
        PieChart.Data[] podatkiGrafa = {
                new PieChart.Data("Opravljeni", PieChartData[0]),
                new PieChart.Data("Neopravljeni", PieChartData[1])
        };

        pieChart.getData().addAll(podatkiGrafa);
        pieChart.setTitle("Task done: "+napredek);
        pieChart.setLabelsVisible(false);

    }


    @FXML
    private void drawBarChart(){
        listPredmetovCas = db.getArrayOfCas();

        xAxis.setLabel("Predmeti");
        yAxis.setLabel("Cas");
        barChart.setTitle(updateTime());

        // Ustvarite serije podatkov
        XYChart.Series<String, Number> dataSeries1 = new XYChart.Series<>();
        dataSeries1.setName("Minute ucenja");

        for (int i = 0; i < listPredmetovCas.length; i++) {
            System.out.print(listPredmeti[i]);
            System.out.print(listPredmetovCas[i]);
            System.out.println();
            dataSeries1.getData().add(new XYChart.Data<>(listPredmeti[i], (int)(listPredmetovCas[i]/60)));
        }

        barChart.getData().addAll(dataSeries1);



    }



}
