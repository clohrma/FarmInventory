/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Item;

/**
 *
 * @author Craig Lohrman
 */
public class ItemQueries {
    public static int insert(String name, String animalFor, String dateOfPurchase, String type, double cost, String reason) throws SQLException {
        
        String sql = "INSERT INTO item (name, animalFor, dateOfPurchase, type, cost, reason) "
                + "VALUES(?, ?, ?, ?, ?, ?)";
        
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, name);
        ps.setString(2, animalFor);
        ps.setString(3, programToDatabase(dateOfPurchase));
        ps.setString(4, type);
        ps.setDouble(5, cost);
        ps.setString(6, reason);
        
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }
    
    public static int update(int itemID, String name, String animalFor, String dateOfPurchase, String type, double cost, String reason) throws SQLException {
        
    String sql = "UPDATE item SET itemID = ?, name = ?, animalFor = ?, dateOfPurchase = ?, type = ?, cost = ?, reason = ? "
                + "WHERE (itemID = ?)";
    
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, itemID);
        ps.setString(2, name);
        ps.setString(3, animalFor);
        ps.setString(4, programToDatabase(dateOfPurchase));
        ps.setString(5, type);
        ps.setDouble(6, cost);
        ps.setString(7, reason);
        ps.setInt(8, itemID);
        
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }
    
    public static int delete(int itemID) throws SQLException{
        
        String sql = "DELETE FROM item Where itemID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, itemID);
        
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }
    
    public static ObservableList<Item> getAllItems() throws SQLException{
        ObservableList<Item> allfoodSupplyItems = FXCollections.observableArrayList();
        
        String sql ="SELECT * FROM item order by dateOfPurchase DESC;";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        
        while(rs.next()){
            int itemID = rs.getInt("itemID");
            String name = rs.getString("name");
            String animalFor  = rs.getString("animalFor");
            String dateOfPurchase  = dbToStringDate(rs.getString("dateOfPurchase"));
            String type = rs.getString("type");
            double cost = rs.getDouble("cost");
            String reason = rs.getString("reason");
            
            Item newItem =  new Item(itemID, name, animalFor, dateOfPurchase, type, cost, reason);
            
            allfoodSupplyItems.add(newItem);
        }
        return allfoodSupplyItems;
    }
    
    public static String dbToStringDate (String dbDate){
        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        LocalDate dateLD = LocalDate.parse(dbDate, inputFormat);
        
        String date = outputFormat.format(dateLD);
        return date;
    }
    
    public static String programToDatabase (String dbDate){
        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateLD = LocalDate.parse(dbDate, inputFormat);
        
        String date = outputFormat.format(dateLD);
        return date;
    }
}
