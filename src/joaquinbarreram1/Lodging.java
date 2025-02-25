/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package joaquinbarreram1;

/**
 *
 * @author jabar
 */
public class Lodging {
    int numberOfBedrooms;
    double basePricePerNight;
    String description;
 
    int maxOccupants;
    // Default Cnstructor
    public Lodging (){
        this.numberOfBedrooms = 0;
        this.basePricePerNight = 0.00;
        this.description = null;
        this.maxOccupants = 0;
    }
    
    // Parameter constructor
    public Lodging (int inNumberOfBedrooms, double inBasePricePerNight, String inDescription, int inMaxOccupants){
        this.numberOfBedrooms = inNumberOfBedrooms;
        this.basePricePerNight = inBasePricePerNight;
        this.description = inDescription; 
        this.maxOccupants = inMaxOccupants;
    }
    
    // Override to string method
    @Override
    public String toString(){
        return "Lodging info \n" + 
                "Number Of Bedrooms: " + this.numberOfBedrooms +
                "\nBase price per night: " + this.basePricePerNight +
                "\nDescription: " + this.description +
                "\nMax occupants: " + this.maxOccupants;
    }
    
    
}
