/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package joaquinbarreram1;

/**
 *
 * @author jabar
 */
public class Hotel extends Lodging {
    boolean hasFreeBreakfast;
    int vacancies;
    double parkingFee;
    boolean valetParking;
    // Default constructor
    public Hotel (){
        super();
        Lodging lodging;
        this.vacancies = 0;
        this.hasFreeBreakfast = false;
        this.parkingFee = 0.00;
        this.valetParking = false;
   }
    
    // Constructor with parameter
    public Hotel (boolean inHasFreeBreakfast, int inVacancies, int inNumberOfBedrooms, double inBasePricePerNight, double inParkingFee, String inDescription, boolean inValetParking, int inMaxOccupants){
        super(inNumberOfBedrooms, inBasePricePerNight, inDescription, inMaxOccupants);
        this.hasFreeBreakfast = inHasFreeBreakfast;
        this.parkingFee = inParkingFee;
        this.vacancies = inVacancies;
        this.valetParking = inValetParking;
    }
    
    @Override 
    public String toString(){
        return super.toString() + "\nHas free breakfast: " + this.hasFreeBreakfast +
               "\nParking fee: " + this.parkingFee +
               "\nVacancies: " + this.vacancies +
               "\nValet Parking: " + this.valetParking;
    }
}
