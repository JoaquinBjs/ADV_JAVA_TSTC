/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package joaquinbarreram1;

/**
 *
 * @author jabar
 */
public class HotelRoom extends Lodging {
    int roomNumber;
    
    // Default constructor
    public HotelRoom (){
        this.roomNumber = 0;
    }
    
    // Constructor with parameters
    public HotelRoom (int inRoomNumber, int inNumberOfBedrooms, double inBasePricePerNight, String inDescription, int inMaxOccupants){
        super(inNumberOfBedrooms, inBasePricePerNight, inDescription, inMaxOccupants);
        this.roomNumber = inRoomNumber;
    }
    
    @Override
    public String toString(){
        return super.toString() + "\nRoom Number: " + this.roomNumber;
    }
    
}
