package joaquinbarreram2;
import java.util.ArrayList; 
import java.util.Scanner;

class LoginState extends ViewState {
    Scanner scan = new Scanner(System.in);
    static ArrayList<TravelAgencyEmployee> employees = new ArrayList<>();
    static ArrayList<Customer> customers = new ArrayList<>();
    String option = null;
    String inName = null;
    String inPassword = null;
    final int REQUIRED_LENGTH = 9;
    private boolean isRunning = true;
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

    public void signUp() {
        System.out.println("Enter a new username: ");
//        System.out.println("customers array size before signup is: "+customers.size());
        inName = scan.nextLine();
        boolean nameExists = false;
        for (int i = 0; i < customers.size(); i++) {
            if (inName.equals(customers.get(i).getUsername())) {
                nameExists = true;
                break;
            }
        }
        while (nameExists) {
            System.out.println("Username already exists! Enter a new username: ");
            inName = scan.nextLine();
            nameExists = false;
            for (int i = 0; i < customers.size(); i++) {
                if (inName.equals(customers.get(i).getUsername())) {
                    nameExists = true;
                    break;
                }
            }
        }

        System.out.println("Enter a password: ");
        inPassword = scan.nextLine();
        while (!validation()) {
            System.out.println("Password must be at least 9 characters long, contain an uppercase letter, and a special character.");
            System.out.println("Enter a valid password: ");
            inPassword = scan.nextLine();
        }

        customers.add(new Customer(0.0, inName, "Unknown", customers.size() + 1, inName, inPassword));
        System.out.println("Account successfully created!");
//        System.out.println("The customer array size after signup: " + customers.size());
    }


    public boolean matches() {
        // check employees
        for (int i = 0; i < employees.size(); i++) {
            if (inName.equals(employees.get(i).getLoginName()) && inPassword.equals(employees.get(i).getPassword())) {
//                if (employees.get(i).isAManager) {
//                    isManager = true;
//                } else {
//                    isEmployee = true;
//                }
                return true;
            }
        }
        // check customers
        for (int i = 0; i < customers.size(); i++) {
            if (inName.equals(customers.get(i).getUsername()) && inPassword.equals(customers.get(i).getPassword())) {
                return true; 
            }
        }
        return false;
    }
    
    // Return the customer thst mstches if the, inputed name and password matches
    public Customer cMatches() {
        for (int i = 0; i < customers.size(); i++) {
            if (inName.equals(customers.get(i).getUsername()) && 
                inPassword.equals(customers.get(i).getPassword())) {
                return customers.get(i);
            }
        }
        return null;
    }
    
    // Return the employee thst mstches if the, inputed name and password matches
    public TravelAgencyEmployee eOMatches() {  
        for (int i = 0; i < employees.size(); i++) {
            if (inName.equals(employees.get(i).getLoginName()) && inPassword.equals(employees.get(i).getPassword())) {
                return employees.get(i);
            }
        }
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
                ManagerViewState mView = new ManagerViewState(loggedInEmployee);
                mView.update();
            } else {
                EmployeeViewState eView = new EmployeeViewState(loggedInEmployee);
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
        // Adds the password and username stated in the rubric
        TravelAgencyEmployee emp = new TravelAgencyEmployee(false, 50000, "123-456-7890", "Jane Doe", "Company Address", 1, "password", "Employee");
        TravelAgencyEmployee mgr = new TravelAgencyEmployee(true, 50000, "123-456-7890", "John Doe", "Company Address", 1, "password", "Manager");
        employees.add(emp);
        employees.add(mgr);
        Hotel hotel1 = new Hotel(20,83,9.0,12, "4 seasons");
        Hotel hotel2 = new Hotel(67,12,37.2,5, "956 Hotel");
        Home home1 = new Home(12.1,812, "Blue House");
        Home home2 = new Home(21.23,3, "Mansion");
        Home home3 = new Home(10.61,2, "Big Home");
        Lodging.allLodgings.add(hotel1);
        Lodging.allLodgings.add(hotel2);
        Lodging.allLodgings.add(home1);
        Lodging.allLodgings.add(home2);
        Lodging.allLodgings.add(home3);
//        CustomerViewState cView = new CustomerViewState();
//        EmployeeViewState eView = new EmployeeViewState();
//        ManagerViewState mView = new ManagerViewState();
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
}