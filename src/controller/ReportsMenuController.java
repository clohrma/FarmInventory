/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Craig Lohrman
 */
public class ReportsMenuController implements Initializable {
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    @FXML
    void onActionVisitReport(ActionEvent event) throws IOException {
        switchScreens("/view/visitReports.fxml", event);
    }
    
    @FXML
    void onActionAnimalReport(ActionEvent event) throws IOException {
        switchScreens("/view/animalReports.fxml", event);
    }
    
    @FXML
    void onActionItemReport(ActionEvent event) throws IOException {
        switchScreens("/view/itemReports.fxml", event);
    }
    
     @FXML
    void onActionMedicationReport(ActionEvent event) throws IOException {
        switchScreens("/view/medicationReport.fxml", event);
    }
    
    @FXML
    void onActionHome(ActionEvent event) throws IOException {
        switchScreens("/view/home.fxml", event);
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