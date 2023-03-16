/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import dao.MedicationQueries;
import java.io.IOException;
import java.math.RoundingMode;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.Medication;

/**
 * FXML Controller class
 *
 * @author Craig Lohrman
 */
public class MedicationReportController implements Initializable {
    
    @FXML
    private TableView<Medication> tableDisplay;
    @FXML
    private TableColumn<Medication, String> tblColAnimalFor;
    @FXML
    private TableColumn<Medication, String> tblColCost;
    @FXML
    private TableColumn<Medication, String> tblColDate;
    @FXML
    private TableColumn<Medication, String> tblColER;
    @FXML
    private TableColumn<Medication, String> tblColName;
    @FXML
    private TableColumn<Medication, String> tblColReason;
    @FXML
    private Label showTotalCost;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tblColName.setCellValueFactory(cellData -> {
            return new ReadOnlyStringWrapper(cellData.getValue().getName());
        });
        tblColAnimalFor.setCellValueFactory(cellData -> {
            return new ReadOnlyStringWrapper(cellData.getValue().getAnimalFor());
        });
        tblColCost.setCellValueFactory(cellData -> {
            return new ReadOnlyObjectWrapper(cellData.getValue().getCost());
        });
        tblColDate.setCellValueFactory(cellData -> {
            return new ReadOnlyStringWrapper(cellData.getValue().getDateOfPurchase());
        });
        tblColER.setCellValueFactory(cellData -> {
            return new ReadOnlyStringWrapper(cellData.getValue().getEmergency());
        });
        tblColReason.setCellValueFactory(cellData -> {
            return new ReadOnlyStringWrapper(cellData.getValue().getReason());
        });
        
        try {
            tableDisplay.setItems(MedicationQueries.getAllMeds());
        } catch (SQLException ex) {
            Logger.getLogger(MedicationReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    @FXML
    void onActionAll(ActionEvent event) throws SQLException {
        double cost = 0.00;
        for(Medication med : MedicationQueries.getAllMeds()){
            cost += med.getCost();
        }
        tableDisplay.setItems(MedicationQueries.getAllMeds());
         DecimalFormat df = new DecimalFormat("#,###.##");
         df.setRoundingMode(RoundingMode.HALF_UP);
         String rounded = df.format(cost);
         showTotalCost.setText(rounded);
    }
    
    @FXML
    void onAction30Days(ActionEvent event) throws ParseException, SQLException {
        
        double cost = 0.00;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        ObservableList<Medication> filteredMeds = FXCollections.observableArrayList();
        int monthNum = currentMonthFinder() + 1;
        
        for(Medication med : MedicationQueries.getAllMeds()){
            String purchaseDate = med.getDateOfPurchase();
            LocalDate getDate = LocalDate.parse(purchaseDate, formatter);
            int monthNumber = getDate.getMonthValue();
            
            if(monthNumber == monthNum){
                filteredMeds.add(med);
                cost += med.getCost();
            }
            tableDisplay.setItems(filteredMeds);
            DecimalFormat df = new DecimalFormat("#,###.##");
            df.setRoundingMode(RoundingMode.HALF_UP);
            String rounded = df.format(cost);
            showTotalCost.setText(rounded);
        }
    }

    @FXML
    void onAction90Days(ActionEvent event) throws ParseException, SQLException {
        
        double cost = 0.00;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        ObservableList<Medication> filteredVisits = FXCollections.observableArrayList();
        int monthCur = currentMonthFinder() + 1;
        int monthTo = currentMonthFinder() + 4;
        
        for(Medication med : MedicationQueries.getAllMeds()){
            String purchaseDate = med.getDateOfPurchase();
            LocalDate getDate = LocalDate.parse(purchaseDate, formatter);
            int monthNumber = getDate.getMonthValue();
            
            if(monthNumber >= monthCur && monthNumber <= monthTo){
                filteredVisits.add(med);
                cost += med.getCost();
            }
            tableDisplay.setItems(filteredVisits);
            DecimalFormat df = new DecimalFormat("#,###.##");
            df.setRoundingMode(RoundingMode.HALF_UP);
            String rounded = df.format(cost);
            showTotalCost.setText(rounded);
        }
    }

    @FXML //3, 4, 5
    void onActionSpring(ActionEvent event) throws ParseException, SQLException {
        
        double cost = 0.00;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        ObservableList<Medication> filteredVisits = FXCollections.observableArrayList();
        
        for(Medication med : MedicationQueries.getAllMeds()){
            String serviceDate = med.getDateOfPurchase();
            LocalDate getDate = LocalDate.parse(serviceDate, formatter);
            int monthNumber = getDate.getMonthValue();
            
            if(monthNumber == 3 || monthNumber == 4 || monthNumber == 5){
                filteredVisits.add(med);
                cost += med.getCost();
            }
            tableDisplay.setItems(filteredVisits);
            DecimalFormat df = new DecimalFormat("#,###.##");
            df.setRoundingMode(RoundingMode.HALF_UP);
            String rounded = df.format(cost);
            showTotalCost.setText(rounded);
        }
    }

    @FXML //6, 7, 8
    void onActionSummer(ActionEvent event) throws ParseException, SQLException {
        
        double cost = 0.00;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        ObservableList<Medication> filteredVisits = FXCollections.observableArrayList();
        
        for(Medication med : MedicationQueries.getAllMeds()){
            String serviceDate = med.getDateOfPurchase();
            LocalDate getDate = LocalDate.parse(serviceDate, formatter);
            int monthNumber = getDate.getMonthValue();
            
            if(monthNumber == 6 || monthNumber == 7 || monthNumber == 8){
                filteredVisits.add(med);
                cost += med.getCost();
            }
         
            tableDisplay.setItems(filteredVisits);
            DecimalFormat df = new DecimalFormat("#,###.##");
            df.setRoundingMode(RoundingMode.HALF_UP);
            String rounded = df.format(cost);
            showTotalCost.setText(rounded);
        }
    }
    
    @FXML //9, 10, 11
    void onActionFall(ActionEvent event) throws ParseException, SQLException {
        
        double cost = 0.00;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        ObservableList<Medication> filteredVisits = FXCollections.observableArrayList();
        
        for(Medication med : MedicationQueries.getAllMeds()){
            String serviceDate = med.getDateOfPurchase();
            LocalDate getDate = LocalDate.parse(serviceDate, formatter);
            int monthNumber = getDate.getMonthValue();
            
            if(monthNumber == 9 || monthNumber == 10 || monthNumber == 11){
                filteredVisits.add(med);
                cost += med.getCost();
            }
            tableDisplay.setItems(filteredVisits);
            DecimalFormat df = new DecimalFormat("#,###.##");
            df.setRoundingMode(RoundingMode.HALF_UP);
            String rounded = df.format(cost);
            showTotalCost.setText(rounded);
        }
    }

    @FXML //12, 1, 2
    void onActionWinter(ActionEvent event) throws ParseException, SQLException {
        
        double cost = 0.00;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        ObservableList<Medication> filteredVisits = FXCollections.observableArrayList();
        
        for(Medication med : MedicationQueries.getAllMeds()){
            String serviceDate = med.getDateOfPurchase();
            LocalDate getDate = LocalDate.parse(serviceDate, formatter);
            int monthNumber = getDate.getMonthValue();
            
            if(monthNumber == 12 || monthNumber == 1 || monthNumber == 2){
                filteredVisits.add(med);
                cost += med.getCost();
            }
            tableDisplay.setItems(filteredVisits);
            DecimalFormat df = new DecimalFormat("#,###.##");
            df.setRoundingMode(RoundingMode.HALF_UP);
            String rounded = df.format(cost);
            showTotalCost.setText(rounded);
        }
    }
    
    private int currentMonthFinder() throws ParseException{
        DateTimeFormatter formatMMM = DateTimeFormatter.ofPattern("MMM");
        
        LocalDateTime currentDateLDT = LocalDateTime.now();
        String currentMonth = currentDateLDT.format(formatMMM);
        
        Date newDate = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(currentMonth);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(newDate);
        int monthNumber = calendar.get(Calendar.MONTH);
        
        return monthNumber;
    }
    
    @FXML
    void onActionHome(ActionEvent event) throws IOException {
        switchScreens("/view/Home.fxml", event);
    }

    @FXML
    void onActionReportsMenu(ActionEvent event) throws IOException {
        switchScreens("/view/reportsMenu.fxml", event);
    }
    
     public void switchScreens(String location, ActionEvent actionEvent) throws IOException {
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(location));
        loader.load();
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }
}
