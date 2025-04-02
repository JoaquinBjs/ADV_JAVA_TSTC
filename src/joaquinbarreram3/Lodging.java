package joaquinbarreram3;
import java.util.ArrayList;

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
   
    // Adds to list
    public void registerLodging(Lodging lodging) {
        allLodgings.add(lodging);
    }
    

    
    // Display list
    static public void displayAllLodging(){
        for (int i = 0; i < allLodgings.size(); i++) {
            System.out.println("=======================================");
            allLodgings.get(i).displayDetails();
            System.out.println("=======================================");
        }
    }
    
    abstract void displayDetails();
}