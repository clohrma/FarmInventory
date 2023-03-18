/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import dao.JDBC;
import dao.ItemQueries;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Item;
import utilities.AlertConfirm;
import utilities.AlertError;
import utilities.AlertInfo;
import utilities.AlertWarning;

/**
 * FXML Controller class
 *
 * @author Craig Lohrman
 */
public class ItemController implements Initializable {

    ObservableList<String> type = FXCollections.observableArrayList();
    int itemID = -1;
    
    @FXML
    private ComboBox<String> comboItemType;
    @FXML
    private TextField txtItemName;
    @FXML
    private DatePicker dateItemPurchaseDate;
    @FXML
    private TextField txtItemCost;
    @FXML
    private TextArea txtItemReason;
    @FXML
    private TableView<Item> tableItemDisplay;
    @FXML
    private TableColumn<Item, String> tblColItemDate;
    @FXML
    private TableColumn<Item, String> tblColItemName;
    @FXML
    private TableColumn<Item, String> tblColItemReason;
    @FXML
    private ComboBox<String> comboAnimalFor; 
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tblColItemName.setCellValueFactory(cellData -> {
            return new ReadOnlyStringWrapper(cellData.getValue().getName());
        });
        tblColItemDate.setCellValueFactory(cellData -> {
            return new ReadOnlyStringWrapper(cellData.getValue().getDateOfPurchase());
        });
        tblColItemReason.setCellValueFactory(cellData -> {
            return new ReadOnlyStringWrapper(cellData.getValue().getReason());
        });
        comboTypeFiller();
        try {
            refreshItemsTable();
            fillComboAnimalFor();
        } catch (SQLException ex) {
            Logger.getLogger(ItemController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    /**
     * Takes the entered data and creates a new Item then adds it to the database or takes the updated information and updates the database.
     * @param event
     * @throws SQLException 
     */
    @FXML
    public void onActionAddItem(ActionEvent event) throws SQLException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        int currentID = itemID;
        
        if(currentID == -1){
            String name = txtItemName.getText();
            String reason = txtItemReason.getText();
            String animalFor = comboAnimalFor.getValue();
            LocalDate getDate = dateItemPurchaseDate.getValue();
            String dateOfPurchase = getDate.format(formatter);
            String type = comboItemType.getValue();
            double cost = Double.parseDouble(txtItemCost.getText());
            
            ItemQueries.insert(name, animalFor, dateOfPurchase, type, cost, reason);
        }
        else{
            int currentItemID = itemID;
            String name = txtItemName.getText();
            String reason = txtItemReason.getText();
            String animalFor = comboAnimalFor.getValue();
            LocalDate getDate = dateItemPurchaseDate.getValue();
            String dateOfPurchase = getDate.format(formatter);
            String type = comboItemType.getValue();
            double cost = Double.parseDouble(txtItemCost.getText());
            
            ItemQueries.update(currentItemID, name, animalFor, dateOfPurchase, type, cost, reason);
        }
        clearFields();
        refreshItemsTable();
    }
    
    /**
     * Gets the Item ID of the selected item and confirms you want to delete it.
     * Then sends the request to the database to delete the item. 
     * After that is done the table view is then reloaded and the text fields are cleared.
     * @param event
     * @throws SQLException 
     */
    @FXML
    public void onActionDeleteItem(ActionEvent event) throws SQLException {
         String name = txtItemName.getText();
        
        if(confirm.alertConfirm("Delete Confirmation", "Are you sure you what to delete " + name + " from the Item's table??")){
            ItemQueries.delete(itemID);
            infoAlert.alertInfomation("Information Dialog", "" + name + " has been deleted from the Item's table.");
            
        }
        clearFields();
        refreshItemsTable();
    }

    /**
     * Calls the clear fields method when the button is clicked.
     * @param event 
     */
    @FXML
    public void onActionClearItem(ActionEvent event) {
        clearFields();
    }
    
    /**
     * Switches to the Home screen menu. 
     * If itemID is not -1 it confirms with you changes will not be saved. 
     * If itemID is -1 it just goes to the Home screen.
     * @param event
     * @throws IOException 
     */
    @FXML
    public void onActionHome(ActionEvent event) throws IOException {
        if(itemID != -1){
            if(confirm.alertConfirm("Leave Without Saving", "Are you sure you want to go back Home?\nUnsave information will be lost?")){
                switchScreens("/view/Home.fxml", event);
        }
        }else {
            switchScreens("/view/Home.fxml", event);
        }
    }
    
    /**
     * Brags all the animals from the database and loads the combo box with them.
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
     * Clears the text fields. Gets the itemID of the selected item in the table view.
     * @param event
     * @throws Exception 
     */
    @FXML
    public void onMouseClickItemRowHandler(MouseEvent event) throws SQLException {
        clearFields();
        try {
            tableviewSelectHandler(tableItemDisplay.getSelectionModel().getSelectedItem().getItemID());
        } catch (NullPointerException e){ }
    }
    
    /**
     * Get the information from the database for the selected item and loads the text fields and combo boxes.
     * @param sentItemID The item ID to search the database.
     * @throws SQLException 
     */
    public void tableviewSelectHandler(int sentItemID) throws SQLException {
        
        PreparedStatement ps = JDBC.connection.prepareStatement("Select * FROM item WHERE itemID = ?");
        ps.setInt(1, sentItemID);
        ResultSet rSet = ps.executeQuery();
        while(rSet.next()){
            itemID = rSet.getInt("itemID");
            String name = rSet.getString("name");
            String dateOfPurchase = rSet.getString("dateOfPurchase");
            String animalFor = rSet.getString("animalFor");
            Double cost = rSet.getDouble("cost");
            String reason = rSet.getString("reason");
            String type = rSet.getString("type");
            String costStr = cost.toString();

            comboItemType.setValue(type);
            dateItemPurchaseDate.setValue(dbToDatePicker(dateOfPurchase));
            comboAnimalFor.setValue(animalFor);
            txtItemName.setText(name);
            txtItemReason.setText(reason);
            txtItemCost.setText(costStr);
        }
    }
    
    /**
     * Clears all the fields, set itemID back to -1, and sets combo boxes to blank.
     */
    public void clearFields(){
	itemID = -1;
        dateItemPurchaseDate.getEditor().clear();
        comboAnimalFor.valueProperty().set(null);
        txtItemName.clear();
        txtItemReason.clear();
        txtItemCost.clear();
        comboItemType.valueProperty().set(null);
    }
    
    /**
     * Loads or refreshes with the latest items listed from the database to the table view.
     * @throws SQLException 
     */
    public void refreshItemsTable() throws SQLException{
        tableItemDisplay.setItems(ItemQueries.getAllItems());
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
     * Adds the list of Strings to the array list then adds the array list to the combo box.
     */
    public void comboTypeFiller(){
        type.addAll("Food", "Supply", "Other");
        comboItemType.setItems(type);
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
