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
                
                // makes next id
                int nextId = 1;
                try (BufferedReader reader = new BufferedReader(new FileReader("src/joaquinbarreram3/employeeAccounts.txt"))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        nextId++;
                    }
                } catch (IOException ex) {
                    Logger.getLogger(ManagerViewState.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                // make a new employee
                TravelAgencyEmployee emp = new TravelAgencyEmployee(name, loginState.encryptPassword(password), nextId, false, salary, workNumber, name, address);
                
                try {
                    System.out.println("MANAGER VIEW STATE IS POPPING OFF!");
                    BufferedWriter writer = new BufferedWriter(new FileWriter("src/joaquinbarreram3/employeeAccounts.txt", true));
                    writer.write(String.format("\n%s,%s,%d,%b,$%.2f,%s,%s,%s",
                            emp.getLoginName(), emp.getPassword(), emp.id, emp.isAManager, emp.salary, emp.workNumber, emp.name, emp.address));
                    writer.flush();
                    writer.close();
                } catch (IOException ex) {
                    System.out.println("NOT POPPING OFF IN MANAGER VIEW STATE");
                    Logger.getLogger(ManagerViewState.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("Employee added successfully!");
                break;

            case "2":
                // removes employee
                java.util.ArrayList<TravelAgencyEmployee> employees = new java.util.ArrayList<>();
                try (BufferedReader reader = new BufferedReader(new FileReader("src/joaquinbarreram3/employeeAccounts.txt"))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] employeeData = line.split(",");
                        if (employeeData.length >= 8) {
                            String username = employeeData[0];
                            String empPassword = employeeData[1];
                            int id = Integer.parseInt(employeeData[2]);
                            boolean isManager = Boolean.parseBoolean(employeeData[3]);
                            double empSalary = Double.parseDouble(employeeData[4].replace("$", ""));
                            String empWorkNumber = employeeData[5];
                            String empName = employeeData[6];
                            String empAddress = employeeData[7];
                            
                            employees.add(new TravelAgencyEmployee(
                                username,
                                empPassword,
                                id,
                                isManager,
                                empSalary,
                                empWorkNumber,
                                empName,
                                empAddress
                            ));
                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(ManagerViewState.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                // Display employees
                System.out.println("Select an employee to remove: ");
                for (int i = 0; i < employees.size(); i++) {
                    System.out.println((i + 1) + ". " + employees.get(i).name);
                }
                
                int rChoice = Integer.parseInt(sc.nextLine());
                if (rChoice > 0 && rChoice <= employees.size()) {
                    System.out.println(employees.get(rChoice - 1).name + " removed successfully");
                    employees.remove(rChoice - 1);
                    
                    // update list and bring back to file
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/joaquinbarreram3/employeeAccounts.txt"))) {
                        for (int i = 0; i < employees.size(); i++) {
                            TravelAgencyEmployee employee = employees.get(i);
                            // If it's the first line, don't add a newline at the beginning
                            if (i == 0) {
                                writer.write(String.format("%s,%s,%d,%b,$%.2f,%s,%s,%s",
                                    employee.getLoginName(), employee.getPassword(), employee.id, employee.isAManager, 
                                    employee.salary, employee.workNumber, employee.name, employee.address));
                            } else {
                                writer.write(String.format("\n%s,%s,%d,%b,$%.2f,%s,%s,%s",
                                    employee.getLoginName(), employee.getPassword(), employee.id, employee.isAManager, 
                                    employee.salary, employee.workNumber, employee.name, employee.address));
                            }
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(ManagerViewState.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    System.out.println("Invalid selection");
                }
                break;
                
            case "3":
                // List Employees
                System.out.println("Listing employees...");
                try (BufferedReader reader = new BufferedReader(new FileReader("src/joaquinbarreram3/employeeAccounts.txt"))) {
                    String line;
                    int count = 1;
                    while ((line = reader.readLine()) != null) {
                        String[] employeeData = line.split(",");
                        if (employeeData.length >= 8) {
                            String empName = employeeData[6];
                            System.out.println(count + ". " + empName);
                            count++;
                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(ManagerViewState.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
                
            case "4":
                // Promote employee to manager
                // First, read all employees from file
                java.util.ArrayList<TravelAgencyEmployee> allEmployees = new java.util.ArrayList<>();
                try (BufferedReader reader = new BufferedReader(new FileReader("src/joaquinbarreram3/employeeAccounts.txt"))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] employeeData = line.split(",");
                        if (employeeData.length >= 8) {
                            String username = employeeData[0];
                            String empPassword = employeeData[1];
                            int id = Integer.parseInt(employeeData[2]);
                            boolean isManager = Boolean.parseBoolean(employeeData[3]);
                            double empSalary = Double.parseDouble(employeeData[4].replace("$", ""));
                            String empWorkNumber = employeeData[5];
                            String empName = employeeData[6];
                            String empAddress = employeeData[7];
                            
                            allEmployees.add(new TravelAgencyEmployee(
                                username,
                                empPassword,
                                id,
                                isManager,
                                empSalary,
                                empWorkNumber,
                                empName,
                                empAddress
                            ));
                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(ManagerViewState.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                // Display employees
                System.out.println("Select an employee to promote to manager: ");
                for (int i = 0; i < allEmployees.size(); i++) {
                    System.out.println((i + 1) + ". " + allEmployees.get(i).name + " - Manager: " + allEmployees.get(i).isAManager);
                }
                
                int pChoice = Integer.parseInt(sc.nextLine());
                pChoice = pChoice - 1;
                
                if (pChoice >= 0 && pChoice < allEmployees.size()) { 
                    allEmployees.get(pChoice).isAManager = true;
                    System.out.println(allEmployees.get(pChoice).name + " promoted to manager");
                    
                    // Write updated list back to file
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/joaquinbarreram3/employeeAccounts.txt"))) {
                        for (int i = 0; i < allEmployees.size(); i++) {
                            TravelAgencyEmployee employee = allEmployees.get(i);
                            // If it's the first line, don't add a new line character at the beginning
                            if (i == 0) {
                                writer.write(String.format("%s,%s,%d,%b,$%.2f,%s,%s,%s",
                                    employee.getLoginName(), employee.getPassword(), employee.id, employee.isAManager, 
                                    employee.salary, employee.workNumber, employee.name, employee.address));
                            } else {
                                writer.write(String.format("\n%s,%s,%d,%b,$%.2f,%s,%s,%s",
                                    employee.getLoginName(), employee.getPassword(), employee.id, employee.isAManager, 
                                    employee.salary, employee.workNumber, employee.name, employee.address));
                            }
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(ManagerViewState.class.getName()).log(Level.SEVERE, null, ex);
                    }
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
                System.out.println("Logging out...");
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