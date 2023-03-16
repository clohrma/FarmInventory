/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import dao.MedicationQueries;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
    private ComboBox<String> comboMedList;
    @FXML
    private TableView<Medication> tableDisplay;
    @FXML
    private TableColumn<Medication, String> tblColMedAnimalFor;
    @FXML
    private TableColumn<Medication, String> tblColMedCost;
    @FXML
    private TableColumn<Medication, String> tblColMedDate;
    @FXML
    private TableColumn<Medication, String> tblColMedER;
    @FXML
    private TableColumn<Medication, String> tblColMedName;
    @FXML
    private TableColumn<Medication, String> tblColMedReason;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tblColMedName.setCellValueFactory(cellData -> {
            return new ReadOnlyStringWrapper(cellData.getValue().getName());
        });
        tblColMedAnimalFor.setCellValueFactory(cellData -> {
            return new ReadOnlyStringWrapper(cellData.getValue().getAnimalFor());
        });
        tblColMedCost.setCellValueFactory(cellData -> {
            return new ReadOnlyObjectWrapper(cellData.getValue().getCost());
        });
        tblColMedDate.setCellValueFactory(cellData -> {
            return new ReadOnlyStringWrapper(cellData.getValue().getDateOfPurchase());
        });
        tblColMedER.setCellValueFactory(cellData -> {
            return new ReadOnlyStringWrapper(cellData.getValue().getEmergency());
        });
        tblColMedReason.setCellValueFactory(cellData -> {
            return new ReadOnlyStringWrapper(cellData.getValue().getReason());
        });
        
        try {
            tableDisplay.setItems(MedicationQueries.getAllMeds());
        } catch (SQLException ex) {
            Logger.getLogger(MedicationReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    @FXML
    void onActionMedList(ActionEvent event) {

    }
    
    @FXML
    void onActionAll(ActionEvent event) {
        
    }
    
    @FXML
    void onAction30Days(ActionEvent event) {

    }

    @FXML
    void onAction90Days(ActionEvent event) {

    }

    @FXML
    void onActionSpring(ActionEvent event) {

    }

    @FXML
    void onActionSummer(ActionEvent event) {

    }
    
    @FXML
    void onActionFall(ActionEvent event) {

    }

    @FXML
    void onActionWinter(ActionEvent event) {

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
