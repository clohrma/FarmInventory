/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Craig Lohrman
 */
public class Visit {
    
    private int visitID;
    private String type, name, dateOfService, animalFor, emergency, reason;
    private double cost;

    public Visit(int visitID, String name, String animalFor, String dateOfService, String type, double cost, String emergency, String reason) {
        this.visitID = visitID;
        this.type = type;
        this.name = name;
        this.dateOfService = dateOfService;
        this.animalFor = animalFor;
        this.emergency = emergency;
        this.reason = reason;
        this.cost = cost;
    }

    public int getVisitID() {
        return visitID;
    }

    public void setVisitID(int visitID) {
        this.visitID = visitID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateOfService() {
        return dateOfService;
    }

    public void setDateOfService(String dateOfService) {
        this.dateOfService = dateOfService;
    }

    public String getAnimalFor() {
        return animalFor;
    }

    public void setAnimalFor(String animalFor) {
        this.animalFor = animalFor;
    }

    public String getEmergency() {
        return emergency;
    }

    public void setEmergency(String emergency) {
        this.emergency = emergency;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
    
    
}
