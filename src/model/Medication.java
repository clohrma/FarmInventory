/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Craig Lohrman
 */
public class Medication {
    
    private int medID;
    private String name, animalFor, dateOfPurchase, reason, emergency;
    private double cost;

    public Medication(int medID, String name, String animalFor, String dateOfPurchase, double cost, String emergency, String reason) {
        this.medID = medID;
        this.name = name;
        this.animalFor = animalFor;
        this.dateOfPurchase = dateOfPurchase;
        this.reason = reason;
        this.emergency = emergency;
        this.cost = cost;
    }

    public int getMedID() {
        return medID;
    }

    public void setMedID(int medID) {
        this.medID = medID;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getEmergency() {
        return emergency;
    }

    public void setEmergency(String emergency) {
        this.emergency = emergency;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
    
    
}
