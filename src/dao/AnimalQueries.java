/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Animal;

/**
 *
 * @author Craig Lohrman
 */
public class AnimalQueries {
    
    public static int insert(String name, String dateOfBirth, String gender, String altered, String breed, String color, String type) throws SQLException {
        
        String sql = "INSERT INTO animal (name, dateOfBirth, gender, altered, breed, color, type) "
                + "VALUES(?, ?, ?, ?, ?, ?, ?)";
        
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, name);
        ps.setString(2, dateOfBirth );
        ps.setString(3, gender);
        ps.setString(4, altered);
        ps.setString(5, breed);
        ps.setString(6, color);
        ps.setString(7, type);
        
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }
    
    public static int update(int animalID, String name, String dateOfBirth, String gender, String altered, String breed, String color, String type) throws SQLException {
        
    String sql = "UPDATE animal SET animalID = ?, name = ?, dateOfBirth = ?, gender = ?, altered = ?, breed = ?, color = ?, type = ? "
                + "WHERE animalID = ?";
    
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, animalID);
        ps.setString(2, name);
        ps.setString(3, dateOfBirth);
        ps.setString(4, gender);
        ps.setString(5, altered);
        ps.setString(6, breed);
        ps.setString(7, color);
        ps.setString(8, type);
        ps.setInt(9, animalID);
        
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }
    
    public static int delete(int animalID) throws SQLException{
        
        String sql = "DELETE FROM animal Where animalID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, animalID);
        
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }
    
    public static ObservableList<Animal> getAllAnimals() throws SQLException{
        ObservableList<Animal> allAnimals = FXCollections.observableArrayList();
        
        String sql = "SELECT * FROM animal";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet resultSet = ps.executeQuery();
        
        while(resultSet.next()){
            int animalID = resultSet.getInt("animalID");
            String name = resultSet.getString("name");
            String gender = resultSet.getString("gender");
            String altered = resultSet.getString("altered");
            String dateOfBirth = resultSet.getString("dateOfBirth");
            String color = resultSet.getString("color");
            String  breed = resultSet.getString("breed");
            String type = resultSet.getString("type");
            
            Animal newAnimal = new Animal(animalID, name, dateOfBirth, gender, altered, breed, color, type);
            
            allAnimals.add(newAnimal);
        }
        return allAnimals;
    }
}