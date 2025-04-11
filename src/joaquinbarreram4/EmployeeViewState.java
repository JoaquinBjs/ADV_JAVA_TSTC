package joaquinbarreram4;
import java.io.*;
import java.util.Scanner;
import javax.swing.JPanel;

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
        load(); // ensures all existing lodges are loaded in
    }
    public void editLodge() {
        System.out.println("Select a lodging to edit:");
        Lodging.displayAllLodging();
        System.out.print("Enter choice (1-" + Lodging.allLodgings.size() + "): ");
        int editChoice = Integer.parseInt(sc.nextLine());
        
        if (editChoice > 0 && editChoice <= Lodging.allLodgings.size()) {
            Lodging lodge = Lodging.allLodgings.get(editChoice - 1);
            
            System.out.println("\nEditing: " + lodge.name);
            System.out.println("1. Edit name");
            System.out.println("2. Edit price");
            
            // Type-specific options
            if (lodge instanceof Hotel) {
                System.out.println("3. Edit vacancies");
                System.out.println("4. Edit max occupants");
            } else if (lodge instanceof Home) {
                System.out.println("3. Edit bedroom count");
            }
            
            System.out.println("0. Cancel");
            System.out.print("Select option: ");
            String editOpt = sc.nextLine();
            
            switch (editOpt) {
                case "1": // Edit name
                    System.out.print("Enter new name: ");
                    lodge.name = sc.nextLine();
                    break;
                    
                case "2": // Edit price
                    if (lodge instanceof Home) {
                        System.out.print("Enter new home cost: $");
                        ((Home)lodge).cost = Double.parseDouble(sc.nextLine());
                    } else {
                        System.out.print("Enter new base price per night: $");
                        ((Hotel)lodge).basePricePerNight = Double.parseDouble(sc.nextLine());
                    }
                    break;
                    
                case "3": // Type-specific options
                    if (lodge instanceof Hotel) {
                        System.out.print("Enter new vacancy count: ");
                        ((Hotel)lodge).vacancies = Integer.parseInt(sc.nextLine());
                    } else {
                        System.out.print("Enter new bedroom count: ");
                        lodge.numberOfBedrooms = Integer.parseInt(sc.nextLine());
                    }
                    break;
                    
                case "4": // Hotel-only option
                    if (lodge instanceof Hotel) {
                        System.out.print("Enter new max occupants: ");
                        ((Hotel)lodge).maxOccupants = Integer.parseInt(sc.nextLine());
                    } else {
                        System.out.println("Invalid option!");
                    }
                    break;
                    
                case "0":
                    System.out.println("Edit cancelled.");
                    return;
                    
                default:
                    System.out.println("Invalid option!");
                    return;
            }
            
            save();
            System.out.println("Lodge updated successfully!");
        } else {
            System.out.println("Invalid selection!");
        }
    }
    public void removeLodge(){
        System.out.println("Select a lodging to remove: ");
        for (int i = 0; i < Lodging.allLodgings.size(); i++) {
            System.out.println((i + 1) + ". " + Lodging.allLodgings.get(i).name);
        }
        int rChoice = Integer.parseInt(sc.nextLine());
        if (rChoice > 0 && rChoice <= Lodging.allLodgings.size()) {
            System.out.println(Lodging.allLodgings.get(rChoice - 1).name + " removed successfully");
//            System.out.println("ARRAY SIZE BEFORE: " + Lodging.allLodgings.size());
            Lodging.allLodgings.remove(rChoice - 1);
//            System.out.println("ARRAY SIZE AFTER: " + Lodging.allLodgings.size());
        // Update file, to remove text
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src\\joaquinbarreram4\\lodgingInfo.txt"));
            // Gets updated array, and then rewrites the file with the contents of the updated array, which no longer contains the removed item
            for (int i = 0; i < Lodging.allLodgings.size(); i++) {
                Lodging lodge = Lodging.allLodgings.get(i);
                if (lodge instanceof Hotel) {
                    Hotel hotel = (Hotel)lodge;
                    writer.write(String.format("%s,%d,%d,%.2f,%d",
                        hotel.name,
                        hotel.vacancies,
                        hotel.numberOfBedrooms,
                        hotel.basePricePerNight,
                        hotel.maxOccupants
                    ));
                } else {
                    Home home = (Home)lodge;
                    writer.write(String.format("%s,%.2f,%d",
                        home.name,
                        home.cost,
                        home.numberOfBedrooms
                    ));
                }
                if (i < Lodging.allLodgings.size() - 1) {
                    writer.newLine();
                }
            }
//            System.out.println("ARRAY SIZE TRY CATCH REMOVE: " + Lodging.allLodgings.size());
            writer.close();
        } catch (IOException ex) {
            System.out.println("Error updating file: " + ex.getMessage());
        }
        } else {
            System.out.println("Invalid selection");
        }
    }
    @Override
    void enter() {
        System.out.println("====================");
        System.out.println("1. Add Lodge");
        System.out.println("2. Remove Lodge");
        System.out.println("3. List Lodges");
        System.out.println("4. Edit Lodges");
        System.out.println("5. Log out");
        if (currEmployee.isAManager) {
            System.out.println("6. Manager View");
        }
        System.out.println("====================");
    }

    @Override
    void update() {
        while (isRunning) {
            enter();
            if (!currEmployee.isAManager) {
                System.out.println("Select an option (1-5): ");
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
//                                System.out.println("ARRAY SIZE BEFORE: " + Lodging.allLodgings.size());
                                Lodging.allLodgings.add(house);
//                                System.out.println("ARRAY SIZE AFTER: " + Lodging.allLodgings.size());
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
//                                System.out.println("ARRAY SIZE AFTER: " + Lodging.allLodgings.size());
                                Lodging.allLodgings.add(hotel);
//                                System.out.println("ARRAY SIZE AFTER: " + Lodging.allLodgings.size());
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
                        removeLodge();
                        break;
                    case "3":
                        // List Lodges
                        Lodging.displayAllLodging();
//                        System.out.println("Running"); testing
                        break;
                    case "4":
                        // Edit Lodging
                        editLodge();
                        break;
                    case "5":
                        // Log out
                        isRunning = false;
                        currEmployee = null;
                        break;
                    default:
                        System.out.println("Invalid option");
                        break;
                }
            } else {
                System.out.println("Select an option (1-6): ");
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
                            Lodging.allLodgings.add(house);
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
                            Lodging.allLodgings.add(hotel);
                            System.out.println("Hotel added successfully!");
                        } else {
                            System.out.println("Invalid option");
                        }
                        break;
                    case "2":
                        // Remove Lodge (same as above)
                        removeLodge();
                        break;
                    case "3":
                        // List Lodges
                        Lodging.displayAllLodging();
                        break;
                    case "4":
                        // Edit Lodges
                        editLodge();
                        break;
                    case "5":
                        // Log out
                        isRunning = false;
                        currEmployee = null;
                        System.out.println("Logging out");
                        LoginState loginView = new LoginState();
                        loginView.update();
                        break;
                    case "6":
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
    public void load() {
        try {
            // Load lodgings from file
            BufferedReader reader = new BufferedReader(new FileReader("src\\joaquinbarreram4\\lodgingInfo.txt"));
            String line;
            Lodging.allLodgings.clear(); // Clear existing before loading
//            System.out.println("POPPING OFF!");
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 5) { // Hotel
                    Lodging.allLodgings.add(new Hotel(
                        data[0], 
                        Integer.parseInt(data[1]), 
                        Integer.parseInt(data[2]), 
                        Double.parseDouble(data[3]), 
                        Integer.parseInt(data[4])
                    ));
                } else if (data.length == 3) { // Home
                    Lodging.allLodgings.add(new Home(
                        data[0],
                        Double.parseDouble(data[1]),
                        Integer.parseInt(data[2])
                    ));
                }
            }
            reader.close();
            System.out.println("Lodgings loaded successfully!"); // Debug
        } catch (IOException ex) {
            System.out.println("Error loading lodgings: " + ex.getMessage()); // Debug
        }
    }
    
    @Override
    public void save() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src\\joaquinbarreram4\\lodgingInfo.txt"));
            
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
    @Override
    public JPanel getPanel(){
        return new JPanel();
    }
}