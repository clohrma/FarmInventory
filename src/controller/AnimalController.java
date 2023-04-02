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
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
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
import utilities.AlertInfo;
import utilities.Dates;


/**
 * FXML Controller class
 *
 * @author Craig Lohrman
 */
public class AnimalController implements Initializable {
    
    ObservableList<String> type = FXCollections.observableArrayList();
    ObservableList<String> gender = FXCollections.observableArrayList();
    int animalID = -1;
    Dates dates = new Dates();
    
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
    
    /**
     * Takes the entered data and creates a new Animal then adds it to the database or updates the new database with the new data.
     * @param event Stores the action event.
     * @throws SQLException  Throws SQL Exception.
     * @throws java.text.ParseException
     */
    @FXML
    public void onActionAdd(ActionEvent event) throws SQLException, ParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        int currentID = animalID;
        String altered = "No";
        if(rbnAlteredYes.isSelected()){
                altered = "Yes";
        }
        if(fieldCheck()){
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
    }

    /**
     * Gets the animal ID of the selected animal and confirms you want to delete it.
     * Then sends the request to the database to delete the animal. 
     * After that is done the table view is then reloaded and the text fields are cleared.
     * @param event Stores the action event.
     * @throws SQLException Throws SQL Exception.
     */
    @FXML
    public void onActionDelete(ActionEvent event) throws SQLException {
        
        String name = txtName.getText();
        
        if(animalID != -1){
            if(confirm.alertConfirm("Delete Confirmation", "Are you sure you what to delete, " + name + " from the Animal's table?")){
                AnimalQueries.delete(animalID);
                infoAlert.alertInfomation("Information Dialog", "" + name + " has been deleted from the Animal's table.");
            }
        }
        else{
            infoAlert.alertInfomation("Select an Animal to Delete", "Please select an Animal to delete.");
        }
        clearFields();
        refreshAnimalTable();
    }
    
    /**
     * Clears all the fields.
     * @param event  Stores the mouse event.
     */
    @FXML
    public void onActionClear(ActionEvent event) {
        clearFields();
    }

    /**
     *Gets the animal ID of the selected animal in the table view.
     * @param event Stores the mouse event.
     * @throws Exception  Throws Exception.
     */
    @FXML
    public void onMouseClickRowHandler(MouseEvent event) throws Exception {
        clearFields();
        try {
            tableviewSelectHandler(tblvwDisplay.getSelectionModel().getSelectedItem().getAnimalID());
        } catch (NullPointerException e){ }
    }

    /**
     * Switches to the Home screen menu. 
     * If animalID is not -1 it confirms with you changes will not be saved. 
     * If animalID is -1 it just goes to the Home screen.
     * @param event
     * @throws IOException 
     */
    @FXML
    public void onActionHome(ActionEvent event) throws IOException {
        if(animalID != -1){
            if(confirm.alertConfirm("Leave Without Saving", "Are you sure you want to go back Home?\nUnsave information will be lost?")){
                switchScreens("/view/Home.fxml", event);
        }
        }else {
            switchScreens("/view/Home.fxml", event);
        }
    }
    
    /**
     * Fills the animal type field with the list of options.
     * This prevents misspellings and if needed can be added to here.
     */
    public void fillComboType(){
        type.addAll("Horse", "Donkey", "Dog", "Cat", "Chicken", "Duck", "Other");
        comboType.setItems(type);
    }
    
    /**
     * Fills the animal gender field with the list of options.
     * This prevents misspellings.
     */
    public void fillComboGender(){
        gender.addAll("Female", "Male");
        comboGender.setItems(gender);
    }
    
    /**
     * Takes the String version of the date, puts it in the order needed, converts it to a LocalDate version.
     * @param dbDate The String date from the database.
     * @return The date is returned in MM-dd-yyyy format
     */
    public LocalDate dbToDatePicker (String dbDate){
        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        LocalDate date = LocalDate.parse(dbDate, inputFormat);
        
        date.format(outputFormat);
        
        return date;
    }
    
    /**
     * Get the information from the database for the selected animal and loads the text fields and combo boxes.
     * @param sentAnimalID The ID for the selected animal.
     * @throws SQLException 
     * @throws Exception 
     */
    public void tableviewSelectHandler(int sentAnimalID) throws SQLException, Exception {
        
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
    
    /**
     * This clears the Text fields, sets animalID back to -1, blanks all the ComboBoxes.
     */
    public void clearFields(){
        animalID = -1;
        comboType.valueProperty().set(null);
        comboGender.valueProperty().set(null);
        dateBirthdate.getEditor().clear();
        txtName.clear();
        txtColor.clear();
        txtBreed.clear();
    }
    
    /**
     * Updates the TableView to the newest data.
     * @throws SQLException 
     */
    public void refreshAnimalTable() throws SQLException{
        tblvwDisplay.setItems(AnimalQueries.getAllAnimals());
    }
    
    /**
     * Checks all the fields for information and displays an error message if there are.
     * @return True if there are no errors, and False if there are errors.
     * @throws ParseException 
     */
    public boolean fieldCheck() throws ParseException{
        
        String type = comboType.getValue();
        String breed = txtBreed.getText();
        String color = txtColor.getText();
        String name = txtName.getText();
        String gender = comboGender.getValue();
        LocalDate dateOfBirth = dateBirthdate.getValue();
        String error = "";
        
        if(type == null || type.length() == 0){
            error += "\n\nType field is blank, please enter a type.";
        }
        
        if(breed == null || breed.length() == 0){
            error += "\n\nBreed field is blank, please enter a breed.";
        }
        if(color == null || color.length() == 0){
            error += "\n\nColor field is blank, please enter a color.";
        }
        if(name == null || name.length() == 0){
            error += "\n\nName field is blank, please enter a name.";
        }
        if(gender == null || gender.length() == 0){
            error += "\n\nGender field is blank, please select a gender.";
        }
        
        try{
            Calendar currentDate = dates.currentDateFinder();
            Calendar birthdate = dates.convertDateToCalendar(dateOfBirth);
            if(currentDate.before(birthdate)){
                error += "\n\nThe date selected is in the future, please select another date that is in the past.";
            }
        }catch(NullPointerException ex){
            error += "\n\nBirthdate field is blank, please select a date.";
        }
        
        if(error.length() == 0){
            return true;
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText(error);
            alert.showAndWait();
            return false;
        }
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
