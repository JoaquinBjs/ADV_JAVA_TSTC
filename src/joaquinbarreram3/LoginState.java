package joaquinbarreram3;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


class LoginState extends ViewState {
    Scanner scan = new Scanner(System.in);
    String option = null;
    String inName = null;
    String inPassword = null;
    public static ArrayList<Customer> customers = new ArrayList<>();
    public static ArrayList<TravelAgencyEmployee> employees = new ArrayList<>();
    final int REQUIRED_LENGTH = 9;
    private boolean isRunning = true;
    int count = 0;
//    boolean isEmployee = false;

    public boolean validation() {
        boolean maxLength = true;   
        boolean hasUpper = false;
        boolean hasSpecial = false;
        if(inPassword.length() >= REQUIRED_LENGTH){
            maxLength = true;        
        }        
        for (int i = 0; i < inPassword.length(); i++) {
            if (Character.isUpperCase(inPassword.charAt(i))) {
                hasUpper = true;
            }
            if (!Character.isLetterOrDigit(inPassword.charAt(i))) {
                hasSpecial = true;
            }
        }
        if(maxLength && hasUpper && hasSpecial){
            return true;
        } else{
            return false;
        }
    }
    
    // Encrypt password method
    public String encryptPassword(String inPassword){
        StringBuilder encryptedPassword = new StringBuilder(inPassword); // Object
        
        // Encrypts password
        // Starts to encrypt password by adding 5 to the decimal value of each character
        for (int i = 0; i < encryptedPassword.length(); i++){
            encryptedPassword.setCharAt(i, (char) (encryptedPassword.charAt(i) + 5));
        }
        // Stores variable after shifting ascii decimals
//        String afterShifting = encryptedPassword.toString();
//        System.out.println("(Encrypt)Encrypted password before swap: " + encryptedPassword); testing
        // Swaps first character with last
        // Saves the first character
        char firstChar = encryptedPassword.charAt(0);        
        
        // Swaps the first with the last character
        encryptedPassword.setCharAt(0, encryptedPassword.charAt(encryptedPassword.length() - 1));
        
        // Swaps the last with the first character, using our variable that saves the first character
        encryptedPassword.setCharAt(encryptedPassword.length() - 1, firstChar);
        
        // Stores password after swapping letters
//        String afterSwapping = encryptedPassword.toString();
        
        
        // add extra character
        encryptedPassword.append('$');
        
        // Stores password after adding the distractor
//        String afterDistractor = encryptedPassword.toString();
        
        return encryptedPassword.toString();
    }
    
    // Decrypt password method
    public String decryptPassword(String inPassword){
        // Object
        StringBuilder decryptedPassword = new StringBuilder(inPassword);
        
        // Decrypts password
        // Removes the extra character
        decryptedPassword.deleteCharAt(decryptedPassword.length() - 1);  
        
//        System.out.println("(Decrypt)Encrypted password before swap: " + decryptedPassword); testing
        // Saves last and first character
        char lastChar = decryptedPassword.charAt(decryptedPassword.length() - 1);
        char firstChar = decryptedPassword.charAt(0);
        
        // Swaps Characters back
        decryptedPassword.setCharAt(0, lastChar);
        decryptedPassword.setCharAt(decryptedPassword.length() - 1, firstChar);            

        // Decrypt password loop by shifting the ascii decimal values back
        for (int i = 0; i < decryptedPassword.length(); i++){
            decryptedPassword.setCharAt(i, (char) (decryptedPassword.charAt(i) - 5));
        }
        
        return decryptedPassword.toString();
        
    }
    
    public void signUp() {
        System.out.println("Enter a new username: ");
        inName = scan.nextLine();
        
        // checks if a name exists by reading through the file
        boolean nameExists = false;
        try (BufferedReader reader = new BufferedReader(new FileReader("src/joaquinbarreram3/customerAccounts.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] lineData = line.split(",");
                if (lineData.length > 0 && inName.equals(lineData[0])) {
                    nameExists = true;
                    break;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(LoginState.class.getName()).log(Level.SEVERE, null, ex);
        }
    
        while (nameExists) {
            System.out.println("Username already exists! Enter a new username: ");
            inName = scan.nextLine();
            nameExists = false;
            try (BufferedReader reader = new BufferedReader(new FileReader("src/joaquinbarreram3/customerAccounts.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] lineData = line.split(",");
                    if (lineData.length > 0 && inName.equals(lineData[0])) {
                        nameExists = true;
                        break;
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(LoginState.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    
        System.out.println("Enter a password: ");
        inPassword = scan.nextLine();
        while (!validation()) {
            System.out.println("Password must be at least 9 characters long, contain an uppercase letter, and a special character.");
            System.out.println("Enter a valid password: ");
            inPassword = scan.nextLine();
        }
    
        // Makes new id by counting existing customers + 1
        int newId = customers.size() + 1;
    
        // Create new customer object (store unencrypted password in memory)
        Customer newCustomer = new Customer(
            inName,
            inPassword,  // Store unencrypted in memory
            newId,
            0.0,
            inName,
            "Unknown"
        );
        
        // Add to customers ArrayList
        customers.add(newCustomer);
        
        // Save to file (this will encrypt the password)
        save();
        
        System.out.println("Account created!");
    }
    
    // Return the customer thst mstches if the, inputed name and password matches
    public Customer cMatches() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/joaquinbarreram3/customerAccounts.txt"));
            String line;
        
            while ((line = reader.readLine()) != null) {
//                System.out.println("Popping off in cMatches while loop"); testing
                // Splits the line the loop is on into 8 parts, and puts it into a array
                String[] employeeData = line.split(",");

                if (employeeData.length >= 6) { // Ensure we have all 6 attributes in the array
                    String username = employeeData[0];
                    String password = decryptPassword(employeeData[1]);
//                    System.out.println("NAME: " + employeeData[0]); testing
//                    System.out.println("Popping off in cMatches if statement"); testing
                    // If the username and password is equal, continue recreating the objecr, if not, move to the next line, and check if that password and username is equal
                    if (inName.equals(username) && inPassword.equals(password)) {

                        // recreate object
                        int id = Integer.parseInt(employeeData[2]);
                        double balanceOwed = Double.parseDouble(employeeData[3]);
                        String name = employeeData[4];
                        String address = employeeData[5];
                        System.out.println(String.format("\n%s,%s,%d,%.2f,%s,%s", username, password,id, balanceOwed, name, address));
                        return new Customer(
                            username,
                            password,
                            id,
                            balanceOwed,
                            name,
                            address
                        );
                    }
                }
            }
            reader.close();
        } catch (IOException ex) {
            Logger.getLogger(LoginState.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    // Return the employee thst mstches if the, inputed name and password matches
    public TravelAgencyEmployee eOMatches() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/joaquinbarreram3/employeeAccounts.txt"));
            String line;
        
            while ((line = reader.readLine()) != null) {
                // Splits the line the loop is on into 8 parts, and puts it into a array
                String[] employeeData = line.split(",");
                if (employeeData.length >= 8) { // Ensure we have all 8 attributes in the array
                    String username = employeeData[0];
                    String password = decryptPassword(employeeData[1]);
                    
                    // If the username and password is equal, continue recreating the objecr, if not, move to the next line, and check if that password and username is equal
                    if (inName.equals(username) && inPassword.equals(password)) {
                        // recreate object
                        int id = Integer.parseInt(employeeData[2]);
                        boolean isManager = Boolean.parseBoolean(employeeData[3]);
                        double salary = Double.parseDouble(employeeData[4].replace("$", ""));
                        String workNumber = employeeData[5];
                        String name = employeeData[6];
                        String address = employeeData[7];
                        System.out.println(String.format("%s,%s,%d,%b,$%.2f,%s,%s,%s",username, password, id, isManager, salary, workNumber, name, address)); 
                        return new TravelAgencyEmployee(
                            username,
                            password,
                            id,
                            isManager,
                            salary, 
                            workNumber,
                            name,
                            address
                        );
                    }
                }
            }
            reader.close();
        } catch (IOException ex) {
            Logger.getLogger(LoginState.class.getName()).log(Level.SEVERE, null, ex);
        }
        // If no match is found
        return null; 
    }
    
    public void login() {
        System.out.println("Enter username: ");
        inName = scan.nextLine();
        System.out.println("Enter password: ");
        inPassword = scan.nextLine();
        
        Customer loggedInCustomer = cMatches();
        TravelAgencyEmployee loggedInEmployee = eOMatches();
        
//        for (int i = 0; i < customers.size(); i++) {
//            System.out.println("Customer: " + customers.get(i).name);
//            System.out.println("Password: " + customers.get(i).getPassword());
//        }

        if (loggedInEmployee != null) {
            System.out.println("Login successful!");
            if (loggedInEmployee.isAManager) {
//                System.out.println("customers array size when manager logs in is: " + customers.size());
                ManagerViewState mView = new ManagerViewState(loggedInEmployee, this); // This is getting the loginstate
                mView.update();
            } else {
                EmployeeViewState eView = new EmployeeViewState(loggedInEmployee, this); // This is getting loginstate 
                eView.update();
            }
        } else if (loggedInCustomer != null) { // If not an employee, check customers
            System.out.println("Login successful!");
            CustomerViewState cView = new CustomerViewState(loggedInCustomer);
            cView.update();

        } else {
            System.out.println("Invalid username or password");
        }
    }

    @Override
    void enter() {
//        System.out.println("ENTER POP!");
        System.out.println("==============");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Continue as Guest");
        System.out.println("4. Exit");
        System.out.println("==============");
        System.out.print("Choose (1 - 4): ");
    }

    @Override
    void update() {
        // load data
        load();
        while (isRunning) {
            enter();
            option = scan.nextLine();
            switch (option) {
                case "1":
                    login();
                    break;
                case "2":
                    signUp();
                    break;
                case "3":
                    System.out.println("Continuing as a guest");
                    CustomerViewState cView = new CustomerViewState(null);
                    cView.update();
                    break;
                case "4":
                    System.out.println("Exiting program");
                    isRunning = false;
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }
    @Override
    public void save() {
        try {
            // Save customers to file
            BufferedWriter customerWriter = new BufferedWriter(new FileWriter("src/joaquinbarreram3/customerAccounts.txt"));
            for (int i = 0; i < customers.size(); i++) {
                Customer customer = customers.get(i);
                customerWriter.write(String.format("%s,%s,%d,%.2f,%s,%s",
                        customer.getUsername(),
                        encryptPassword(customer.getPassword()),
                        customer.id,
                        customer.balanceOwed,
                        customer.name,
                        customer.address
                ));
                
                // Add newline for all entries except the last one
                if (i < customers.size() - 1) {
                    customerWriter.write("\n");
                }
            }
            customerWriter.close();
            
            // Save employees to file
            BufferedWriter employeeWriter = new BufferedWriter(new FileWriter("src/joaquinbarreram3/employeeAccounts.txt"));
            for (int i = 0; i < employees.size(); i++) {
                TravelAgencyEmployee employee = employees.get(i);
                employeeWriter.write(String.format("%s,%s,%d,%b,$%.2f,%s,%s,%s",
                        employee.getLoginName(),
                        encryptPassword(employee.getPassword()),
                        employee.id,
                        employee.isAManager,
                        employee.salary,
                        employee.workNumber,
                        employee.name,
                        employee.address
                ));
                
                // Add newline for all entries except the last one
                if (i < employees.size() - 1) {
                    employeeWriter.write("\n");
                }
            }
            employeeWriter.close();
            System.out.println("Data saved successfully!");
        } catch (IOException ex) {
            Logger.getLogger(LoginState.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error saving data: " + ex.getMessage());
        }
    }
    
    @Override
    public void load() {
        // Clear existing data before loading
        customers.clear();
        employees.clear();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/joaquinbarreram3/employeeAccounts.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                count++;
            }
            reader.close();
        } catch (IOException ex) {
            Logger.getLogger(LoginState.class.getName()).log(Level.SEVERE, null, ex);
        }
        // only creates the manager and employee loging if the file is empty
        if (count < 1){
            try {
            //    System.out.println("POPPING OFF!");
                // Saves manager and employee account to accounts
                BufferedWriter writer = new BufferedWriter(new FileWriter("src/joaquinbarreram3/employeeAccounts.txt", true));
                TravelAgencyEmployee emp = new TravelAgencyEmployee(
                        "Employee",
                        encryptPassword("password"), 
                        1,
                        false,
                        50000, 
                        "123-456-7890", 
                        "Jane Doe", 
                        "Company Address"
                );
                writer.write(String.format("%s,%s,%d,%b,$%.2f,%s,%s,%s",
                        emp.getLoginName(), emp.getPassword(),emp.id,emp.isAManager, emp.salary, emp.workNumber, emp.name, emp.address));
            
                TravelAgencyEmployee mgr = new TravelAgencyEmployee(
                        "Manager",
                        encryptPassword("password"),
                        2,
                        true,
                        50000,
                        "123-456-7890",
                        "John Doe",
                        "Company Address"
                );
                writer.write(String.format("\n%s,%s,%d,%b,$%.2f,%s,%s,%s",
                         mgr.getLoginName(), mgr.getPassword(),mgr.id, mgr.isAManager, mgr.salary, mgr.workNumber, mgr.name, mgr.address));            
                writer.close();
            } catch (IOException ex) {
                System.out.println("ERROR: " + ex);
                Logger.getLogger(LoginState.class.getName()).log(Level.SEVERE, null, ex);
            }
        }       // Adds the lodges and houses to the lodgING text file
        try {
        //    System.out.println("POPPING OFF!");
            BufferedWriter writer = new BufferedWriter(new FileWriter("src\\joaquinbarreram3\\lodgingInfo.txt"));
            writer.write(String.format("%s,%d,%d,%.2f,%d",
                    "4 seasons",
                    20,
                    83,
                    9.0,
                    12
            ));
            writer.write(String.format("\n%s,%d,%d,%.2f,%d",
                    "956 Hotel",
                    67,
                    12,
                    37.2,
                    5
            ));
            writer.write(String.format("\n%s,%.2f,%d", 
                    "Blue House",
                    12.1,
                    812
            ));
            writer.write(String.format("\n%s,%.2f,%d", 
                    "Mansion",
                    21.23,
                    3
            ));
            writer.write(String.format("\n%s,%.2f,%d", 
                    "Big Home",
                    10.61,
                    2
            ));
            writer.close();
        } catch (IOException ex) {
            System.out.println("NOT POPPING OFF");
            Logger.getLogger(Lodging.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            // Load customers from file
            BufferedReader customerReader = new BufferedReader(new FileReader("src/joaquinbarreram3/customerAccounts.txt"));
            String line;
            
            while ((line = customerReader.readLine()) != null) {
                String[] customerData = line.split(",");
                if (customerData.length >= 6) {
                    String username = customerData[0];
                    String password = decryptPassword(customerData[1]);
                    int id = Integer.parseInt(customerData[2]);
                    double balanceOwed = Double.parseDouble(customerData[3]);
                    String name = customerData[4];
                    String address = customerData[5];
                    
                    customers.add(new Customer(
                        username,
                        password,
                        id,
                        balanceOwed,
                        name,
                        address
                    ));
                }
            }
            customerReader.close();
            
            // Load employees from file
            BufferedReader employeeReader = new BufferedReader(new FileReader("src/joaquinbarreram3/employeeAccounts.txt"));
            
            while ((line = employeeReader.readLine()) != null) {
                String[] employeeData = line.split(",");
                if (employeeData.length >= 8) {
                    String loginName = employeeData[0];
                    String password = decryptPassword(employeeData[1]);
                    int id = Integer.parseInt(employeeData[2]);
                    boolean isManager = Boolean.parseBoolean(employeeData[3]);
                    double salary = Double.parseDouble(employeeData[4].replace("$", ""));
                    String workNumber = employeeData[5];
                    String name = employeeData[6];
                    String address = employeeData[7];
                    
                    employees.add(new TravelAgencyEmployee(
                        loginName,
                        password,
                        id,
                        isManager,
                        salary,
                        workNumber,
                        name,
                        address
                    ));
                }
            }
            employeeReader.close();
            System.out.println("Data loaded successfully!");
        } catch (IOException ex) {
            Logger.getLogger(LoginState.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error loading data: " + ex.getMessage());
        }
    }
}