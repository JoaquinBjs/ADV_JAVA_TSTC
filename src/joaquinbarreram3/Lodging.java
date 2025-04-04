package joaquinbarreram3;
import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

abstract class Lodging {
    int numberOfBedrooms;
    double basePricePerNight;
    int maxOccupants;
    String name;
    public static ArrayList<Lodging> allLodgings = new ArrayList<>();
    // Default Cnstructor
    public Lodging (){
        this.numberOfBedrooms = 0;
        this.basePricePerNight = 0.00;
        this.maxOccupants = 0;
    }
    
    // Parameter constructor
    public Lodging (int inNumberOfBedrooms, double inBasePricePerNight,int inMaxOccupants, String inName){
        this.numberOfBedrooms = inNumberOfBedrooms;
        this.basePricePerNight = inBasePricePerNight;
        this.maxOccupants = inMaxOccupants;
        this.name = inName;
    }
   
    // Made into abstract because it was easier than trtying to differantiate the hotel from the house 
    public abstract void registerLodging();
    

    
    // Display list
    static public void displayAllLodging(){
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src\\joaquinbarreram3\\lodgingInfo.txt"));
            String line;
            int count = 0;
            while((line = reader.readLine()) != null){
                count++;
                String[] lodgingData = line.split(","); // Splits a line into a array
                // If the array has a length equal to 5 then it must be a hotel, if not then it has to be a house
                if (lodgingData.length == 5){
//                    System.out.println("Hotel");
                    // Extracting variables, and parsing if neccesary
                    String name = lodgingData[0];
                    int vacancies = Integer.parseInt(lodgingData[1]);
                    int numberOfBedrooms = Integer.parseInt(lodgingData[2]);
                    double basePricePerNight = Double.parseDouble(lodgingData[3]);
                    System.out.println(String.format("%d. Hotel Name: %s | Vacancies %d | Number Of Bedrooms: %d | Base Price Per Night: $%.2f",
                            count,
                            name,
                            vacancies,
                            numberOfBedrooms,
                            basePricePerNight
                    ));
                } else {
//                    System.out.println("House");
                    // Extracting variables, and parsing if neccesary
                    String name = lodgingData[0];
                    double cost = Double.parseDouble(lodgingData[1]);
                    int numberOfBedrooms = Integer.parseInt(lodgingData[2]);
                    System.out.println(String.format("%d. House Name: %s | Cost $%.2f | Number Of Bedrooms: %d",
                            count,
                            name,
                            cost,
                            numberOfBedrooms
                    ));
                }
                
            }
        } catch (IOException ex) {
            Logger.getLogger(Lodging.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    abstract void displayDetails();
}