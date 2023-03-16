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
import javafx.scene.control.ComboBox;
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
    private ComboBox<String> comboItemList;
    @FXML
    private TableView<Item> tblvwDisplay;
    @FXML
    private TableColumn<Item, String> tblColAnimalFor;
    @FXML
    private TableColumn<Item, String> tblColCost;
    @FXML
    private TableColumn<Item, String> tblColDate;
    @FXML
    private TableColumn<Item, String> tblColER;
    @FXML
    private TableColumn<Item, String> tblColName;
    @FXML
    private TableColumn<Item, String> tblColReason;

    
     /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    @FXML
    void onActionItemList(ActionEvent event) {

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
