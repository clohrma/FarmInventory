/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;


import dao.AnimalQueries;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.Animal;

/**
 * FXML Controller class
 *
 * @author Craig Lohrman
 */
public class AnimalReportsController implements Initializable {
    
    @FXML
    private TableView<Animal> tableDisplay;
    @FXML
    private TableColumn<Animal, String> tblColBreed;
    @FXML
    private TableColumn<Animal, String> tblColColor;
    @FXML
    private TableColumn<Animal, String> tblColDateOfBirth;
    @FXML
    private TableColumn<Animal, String> tblColGender;
    @FXML
    private TableColumn<Animal, String> tblColName;
    @FXML
    private TableColumn<Animal, String> tblColType;

    
     /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tblColName.setCellValueFactory(cellData -> {
            return new ReadOnlyStringWrapper(cellData.getValue().getName());
        });
        tblColType.setCellValueFactory(cellData -> {
            return new ReadOnlyStringWrapper(cellData.getValue().getType());
        });
        tblColGender.setCellValueFactory(cellData -> {
            return new ReadOnlyStringWrapper(cellData.getValue().getGender());
        });
        tblColDateOfBirth.setCellValueFactory(cellData -> {
            return new ReadOnlyStringWrapper(cellData.getValue().getDateOfBirth());
        });
        tblColColor.setCellValueFactory(cellData -> {
            return new ReadOnlyStringWrapper(cellData.getValue().getColor());
        });
        tblColBreed.setCellValueFactory(cellData -> {
            return new ReadOnlyStringWrapper(cellData.getValue().getBreed());
        });
        try {
            tableDisplay.setItems(AnimalQueries.getAllAnimals());
        } catch (SQLException ex) {
            Logger.getLogger(AnimalReportsController.class.getName()).log(Level.SEVERE, null, ex);
        }
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