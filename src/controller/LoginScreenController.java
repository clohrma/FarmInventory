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
import java.util.Optional;
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
import utilities.AlertConfirm;
import utilities.AlertError;
import utilities.AlertInfo;
import utilities.AlertWarning;

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
    
    @FXML
    void onActionLogin(ActionEvent event) throws IOException, SQLException {
        
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
    
    @FXML
    private void onActionExit(ActionEvent event) {
        String meassage = "Are you sure you want to exit?";
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, meassage);
        alert.showAndWait().filter(result -> result == ButtonType.OK).ifPresent(result -> {System.exit(0);});
        JDBC.closeConnection();
    }
    
    private boolean usernamePasswordCheck(String name, String password) throws SQLException {
        String sql = "SELECT * FROM user WHERE userName = ? AND uesrPassword = ?";
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