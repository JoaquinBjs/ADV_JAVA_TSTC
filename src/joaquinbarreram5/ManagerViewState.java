package joaquinbarreram5;
import java.util.Scanner;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

class ManagerViewState extends ViewState {
    // Variables    
    // Objects
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
    }
    @Override
    public void load(){
        // Load method is not needed here
    }
    @Override
    public void save() {
        try {
//            System.out.println("MANAGER VIEW STATE IS POPPING OFF!");
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/joaquinbarreram4/employeeAccounts.txt"));
            
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

    @Override
    public JPanel getPanel() {
        // Nothing needed here
        return null;
    }
}