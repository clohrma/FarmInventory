/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import dao.JDBC;
import dao.MedicationQueries;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import model.Medication;
import utilities.AlertConfirm;
import utilities.AlertInfo;
import utilities.Dates;



/**
 * FXML Controller class
 *
 * @author Craig Lohrman
 */
public class MedicationController implements Initializable {
    
    int medID = -1;
    Dates dates = new Dates();
    
    @FXML
    private DatePicker datePurchaseDate;
    @FXML
    private RadioButton rbnEmergencyNo;
    @FXML
    private RadioButton rbnEmergencyYes;
    @FXML
    private TableView<Medication> tblvwDisplay;
    @FXML
    private TableColumn<Medication, String> tblColReason;
    @FXML
    private TableColumn<Medication, String> tblColDate;
    @FXML
    private TableColumn<Medication, String> tblColName;
    @FXML
    private TableColumn<Medication, String> tblColAnimalFor;
    @FXML
    private TextArea txtAreaReason;
    @FXML
    private TextField txtCost;
    @FXML
    private TextField txtName;
    @FXML
    private ComboBox<String> comboAnimalFor;    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tblColName.setCellValueFactory(cellData -> {
            return new ReadOnlyObjectWrapper(cellData.getValue().getName());
        });
        tblColAnimalFor.setCellValueFactory(cellData -> {
            return new ReadOnlyStringWrapper(cellData.getValue().getAnimalFor());
        });
        tblColDate.setCellValueFactory(cellData -> {
            return new ReadOnlyStringWrapper(cellData.getValue().getDateOfPurchase());
        });
        tblColReason.setCellValueFactory(cellData -> {
            return new ReadOnlyStringWrapper(cellData.getValue().getReason());
        });
        try {
            refreshMedTable();
            fillComboAnimalFor();
        } catch (SQLException ex) {
            Logger.getLogger(VisitController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Takes the entered data and creates a new Medication then adds it to the database or takes the updated information and updates the database.
     * @param event
     * @throws SQLException 
     */
    @FXML
    public void onActionAdd(ActionEvent event) throws SQLException, ParseException{
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        int currentID = medID;
        String emergency = "No";
        if(rbnEmergencyYes.isSelected()){
                emergency = "Yes";
        }
        else{
            emergency = "No";
        }
        
        if(fieldCheck()){
            if(currentID == -1){
                String name = txtName.getText();
                String reason = txtAreaReason.getText();
                String animalFor = comboAnimalFor.getValue();
                LocalDate getDate = datePurchaseDate.getValue();
                String dateOfPurchase = getDate.format(formatter);
                double cost = Double.parseDouble(txtCost.getText());

                MedicationQueries.insert(name, animalFor, dateOfPurchase, cost, emergency, reason);
            }
            else{
                String name = txtName.getText();
                String reason = txtAreaReason.getText();
                String animalFor = comboAnimalFor.getValue();
                LocalDate getDate = datePurchaseDate.getValue();
                String dateOfPurchase = getDate.format(formatter);
                double cost = Double.parseDouble(txtCost.getText());

                MedicationQueries.update(currentID, name, animalFor, dateOfPurchase, cost, emergency, reason);
            }
            clearFields();
            refreshMedTable();
        }
    }

    /**
     * Gets the Medication ID of the selected med and confirms that you want to delete it.
     * Then sends the request to the database to delete the medication. 
     * After that is done the table view is then reloaded and the text fields are cleared.
     * @param event
     * @throws SQLException 
     */
    @FXML
    public void onActionDelete(ActionEvent event) throws SQLException {
        
        String name = txtName.getText();
        if(medID != -1){
            if(confirm.alertConfirm("Delete Confirmation", "Are you sure you what to delete, " + name + " from the Medication's table?")){
                MedicationQueries.delete(medID);
                infoAlert.alertInfomation("Information Dialog", "" + name + " has been deleted from the Medication's table.");
            }
        }
        else{
            infoAlert.alertInfomation("Select a Medication to Delete", "Please select a Medication to delete.");
        }
        clearFields();
        refreshMedTable();
    }
    
    /**
     * Calls the clear fields method when the button is clicked.
     * @param event 
     */
    @FXML
    public void onActionClear(ActionEvent event) {
        clearFields();
    }
    
    /**
     * Switches to the Home screen menu. 
     * If medID is not -1 it confirms with you changes will not be saved. 
     * If medID is -1 it just goes to the Home screen.
     * @param event
     * @throws IOException 
     */
    @FXML
    public void onActionHome(ActionEvent event) throws IOException {
        if(medID != -1){
            if(confirm.alertConfirm("Leave Without Saving", "Are you sure you want to go back Home?\nUnsave information will be lost?")){
                switchScreens("/view/Home.fxml", event);
        }
        }else {
            switchScreens("/view/Home.fxml", event);
        }
    }
    
    /**
     * Clears the text fields. Gets the medID of the selected medication in the table view.
     * @param event
     * @throws Exception 
     */
    @FXML
    public void onMouseClickRowHandler(MouseEvent event) throws Exception {
        clearFields();
        try {
            tableviewSelectHandler(tblvwDisplay.getSelectionModel().getSelectedItem().getMedID());
        } catch (NullPointerException e){ }
    }
    
    /**
     * Brings all the animal names listed in the database and loads the combo box with them.
     * @throws SQLException 
     */
    public void fillComboAnimalFor() throws SQLException{
        
    String sql = "SELECT name FROM animal";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rSet = ps.executeQuery();
        while(rSet.next()){
            comboAnimalFor.getItems().add(rSet.getString("name"));
         }
    }
    
    /**
     * Get the information from the database for the selected medication and loads the text fields and combo boxes.
     * @param sentMedID The medication ID to search the database.
     * @throws SQLException
     * @throws Exception 
     */
    public void tableviewSelectHandler(int sentMedID) throws SQLException, Exception {
        
        PreparedStatement ps = JDBC.connection.prepareStatement("Select * FROM medication WHERE medID = ?");
        ps.setInt(1, sentMedID);
        ResultSet rSet = ps.executeQuery();
        while(rSet.next()){
            medID = rSet.getInt("medID");
            String name = rSet.getString("name");
            String dateOfPurchase = rSet.getString("dateOfPurchase");
            String animalFor = rSet.getString("animalFor");
            Double cost = rSet.getDouble("cost");
            String reason = rSet.getString("reason");
            String emergency = rSet.getString("emergency");
            String costStr = cost.toString();

            datePurchaseDate.setValue(dbToDatePicker(dateOfPurchase));
            comboAnimalFor.setValue(animalFor);
            txtName.setText(name);
            txtAreaReason.setText(reason);
            txtCost.setText(costStr);
            if(emergency.equalsIgnoreCase("Yes")){
                rbnEmergencyYes.setSelected(true);
            }
            else{
                rbnEmergencyNo.setSelected(true);
            }
        }
    }
    
    /**
     * Clears all the fields, set medID back to -1, and sets combo boxes to blank.
     */
    public void clearFields(){
        medID = -1;
        datePurchaseDate.getEditor().clear();
        comboAnimalFor.valueProperty().set(null);
        txtName.clear();
        txtAreaReason.clear();
        txtCost.clear();
        rbnEmergencyNo.setSelected(true);
    }
    
    /**
     * Loads or refreshes the table view with the latest medications listed from the database to the table view.
     * @throws SQLException 
     */
    public void refreshMedTable() throws SQLException{
        tblvwDisplay.setItems(MedicationQueries.getAllMeds());
    }
    
    /**
     * Takes the String version of the date, puts it in the order needed, converts it to a LocalDate version.
     * @param dbDate The String date from the database.
     * @return 
     */
    public LocalDate dbToDatePicker (String dbDate){
        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        LocalDate date = LocalDate.parse(dbDate, inputFormat);
        
        date.format(outputFormat);
        
        return date;
    }
    
    /**
     * Checks all the fields for information and displays an error message if there are.
     * @return True if there are no errors, and False if there are errors.
     * @throws ParseException 
     */
    public boolean fieldCheck() throws ParseException{
        double cost = -1;
        
        String error = "";
        String reason = txtAreaReason.getText();
        String name = txtName.getText();
        String animalName = comboAnimalFor.getValue();
        LocalDate purchaseDate = datePurchaseDate.getValue();
        
        try{
            cost = Double.parseDouble(txtCost.getText());
        }catch(NumberFormatException e){
            error += "Cost needs to be in a numbers like 0.00 or 0";
        }
        
        if(reason == null || reason.length() == 0){
            error += "\n\nReaon field is blank, please select a type.";
        }
        if(cost == -1){
            error += "\n\nCost field is 0.00 or blank, please select a type.";
        }
        if(name == null || name.length() == 0){
            error += "\n\nName field is blank, please select a type.";
        }
        if(animalName == null || animalName.length() == 0){
            error += "\n\nAnimal for field is blank, please select a type.";
        }
        if(purchaseDate == null){
            error += "\n\nPurchase Date field is blank, please select a type.";
        }
        
        Calendar currentDate = dates.currentDateFinder();
        Calendar purchaseDateCalendar = dates.convertDateToCalendar(purchaseDate);
        if(currentDate.before(purchaseDateCalendar)){
            error += "\n\nThe purchase date selected is in the future, please select current date or before.";
        }
        
        if(error.length() == 0){
            return true;
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText(error);
            alert.showAndWait();
            return false;
        }
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
    
    AlertConfirm confirm = (title, contentText) -> {
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setContentText(contentText);
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    };
}
