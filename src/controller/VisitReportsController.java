/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import dao.JDBC;
import dao.VisitQueries;
import java.io.IOException;
import java.math.RoundingMode;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
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
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.Visit;

/**
 * FXML Controller class
 *
 * @author Craig Lohrman
 */
public class VisitReportsController implements Initializable {
    

    @FXML
    private ComboBox<String> comboVisitList;
    @FXML
    private TableView<Visit> tableDisplay;
    @FXML
    private TableColumn<Visit, String> tblColAnimalFor;
    @FXML
    private TableColumn<Visit, String> tblColCost;
    @FXML
    private TableColumn<Visit, String> tblColDate;
    @FXML
    private TableColumn<Visit, String> tblColER;
    @FXML
    private TableColumn<Visit, String> tblColName;
    @FXML
    private TableColumn<Visit, String> tblColReason;
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
        tblColER.setCellValueFactory(cellData -> {
            return new ReadOnlyStringWrapper(cellData.getValue().getEmergency());
        });
        tblColDate.setCellValueFactory(cellData -> {
            return new ReadOnlyStringWrapper(cellData.getValue().getDateOfService());
        });
        tblColCost.setCellValueFactory(cellData -> {
            return new ReadOnlyObjectWrapper(cellData.getValue().getCost());
        });
        tblColReason.setCellValueFactory(cellData -> {
            return new ReadOnlyStringWrapper(cellData.getValue().getReason());
        });
        try {
            tableDisplay.setItems(VisitQueries.getAllVisits());
        } catch (SQLException ex) {
            Logger.getLogger(VisitReportsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    void onActionAll(ActionEvent event) throws SQLException {
        double cost = 0.00;
        for(Visit v : VisitQueries.getAllVisits()){
            cost += v.getCost();
        }
        tableDisplay.setItems(VisitQueries.getAllVisits());
         DecimalFormat df = new DecimalFormat("#,###.##");
         df.setRoundingMode(RoundingMode.HALF_UP);
         String rounded = df.format(cost);
         showTotalCost.setText(rounded);
    }
    
    @FXML
    void onAction30Days(ActionEvent event) throws ParseException, SQLException {
        
        double cost = 0.00;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        ObservableList<Visit> allVisits = VisitQueries.getAllVisits();
        ObservableList<Visit> filteredVisits = FXCollections.observableArrayList();
        int monthNum = currentMonthFinder() + 1;
        
        for(Visit v : allVisits){
            String serviceDate = v.getDateOfService();
            LocalDate getDate = LocalDate.parse(serviceDate, formatter);
            int monthNumber = getDate.getMonthValue();
            
            if(monthNumber == monthNum){
                filteredVisits.add(v);
                cost += v.getCost();
            }
            tableDisplay.setItems(filteredVisits);
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
        ObservableList<Visit> allVisits = VisitQueries.getAllVisits();
        ObservableList<Visit> filteredVisits = FXCollections.observableArrayList();
        int monthCur = currentMonthFinder() + 1;
        int monthTo = currentMonthFinder() + 4;
        
        for(Visit v : allVisits){
            String serviceDate = v.getDateOfService();
            LocalDate getDate = LocalDate.parse(serviceDate, formatter);
            int monthNumber = getDate.getMonthValue();
            
            if(monthNumber >= monthCur && monthNumber <= monthTo){
                filteredVisits.add(v);
                cost += v.getCost();
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
        ObservableList<Visit> allVisits = VisitQueries.getAllVisits();
        ObservableList<Visit> filteredVisits = FXCollections.observableArrayList();
        
        for(Visit v : allVisits){
            String serviceDate = v.getDateOfService();
            LocalDate getDate = LocalDate.parse(serviceDate, formatter);
            int monthNumber = getDate.getMonthValue();
            
            if(monthNumber == 3 || monthNumber == 4 || monthNumber == 5){
                filteredVisits.add(v);
                cost += v.getCost();
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
        ObservableList<Visit> allVisits = VisitQueries.getAllVisits();
        ObservableList<Visit> filteredVisits = FXCollections.observableArrayList();
        
        for(Visit v : allVisits){
            String serviceDate = v.getDateOfService();
            LocalDate getDate = LocalDate.parse(serviceDate, formatter);
            int monthNumber = getDate.getMonthValue();
            
            if(monthNumber == 6 || monthNumber == 7 || monthNumber == 8){
                filteredVisits.add(v);
                cost += v.getCost();
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
        ObservableList<Visit> allVisits = VisitQueries.getAllVisits();
        ObservableList<Visit> filteredVisits = FXCollections.observableArrayList();
        
        for(Visit v : allVisits){
            String serviceDate = v.getDateOfService();
            LocalDate getDate = LocalDate.parse(serviceDate, formatter);
            int monthNumber = getDate.getMonthValue();
            
            if(monthNumber == 9 || monthNumber == 10 || monthNumber == 11){
                filteredVisits.add(v);
                cost += v.getCost();
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
        ObservableList<Visit> allVisits = VisitQueries.getAllVisits();
        ObservableList<Visit> filteredVisits = FXCollections.observableArrayList();
        
        for(Visit v : allVisits){
            String serviceDate = v.getDateOfService();
            LocalDate getDate = LocalDate.parse(serviceDate, formatter);
            int monthNumber = getDate.getMonthValue();
            
            if(monthNumber == 12 || monthNumber == 1 || monthNumber == 2){
                filteredVisits.add(v);
                cost += v.getCost();
            }
            tableDisplay.setItems(filteredVisits);
            DecimalFormat df = new DecimalFormat("#,###.##");
            df.setRoundingMode(RoundingMode.HALF_UP);
            String rounded = df.format(cost);
            showTotalCost.setText(rounded);
        }
    }
    
    private double findTotalCost(){
        double totalCost = 0.00;
        
        return totalCost;
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