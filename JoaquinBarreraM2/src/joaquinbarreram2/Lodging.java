package joaquinbarreram2;

public class Lodging {
    int numberOfBedrooms;
    double basePricePerNight;
    int maxOccupants;
    
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
    
    // Override to string method
    @Override
    public String toString(){
        return "Lodging info \n" + 
                "Number Of Bedrooms: " + this.numberOfBedrooms +
                "\nBase price per night: " + String.format("%.2f", this.basePricePerNight) +
                "\nMax occupants: " + this.maxOccupants;
    }
    
    
}
