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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.Medication;
import utilities.AlertInfo;
import utilities.Dates;

/**
 * FXML Controller class
 *
 * @author Craig Lohrman
 */
public class MedicationReportController implements Initializable {
    
    ObservableList<String> yearList = FXCollections.observableArrayList();
    Dates dates = new Dates();

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
    private ComboBox<String> comboYear;
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
        fillComboYeare();
        try {
            tableDisplayloader();
        } catch (SQLException ex) {
            Logger.getLogger(MedicationReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    /**
     * Displays all the medication in the database, and shows the total of each item added up.
     * @param event
     * @throws SQLException 
     */
    @FXML
    public void onActionAll(ActionEvent event) throws SQLException {
        tableDisplayloader();
    }
    
    /**
     * Filters out all the medications that are 30 days old to current, and displays them.
     * @param event
     * @throws ParseException
     * @throws SQLException 
     */
    @FXML
    public void onAction30Days(ActionEvent event) throws ParseException, SQLException {
        
        double cost = 0.00;
        
        ObservableList<Medication> filteredMeds = FXCollections.observableArrayList();
        Calendar currentDate = dates.currentDateFinder();
        Calendar minus30Days = dates.minus30DaysDateFinder();
        
        for(Medication med : MedicationQueries.getAllMeds()){
            String purchaseDate = med.getDateOfPurchase();
            Date datePurchased = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH).parse(purchaseDate);
            Calendar dateBought = Calendar.getInstance();
            dateBought.setTime(datePurchased);
            
            if(dateBought.after(minus30Days) && dateBought.before(currentDate)){
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

    /**
     * Filters out all the medications that are 90 days old to current, and displays them.
     * @param event
     * @throws ParseException
     * @throws SQLException 
     */
    @FXML
    public void onAction90Days(ActionEvent event) throws ParseException, SQLException {
        
        double cost = 0.00;
        
        ObservableList<Medication> filteredVisits = FXCollections.observableArrayList();
        Calendar currentDate = dates.currentDateFinder();
        Calendar minus90Days = dates.minus90DaysDateFinder();
        
        for(Medication med : MedicationQueries.getAllMeds()){
            String purchaseDate = med.getDateOfPurchase();
            Date datePurchased = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH).parse(purchaseDate);
            Calendar dateBought = Calendar.getInstance();
            dateBought.setTime(datePurchased);
            
            if(dateBought.after(minus90Days) && dateBought.before(currentDate)){
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

    /**
     * Filters out all the medications that are in the Spring months of March, April and May of the selected current or last year and displays them.
     * @param event
     * @throws ParseException
     * @throws SQLException 
     */
    @FXML 
    public void onActionSpring(ActionEvent event) throws ParseException, SQLException {
        
        double cost = 0.00;
        int selectedYear = 0;
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        ObservableList<Medication> filteredItems = FXCollections.observableArrayList();
        
        try{
            selectedYear = selectYear();
        }catch(NullPointerException e){
            infoAlert.alertInfomation("Information Dialog", "Please select which year you want.");
        }
        
        for(Medication med : MedicationQueries.getAllMeds()){
            String serviceDate = med.getDateOfPurchase();
            LocalDate getDate = LocalDate.parse(serviceDate, formatter);
            int monthNumber = getDate.getMonthValue();
            int yearNumber = getDate.getYear();

            if((monthNumber == 3 || monthNumber == 4 || monthNumber == 5) && selectedYear == yearNumber){
                filteredItems.add(med);
                cost += med.getCost();
            }
            tableDisplay.setItems(filteredItems);
            DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
            decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
            String formatedCost = decimalFormat.format(cost);
            showTotalCost.setText(formatedCost);
        }
    }

    /**
     * Filters out all the medications that are in the Spring months of June, July, August of the selected current or last year and displays them.
     * @param event
     * @throws ParseException
     * @throws SQLException 
     */
    @FXML
    public void onActionSummer(ActionEvent event) throws ParseException, SQLException {
        
        double cost = 0.00;
        int selectedYear = 0;
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        ObservableList<Medication> filteredItems = FXCollections.observableArrayList();
        
        try{
            selectedYear = selectYear();
        }catch(NullPointerException e){
            infoAlert.alertInfomation("Information Dialog", "Please select which year you want.");
        }
        
        for(Medication med : MedicationQueries.getAllMeds()){
            String serviceDate = med.getDateOfPurchase();
            LocalDate getDate = LocalDate.parse(serviceDate, formatter);
            int monthNumber = getDate.getMonthValue();
            int yearNumber = getDate.getYear();

            if((monthNumber == 6 || monthNumber == 7 || monthNumber == 8) && selectedYear == yearNumber){
                filteredItems.add(med);
                cost += med.getCost();
            }
            tableDisplay.setItems(filteredItems);
            DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
            decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
            String formatedCost = decimalFormat.format(cost);
            showTotalCost.setText(formatedCost);
        }
    }
    
    /**
     * Filters out all the medications that are in the Spring months of September, October, November of the selected current or last year and displays them.
     * @param event
     * @throws ParseException
     * @throws SQLException 
     */
    @FXML
    public void onActionFall(ActionEvent event) throws ParseException, SQLException {
        
        double cost = 0.00;
        int selectedYear = 0;
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        ObservableList<Medication> filteredItems = FXCollections.observableArrayList();
        
        try{
            selectedYear = selectYear();
        }catch(NullPointerException e){
            infoAlert.alertInfomation("Information Dialog", "Please select which year you want.");
        }
        
        for(Medication med : MedicationQueries.getAllMeds()){
            String serviceDate = med.getDateOfPurchase();
            LocalDate getDate = LocalDate.parse(serviceDate, formatter);
            int monthNumber = getDate.getMonthValue();
            int yearNumber = getDate.getYear();

            if((monthNumber == 9 || monthNumber == 10 || monthNumber == 11) && selectedYear == yearNumber){
                filteredItems.add(med);
                cost += med.getCost();
            }
            tableDisplay.setItems(filteredItems);
            DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
            decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
            String formatedCost = decimalFormat.format(cost);
            showTotalCost.setText(formatedCost);
        }
    }

    /**
     * Filters out all the medications that are in the Spring months of December, January, February of the selected current or last year and displays them.
     * @param event
     * @throws ParseException
     * @throws SQLException 
     */
    @FXML
    public void onActionWinter(ActionEvent event) throws ParseException, SQLException {
        
        double cost = 0.00;
        int selectedYear = 0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        ObservableList<Medication> filteredItems = FXCollections.observableArrayList();
        
        try{
            selectedYear = selectYear();
        }catch(NullPointerException e){
            infoAlert.alertInfomation("Information Dialog", "Please select which year you want.");
        }
        
        for(Medication med : MedicationQueries.getAllMeds()){
            String serviceDate = med.getDateOfPurchase();
            LocalDate getDate = LocalDate.parse(serviceDate, formatter);
            int monthNumber = getDate.getMonthValue();
            int yearNumber = getDate.getYear();

            if(monthNumber == 12) {
                if(selectedYear == yearNumber){
                    filteredItems.add(med);
                    cost += med.getCost();
                }
            }
            if(monthNumber == 1 || monthNumber == 2) {
                if(selectedYear == yearNumber){
                    filteredItems.add(med);
                    cost += med.getCost();
                }
            }
            tableDisplay.setItems(filteredItems);
            DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
            decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
            String formatedCost = decimalFormat.format(cost);
            showTotalCost.setText(formatedCost);
        }
    }
    
    /**
     * Switches to the Home screen menu. 
     * @param event
     * @throws IOException 
     */
    @FXML
    public void onActionHome(ActionEvent event) throws IOException {
        switchScreens("/view/Home.fxml", event);
    }

    
    /**
     * Switches to the Reports Menu screen menu. 
     * @param event
     * @throws IOException 
     */
    @FXML
    public void onActionReportsMenu(ActionEvent event) throws IOException {
        switchScreens("/view/reportsMenu.fxml", event);
    }
    
    
    /**
     * Get the information from the database and the table view.
     * @throws SQLException 
     */
    public void tableDisplayloader() throws SQLException {
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
    
    /**
     * Fills the array list with the list of years, then loads the combo box with the list.
     */
    public void fillComboYeare(){
        yearList.addAll("Current Year", "Last Year");
        comboYear.setItems(yearList);
    }
    
    /**
     * Gets the selected year, and returns that years number.
     * @return The year as an int.
     * @throws ParseException 
     */
    public int selectYear() throws ParseException{
        int selectedYear = 2000;
        
        if(comboYear.getValue().equalsIgnoreCase("Current Year")){
            Calendar year = dates.currentDateFinder();
            selectedYear = year.get(Calendar.YEAR);
        }
        else if(comboYear.getValue().equalsIgnoreCase("Last Year")){
            Calendar year = dates.currentDateFinder();
            selectedYear = year.get(Calendar.YEAR) - 1;
        }
        return selectedYear;
    }
    
    /**
     * Makes it easier to switches between screens and not have all this repeated each time the screen is changed.
     * @param location FXML file name to switch too.
     * @param actionEvent Stores the action event.
     * @throws IOException  Throws IO Exception.
     */
    public void switchScreens(String location, ActionEvent actionEvent) throws IOException {
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(location));
        loader.load();
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }
    
    AlertInfo infoAlert = (dialog, message) -> {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(dialog);
        alert.setContentText(message);
        alert.showAndWait();
    };
     
}
