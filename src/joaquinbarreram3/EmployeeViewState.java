package joaquinbarreram3;
import java.util.Scanner;

public class EmployeeViewState extends ViewState {
    // is running boolean for the update method
    private boolean isRunning = true;
    
    // Objects
    Scanner sc = new Scanner(System.in);
    String eOption = null;
    TravelAgencyEmployee currEmployee = null;
    LoginState lstate;
    // Constructor to hold the logged in employee
    public EmployeeViewState(TravelAgencyEmployee inEmployee, LoginState inLState){
        this.currEmployee = inEmployee;
        this.lstate = inLState;
    }
    @Override
    void enter() {
        System.out.println("====================");
        System.out.println("1. Add Lodge");
        System.out.println("2. Remove Lodge");
        System.out.println("3. List Lodges");
        System.out.println("4. Log out");
        if (currEmployee.isAManager) {
            System.out.println("5. Manager View");
        }
        System.out.println("====================");
    }

    @Override
    void update() {
        while (isRunning) {
            enter();
            if (!currEmployee.isAManager) {
                System.out.println("Select an option (1-4): ");
                eOption = sc.nextLine();
                switch (eOption) {
                    case "1":
                        System.out.println("=======================");
                        System.out.println("Select type of Lodging");
                        System.out.println("1. Home");
                        System.out.println("2. Hotel");
                        System.out.println("=======================");
                        String sOption = sc.nextLine();
                    switch (sOption) {
                        case "1":
                            {
                                // Add Home
                                System.out.println("Enter cost: ");
                                double cost = Double.parseDouble(sc.nextLine());
                                System.out.println("Enter number of bedrooms: ");
                                int bedrooms = Integer.parseInt(sc.nextLine());
                                System.out.println("Enter name: ");
                                String name = sc.nextLine();
                                Home house = new Home(name,cost, bedrooms);
                                house.registerLodging();
                                System.out.println("Home added successfully!");
                                break;
                            }
                        case "2":
                            {
                                // Add Hotel
                                System.out.println("Enter vacancies: ");
                                int vacancies = Integer.parseInt(sc.nextLine());
                                System.out.println("Enter number of bedrooms: ");
                                int bedrooms = Integer.parseInt(sc.nextLine());
                                System.out.println("Enter base price per night: ");
                                double basePrice = Double.parseDouble(sc.nextLine());
                                System.out.println("Enter max occupants: ");
                                int maxOccupants = Integer.parseInt(sc.nextLine());
                                System.out.println("Enter name: ");
                                String name = sc.nextLine();
                                Hotel hotel = new Hotel(name,vacancies, bedrooms, basePrice, maxOccupants);
                                hotel.registerLodging();
                                System.out.println("Hotel added successfully!");
                                break;
                            }
                        default:
                            System.out.println("Invalid option");
                            break;
                    }
                        break;

                    case "2":
                        // Remove Lodge
                        System.out.println("Select a lodging to remove: ");
                        for (int i = 0; i < Lodging.allLodgings.size(); i++) {
                            System.out.println((i + 1) + ". " + Lodging.allLodgings.get(i).name);
                        }
                        int rChoice = Integer.parseInt(sc.nextLine());
                        if (rChoice > 0 && rChoice <= Lodging.allLodgings.size()) {
                            System.out.println(Lodging.allLodgings.get(rChoice - 1).name + " removed successfully");
                            Lodging.allLodgings.remove(rChoice - 1);
                        } else {
                            System.out.println("Invalid selection");
                        }
                        break;
                    case "3":
                        // List Lodges
                        Lodging.displayAllLodging();
//                        System.out.println("Running"); testing
                        break;
                    case "4":
                        // Log out
                        isRunning = false;
                        currEmployee = null;
                        break;
                    default:
                        System.out.println("Invalid option");
                        break;
                }
            } else {
                System.out.println("Select an option (1-5): ");
                eOption = sc.nextLine();
                switch (eOption) {
                    case "1":
                        // Add Lodge (same as above)
                        System.out.println("=======================");
                        System.out.println("Select type of Lodging");
                        System.out.println("1. Home");
                        System.out.println("2. Hotel");
                        System.out.println("=======================");
                        String sOption = sc.nextLine();
                        if (sOption.equals("1")) {
                            // Add Home
                            System.out.println("Enter cost: ");
                            double cost = Double.parseDouble(sc.nextLine());
                            System.out.println("Enter number of bedrooms: ");
                            int bedrooms = Integer.parseInt(sc.nextLine());
                            System.out.println("Enter name: ");
                            String name = sc.nextLine();
                            Home house = new Home(name,cost, bedrooms);
                            house.registerLodging();
                            System.out.println("Home added successfully!");
                        } else if (sOption.equals("2")) {
                            // Add Hotel
                            System.out.println("Enter vacancies: ");
                            int vacancies = Integer.parseInt(sc.nextLine());
                            System.out.println("Enter number of bedrooms: ");
                            int bedrooms = Integer.parseInt(sc.nextLine());
                            System.out.println("Enter base price per night: ");
                            double basePrice = Double.parseDouble(sc.nextLine());
                            System.out.println("Enter max occupants: ");
                            int maxOccupants = Integer.parseInt(sc.nextLine());
                            System.out.println("Enter name: ");
                            String name = sc.nextLine();
                            Hotel hotel = new Hotel(name,vacancies, bedrooms, basePrice, maxOccupants);
                            hotel.registerLodging();
                            System.out.println("Hotel added successfully!");
                        } else {
                            System.out.println("Invalid option");
                        }
                        break;
                    case "2":
                        // Remove Lodge (same as above)
                        System.out.println("Select a lodging to remove: ");
                        for (int i = 0; i < Lodging.allLodgings.size(); i++) {
                            System.out.println((i + 1) + ". " + Lodging.allLodgings.get(i).name);
                        }
                        int rChoice = Integer.parseInt(sc.nextLine());
                        if (rChoice > 0 && rChoice <= Lodging.allLodgings.size()) {
                            System.out.println(Lodging.allLodgings.get(rChoice - 1).name + " removed successfully");
                            Lodging.allLodgings.remove(rChoice - 1);
                        } else {
                            System.out.println("Invalid selection");
                        }
                        break;
                    case "3":
                        // List Lodges
                        Lodging.displayAllLodging();
                        break;
                    case "4":
                        // Log out
                        isRunning = false;
                        currEmployee = null;
                        break;
                    case "5":
                        // Switch to Manager View
                        ManagerViewState mView = new ManagerViewState(this.currEmployee, this.lstate);
                        mView.update();
                        break;
                    default:
                        System.out.println("Invalid option");
                        break;
                }
            }
        }
    }
    @Override
    public void load(){
    
    }
    @Override
    public void save(){
    
    }
}