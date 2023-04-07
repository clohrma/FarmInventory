/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import dao.AnimalQueries;
import dao.ItemQueries;
import dao.JDBC;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.Item;
import utilities.AlertInfo;
import utilities.Dates;

/**
 * FXML Controller class
 *
 * @author Craig Lohrman
 */
public class ItemReportsController implements Initializable {
    
    Dates dates = new Dates();
    ObservableList<String> yearList = FXCollections.observableArrayList();
    ObservableList<String> animalNameList = FXCollections.observableArrayList();
    int selectedYear;

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
    private ComboBox<String> comboYear;
    @FXML
    private ComboBox<String> comboAnimal;
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
            tableDisplayloader();
            fillComboAnimal();
            fillComboYeare();
        } catch (SQLException | ParseException ex) {
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
        tableDisplayloader();
    }
    
    /**
     * Filters out all the items that are 30 days old to current, and displays them.
     * @param event
     * @throws ParseException
     * @throws SQLException 
     */
    @FXML
    public void onAction30Days(ActionEvent event) throws ParseException, SQLException {
        double cost = 0.00;
        int nameCount = 0;
        
        ObservableList<Item> filteredItems = FXCollections.observableArrayList();
        Calendar currentDate = dates.currentDateFinder();
        Calendar minus30Days = dates.minus30DaysDateFinder();
        String animalName = comboAnimal.getValue();
        
        try{
            if(animalName.equalsIgnoreCase("All") || animalName.isBlank() || animalName.isEmpty()) {
                for(Item item : ItemQueries.getAllItems()){
                    String purchaseDate = item.getDateOfPurchase();
                    Date datePurchased = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH).parse(purchaseDate);
                    Calendar dateBought = Calendar.getInstance();
                    dateBought.setTime(datePurchased);

                    if(dateBought.after(minus30Days) && dateBought.before(currentDate)){
                        filteredItems.add(item);
                        cost += item.getCost();
                        nameCount += 1;
                    }
                    tableDisplay.setItems(filteredItems);
                    DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
                    decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
                    String formatedCost = decimalFormat.format(cost);
                    showTotalCost.setText(formatedCost);
                }
            }
            else{
                for(Item item : ItemQueries.getAllItems()){
                        if(item.getAnimalFor().equalsIgnoreCase(animalName)){
                            String purchaseDate = item.getDateOfPurchase();
                            Date datePurchased = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH).parse(purchaseDate);
                            Calendar dateBought = Calendar.getInstance();
                            dateBought.setTime(datePurchased);

                            if(dateBought.after(minus30Days) && dateBought.before(currentDate) && animalName.equalsIgnoreCase(item.getAnimalFor())){
                                filteredItems.add(item);
                                cost += item.getCost();
                                nameCount += 1;
                            }
                            tableDisplay.setItems(filteredItems);
                            DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
                            decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
                            String formatedCost = decimalFormat.format(cost);
                            showTotalCost.setText(formatedCost);
                        }
                    }
                }
            if(nameCount <= 0){
            infoAlert.alertInfomation("Information Dialog", "There are no Items for " + animalName + " in the last 30 days");
            }
        }catch(NullPointerException e){
                    infoAlert.alertInfomation("Information Dialog", "Please select an Animal or All.");
        }
    }

    /**
     * Filters out all the items that are 90 days old to current, and displays them.
     * @param event
     * @throws ParseException
     * @throws SQLException 
     */
    @FXML
    public void onAction90Days(ActionEvent event) throws ParseException, SQLException {
        double cost = 0.00;
        int nameCount = 0;
        
        ObservableList<Item> filteredItems = FXCollections.observableArrayList();
        Calendar currentDate = dates.currentDateFinder();
        Calendar minus90Days = dates.minus90DaysDateFinder();
        String animalName = comboAnimal.getValue();
        
        try{
            if(animalName.equalsIgnoreCase("All") || animalName.isBlank() || animalName.isEmpty()) {
                for(Item item : ItemQueries.getAllItems()){
                    String purchaseDate = item.getDateOfPurchase();
                    Date datePurchased = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH).parse(purchaseDate);
                    Calendar dateBought = Calendar.getInstance();
                    dateBought.setTime(datePurchased);

                    if(dateBought.after(minus90Days) && dateBought.before(currentDate)){
                        filteredItems.add(item);
                        cost += item.getCost();
                        nameCount += 1;
                    }
                    tableDisplay.setItems(filteredItems);
                    DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
                    decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
                    String formatedCost = decimalFormat.format(cost);
                    showTotalCost.setText(formatedCost);
                }
            }
            else{
                for(Item item : ItemQueries.getAllItems()){
                    if(item.getAnimalFor().equalsIgnoreCase(animalName)){
                        String purchaseDate = item.getDateOfPurchase();
                        Date datePurchased = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH).parse(purchaseDate);
                        Calendar dateBought = Calendar.getInstance();
                        dateBought.setTime(datePurchased);

                        if(dateBought.after(minus90Days) && dateBought.before(currentDate) && animalName.equalsIgnoreCase(item.getAnimalFor())){
                            filteredItems.add(item);
                            cost += item.getCost();
                            nameCount += 1;
                        }
                        tableDisplay.setItems(filteredItems);
                        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
                        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
                        String formatedCost = decimalFormat.format(cost);
                        showTotalCost.setText(formatedCost);
                    }
                }
            }
            if(nameCount <= 0){
            infoAlert.alertInfomation("Information Dialog", "There are no Items for " + animalName + " in the last 90 days");
            }
        }catch(NullPointerException e){
                    infoAlert.alertInfomation("Information Dialog", "Please select an Animal or All.");
        }
    }

    /**
     * Filters out all the items that are in the Spring months of March, April and May of the selected current or last year, and displays them.
     * @param event
     * @throws ParseException
     * @throws SQLException 
     */
    @FXML 
    public void onActionSpring(ActionEvent event) throws ParseException, SQLException {
        
        double cost = 0.00;
        int nameCount = 0;
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        ObservableList<Item> filteredItems = FXCollections.observableArrayList();
        String animalName = comboAnimal.getValue();
        
        try{
            String yearSelected = comboYear.getValue();
            selectedYear = Integer.parseInt(yearSelected);
        }catch(NumberFormatException ex){
            infoAlert.alertInfomation("Information Dialog", "Please select which year you want.");
        }
        
        try{
            if(animalName.equalsIgnoreCase("All")){
                for(Item item : ItemQueries.getAllItems()){
                    String serviceDate = item.getDateOfPurchase();
                    LocalDate getDate = LocalDate.parse(serviceDate, formatter);
                    int monthNumber = getDate.getMonthValue();
                    int yearNumber = getDate.getYear();
                    
                    if(selectedYear == yearNumber){
                        if(monthNumber == 3 || monthNumber == 4 || monthNumber == 5){
                            filteredItems.add(item);
                            cost += item.getCost();
                            nameCount += 1;
                        }
                        tableDisplay.setItems(filteredItems);
                        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
                        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
                        String formatedCost = decimalFormat.format(cost);
                        showTotalCost.setText(formatedCost);
                    }
                }
            }
            else{
                for(Item item : ItemQueries.getAllItems()){
                    if(item.getAnimalFor().equalsIgnoreCase(animalName)){
                        String serviceDate = item.getDateOfPurchase();
                        LocalDate getDate = LocalDate.parse(serviceDate, formatter);
                        int monthNumber = getDate.getMonthValue();
                        int yearNumber = getDate.getYear();

                        if(selectedYear == yearNumber){
                            if(monthNumber == 3 || monthNumber == 4 || monthNumber == 5){
                                filteredItems.add(item);
                                cost += item.getCost();
                                nameCount += 1;
                            }
                            tableDisplay.setItems(filteredItems);
                            DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
                            decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
                            String formatedCost = decimalFormat.format(cost);
                            showTotalCost.setText(formatedCost);
                        }
                    }
                }
            }
            if(nameCount == 0 && selectedYear != 0){
                infoAlert.alertInfomation("Information Dialog", "There are no Items for " + animalName + "\nfor the Spring months of the year " + selectedYear);
            }
            
        }catch(NullPointerException e){
            infoAlert.alertInfomation("Information Dialog", "Please select an Animal or All.");
        }
    }

    /**
     * Filters out all the items that are of the summer months June, July, August of the selected current or last year, and displays them.
     * @param event
     * @throws ParseException
     * @throws SQLException 
     */
    @FXML
    public void onActionSummer(ActionEvent event) throws ParseException, SQLException {
        
        double cost = 0.00;
        int nameCount = 0;
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        ObservableList<Item> filteredItems = FXCollections.observableArrayList();
        String animalName = comboAnimal.getValue();
        
        try{
            String yearSelected = comboYear.getValue();
            selectedYear = Integer.parseInt(yearSelected);
        }catch(NumberFormatException ex){
            infoAlert.alertInfomation("Information Dialog", "Please select which year you want.");
        }
        
        try{
            if(animalName.equalsIgnoreCase("All")){
                for(Item item : ItemQueries.getAllItems()){
                    String serviceDate = item.getDateOfPurchase();
                    LocalDate getDate = LocalDate.parse(serviceDate, formatter);
                    int monthNumber = getDate.getMonthValue();
                    int yearNumber = getDate.getYear();

                    if(selectedYear == yearNumber){
                        if(monthNumber == 6 || monthNumber == 7 || monthNumber == 8){
                            filteredItems.add(item);
                            cost += item.getCost();
                            nameCount += 1;
                        }
                        tableDisplay.setItems(filteredItems);
                        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
                        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
                        String formatedCost = decimalFormat.format(cost);
                        showTotalCost.setText(formatedCost);
                    }
                }
            }
            else{
                for(Item item : ItemQueries.getAllItems()){
                    if(item.getAnimalFor().equalsIgnoreCase(animalName)){
                        String serviceDate = item.getDateOfPurchase();
                        LocalDate getDate = LocalDate.parse(serviceDate, formatter);
                        int monthNumber = getDate.getMonthValue();
                        int yearNumber = getDate.getYear();

                        if(selectedYear == yearNumber){
                            if(monthNumber == 6 || monthNumber == 7 || monthNumber == 8){
                                filteredItems.add(item);
                                cost += item.getCost();
                                nameCount += 1;
                            }
                            tableDisplay.setItems(filteredItems);
                            DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
                            decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
                            String formatedCost = decimalFormat.format(cost);
                            showTotalCost.setText(formatedCost);
                        }
                    }
                }
            }
            if(nameCount == 0 && selectedYear != 0){
                infoAlert.alertInfomation("Information Dialog", "There are no Items for " + animalName + "\nfor the Summer months of the year " + selectedYear);
            }
        }catch(NullPointerException e){
            infoAlert.alertInfomation("Information Dialog", "Please select an Animal or All.");
        }
    }

    /**
     * Filters out all the items that are of the fall months September, October, November of the selected current or last year, and displays them.
     * @param event
     * @throws ParseException
     * @throws SQLException 
     */
    @FXML
    public void onActionFall(ActionEvent event) throws ParseException, SQLException {
        
        double cost = 0.00;
        int nameCount = 0;
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        ObservableList<Item> filteredItems = FXCollections.observableArrayList();
        String animalName = comboAnimal.getValue();
        
        try{
            String yearSelected = comboYear.getValue();
            selectedYear = Integer.parseInt(yearSelected);
        }catch(NumberFormatException ex){
            infoAlert.alertInfomation("Information Dialog", "Please select which year you want.");
        }
        
        try{
            if(animalName.equalsIgnoreCase("All")){
                for(Item item : ItemQueries.getAllItems()){
                    String serviceDate = item.getDateOfPurchase();
                    LocalDate getDate = LocalDate.parse(serviceDate, formatter);
                    int monthNumber = getDate.getMonthValue();
                    int yearNumber = getDate.getYear();

                    if(selectedYear == yearNumber){
                        if(monthNumber == 9 || monthNumber == 10 || monthNumber == 11){
                            filteredItems.add(item);
                            cost += item.getCost();
                            nameCount += 1;
                        }
                        tableDisplay.setItems(filteredItems);
                        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
                        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
                        String formatedCost = decimalFormat.format(cost);
                        showTotalCost.setText(formatedCost);
                    }
                }
            }
            else{
                for(Item item : ItemQueries.getAllItems()){
                    if(item.getAnimalFor().equalsIgnoreCase(animalName)){
                        String serviceDate = item.getDateOfPurchase();
                        LocalDate getDate = LocalDate.parse(serviceDate, formatter);
                        int monthNumber = getDate.getMonthValue();
                        int yearNumber = getDate.getYear();

                        if(selectedYear == yearNumber){
                            if(monthNumber == 9 || monthNumber == 10 || monthNumber == 11){
                                filteredItems.add(item);
                                cost += item.getCost();
                                nameCount += 1;
                            }
                            tableDisplay.setItems(filteredItems);
                            DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
                            decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
                            String formatedCost = decimalFormat.format(cost);
                            showTotalCost.setText(formatedCost);
                        }
                    }
                }
            }
            if(nameCount == 0 && selectedYear != 0){
                infoAlert.alertInfomation("Information Dialog", "There are no Items for " + animalName + "\nfor the Fall months of the year " + selectedYear);
            }
        }catch(NullPointerException e){
            infoAlert.alertInfomation("Information Dialog", "Please select an Animal or All.");
        }
    }

    /**
     * Filters out all the items that are of the fall months December, January, February of the selected current or last year, and displays them.
     * @param event
     * @throws ParseException
     * @throws SQLException
     */
    @FXML
    public void onActionWinter(ActionEvent event) throws ParseException, SQLException {
        
        double cost = 0.00;
        int nameCount = 0;
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        ObservableList<Item> filteredItems = FXCollections.observableArrayList();
        String animalName = comboAnimal.getValue();
        
        try{
            String yearSelected = comboYear.getValue();
            selectedYear = Integer.parseInt(yearSelected);
        }catch(NumberFormatException ex){
            infoAlert.alertInfomation("Information Dialog", "Please select which year you want.");
        }
        
        try{
            if(animalName.equalsIgnoreCase("All")){
                for(Item item : ItemQueries.getAllItems()){
                    String serviceDate = item.getDateOfPurchase();
                    LocalDate getDate = LocalDate.parse(serviceDate, formatter);
                    int monthNumber = getDate.getMonthValue();
                    int yearNumber = getDate.getYear();

                    if(selectedYear - 1 == yearNumber){
                        if(monthNumber == 12){
                            filteredItems.add(item);
                            cost += item.getCost();
                            nameCount += 1;
                        }
                    }
                    if(selectedYear == yearNumber){
                        if(monthNumber == 1 || monthNumber == 2){
                            filteredItems.add(item);
                            cost += item.getCost();
                            nameCount += 1;
                        }
                        tableDisplay.setItems(filteredItems);
                        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
                        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
                        String formatedCost = decimalFormat.format(cost);
                        showTotalCost.setText(formatedCost);
                    }
                }
            }
            else{
                for(Item item : ItemQueries.getAllItems()){
                    if(item.getAnimalFor().equalsIgnoreCase(animalName)){
                        String serviceDate = item.getDateOfPurchase();
                        LocalDate getDate = LocalDate.parse(serviceDate, formatter);
                        int monthNumber = getDate.getMonthValue();
                        int yearNumber = getDate.getYear();
                        
                        if(selectedYear - 1 == yearNumber){
                            if(monthNumber == 12){
                                filteredItems.add(item);
                                cost += item.getCost();
                                nameCount += 1;
                            }
                        }
                        if(selectedYear == yearNumber){
                            if(monthNumber == 1 || monthNumber == 2){
                                filteredItems.add(item);
                                cost += item.getCost();
                                nameCount += 1;
                            }
                            tableDisplay.setItems(filteredItems);
                            DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
                            decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
                            String formatedCost = decimalFormat.format(cost);
                            showTotalCost.setText(formatedCost);
                        }
                    }
                }
            }
            if(nameCount == 0 && selectedYear != 0){
                infoAlert.alertInfomation("Information Dialog", "There are no Items for " + animalName + "\nfor the Winter months of the year " + selectedYear);
            }
        }catch(NullPointerException e){
            infoAlert.alertInfomation("Information Dialog", "Please select an Animal or All.");
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
     * Fills the array list with the list of years, then loads the combo box with the list.
     * @throws java.sql.SQLException
     * @throws java.text.ParseException
     */
    public void fillComboYeare() throws SQLException, ParseException{
        String sql = "Select distinct year FROM item;";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        
        while(rs.next()){
            String year = (rs.getString("year"));
            
            yearList.add(year);
        }
        comboYear.setItems(yearList);
    }
    
    /**
     * Gets all the Animals names from the database and puts them in a list for the drop down menu.
     * @throws SQLException 
     */
    public void fillComboAnimal() throws SQLException{
        animalNameList = AnimalQueries.getAllAnimalNames();
        comboAnimal.setItems(animalNameList);
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
