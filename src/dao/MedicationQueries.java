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
import model.Medication;

/**
 *
 * @author Craig Lohrman
 */
public class MedicationQueries {
    
    public static int insert(String name, String animalFor, String dateOfPurchase, double cost, String emergency, String reason) throws SQLException {
        
        String sql = "INSERT INTO medication (name, animalFor, dateOfPurchase, cost, emergency, reason) "
                + "VALUES(?, ?, ?, ?, ?, ?)";
        
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, name);
        ps.setString(2, animalFor);
        ps.setString(3, dateOfPurchase);
        ps.setDouble(4, cost);
        ps.setString(5, emergency);
        ps.setString(6, reason);
        
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }
    
     public static int update(int medID, String name, String animalFor, String dateOfPurchase, double cost, String emergency, String reason) throws SQLException {
         
         String sql = "UPDATE medication SET medID = ?, name = ?, animalFor = ?, dateOfPurchase = ?, cost = ?, emergency = ?, reason = ? "
                + "WHERE (medID = ?)";
    
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, medID);
        ps.setString(2, name);
        ps.setString(3, animalFor);
        ps.setString(4, dateOfPurchase);
        ps.setDouble(5, cost);
        ps.setString(6, emergency);
        ps.setString(7, reason);
        ps.setInt(8, medID);
        
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }
     
     public static int delete(int medID) throws SQLException{
        
        String sql = "DELETE FROM medication Where medID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, medID);
        
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }
    
    public static ObservableList<Medication> getAllMeds() throws SQLException{
        ObservableList<Medication> allMeds = FXCollections.observableArrayList();
        
        String sql ="SELECT * FROM medication;";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        
        while(rs.next()){
            int medID = rs.getInt("medID");
            String name = rs.getString("name");
            String dateOfPurchase = rs.getString("dateOfPurchase");
            String animalFor = rs.getString("animalFor");
            double cost = rs.getDouble("cost");
            String reason = rs.getString("reason");
            String emergency = rs.getString("emergency");
            
            Medication newMed =  new Medication(medID, name, animalFor, dateOfPurchase, cost, emergency, reason);
                                                
            allMeds.add(newMed);
        }
        return allMeds;
    }
}