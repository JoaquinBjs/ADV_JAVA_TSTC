package joaquinbarreram2;
import java.util.ArrayList;

abstract class Lodging {
    int numberOfBedrooms;
    double basePricePerNight;
    int maxOccupants;
    ArrayList<Lodging> allLodgings = new ArrayList<>();
    // Default Cnstructor
    public Lodging (){
        this.numberOfBedrooms = 0;
        this.basePricePerNight = 0.00;
        this.maxOccupants = 0;
    }
    
    // Parameter constructor
    public Lodging (int inNumberOfBedrooms, double inBasePricePerNight,int inMaxOccupants){
        this.numberOfBedrooms = inNumberOfBedrooms;
        this.basePricePerNight = inBasePricePerNight;
        this.maxOccupants = inMaxOccupants;
    }
   
    // Adds to list
    public void registerLodging(Lodging lodging) {
        allLodgings.add(lodging);
    }
    
    // Display list
    public void displayAllLodging(){
        for (int i = 0; i < allLodgings.size(); i++) {
            allLodgings.get(i).displayDetails();
        }
    }
    
    abstract void displayDetails();
}