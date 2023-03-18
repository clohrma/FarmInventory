/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import dao.ItemQueries;
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
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.Item;

/**
 * FXML Controller class
 *
 * @author Craig Lohrman
 */
public class ItemReportsController implements Initializable {

    @FXML
    private TableView<Item> tableDisplay;
    @FXML
    private TableColumn<Item, String> tblColAnimalFor;
    @FXML
    private TableColumn<Item, String> tblColCost;
    @FXML
    private TableColumn<Item, String> tblColDate;
    @FXML
    private TableColumn<Item, String> tblColType;
    @FXML
    private TableColumn<Item, String> tblColName;
    @FXML
    private TableColumn<Item, String> tblColReason;
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
        tblColType.setCellValueFactory(cellData -> {
            return new ReadOnlyStringWrapper(cellData.getValue().getType());
        });
        tblColReason.setCellValueFactory(cellData -> {
            return new ReadOnlyStringWrapper(cellData.getValue().getReason());
        });
        
        try {
            tableDisplay.setItems(ItemQueries.getAllItems());
        } catch (SQLException ex) {
            Logger.getLogger(ItemReportsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Displays all the items in the database, and shows the total of each item added up.
     * @param event
     * @throws SQLException 
     */
    @FXML
    public void onActionAll(ActionEvent event) throws SQLException {
        double cost = 0.00;
        for(Item item : ItemQueries.getAllItems()){
            cost += item.getCost();
        }
        tableDisplay.setItems(ItemQueries.getAllItems());
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        String formatedCost = decimalFormat.format(cost);
        showTotalCost.setText(formatedCost);
    }
    
    /**
     * 
     * @param event
     * @throws ParseException
     * @throws SQLException 
     */
    @FXML
    public void onAction30Days(ActionEvent event) throws ParseException, SQLException {
        double cost = 0.00;
        
        ObservableList<Item> filteredItems = FXCollections.observableArrayList();
        Calendar currentDate = currentDateFinder();
        Calendar minus30Days = minus30DaysDateFinder();
        
        for(Item item : ItemQueries.getAllItems()){
            String purchaseDate = item.getDateOfPurchase();
            Date datePurchased = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH).parse(purchaseDate);
            Calendar dateBought = Calendar.getInstance();
            dateBought.setTime(datePurchased);
            
            if(dateBought.after(minus30Days) && dateBought.before(currentDate)){
                filteredItems.add(item);
                cost += item.getCost();
            }
            tableDisplay.setItems(filteredItems);
            DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
            decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
            String formatedCost = decimalFormat.format(cost);
            showTotalCost.setText(formatedCost);
        }
    }

    @FXML
    public void onAction90Days(ActionEvent event) throws ParseException, SQLException {
        double cost = 0.00;
        
        ObservableList<Item> filteredItems = FXCollections.observableArrayList();
        Calendar currentDate = currentDateFinder();
        Calendar minus90Days = minus90DaysDateFinder();
                
        for(Item item : ItemQueries.getAllItems()){
            String purchaseDate = item.getDateOfPurchase();
            Date datePurchased = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH).parse(purchaseDate);
            Calendar dateBought = Calendar.getInstance();
            dateBought.setTime(datePurchased);
            
            if(dateBought.after(minus90Days) && dateBought.before(currentDate)){
                filteredItems.add(item);
                cost += item.getCost();
            }
            tableDisplay.setItems(filteredItems);
            DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
            decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
            String formatedCost = decimalFormat.format(cost);
            showTotalCost.setText(formatedCost);
        }
    }

    @FXML //3, 4, 5
    public void onActionSpring(ActionEvent event) throws ParseException, SQLException {
        
        double cost = 0.00;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        ObservableList<Item> filteredItems = FXCollections.observableArrayList();
        
        for(Item item : ItemQueries.getAllItems()){
            String serviceDate = item.getDateOfPurchase();
            LocalDate getDate = LocalDate.parse(serviceDate, formatter);
            int monthNumber = getDate.getMonthValue();
            
            if(monthNumber == 3 || monthNumber == 4 || monthNumber == 5){
                filteredItems.add(item);
                cost += item.getCost();
            }
            tableDisplay.setItems(filteredItems);
            DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
            decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
            String formatedCost = decimalFormat.format(cost);
            showTotalCost.setText(formatedCost);
        }
    }

    @FXML //6, 7, 8
    public void onActionSummer(ActionEvent event) throws ParseException, SQLException {
        
        double cost = 0.00;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        ObservableList<Item> filteredItems = FXCollections.observableArrayList();
        
        for(Item item : ItemQueries.getAllItems()){
            String serviceDate = item.getDateOfPurchase();
            LocalDate getDate = LocalDate.parse(serviceDate, formatter);
            int monthNumber = getDate.getMonthValue();
            
            if(monthNumber == 6 || monthNumber == 7 || monthNumber == 8){
                filteredItems.add(item);
                cost += item.getCost();
            }
         
            tableDisplay.setItems(filteredItems);
            DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
            decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
            String formatedCost = decimalFormat.format(cost);
            showTotalCost.setText(formatedCost);
        }
    }
    
    @FXML //9, 10, 11
    public void onActionFall(ActionEvent event) throws ParseException, SQLException {
        
        double cost = 0.00;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        ObservableList<Item> filteredItems = FXCollections.observableArrayList();
        
        for(Item item : ItemQueries.getAllItems()){
            String serviceDate = item.getDateOfPurchase();
            LocalDate getDate = LocalDate.parse(serviceDate, formatter);
            int monthNumber = getDate.getMonthValue();
            
            if(monthNumber == 9 || monthNumber == 10 || monthNumber == 11){
                filteredItems.add(item);
                cost += item.getCost();
            }
            tableDisplay.setItems(filteredItems);
            DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
            decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
            String formatedCost = decimalFormat.format(cost);
            showTotalCost.setText(formatedCost);
        }
    }

    @FXML //12, 1, 2
    public void onActionWinter(ActionEvent event) throws ParseException, SQLException {
        
        double cost = 0.00;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        ObservableList<Item> filteredVisits = FXCollections.observableArrayList();
        
        for(Item item : ItemQueries.getAllItems()){
            String serviceDate = item.getDateOfPurchase();
            LocalDate getDate = LocalDate.parse(serviceDate, formatter);
            int monthNumber = getDate.getMonthValue();
            
            if(monthNumber == 12 || monthNumber == 1 || monthNumber == 2){
                filteredVisits.add(item);
                cost += item.getCost();
            }
            tableDisplay.setItems(filteredVisits);
            DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
            decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
            String formatedCost = decimalFormat.format(cost);
            showTotalCost.setText(formatedCost);
        }
    }
    
    @FXML
    public void onActionHome(ActionEvent event) throws IOException {
        switchScreens("/view/Home.fxml", event);
    }

    @FXML
    public void onActionReportsMenu(ActionEvent event) throws IOException {
        switchScreens("/view/reportsMenu.fxml", event);
    }
    
    public Calendar currentDateFinder() throws ParseException{
        DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        
        LocalDateTime currentDateLDT = LocalDateTime.now();
        String currentDate = currentDateLDT.format(formatDate);
        
        Date newDate = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH).parse(currentDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(newDate);
        
        return calendar;
    }
    
    public Calendar minus30DaysDateFinder() throws ParseException{
        DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        
        LocalDateTime currentDateLDT = LocalDateTime.now();
        String currentDate = currentDateLDT.format(formatDate);
        
        Date newDate = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH).parse(currentDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(newDate);
        calendar.add(Calendar.DAY_OF_MONTH, -30);
        return calendar;
    }
    
    public Calendar minus90DaysDateFinder() throws ParseException{
        DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        
        LocalDateTime currentDateLDT = LocalDateTime.now();
        String currentDate = currentDateLDT.format(formatDate);
        
        Date newDate = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH).parse(currentDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(newDate);
        calendar.add(Calendar.DAY_OF_MONTH, -90);
        return calendar;
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
}
