package joaquinbarreram3;
import java.util.Scanner;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

class ManagerViewState extends ViewState {
    // Variables
    boolean isRunning = true;
    
    // Objects
    Scanner sc = new Scanner(System.in);
    LoginState lState;
    LoginState loginState;
    TravelAgencyEmployee currUser; 
    
     // get existing logged in user
    public ManagerViewState(TravelAgencyEmployee user) {
        this.currUser = user; 
    }
    
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
                    TravelAgencyEmployee emp = new TravelAgencyEmployee(name, password,lState.employees.size() + 2, false, salary, workNumber, name, address);
                    lState.employees.add(emp); 
                    try {
                        System.out.println("MANAGER VIEW STATE IS POPPING OFF!");
                        BufferedWriter writer = new BufferedWriter(new FileWriter("src/joaquinbarreram3/employeeAccounts.txt", true));
//                        writer.write("\n Test");
                        writer.write(String.format("\nManager: %b | Salary: $%.2f | Work Number: %s | name: %s | address: %s | id: %d | password: %s | loginName: %s", emp.isAManager, emp.salary, emp.workNumber, emp.name, emp.address, emp.id, emp.getPassword(), emp.getLoginName()));
                        writer.flush();
                        writer.close();
                    } catch (IOException ex) {
                        System.out.println("NOT POPPING OFF IN MANAGER VIEW STATE");
                        Logger.getLogger(ManagerViewState.class.getName()).log(Level.SEVERE, null, ex);
                    }
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
                    EmployeeViewState eView = new EmployeeViewState(this.currUser);
                    eView.update();
                    break;
                case "6":
                    // Log Out
                    isRunning = false;
//                    lState.isManager = false;
                    System.out.println("Logging out...");
                    isRunning = false;
                    LoginState loginView = new LoginState();
                    loginView.update(); 
                    currUser = null;
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }
}