package joaquinbarreram2;

public class JoaquinBarreraM2 {
    public static void main(String[] args) {
        TravelAgencyEmployee employee1 = new TravelAgencyEmployee(false, 800000.01, "956-583-0943", "Edward Elric", "1234 abc street", 32,"AE1234", "EdwardElric1");
        TravelAgencyEmployee employee2 = new TravelAgencyEmployee(true,1000000.45, "956-513-0942", "Ash Ketchum", "328 walnut grove", 12,"KA1234", "AshKetchum1");
        Customer customer1 = new Customer(8.00, "John Marson", "1234 Green drive", 288);
        Customer customer2 = new Customer(69.00,"John Doe", "4321 Applewood drive", 228);
        Customer customer3 = new Customer(0.01, "Bruce Wayne", "Wayne Mansion", 28);
        
        System.out.println(employee1);
        System.out.println(employee2);
        System.out.println(customer1);
        System.out.println(customer2);
        System.out.println(customer3);

    }
}
