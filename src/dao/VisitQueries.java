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
import model.Visit;

/**
 *
 * @author Craig Lohrman
 */
public class VisitQueries {
    

    public static int insert(String name, String animalFor, String dateOfService, String type, double cost, String emergency, String reason) throws SQLException {
        
        String sql = "INSERT INTO visit (name, animalFor, dateOfService, type, cost, emergency, reason) "
                + "VALUES(?, ?, ?, ?, ?, ?, ?)";
        
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, name);
        ps.setString(2, animalFor);
        ps.setString(3, dateOfService);
        ps.setString(4, type);
        ps.setDouble(5, cost);
        ps.setString(6, emergency);
        ps.setString(7, reason);
        
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }
    
    public static int update(int visitID, String name, String animalFor, String dateOfService, String type, double cost, String emergency, String reason) throws SQLException {
        
    String sql = "UPDATE visit SET visitID = ?, name = ?, animalFor = ?, dateOfService = ?, type = ?, cost = ?, emergency = ?, reason = ? "
                + "WHERE (visitID = ?)";
    
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, visitID);
        ps.setString(2, name);
        ps.setString(3, animalFor);
        ps.setString(4, dateOfService);
        ps.setString(5, type);
        ps.setDouble(6, cost);
        ps.setString(7, emergency);
        ps.setString(8, reason);
        ps.setInt(9, visitID);
        
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }
    
    public static int delete(int visitID) throws SQLException{
        
        String sql = "DELETE FROM visit Where visitID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, visitID);
        
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }
    
    public static ObservableList<Visit> getAllVisits() throws SQLException{
        ObservableList<Visit> allVisits = FXCollections.observableArrayList();
        
        String sql = "SELECT * FROM visit;";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet resultSet = ps.executeQuery();
        
        while(resultSet.next()){
            int visitID = resultSet.getInt("visitID");
            String name = resultSet.getString("name");
            String dateOfService = resultSet.getString("dateOfService");
            String animalFor = resultSet.getString("animalFor");
            double cost = resultSet.getDouble("cost");
            String reason = resultSet.getString("reason");
            String type = resultSet.getString("type");
            String emergency = resultSet.getString("emergency");
            
            Visit newVisit = new Visit(visitID, name, animalFor, dateOfService, type, cost, emergency, reason);
            
            allVisits.add(newVisit);
        }
        return allVisits;
    }
}