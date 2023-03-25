/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Craig Lohrman
 */
public class Item {
    
    private int itemID;
    private String name, animalFor, dateOfPurchase, type, reason, year;
    private double cost;

    public Item(int itemID, String name, String animalFor, String dateOfPurchase, String type, double cost, String reason, String year) {
        this.itemID = itemID;
        this.name = name;
        this.animalFor = animalFor;
        this.dateOfPurchase = dateOfPurchase;
        this.type = type;
        this.reason = reason;
        this.cost = cost;
        this.year = year;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAnimalFor() {
        return animalFor;
    }

    public void setAnimalFor(String animalFor) {
        this.animalFor = animalFor;
    }

    public String getDateOfPurchase() {
        return dateOfPurchase;
    }

    public void setDateOfPurchase(String dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
    
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
    
}
