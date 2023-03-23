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

    /**
     * Animal constructor with 8 parameters.
     * @param animalID Stores the ID of the animal.
     * @param name Stores the name of the animal.
     * @param dateOfBirth Stores the birth date of the animal
     * @param gender Stores which the animal is male/female.
     * @param altered Stores Yes or No if the animal has been fixed.
     * @param breed Stores the breed of the animal.
     * @param color Stores the color of the animal.
     * @param type Stores the type of the animal, horse, dog, cat, etc.
     */
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

    /**
     * Gets the animalID of the animal.
     * @return the animalID.
     */
    public int getAnimalID() {
        return animalID;
    }

    /**
     * Sets the animalID that is passed in to animalID.
     * @param animalID stores the ID that is passed in.
     */
    public void setAnimalID(int animalID) {
        this.animalID = animalID;
    }

    /**
     * Gets the animal's name.
     * @return the name of the animal.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the animal that is passed in to name.
     * @param name stores the name that is passed in.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the animal's gender.
     * @return the gender of the animal.
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the gender of the animal that is passed in to gender.
     * @param gender stores the gender that is passed in.
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    
    /**
     * Gets Yes or No if the animal has been fixed.
     * @return Yes or No if the animal has been fixed.
     */
    public String getAltered() {
        return altered;
    }

    /**
     * Sets Yes or No if the animal has been fixed.
     * @param altered stores Yes or No if the animal has been fixed.
     */
    public void setAltered(String altered) {
        this.altered = altered;
    }

    
    /**
     * Gets the animal's birth date.
     * @return the date of birth of the animal.
     */
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Sets the animal's Birthday.
     * @param dateOfBirth stores the date of birth of the animal.
     */
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    
    /**
     * Gets the color of the animal.
     * @return the color of the animal.
     */
    public String getColor() {
        return color;
    }

    /**
     * Sets the color of the animal.
     * @param color stores the color of the animal.
     */
    public void setColor(String color) {
        this.color = color;
    }

    
    /**
     * Gets the breed of the animal.
     * @return the breed of the animal.
     */
    public String getBreed() {
        return breed;
    }

    /**
     * Sets the breed of the animal.
     * @param breed stores the breed of the animal.
     */
    public void setBreed(String breed) {
        this.breed = breed;
    }

    
    /**
     * Gets the type of the animal.
     * @return the type of the animal.
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of the animal.
     * @param type stores the type of the animal.
     */
    public void setType(String type) {
        this.type = type;
    }
}
