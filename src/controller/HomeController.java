/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import dao.JDBC;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Craig Lohrman
 */
public class HomeController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    void onActionVisits(ActionEvent event) throws IOException {
        switchScreens("/view/visit.fxml", event);
    }
	
	@FXML
    void onActionItems(ActionEvent event) throws IOException {
        switchScreens("/view/item.fxml", event);
    }

    @FXML
    void onActionMedications(ActionEvent event) throws IOException {
        switchScreens("/view/medication.fxml", event);
    }
	
    @FXML
    void onActionAnimals(ActionEvent event) throws IOException {
        switchScreens("/view/animal.fxml", event);
    }

    @FXML
    void onActionReports(ActionEvent event) throws IOException {
        switchScreens("/view/reportsMenu.fxml", event);
    }
    
    @FXML
    void onActionExit(ActionEvent event) {
        String message = "Are you sure you want to exit the program?";
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message);
        alert.showAndWait().filter(result -> result == ButtonType.OK).ifPresent(result -> {System.exit(0);});
        JDBC.closeConnection();
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