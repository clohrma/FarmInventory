/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Craig Lohrman
 */
public class Animal {
    
    private int animalID;
    private String name, gender, altered, dateOfBirth, color, breed, type;

    public Animal(int animalID, String name, String dateOfBirth, String gender, String altered, String breed, String color, String type) {
        this.animalID = animalID;
        this.name = name;
        this.gender = gender;
        this.altered = altered;
        this.dateOfBirth = dateOfBirth;
        this.color = color;
        this.breed = breed;
        this.type = type;
    }

    public int getAnimalID() {
        return animalID;
    }

    public void setAnimalID(int animalID) {
        this.animalID = animalID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAltered() {
        return altered;
    }

    public void setAltered(String altered) {
        this.altered = altered;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    
}
