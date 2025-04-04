package joaquinbarreram3;

import java.io.*;
import java.util.Scanner;

public class CustomerViewState extends ViewState {
    // Objects
    Scanner sc = new Scanner(System.in);
    Lodging lodgeOrder = null;
    Customer currCustomer = new Customer();
    LoginState lState = new LoginState();
   
    
    // Variables
    boolean isRunning = true;
    int numOfNights = 0;
    
    // Constructor to hold the current logged in customer, to be used if I find a use for it
    public CustomerViewState (Customer inCustomer){
        this.currCustomer = inCustomer;
    }
    
    //  Display Lodgings
    private void viewLodgings() {
        System.out.println("Displaying available lodgings...");
        for (int i = 0; i < Lodging.allLodgings.size(); i++) {
            System.out.println((1 + i) + ". " + Lodging.allLodgings.get(i).name + " || price: $" + String.format("%.2f", Lodging.allLodgings.get(i).basePricePerNight));
        }
        
        // Loops forever until a valid option is chosen
        while (true) {
            // Prompts user to select a lodge
            System.out.println("=================================");
            System.out.println("a. Select Lodge");
            System.out.println("b. Return");
            System.out.println("=================================");
            char input = sc.nextLine().charAt(0);
            if (input == 'a' || input == 'A') {
                System.out.println("Select a Lodge: ");
                String lodgeInput = sc.nextLine();
                int lodgeIndex = Integer.parseInt(lodgeInput) - 1;
                if (lodgeIndex >= 0 && lodgeIndex < Lodging.allLodgings.size()) {
                    lodgeOrder = Lodging.allLodgings.get(lodgeIndex);
                    System.out.println("Lodge Selected: " + lodgeOrder.name);
                    System.out.println("Select Number Of Nights: ");
                    numOfNights = Integer.parseInt(sc.nextLine());
                    break;
                } else {
                    System.out.println("Invalid selection");
                }
            } else if (input == 'b' || input == 'B') {
                break;
            } else {
                System.out.println("Select a valid option");
            }
        }
    }

    // Method to book a lodging
    private void bookLodging() {
        if (lodgeOrder == null) {
            System.out.println("No lodging selected");
        } else {
            // Book lodging prompt
            System.out.println("============================");
            System.out.println("Booking a lodging...");
            System.out.println("============================");
            lodgeOrder.displayDetails();
            System.out.println("Number of Nights: " + numOfNights);
            System.out.println("Total Cost: $" + String.format("%.2f", lodgeOrder.basePricePerNight * numOfNights));
            System.out.println("============================");
            System.out.println("a. Confirm");
            System.out.println("b. Cancel");
            System.out.println("c. Exit");
            System.out.println("============================");
            System.out.println("Select an option: ");
            char option = sc.nextLine().charAt(0);
            if (option == 'a' || option == 'A') {
                System.out.println("Order confirmed!");
                lodgeOrder = null;
                numOfNights = 0;
            } else if (option == 'b' || option == 'B') {
                System.out.println("Order canceled");
                lodgeOrder = null;
                numOfNights = 0;
            } else if (option == 'c' || option == 'C') {
                System.out.println("Exited");
            }
        }
    }
    
    // enter prompt
    @Override
    void enter() {
        System.out.println("Customer Menu:");
        System.out.println("1. View Lodges With Price Per Night");
        System.out.println("2. View Order");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
    }

    // update view states
    @Override
    void update() {
//        System.out.println("customers array list on signout is: " + lState.customers.size());
        while (isRunning) {
            enter();
            String input = sc.nextLine();
            if (input.equals("1")) {
                viewLodgings();
            } else if (input.equals("2")) {
                bookLodging();
            } else if (input.equals("3")) {
                System.out.println("Exiting Customer Menu.");
                break;
            } else {
                System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }
    @Override
    public void load() {
        try {
            // Debug message
            System.out.println("Loading available lodgings...");

            
            BufferedReader reader = new BufferedReader(new FileReader("src\\joaquinbarreram3\\lodges.txt"));
            String line;
            
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                // Hotel: name,vacancies,bedrooms,basePrice,maxOccupants
                if (data.length == 5) {
                    Lodging.allLodgings.add(new Hotel(
                        data[0].trim(),
                        Integer.parseInt(data[1].trim()),
                        Integer.parseInt(data[2].trim()),
                        Double.parseDouble(data[3].trim()),
                        Integer.parseInt(data[4].trim())
                    ));
                    System.out.println("Loaded hotel: " + data[0]); // Debug
                } 
                // Home: name,cost,bedrooms
                else if (data.length == 3) {
                    Lodging.allLodgings.add(new Home(
                        data[0].trim(),
                        Double.parseDouble(data[1].trim()),
                        Integer.parseInt(data[2].trim())
                    ));
                    System.out.println("Loaded home: " + data[0]); // Debug
                }
            }
            reader.close();
            System.out.println("Lodgings loaded successfully! Total: " + Lodging.allLodgings.size()); // Debug
        } catch (IOException ex) {
            System.out.println("Error loading lodgings: " + ex.getMessage()); // Debug
        }
    }
    @Override
    public void save() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src\\joaquinbarreram3\\lodgingInfo.txt"));
            
            for (int i = 0; i < Lodging.allLodgings.size(); i++) {
                Lodging lodge = Lodging.allLodgings.get(i);
                
                // If the lodging is a hotel then the hotel will be written to the txt file if not then it will be the home
                if (lodge instanceof Hotel) {
                    Hotel hotel = (Hotel) lodge; // Convert lodge to hotel
                    writer.write(String.format("%s,%d,%d,%.2f,%d",
                        hotel.name,
                        hotel.vacancies,
                        hotel.numberOfBedrooms,
                        hotel.basePricePerNight,
                        hotel.maxOccupants
                    ));
                } else if (lodge instanceof Home) {
                    Home home = (Home) lodge; // convert to house
                    writer.write(String.format("%s,%.2f,%d",
                        home.name,
                        home.cost,
                        home.numberOfBedrooms
                    ));
                }
                
                // Add newline except after last entry
                if (i < Lodging.allLodgings.size() - 1) {
                    writer.write("\n");
                }
            }
            writer.close();
            System.out.println("Lodgings saved successfully!"); // Debug
        } catch (IOException ex) {
            System.out.println("Error saving lodgings: " + ex.getMessage()); // Debug
        }
    }
}