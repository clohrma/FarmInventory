/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;


import dao.AnimalQueries;
import dao.JDBC;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Animal;
import utilities.AlertConfirm;
import utilities.AlertError;
import utilities.AlertInfo;
import utilities.AlertWarning;


/**
 * FXML Controller class
 *
 * @author Craig Lohrman
 */
public class AnimalController implements Initializable {
    
    ObservableList<String> type = FXCollections.observableArrayList();
    ObservableList<String> gender = FXCollections.observableArrayList();
    int animalID = -1;
    
    @FXML
    private ComboBox<String> comboType;
    @FXML
    private RadioButton rbnAlteredNo;
    @FXML
    private RadioButton rbnAlteredYes;
    @FXML
    private TableView<Animal> tblvwDisplay;
    @FXML
    private TableColumn<Animal, String> tblColDateOfBirth;
    @FXML
    private TableColumn<Animal, String> tblColName;
    @FXML
    private TableColumn<Animal, String> tblColType;
    @FXML
    private TextField txtBreed;
    @FXML
    private TextField txtColor;
    @FXML
    private TextField txtName;
    @FXML
    private DatePicker dateBirthdate;
    @FXML
    private ComboBox<String> comboGender;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tblColName.setCellValueFactory(cellData -> {
            return new ReadOnlyStringWrapper(cellData.getValue().getName());
        });
        tblColDateOfBirth.setCellValueFactory(cellData -> {
            return new ReadOnlyStringWrapper(cellData.getValue().getDateOfBirth());
        });
        tblColType.setCellValueFactory(cellData -> {
            return new ReadOnlyStringWrapper(cellData.getValue().getType());
        });
        fillComboType();
        fillComboGender();
        try {
            refreshAnimalTable();
        } catch (SQLException ex) {
            Logger.getLogger(VisitController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    @FXML
    void onActionAdd(ActionEvent event) throws SQLException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        int currentID = animalID;
        String altered = "No";
        if(rbnAlteredYes.isSelected()){
                altered = "Yes";
        }
                
        if(currentID == -1){
            String name = txtName.getText();
            String gender = comboGender.getValue();
            String color = txtColor.getText();
            LocalDate getDate = dateBirthdate.getValue();
            String dateOfBirth = getDate.format(formatter);
            String breed = txtBreed.getText();
            String type = comboType.getValue();
            
            AnimalQueries.insert(name, dateOfBirth, gender, altered, breed, color, type);
        }
        else{
            String name = txtName.getText();
            String gender = comboGender.getValue();
            String color = txtColor.getText();
            LocalDate getDate = dateBirthdate.getValue();
            String dateOfBirth = getDate.format(formatter);
            String breed = txtBreed.getText();
            String type = comboType.getValue();
            
            AnimalQueries.update(currentID, name, dateOfBirth, gender, altered, breed, color, type);
        }
        clearFields();
        refreshAnimalTable();
    }

    @FXML
    void onActionDelete(ActionEvent event) throws SQLException {
        
        String name = txtName.getText();
        
        if(confirm.alertConfirm("Delete Confirmation", "Are you sure you what to delete, " + name + " from the Animal's table?")){
            AnimalQueries.delete(animalID);
            infoAlert.alertInfomation("Information Dialog", "" + name + " has been deleted from the Animal's table.");
        }
        clearFields();
        refreshAnimalTable();
    }
    
    
    @FXML
    void onActionClearOther(ActionEvent event) {
        clearFields();
    }

    @FXML
    void onMouseClickRowHandler(MouseEvent event) throws Exception {
        clearFields();
        tableviewSelectHandler(tblvwDisplay.getSelectionModel().getSelectedItem().getAnimalID());
    }

    @FXML
    void onActionHome(ActionEvent event) throws IOException {
        if(confirm.alertConfirm("Leave Without Saving", "Are you sure you want to go back Home?\nUnsave information will be lost?")){
            switchScreens("/view/Home.fxml", event);
        }
    }
    
    private void fillComboType(){
        type.addAll("Horse", "Donkey", "Dog", "Cat", "Chicken", "Duck", "Other");
        comboType.setItems(type);
    }
    
    private void fillComboGender(){
        gender.addAll("Female", "Male");
        comboGender.setItems(gender);
    }
    
    private LocalDate dbToDatePicker (String dbDate){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        LocalDate date = LocalDate.parse(dbDate, formatter);
        
        return date;
    }
    
    private void tableviewSelectHandler(int sentAnimalID) throws SQLException, Exception {
        
        PreparedStatement ps = JDBC.connection.prepareStatement("Select * FROM animal WHERE animalID = ?");
        
        ps.setInt(1, sentAnimalID);
        ResultSet rSet = ps.executeQuery();
        while(rSet.next()){
            animalID = rSet.getInt("animalID");
            String name = rSet.getString("name");
            String dateOfBirth = rSet.getString("dateOfBirth");
            String gender = rSet.getString("gender");
            String color = rSet.getString("color");
            String breed = rSet.getString("breed");
            String type = rSet.getString("type");
            String altered = rSet.getString("altered");
            
            comboType.setValue(type);
            dateBirthdate.setValue(dbToDatePicker(dateOfBirth));
            comboGender.setValue(gender);
            txtName.setText(name);
            txtColor.setText(color);
            txtBreed.setText(breed);
            if(altered.equalsIgnoreCase("Yes")){
                    rbnAlteredYes.setSelected(true);
            }
            else{
                rbnAlteredNo.setSelected(true);
            }
        }
    }
    
    private void clearFields(){
        animalID = -1;
        comboType.valueProperty().set(null);
        comboGender.valueProperty().set(null);
        dateBirthdate.getEditor().clear();
        txtName.clear();
        txtColor.clear();
        txtBreed.clear();
    }
    
    private void refreshAnimalTable() throws SQLException{
        tblvwDisplay.setItems(AnimalQueries.getAllAnimals());
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
