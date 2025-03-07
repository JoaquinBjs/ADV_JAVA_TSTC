package joaquinbarreram2;

import java.util.Scanner;

public class CustomerViewState extends ViewState{
    Scanner sc = new Scanner(System.in);
    boolean isRunning = true;

    private void viewLodgings() {
        System.out.println("Displaying available lodgings...");

    }
    
    private void bookLodging() {
        System.out.println("Booking a lodging...");

    }
    
    private void viewBookingDetails() {
        System.out.println("Viewing booking details...");

    }
    @Override
    void displayMenu() {
        System.out.println("Customer Menu:");
        System.out.println("1. View Available Lodgings");
        System.out.println("2. Book a Lodging");
        System.out.println("3. View Booking Details");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");     
    }
    @Override
    void start() {
        while(isRunning){
            displayMenu();
            String input = sc.nextLine();
            if (input.equals("1")) {
                viewLodgings();
            } else if (input.equals("2")) {
                bookLodging();
            } else if (input.equals("3")) {
                viewBookingDetails();
            } else if (input.equals("4")) {
                System.out.println("Exiting Customer Menu.");
                break;
            } else {
                System.out.println("Invalid choice. Please enter a valid option.");
            }            
        }
    }
}
