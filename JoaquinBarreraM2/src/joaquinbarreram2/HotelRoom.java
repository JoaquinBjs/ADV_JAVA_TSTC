package joaquinbarreram2;

public class HotelRoom extends Hotel {
    int roomNumber;
    
    // Default constructor
    public HotelRoom (){
        super();
        this.roomNumber = 0;
    }
    
    // Constructor with parameters
    public HotelRoom(int inRoomNumber, int inVacancies, int inNumberOfBedrooms, double inBasePricePerNight, int inMaxOccupants) {
        super(inVacancies, inNumberOfBedrooms, inBasePricePerNight, inMaxOccupants);
        this.roomNumber = inRoomNumber;
    }
    
    @Override
    public String toString(){
        return super.toString() + "\nRoom Number: " + this.roomNumber;
    }
    
}
