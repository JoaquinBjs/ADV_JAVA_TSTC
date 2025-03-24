package joaquinbarreram2;
import java.util.Scanner;

class ManagerViewState extends ViewState {
    // Variables
    boolean isRunning = true;
    
    // Objects
    
    Scanner sc = new Scanner(System.in);
    LoginState lState;
    
    // Display enter prompt
    @Override
    void enter() {
        System.out.println("=================================");
        System.out.println("Manager View");
        System.out.println("1. Add Employee");
        System.out.println("2. Remove Employee");
        System.out.println("3. List Employees");
        System.out.println("4. Make Employee A Manager");
        System.out.println("5. Manage Lodges (Switch to Employee View)");
        System.out.println("6. Log Out");
        System.out.println("=================================");
    }

    // Updates view states
    @Override
    void update() {
        while (isRunning) {
            enter();
            String option = sc.nextLine();
            switch (option) {
                case "1":
                    // Add Employee
                    System.out.println("Enter employee name: ");
                    String name = sc.nextLine();
                    System.out.println("Enter employee password: ");
                    String password = sc.nextLine();
                    System.out.println("Enter employee salary: ");
                    double salary = Double.parseDouble(sc.nextLine());
                    System.out.println("Enter employee work number: ");
                    String workNumber = sc.nextLine();
                    System.out.println("Enter employee address: ");
                    String address = sc.nextLine();
                    TravelAgencyEmployee emp = new TravelAgencyEmployee(false, salary, workNumber, name, address, lState.employees.size() + 1, password, name);
                    lState.employees.add(emp); 
                    System.out.println("Employee added successfully!");
                    break;
                case "2":
                    // Remove Employee
                    System.out.println("Select an employee to remove: ");
                    for (int i = 0; i < lState.employees.size(); i++) {
                        System.out.println((i + 1) + ". " + lState.employees.get(i).name);
                    }
                    int rChoice = Integer.parseInt(sc.nextLine());
                    if (rChoice > 0 && rChoice <= lState.employees.size()) {
                        System.out.println(lState.employees.get(rChoice - 1).name + " removed successfully");
                        lState.employees.remove(rChoice - 1);
                    } else {
                        System.out.println("Invalid selection");
                    }
                    break;
                case "3":
                    // List Employees
                    System.out.println("Listing employees...");
                    for (int i = 0; i < lState.employees.size(); i++) {
                        System.out.println((i + 1) + ". " + lState.employees.get(i).name);
                    }
                    break;
                case "4":
                    // Prompts user to select an employee to premote to a manager
                    System.out.println("Select an employee to promote to manager: ");
                    for (int i = 0; i < lState.employees.size(); i++) {
                        System.out.println((i + 1) + ". " + lState.employees.get(i).name + "Manager: " + lState.employees.get(i).isAManager);
                    }
                    int pChoice = Integer.parseInt(sc.nextLine());
                    pChoice = pChoice - 1; 
                    
                    // Sets the employee to a manager by using the userinput
                    if (pChoice >= 0 && pChoice < lState.employees.size()) { 
                        lState.employees.get(pChoice).isAManager = true;
                        System.out.println(lState.employees.get(pChoice).name + " promoted to manager");
                    } else {
                        System.out.println("Invalid selection");
                    }
                    break;
                case "5":
                    // Switch to Employee View
                    EmployeeViewState eView = new EmployeeViewState();
                    eView.update();
                    break;
                case "6":
                    // Log Out
                    isRunning = false;
                    lState.isManager = false;
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }
}