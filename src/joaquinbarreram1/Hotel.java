package joaquinbarreram1;

public class Hotel extends Lodging {
    int vacancies;
    
    // Default constructor
    public Hotel (){
        super();
        this.vacancies = 0;

   }
    
    // Constructor with parameter
    public Hotel (int inVacancies, int inNumberOfBedrooms, double inBasePricePerNight, int inMaxOccupants){
        super(inNumberOfBedrooms, inBasePricePerNight, inMaxOccupants);        
        this.vacancies = inVacancies;
    }
    
    @Override 
    public String toString(){
        return super.toString() + "\nVacancies: " + this.vacancies;
    }
}
