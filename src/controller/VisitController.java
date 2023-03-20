/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import dao.JDBC;
import dao.VisitQueries;
import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Visit;
import utilities.AlertConfirm;
import utilities.AlertInfo;

/**
 * FXML Controller class
 *
 * @author Craig Lohrman
 */
public class VisitController implements Initializable{
    
    ObservableList<String> type = FXCollections.observableArrayList();
    int visitID = -1;

    @FXML
    private ComboBox<String> comboType;
    @FXML
    private DatePicker dateServiceDate;
    @FXML
    private RadioButton rbnEmergencyNo;
    @FXML
    private RadioButton rbnEmergencyYes;
    @FXML
    private TableView<Visit> tblvwDisplay;
    @FXML
    private TableColumn<Visit, String> tblColDate;
    @FXML
    private TableColumn<Visit, String> tblColName;
    @FXML
    private TableColumn<Visit, String> tblColAnimalFor;
    @FXML
    private TableColumn<Visit, String> tblColReason;
    @FXML
    private TextField txtName;
    @FXML
    private TextArea txtReason;
    @FXML
    private TextField txtCost;
    @FXML
    private ComboBox<String> comboAnimalFor;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tblColName.setCellValueFactory(cellData -> {
            return new ReadOnlyObjectWrapper(cellData.getValue().getName());
        });
        tblColAnimalFor.setCellValueFactory(cellData -> {
            return new ReadOnlyStringWrapper(cellData.getValue().getAnimalFor());
        });
        tblColDate.setCellValueFactory(cellData -> {
            return new ReadOnlyStringWrapper(cellData.getValue().getDateOfService());
        });
        tblColReason.setCellValueFactory(cellData -> {
            return new ReadOnlyStringWrapper(cellData.getValue().getReason());
        });
        comboTypeFiller();
        try {
            refreshVisitsTable();
            fillComboAnimalFor();
        } catch (SQLException ex) {
            Logger.getLogger(VisitController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Takes the entered data and creates a new Visit then adds it to the database or takes the updated information and updates the database.
     * @param event
     * @throws SQLException 
     */
    @FXML
    public void onActionAdd(ActionEvent event) throws SQLException {
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        int cuurentID = visitID;
        String emergency = "No";
        if(rbnEmergencyYes.isSelected()){
                emergency = "Yes";
        }
        else{
            emergency = "No";
        }
        
        if(cuurentID == -1){
            String name = txtName.getText();
            String reason = txtReason.getText();
            String type = comboType.getValue();
            String animalFor = comboAnimalFor.getValue();
            LocalDate getDate = dateServiceDate.getValue();
            String dateOfService = getDate.format(formatter);
            double cost = Double.parseDouble(txtCost.getText());
            
            VisitQueries.insert(name, animalFor, dateOfService, type, cost, emergency, reason);
        }
        else{
            String name = txtName.getText();
            String reason = txtReason.getText();
            String type = comboType.getValue();
            String animalFor = comboAnimalFor.getValue();
            LocalDate getDate = dateServiceDate.getValue();
            String dateOfService = getDate.format(formatter);
            double cost = Double.parseDouble(txtCost.getText());

            VisitQueries.update(cuurentID, name, animalFor, dateOfService, type, cost, emergency, reason);
        }
        clearFields();
        refreshVisitsTable();
    }

    /**
     * Gets the Visit ID of the selected visit and confirms you want to delete it.
     * Then sends the request to the database to delete the visit. 
     * After that is done the table view is then reloaded and the text fields are cleared.
     * @param event
     * @throws SQLException 
     */
    @FXML
    public void onActionDelete(ActionEvent event) throws SQLException {
        
        String name = txtName.getText();
        
        if(confirm.alertConfirm("Delete Confirmation", "Are you sure you what to delete " + name + " from the Visit's table?")){
            VisitQueries.delete(visitID);
            infoAlert.alertInfomation("Information Dialog", "" + name + " has been deleted from the Visit's table.");
        }
        clearFields();
        refreshVisitsTable();
    }

    /**
     * Switches to the Home screen menu. 
     * If visitID is not -1 it confirms with you changes will not be saved. 
     * If visitID is -1 it just goes to the Home screen.
     * @param event
     * @throws IOException 
     */
    @FXML
    public void onActionHome(ActionEvent event) throws IOException {
        if(visitID != -1){
            if(confirm.alertConfirm("Leave Without Saving", "Are you sure you want to go back Home?\nUnsave information will be lost?")){
                switchScreens("/view/Home.fxml", event);
        }
        }else {
            switchScreens("/view/Home.fxml", event);
        }
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
     * 
     * @param event
     * @throws Exception 
     */
    @FXML
    public void onMouseClickRowHandler(MouseEvent event) throws Exception {
        clearFields();
        try {
            tableviewSelectHandler(tblvwDisplay.getSelectionModel().getSelectedItem().getVisitID());
        } catch (NullPointerException e){ }
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
     * Loads or refreshes the table view with the latest visits listed from the database to the table view.
     * @throws SQLException 
     */
    public void refreshVisitsTable() throws SQLException{
        tblvwDisplay.setItems(VisitQueries.getAllVisits());
    }
    
    /**
     * Adds the list of Strings to the array list then adds the array list to the combo box.
     */
    public void comboTypeFiller(){
        type.addAll("Farrier", "Veterinarian", "Other");
        comboType.setItems(type);
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
     * Get the information from the database for the selected item and loads the text fields and combo boxes.
     * @param sentVisitID The visit ID to search the database.
     * @throws SQLException
     * @throws Exception 
     */
    public void tableviewSelectHandler(int sentVisitID) throws SQLException, Exception {
        
        PreparedStatement ps = JDBC.connection.prepareStatement("Select * FROM visit WHERE visitID = ?");
        ps.setInt(1, sentVisitID);
        ResultSet rSet = ps.executeQuery();
        while(rSet.next()){
            visitID = rSet.getInt("visitID");
            String name = rSet.getString("name");
            String dateOfService = rSet.getString("dateOfService");
            String animalFor = rSet.getString("animalFor");
            Double cost = rSet.getDouble("cost");
            String reason = rSet.getString("reason");
            String type = rSet.getString("type");
            String emergency = rSet.getString("emergency");
            String costStr = cost.toString();
            
            comboType.setValue(type);
            dateServiceDate.setValue(dbToDatePicker(dateOfService));
            comboAnimalFor.setValue(animalFor);
            txtName.setText(name);
            txtReason.setText(reason);
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
     * Clears all the fields, set visitID back to -1, and sets combo boxes to blank.
     */
    public void clearFields(){
        visitID = -1;
        comboType.valueProperty().set(null);
        dateServiceDate.getEditor().clear();
        comboAnimalFor.valueProperty().set(null);
        txtName.clear();
        txtReason.clear();
        txtCost.clear();
        rbnEmergencyNo.setSelected(true);
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