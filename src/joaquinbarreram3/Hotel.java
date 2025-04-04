package joaquinbarreram3;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

class Hotel extends Lodging {
    int vacancies;
    
    // Default constructor
    public Hotel (){
        super();
        this.vacancies = 0;

   }
    
    // Constructor with parameter
    public Hotel (String inName, int inVacancies, int inNumberOfBedrooms, double inBasePricePerNight, int inMaxOccupants) {
        super(inNumberOfBedrooms, inBasePricePerNight, inMaxOccupants, inName);        
        this.vacancies = inVacancies;
    }
     @Override
    public void registerLodging() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src\\joaquinbarreram3\\lodgingInfo.txt", true));
            writer.write(String.format("\n%s,%d,%d,%.2f%n", name, vacancies, numberOfBedrooms, basePricePerNight));
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Hotel.class.getName()).log(Level.SEVERE, null, ex);
        }

    }   
    
    @Override
    public void displayDetails(){
        System.out.println("Hotel:");
        System.out.println("Name: " + this.name);
        System.out.println("Base price per night: $" + String.format("%.2f",this.basePricePerNight));
        System.out.println("Vacant rooms: " + this.vacancies);
        System.out.println("Rooms: " + this.numberOfBedrooms);
        System.out.println("Maximum amount of occupants allowed in the hotel: " + this.maxOccupants);
    }


}