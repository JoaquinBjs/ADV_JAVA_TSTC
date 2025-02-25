/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package joaquinbarreram1;

/**
 *
 * @author jabar
 */
public class Home extends Lodging {
    boolean hasAGarage = false;
    
    // Default constructor
    public Home (){
        this.hasAGarage = false;
    }
    
    // Constructor with parameters
    public Home (boolean inhasAGarage, int inNumberOfBedrooms, double inBasePricePerNight, String inDescription, int inMaxOccupants){
        super(inNumberOfBedrooms, inBasePricePerNight, inDescription, inMaxOccupants);
        this.hasAGarage = inhasAGarage;
    }
    
    @Override
    public String toString(){
        return super.toString() + "\nHas A Garage: " + this.hasAGarage;
    }
    
}
