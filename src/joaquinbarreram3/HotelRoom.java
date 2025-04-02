package joaquinbarreram3;

public class HotelRoom extends Hotel {
    int roomNumber;
    
    // Default constructor
    public HotelRoom (){
        super();
        this.roomNumber = 0;
    }
    
    // Constructor with parameters
    public HotelRoom(int inRoomNumber, int inNumberOfBedrooms, double inBasePricePerNight, int inMaxOccupants) {
        super(0, inNumberOfBedrooms, inBasePricePerNight, inMaxOccupants, null);
        this.roomNumber = inRoomNumber;
    }
    
    @Override
    public void displayDetails(){
        System.out.println("Hotel Room");
        System.out.println("Room number: " + this.roomNumber);
        System.out.println("Number of bedrooms: " + this.numberOfBedrooms);
        System.out.println("Max occupants allowed in room: " + this.maxOccupants);
        System.out.println("Price per night: $" + String.format("%.2f", this.basePricePerNight));
    }
}