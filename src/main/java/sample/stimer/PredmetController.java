package sample.stimer;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;



public class PredmetController {

    private SaveData db = new SaveData();

    private boolean addNewToDoClicked = false;
    private String currentPredmet;
    private String[] listToDo;
    private long timestamp;
    private int [] arrayNapredka;
    private ArrayList<String> predmet = new ArrayList<String>();
    private String napredek;
    private  int indexselect;

    PredmetHolder predmetHolder = PredmetHolder.getInstance();


    @FXML
    private TextField addNewToDoField;
    @FXML
    private Button addNewToDoButton;
    @FXML
    private Button deleteToDoButton;
    @FXML
    private Button checkUncheckButton;
    @FXML
    private Text imePredmeta;
    @FXML
    private Text casUcenja;
    @FXML
    private ListView<String> toDdListView;
    @FXML
    private Button sceneChangeButton;
    @FXML
    private Button delPredmet;

    String curentOpravilo;


    @FXML
    private TextField UreField;
    @FXML
    private TextField MinureField;
    @FXML
    private TextField SekundeField;
    @FXML
    private Button startEndTimerButton;
    @FXML
    private PieChart pieChart;

    private  Timer timer;
    private boolean isPaused = false;
    private boolean startStopTimerClicked = false;
    private int timerPrev;
    int ure, minute, sekunde;

    public PredmetController() throws IOException {
    }

    @FXML
    private void initialize() {
        currentPredmet = predmetHolder.getPredmet();
        imePredmeta.setText(currentPredmet);
        toDdListView.getItems().clear();
        toDdListView.getItems().addAll(SaveDataToDo.arrayOpravil(currentPredmet));
        toDdListView.getSelectionModel().clearSelection();
        listViewLisen();
        drawStoparica();
        updateTime();
        predmet.clear();
        predmet.add(currentPredmet);
        arrayNapredka = SaveDataToDo.opravljenaNeopravljenaPieChart(predmet);
        updateNapredek();
        drawPieChart();

    }

    @FXML
    private void dellPredmet() {

        db.delete(currentPredmet);
        try {
            sceneChangeToStart();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }



    @FXML
    private void addNewToDo() {
        if (!addNewToDoClicked) {
            addNewToDoClicked = true;
            addNewToDoField.setVisible(true);
            addNewToDoField.setDisable(false);
        } else {
            String newToDo = addNewToDoField.getText();
            if (!newToDo.equals("")){
                SaveDataToDo.newOpravilo(currentPredmet, newToDo, toDdListView);
                addNewToDoField.clear();
                toDdListView.getItems().clear();
                toDdListView.getItems().addAll(SaveDataToDo.arrayOpravil(currentPredmet));
                addNewToDoField.setVisible(false);
                addNewToDoField.setDisable(true);
                addNewToDoClicked = false;
            }

        }
        updatePieChart();
    }

    @FXML
    private void dellToDo() {

            System.out.println(curentOpravilo);
            SaveDataToDo.deleteOpravilo(currentPredmet, curentOpravilo);
            toDdListView.getItems().clear();
            toDdListView.getItems().addAll(SaveDataToDo.arrayOpravil(currentPredmet));

        updatePieChart();
    }

    @FXML
    private void checkUncheckToDo() {
            SaveDataToDo.checkUncheck(currentPredmet,curentOpravilo);
            toDdListView.getItems().clear();
            toDdListView.getItems().addAll(SaveDataToDo.arrayOpravil(currentPredmet));

        updatePieChart();
    }


    @FXML
    private void updateTime(){
        int input = db.timeUcenja(currentPredmet);
        int numberOfHours = input / 3600 ;
        int numberOfMinutes = (input % 3600) / 60;
        int numberOfSeconds = (input % 3600) % 60;

        // Preverjanje ali je vsaj ena od enot časa različna od nič
        if (input > 0 ) {
            String formattedTime = String.format("Cas ucenja: \n%d ur, %d min, %d sec",
                    numberOfHours, numberOfMinutes, numberOfSeconds);
            casUcenja.setText(formattedTime);
        } else {
            // Če so vse enote časa enake nič, postavite besedilo na prazno
            casUcenja.setText("Zdaj je pravi cas za ucenje");
        }
    }

    @FXML
    private void sceneChangeToStart() throws IOException {
            Parent root = FXMLLoader.load(getClass().getResource("start-view.fxml"));
            Stage stage = (Stage) toDdListView.getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add("style.css");
            stage.setScene(scene);
            stage.show();

    }

    @FXML
    private void startTimer() {
        sekunde = Integer.parseInt(SekundeField.getText());
        minute = Integer.parseInt(MinureField.getText());
        ure = Integer.parseInt(UreField.getText());
        timerPrev = sekunde+60*minute+360*ure;

        timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (timerPrev > 0) {
                    timerPrev--;
                    Platform.runLater(() -> {
                                if (sekunde > 0) {
                                    sekunde--;
                                    SekundeField.setText(Integer.toString(sekunde));
                                }else {
                                    sekunde=60;
                                    SekundeField.setText(Integer.toString(sekunde));
                                    if (minute > 0){
                                        minute--;
                                        MinureField.setText(Integer.toString(minute));
                                    }else {
                                        minute=60;
                                        MinureField.setText(Integer.toString(minute));
                                        if (ure>0){
                                            ure--;
                                            UreField.setText(Integer.toString(ure));
                                        }
                                    }
                                }
                            }
                    );
                } else {
                    Platform.runLater(() -> {
                        int time = (int) ((System.currentTimeMillis() - timestamp)/1000);
                        System.out.println("stop "+time);
                        db.updateTime(imePredmeta.getText(),time);
                        updateTime();
                        startEndTimerButton.setText("Start");
                        SekundeField.setText("00");
                        MinureField.setText("00");
                        UreField.setText("00");
                        startStopTimerClicked = false;
                        timer.cancel();
                        timer.purge();
                        SekundeField.setDisable(false);
                        MinureField.setDisable(false);
                        UreField.setDisable(false);
                        timer.cancel();
                        timer.purge();
                    });
                }
            }
        }, 0, 1000);
    }


    @FXML
    private void startEndTimer() {
        if (!startStopTimerClicked ) {
            timestamp = System.currentTimeMillis();
            startTimer();
            startEndTimerButton.setText("End");
            startStopTimerClicked = true;
            SekundeField.setDisable(true);
            MinureField.setDisable(true);
            UreField.setDisable(true);

        } else if (startStopTimerClicked){
            int time = (int) ((System.currentTimeMillis() - timestamp)/1000);
            System.out.println("stop "+time);
            db.updateTime(imePredmeta.getText(),time);
            updateTime();
            startEndTimerButton.setText("Start");
            SekundeField.setText("00");
            MinureField.setText("00");
            UreField.setText("00");
            startStopTimerClicked = false;
            timer.cancel();
            timer.purge();
            SekundeField.setDisable(false);
            MinureField.setDisable(false);
            UreField.setDisable(false);
        }
    }

    @FXML
    private void drawPieChart(){
        int[] pieChartData = arrayNapredka;
        if (arrayNapredka[0] + arrayNapredka[1] != 0) {
            pieChart.getData().clear();

            PieChart.Data[] podatkiGrafa = {
                    new PieChart.Data("Opravljeni: " + pieChartData[0], pieChartData[0]),
                    new PieChart.Data("Neopravljeni: " + pieChartData[1], pieChartData[1])
            };

            pieChart.getData().addAll(podatkiGrafa);
            pieChart.setTitle("");
            pieChart.setLabelsVisible(false);
        } else {
            pieChart.getData().clear();
            pieChart.setTitle("Dodaj nekaj opravil za začetek učenja");
            pieChart.setLabelsVisible(true);
        }
    }


    @FXML
    private void updatePieChart(){
        arrayNapredka = SaveDataToDo.opravljenaNeopravljenaPieChart(predmet);
        drawPieChart();

    }



    private void updateNapredek() {
        napredek = String.format("Opravljeni: %d Neopravljeni: %d", arrayNapredka[0], arrayNapredka[1]);
    }




    @FXML
    public void listViewLisen() {
        toDdListView.setOnMouseClicked(event -> {
            String selectedItem = toDdListView.getSelectionModel().getSelectedItem();
            System.out.println("MouseClicked: selectedItem: " + selectedItem);
            if (selectedItem != null) {
                int indexselect = toDdListView.getSelectionModel().getSelectedIndex();
                if (selectedItem.contains("☐")) {
                    curentOpravilo = selectedItem.replaceAll("[☐] ", "");
                    checkUncheckToDo();
                } else if (selectedItem.contains("☑")) {
                    curentOpravilo = selectedItem.replaceAll("[☑] ", "");
                    checkUncheckToDo();
                }
                toDdListView.getSelectionModel().select(indexselect);
                String updatedValue = toDdListView.getSelectionModel().getSelectedItem();
                System.out.println("MouseClicked: updatedValue: " + updatedValue);
            }
        });
    }


    public void drawStoparicaElement(TextField field){
        field.setOnMouseClicked(event -> {
            field.clear();
        });

        field.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (field.getText().isEmpty()){
                    field.setText("00");
                }else if (!newValue.matches("\\d*")) {
                    field.setText(newValue.replaceAll("[^\\d]", ""));
                }else if (Integer.parseInt(field.getText()) > 60){
                    field.setText("60");
                } else if (Integer.parseInt(field.getText()) < 10) {
                    field.setText(Integer.toString(Integer.parseInt(field.getText())));
                }
            }
        });
    }

    public void drawStoparica(){
        drawStoparicaElement(SekundeField);
        drawStoparicaElement(MinureField);
        drawStoparicaElement(UreField);
    }

}










