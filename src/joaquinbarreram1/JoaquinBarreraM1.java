/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package joaquinbarreram1;

/**
 *
 * @author jabar
 */
public class JoaquinBarreraM1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TravelAgencyEmployee employee1 = new TravelAgencyEmployee(false,"1/10/25", 800000.01, "956-583-0943", "Edward Elric", "1234 abc street", "956-382-3284", 32,"AE1234", "EdwardElric1");
        TravelAgencyEmployee employee2 = new TravelAgencyEmployee(true,"1/10/24", 1000000.45, "956-513-0942", "Ash Ketchum", "328 walnut grove", "956-382-4321", 12,"KA1234", "AshKetchum1");
        Customer customer1 = new Customer(8.00, "John Marson", "1234 Green drive", "938-454-5435", 288, "JM2822", "JohnMarson1234");
        Customer customer2 = new Customer(69.00,"John Doe", "4321 Applewood drive", "938-414-1235", 228, "JD9220", "JohnDoe321");
        Customer customer3 = new Customer(0.01, "Bruce Wayne", "Wayne Mansion", "938-435-2435", 28, "GC123", "BruceWayne219");
        
        System.out.println(employee1);
        System.out.println(employee2);
        System.out.println(customer1);
        System.out.println(customer2);
        System.out.println(customer3);
        
        
        
        
        
        
        
        
        
        
        // testing
//       Hotel hotel = new Hotel(true,69,5,6.98,9.09,"cool hotel", true, 3);
//       System.out.println(hotel);
//       HotelRoom room = new HotelRoom(29, 2, 500, "Nice room", 5);
//       System.out.println(room);
//       Home home = new Home(true, 5, 32, "Cool House",7);
//        System.out.println(home);
       
    }
    
}
