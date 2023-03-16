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
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
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
import model.Animal;
import model.Medication;
import utilities.AlertConfirm;
import utilities.AlertError;
import utilities.AlertInfo;
import utilities.AlertWarning;



/**
 * FXML Controller class
 *
 * @author Craig Lohrman
 */
public class MedicationController implements Initializable {
    
    int medID = -1;
    
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
    
    @FXML
    private void onActionAdd(ActionEvent event) throws SQLException {
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        int currentID = medID;
        String emergency = "No";
        if(rbnEmergencyYes.isSelected()){
                emergency = "Yes";
        }
        else{
            emergency = "No";
        }
        
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

    @FXML
    private void onActionDelete(ActionEvent event) throws SQLException {
        
        String name = txtName.getText();
        
        if(confirm.alertConfirm("Delete Confirmation", "Are you sure you what to delete, " + name + " from the Medication's table?")){
            MedicationQueries.delete(medID);
            infoAlert.alertInfomation("Information Dialog", "" + name + " has been deleted from the Medication's table.");
        }
        clearFields();
        refreshMedTable();
    }
    
    @FXML
    private void onActionClear(ActionEvent event) {
        clearFields();
    }
    
    @FXML
    private void onActionHome(ActionEvent event) throws IOException {
        if(confirm.alertConfirm("Leave Without Saving", "Are you sure you want to go back Home?\nUnsave information will be lost?")){
            switchScreens("/view/Home.fxml", event);
        }
    }
    
    @FXML
    private void onMouseClickRowHandler(MouseEvent event) throws Exception {
        clearFields();
        tableviewSelectHandler(tblvwDisplay.getSelectionModel().getSelectedItem().getMedID());
    }
    
    private void fillComboAnimalFor() throws SQLException{
        
    String sql = "SELECT name FROM animal";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rSet = ps.executeQuery();
        while(rSet.next()){
            comboAnimalFor.getItems().add(rSet.getString("name"));
         }
    }
    
    private void tableviewSelectHandler(int sentMedID) throws SQLException, Exception {
        
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
    
    private void clearFields(){
        medID = -1;
        datePurchaseDate.getEditor().clear();
        comboAnimalFor.valueProperty().set(null);
        txtName.clear();
        txtAreaReason.clear();
        txtCost.clear();
        rbnEmergencyNo.setSelected(true);
    }
    
    private void refreshMedTable() throws SQLException{
        tblvwDisplay.setItems(MedicationQueries.getAllMeds());
    }
    
    private LocalDate dbToDatePicker (String dbDate){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        LocalDate date = LocalDate.parse(dbDate, formatter);
        
        return date;
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
    
    AlertWarning warning = (title, message) -> {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    };
    
    AlertError error = (dialog, message) -> {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(dialog);
        alert.setContentText(message);
        alert.showAndWait();
    };
    
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