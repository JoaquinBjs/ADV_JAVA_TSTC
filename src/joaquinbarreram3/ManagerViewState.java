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
//    LoginState lState;
    LoginState loginState;
    TravelAgencyEmployee currUser; 
    
     // get existing logged in user
    public ManagerViewState(TravelAgencyEmployee user, LoginState inLoginState) {
        this.currUser = user; 
        this.loginState = inLoginState;
    }
    
    // Display enter prompt
    @Override
    void enter() {
        System.out.println("=================================");
        System.out.println("Manager View");
        System.out.println("1. Add Employee");
        System.out.println("2. Remove Employee");
        System.out.println("3. Edit Employee");
        System.out.println("4. List Employees");
        System.out.println("5. Make Employee A Manager");
        System.out.println("6. Manage Lodges (Switch to Employee View)");
        System.out.println("7. Log Out");
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
                    
                // make next id
                int nextId = 1;
                if (!LoginState.employees.isEmpty()) {
                    nextId = LoginState.employees.get(LoginState.employees.size() - 1).id + 1;
                }
                
                // make a new employee
                TravelAgencyEmployee emp = new TravelAgencyEmployee(name, password, nextId, false, salary, workNumber, name, address);
                
                LoginState.employees.add(emp);
                save();
                System.out.println("Employee added successfully!");
                break;
            case "2":
                // Display employees
                System.out.println("Select an employee to remove: ");
                for (int i = 0; i < LoginState.employees.size(); i++) {
                    System.out.println((i + 1) + ". " + LoginState.employees.get(i).name);
                }
                
                int rChoice = Integer.parseInt(sc.nextLine());
                if (rChoice > 0 && rChoice <= LoginState.employees.size()) {
                    System.out.println(LoginState.employees.get(rChoice - 1).name + " removed successfully");
                    LoginState.employees.remove(rChoice - 1);
                    save();
                } else {
                    System.out.println("Invalid selection");
                }
                break;
            case "3":
                // Edit Employee
                System.out.println("Select an employee to edit: ");
                for (int i = 0; i < LoginState.employees.size(); i++) {
                    System.out.println((i + 1) + ". " + LoginState.employees.get(i).name);
                }
                
                int eChoice = Integer.parseInt(sc.nextLine()) - 1;
                
                if (eChoice >= 0 && eChoice < LoginState.employees.size()) {
                    emp = LoginState.employees.get(eChoice);
                    
                    System.out.println("Editing: " + emp.name);
                    System.out.println("Enter new display name (or press Enter to keep current): ");
                    String newName = sc.nextLine();
                    if (!newName.isEmpty()) {
                        emp.name = newName;
                    }
                    // Edit username (login name)
                    System.out.println("Enter new username (login name) (or press Enter to keep current): ");
                    String newUsername = sc.nextLine();
                    if (!newUsername.isEmpty()) {
                        emp.setLoginName(newUsername);
                    }
                    System.out.println("Enter new password (or press Enter to keep current): ");
                    String newPass = sc.nextLine();
                    if (!newPass.isEmpty()) {
                        emp.setPassword(newPass);
                    }
                    
                    System.out.println("Enter new salary (or press Enter to keep current): ");
                    String newSalary = sc.nextLine();
                    if (!newSalary.isEmpty()) {
                        emp.salary = Double.parseDouble(newSalary);
                    }
                    
                    System.out.println("Enter new work number (or press Enter to keep current): ");
                    String newWorkNum = sc.nextLine();
                    if (!newWorkNum.isEmpty()) {
                        emp.workNumber = newWorkNum;
                    }
                    
                    System.out.println("Enter new address (or press Enter to keep current): ");
                    String newAddress = sc.nextLine();
                    if (!newAddress.isEmpty()) {
                        emp.address = newAddress;
                    }
                    
                    save();
                    System.out.println("Employee updated successfully!");
                } else {
                    System.out.println("Invalid selection");
                }
                break;
            case "4":
                // List Employees
                System.out.println("Listing employees...");
                try (BufferedReader reader = new BufferedReader(new FileReader("src/joaquinbarreram3/employeeAccounts.txt"))) {
                    String line;
                    int count = 1;
                    while ((line = reader.readLine()) != null) {
                        String[] employeeData = line.split(",");
                        if (employeeData.length >= 8) {
                            String empName = employeeData[6];
                            boolean isManager = Boolean.parseBoolean(employeeData[3]);
                            if (isManager) {
                                System.out.println(count + ". " + empName + " (Manager)");
                            } else {
                                System.out.println(count + ". " + empName);
                            }
                            count++;
                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(ManagerViewState.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case "5":
                // Display employees
                System.out.println("Select an employee to promote to manager: ");
                for (int i = 0; i < LoginState.employees.size(); i++) {
                    System.out.println((i + 1) + ". " + LoginState.employees.get(i).getLoginName() + 
                        " - Manager: " + LoginState.employees.get(i).isAManager);
                }
                
                int pChoice = Integer.parseInt(sc.nextLine()) - 1;
                
                if (pChoice >= 0 && pChoice < LoginState.employees.size()) { 
                    LoginState.employees.get(pChoice).isAManager = true;
                    save();
                    System.out.println(LoginState.employees.get(pChoice).name + " promoted to manager");
                } else {
                    System.out.println("Invalid selection");
                }
                break;
                
            case "6":
                // Switch to Employee View
                EmployeeViewState eView = new EmployeeViewState(this.currUser, this.loginState);
                eView.update();
                break;
                
            case "7":
                // Log Out
                isRunning = false;
                currUser = null;
//                System.out.println("LODGING ARRAY SIZE:" + Lodging.allLodgings.size());
                System.out.println("Logging out...");
                LoginState loginView = new LoginState();
                loginView.update(); 
                break;
                
            default:
                System.out.println("Invalid option");
                break;
            }
        }
    }
    @Override
    public void load(){
        // Load method is not needed here
    }
    @Override
    public void save() {
        try {
//            System.out.println("MANAGER VIEW STATE IS POPPING OFF!");
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/joaquinbarreram3/employeeAccounts.txt"));
            
            for (int i = 0; i < LoginState.employees.size(); i++) {
                TravelAgencyEmployee employee = LoginState.employees.get(i);
                if (i == 0) {
                    writer.write(String.format("%s,%s,%d,%b,$%.2f,%s,%s,%s",
                        employee.getLoginName(), loginState.encryptPassword(employee.getPassword()), employee.id, 
                        employee.isAManager, employee.salary, employee.workNumber, 
                        employee.name, employee.address));
                } else { // Makes a new line once their is more than one line
                    writer.write(String.format("\n%s,%s,%d,%b,$%.2f,%s,%s,%s",
                        employee.getLoginName(), loginState.encryptPassword(employee.getPassword()), employee.id, 
                        employee.isAManager, employee.salary, employee.workNumber, 
                        employee.name, employee.address));
                }
            }
            writer.flush(); // Had to use flush because sometimes the text will not appear on the txt file
            writer.close();
        } catch (IOException ex) {
            System.out.println("NOT POPPING OFF IN MANAGER VIEW STATE SAVE");
            Logger.getLogger(ManagerViewState.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}