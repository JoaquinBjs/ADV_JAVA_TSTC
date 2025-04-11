package joaquinbarreram4;
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
   
    // Made into abstract because it was easier than trtying to differantiate the hotel from the house 
    public abstract void registerLodging();
    

    
    // Display list
    static public void displayAllLodging() {
        for (int i = 0; i < allLodgings.size(); i++) {
            Lodging lodge = allLodgings.get(i);
            if (lodge instanceof Hotel) {
                Hotel hotel = (Hotel) lodge;
                System.out.println(String.format(
                    "%d. Hotel Name: %s | Vacancies %d | Number Of Bedrooms: %d | Base Price Per Night: $%.2f",
                    i + 1,
                    hotel.name,
                    hotel.vacancies,
                    hotel.numberOfBedrooms,
                    hotel.basePricePerNight
                ));
            } else {
                Home home = (Home) lodge;
                System.out.println(String.format(
                    "%d. House Name: %s | Cost $%.2f | Number Of Bedrooms: %d",
                    i + 1,
                    home.name,
                    home.cost,
                    home.numberOfBedrooms
                ));
            }
        }
    }
    
    abstract void displayDetails();
    abstract String getDetailsString();

}