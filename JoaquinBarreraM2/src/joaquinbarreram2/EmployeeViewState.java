package joaquinbarreram2;
import java.util.Scanner;

public class EmployeeViewState extends ViewState{
    Scanner sc = new Scanner (System.in);
    private boolean isRunning = true;
    String option = null;
    LoginState lState= new LoginState();
    
    @Override
    void displayMenu() {
        System.out.println("====================");
        System.out.println("1. Add Lodge");
        System.out.println("2. Remove Lodge");
        System.out.println("3. List Lodges");
        System.out.println("4. Log out");
        if (lState.getIsManager()){
            System.out.println("5. Manager View");
        }
        System.out.println("====================");
    }

    @Override
    void start() {
        while(isRunning){
            displayMenu();
            if (!lState.getIsManager()){
                System.out.println("Select an option (1-4): ");
                option = sc.nextLine();
                switch (option) {
                    case "1":
                        System.out.println("1. Add Lodge");
                        break;
                    case "2":
                        System.out.println("2. Remove Lodge");
                        break;
                    case "3":
                        System.out.println("3. List Lodges");
                        break;
                    case "4":                
                        System.out.println("4. Log Out");
                        break;
                    default:
                        System.out.println("Enter a valid option(1-4): ");
                        option = sc.nextLine();
                        break;
                }
            } else {
                System.out.println("Select an option (1-5): ");
                option = sc.nextLine();
                switch (option) {
                    case "1":
                        System.out.println("1. Add Lodge");
                        break;
                    case "2":
                        System.out.println("2. Remove Lodge");
                        break;
                    case "3":
                        System.out.println("3. List Lodges");
                        break;
                    case "4":
                        System.out.println("4. Log Out");
                        break;
                    case "5":
                        System.out.println("6. Manager View");
                        break;
                    default:
                        System.out.println("Enter a valid option(1-5): ");
                        option = sc.nextLine();
                        break;
                }
            }
        }
    }
}
