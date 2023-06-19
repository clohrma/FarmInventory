/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import dao.JDBC;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utilities.AlertError;

/**
 * FXML Controller class
 *
 * @author Craig Lohrman
 */
public class LoginScreenController implements Initializable {
    
    @FXML
    private PasswordField pwdPassword;
    @FXML
    private TextField txtUsername;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    /**
     * Takes the data entered for username and password and verifies it with the database and add a log entry.
     * @param event
     * @throws IOException
     * @throws SQLException 
     */
    @FXML
    public void onActionLogin(ActionEvent event) throws IOException, SQLException {
        
        String uName = txtUsername.getText();
        String password = pwdPassword.getText();
        
        boolean loginCheck = usernamePasswordCheck(uName, password);
        if(loginCheck){
            switchScreens("/view/home.fxml", event);
        }
        else{
            error.ErrorAlert("Invalid Login", "Invalid Username and/or Password, please try again.");
        }
    }
    
    /**
     * Exits the program after confirming you want to leave.
     * @param event 
     */
    @FXML
    public void onActionExit(ActionEvent event) {
        String meassage = "Are you sure you want to exit?";
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, meassage);
        alert.showAndWait().filter(result -> result == ButtonType.OK).ifPresent(result -> {System.exit(0);});
        JDBC.closeConnection();
    }
    
    /**
     * What is typed in to username and password at the login screen is then passed here to verify with the database,
     * if they are correct it returns true and if they are not returns false.
     * @param name The information the user typed for username.
     * @param password The information the user typed for password.
     * @return true or false
     * @throws SQLException 
     */
    public boolean usernamePasswordCheck(String name, String password) throws SQLException {
        String sql = "SELECT * FROM loginuser WHERE userName = ? AND uesrPassword = ?";
        PreparedStatement statement = JDBC.getConnection().prepareStatement(sql);
        statement.setString(1, name);
        statement.setString(2, password);
        
        ResultSet rSet = statement.executeQuery();
        while(rSet.next()){
            if(rSet.getString("uesrPassword").equals(password)){
                return true;
            }
        }
        return false;
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
    
    AlertError error = (dialog, message) -> {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(dialog);
        alert.setContentText(message);
        alert.showAndWait();
    };
}