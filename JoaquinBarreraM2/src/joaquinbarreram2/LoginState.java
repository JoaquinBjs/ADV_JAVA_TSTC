package joaquinbarreram2;
import java.util.ArrayList; 
import java.util.Scanner;

class LoginState extends ViewState {
    // Scanner 
    Scanner scan = new Scanner(System.in);
    // ArrayList
    ArrayList<String> username = new ArrayList<String>();
    ArrayList<String> password = new ArrayList<String>();
    // Variables
    String option = null;
    String inName = null;
    String inPassword = null;
    final int REQUIRED_LENGTH = 9;
    private boolean isRunning = true;
    boolean passValid;
    boolean nameValid;
    boolean isManager;
    boolean isEmployee;
    

    // Checks if username and matches
    public boolean matches (){
        if(inName.equals(username.get(0)) && inPassword.equals(password.get(0))){
            System.out.println("Employee view loading...");
            this.isEmployee = true;
            return true;
        } else if (inName.equals(username.get(1)) && inPassword.equals(password.get(1))){
            System.out.println("Manager view loading...");
            this.isManager = true;
            return true;
        } else {
            // Loops through the name array to see if the username and password match, both arrays are always the same size
            for (int i = 0; i < username.size(); i++){
                // If both match the username and password then return true if not then return false
                if(inName.equals(username.get(i)) && inPassword.equals(password.get(i))){
                    return true;
                } 
            }
        }
        return false;
    }
    
    // Checks if password is valid
    public boolean validation(){
        // Boolean variables for validation
        boolean maxLength = false;
        boolean hasUpper = false;
        boolean hasSpecial = false;
        
        // Checks if password length meets the minimum length
        if(inPassword.length() >= REQUIRED_LENGTH){
            maxLength = true;
        }
        
        // Loops through every character in the password to ensure it is a valid password using decimal values
        for (int i = 0; i < inPassword.length(); i++){
            if(inPassword.charAt(i) >= 65 && inPassword.charAt(i) <= 90){
                hasUpper = true;
            } else if ((inPassword.charAt(i) >= 32 && inPassword.charAt(i) <= 47) || (inPassword.charAt(i) >= 58 && inPassword.charAt(i) <= 64) || (inPassword.charAt(i) >= 91 && inPassword.charAt(i) <= 96) || inPassword.charAt(i) >= 123){
                hasSpecial = true;
            }
            
        }
        return maxLength && hasUpper && hasSpecial;
    }

    // Creates an account by adding the username and password to the array
    
    public void signUp() {
        nameValid = !nameValid;
        System.out.println("Enter a new username: ");
        inName = scan.nextLine();
        while(!nameValid){
            if (username.contains(inName)) {
                System.out.println("Username already exists!");
                System.out.println("Enter a valid name: ");
                inName = scan.nextLine();
                nameValid = false;
            } else {
                nameValid = true;
        }
        }
        passValid = !passValid;
        System.out.println("Enter a password: ");
        inPassword = scan.nextLine();
        
        while(!passValid){
            if (!validation()) {
                System.out.println("Password must be at least 9 characters long, contain an uppercase letter, and a special character.");
                System.out.println("Enter a valid password: ");
                inPassword = scan.nextLine();
                passValid = false;
            } else {
                passValid = true;
            }
        }
        if (passValid && nameValid){
            username.add(inName);
            password.add(inPassword);
            System.out.println("Account successfully created!"); 
        }
    }
    
    // login method
    public void login() {
        System.out.println("Enter username: ");
        inName = scan.nextLine();
        System.out.println("Enter password: ");
        inPassword = scan.nextLine();

        if (matches()) {
            System.out.println("Login successful!");
        } else {
            System.out.println("Invalid username or password.");
        }
    }
    
    // Display menu
    @Override
    void displayMenu() {
        System.out.println("==============");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Continue as Guest");
        System.out.println("4. Exit");
        System.out.println("==============");
        System.out.print("Choose (1 - 4): ");
    }
    @Override
    void start(){
        // Adds the password and username stated in the rubric
        username.add("Employee");
        password.add("password");
        username.add("Manager");
        password.add("password");

        CustomerViewState cView = new CustomerViewState();
        EmployeeViewState eView = new EmployeeViewState();
        ManagerViewState mView = new ManagerViewState();
        while (isRunning) {
            displayMenu();
            option = scan.nextLine();
            switch (option) {
                case "1":
                    login();
                    if(matches()){
                        if(isManager){
                            // Transition goes here
                        } else if (isEmployee){
                            // Transition goes here
                        } else {
                            // Transition goes here
                        }
                    }   break;
                case "2":
                    signUp();
                    break;
                case "3":
                    System.out.println("Continuing as a guest");
                    cView.start();
                    break;
                case "4":
                    System.out.println("Exiting program ");
                    isRunning = false;                    
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }
    // Getter
    public boolean getIsManager(){
        return this.isManager;
    }
}
