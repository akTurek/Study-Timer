package sample.stimer;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


public class HelloController {
    private SaveData db = new SaveData(new File("src/main/resources/data.txt"));
    private boolean addNewPredmetClicked = false;
    private boolean addNewToDoClicked = false;
    private String currentPredmet;
    private String[] listPredmeti;
    private String[] listToDo;
    private long timestamp;


    private  Timer timer;
    private boolean isPaused = false;
    private boolean startStopTimerClicked = false;
    private int timerPrev;

    @FXML
    private Button startEndTimerButton;
    @FXML
    private TextField addNewPredmetField;
    @FXML
    private Button addNewPredmetButton;
    @FXML
    private Button deleteButton;
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
    private ListView<String> listView;
    @FXML
    private ListView<String> toDdListView;
    @FXML
    private Label Predmeti;

    @FXML
    private TextField UreField;
    @FXML
    private TextField MinureField;
    @FXML
    private TextField SekundeField;
    @FXML
    private HBox TimerBox;
    @FXML
    private AnchorPane anchorPane;

    int ure, minute, sekunde;
    String checkuncheck;




    public HelloController() throws IOException {
    }

    @FXML
    private void initialize() {


        SekundeField.setOnMouseClicked(event -> {
            SekundeField.clear();
        });
        MinureField.setOnMouseClicked(event -> {
            MinureField.clear();
        });
        UreField.setOnMouseClicked(event -> {
            UreField.clear();
        });

        SekundeField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (SekundeField.getText().isEmpty()){
                    SekundeField.setText("00");
                }else if (!newValue.matches("\\d*")) {
                    SekundeField.setText(newValue.replaceAll("[^\\d]", ""));
                }else if (Integer.parseInt(SekundeField.getText()) > 60){
                    SekundeField.setText("60");
                } else if (Integer.parseInt(SekundeField.getText()) < 10) {
                    SekundeField.setText(Integer.toString(Integer.parseInt(SekundeField.getText())));
                }
            }
        });

        MinureField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (MinureField.getText().isEmpty()){
                    MinureField.setText("00");
                }else if (!newValue.matches("\\d*")) {
                    MinureField.setText(newValue.replaceAll("[^\\d]", ""));
                }else if (Integer.parseInt(MinureField.getText()) > 60){
                    MinureField.setText("60");
                }else if (Integer.parseInt(MinureField.getText()) < 10) {
                    MinureField.setText(Integer.toString(Integer.parseInt(MinureField.getText())));
                }
            }
        });
        UreField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (UreField.getText().isEmpty()){
                    UreField.setText("00");
                }else if (!newValue.matches("\\d*")) {
                    UreField.setText(newValue.replaceAll("[^\\d]", ""));
                }else if (Integer.parseInt(UreField.getText()) > 60){
                    UreField.setText("60");
                }else if (Integer.parseInt(UreField.getText()) < 10) {
                    UreField.setText(Integer.toString(Integer.parseInt(UreField.getText())));
                }
            }
        });


        // Initialize the list of predmeti and populate the ListView
        listPredmeti = db.getArrayListPredmetov().toArray(new String[0]);
        listView.getItems().addAll(listPredmeti);

        // Add a listener to handle selection changes in the ListView
        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                currentPredmet = listView.getSelectionModel().getSelectedItem();
                imePredmeta.setText(currentPredmet);
                casUcenja.setVisible(true);
                updateTime();
                toDdListView.getItems().clear();
                toDdListView.getItems().addAll(SaveDataToDo.arrayOpravil(currentPredmet));
                checkUncheckButton.setDisable(false);
                checkUncheckButton.setVisible(true);
                checkUncheckButton.setText("☑");
                addNewToDoButton.setDisable(false);
                addNewToDoButton.setVisible(true);
                deleteToDoButton.setDisable(false);
                deleteToDoButton.setVisible(true);
                deleteButton.setVisible(true);
                casUcenja.setVisible(true);
                TimerBox.setVisible(true);

            }
        });


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
    private void dellPredmet() {
        String delPredmet = imePredmeta.getText();
        db.delete(delPredmet);
        listView.getItems().remove(delPredmet);

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
    }

    @FXML
    private void dellToDo() {
        String brisanOpravilo = toDdListView.getSelectionModel().getSelectedItem().replaceAll("[☐☑] ","");
        SaveDataToDo.deleteOpravilo(currentPredmet, brisanOpravilo);
        toDdListView.getItems().clear();
        toDdListView.getItems().addAll(SaveDataToDo.arrayOpravil(currentPredmet));
    }

    @FXML
    private void checkUncheckToDo() {
        if (toDdListView.getSelectionModel().getSelectedItem() != null) {
            checkuncheck = toDdListView.getSelectionModel().getSelectedItem();
        }
        SaveDataToDo.checkUncheck(currentPredmet, checkuncheck, toDdListView);
    }









    @FXML
    private void updateTime(){
        int input = db.timeUcenja(currentPredmet);
        int numberOfDays = input / 86400;
        int numberOfHours = (input % 86400) / 3600 ;
        int numberOfMinutes = ((input % 86400) % 3600) / 60;
        int numberOfSeconds = ((input % 86400) % 3600) % 60;
        String formattedTime = String.format("Cas ucenja: \n%d dni, %d ur, %d min, %d sec",
                numberOfDays, numberOfHours, numberOfMinutes, numberOfSeconds);
        casUcenja.setText(formattedTime);
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
                        SekundeField.setText("00");
                        MinureField.setText("00");
                        UreField.setText("00");
                        startEndTimerButton.setText("start");
                        listView.setDisable(false);
                        timer.cancel();
                        timer.purge();
                    });
                }
            }
        }, 0, 1000);
    }


    @FXML
    private void startEndTimer() {
        if (!startStopTimerClicked) {
            timestamp = System.currentTimeMillis();
            startTimer();
            startEndTimerButton.setText("End");
            startStopTimerClicked = true;
            listView.setDisable(true);
            SekundeField.setDisable(true);
            MinureField.setDisable(true);
            UreField.setDisable(true);
            SekundeField.setOpacity(1);
            MinureField.setOpacity(1);
            UreField.setOpacity(1);

        } else if (startStopTimerClicked){
            int time = (int) ((System.currentTimeMillis() - timestamp)/1000);
            System.out.println("stop "+time);
            db.updateTime(imePredmeta.getText(),time);
            updateTime();
            startEndTimerButton.setText("Start");
            SekundeField.setText("00");
            MinureField.setText("00");
            UreField.setText("00");
            listView.setDisable(false);
            startStopTimerClicked = false;
            timer.cancel();
            timer.purge();
            listView.setDisable(false);
            SekundeField.setDisable(false);
            MinureField.setDisable(false);
            UreField.setDisable(false);
        }
    }







}
