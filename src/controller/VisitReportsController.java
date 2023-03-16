/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import dao.VisitQueries;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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