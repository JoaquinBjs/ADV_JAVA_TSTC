package joaquinbarreram3;

class Hotel extends Lodging {
    int vacancies;
    
    // Default constructor
    public Hotel (){
        super();
        this.vacancies = 0;

   }
    
    // Constructor with parameter
    public Hotel (int inVacancies, int inNumberOfBedrooms, double inBasePricePerNight, int inMaxOccupants, String inName){
        super(inNumberOfBedrooms, inBasePricePerNight, inMaxOccupants, inName);        
        this.vacancies = inVacancies;
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