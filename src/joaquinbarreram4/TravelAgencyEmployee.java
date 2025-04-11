package joaquinbarreram4;

public class TravelAgencyEmployee extends Person {
    boolean isAManager;
    double salary;
    String workNumber = null;
    private String loginName = null;
    private String password = null;    
    
   // Defualt constructor
    public TravelAgencyEmployee (){
        this.isAManager = false;
        this.salary = 0.00;
        this.workNumber = "unknown";
        this.password = "unknown";
        this.loginName = "unknown";    
    }
    
    // 2nd Constructor
    public TravelAgencyEmployee (String inLoginName, String inPassword, int inId, boolean inIsAManager, double inSalary, String inWorkNumber,String inName, String inAddress){
        super(inName, inAddress, inId);
        this.isAManager = inIsAManager;
        this.salary = inSalary;
        this.workNumber = inWorkNumber;
        this.password = inPassword;
        this.loginName = inLoginName;        
    }
 
    // Getters and setters
    
    public String getLoginName() {
        return loginName;
    }
    
    public void setLoginName(String inLoginName) {
        this.loginName = inLoginName;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String inPassword) {
        this.password = inPassword;
    }        
    
    // Override
    @Override 
    public void displayDetails(){
        System.out.println("Employee");
        System.out.println("Name: " + this.name);
        System.out.println("Address: " + this.address);
        System.out.println("Id: " + this.id);
        System.out.println("Is A Manager: " + this.isAManager);
        System.out.println("Salary: $" + String.format("%.2f", this.salary));
        System.out.println("Work Number: " + this.workNumber);
        System.out.println("Login name: " + this.loginName);
        System.out.println("Password: " + this.password);
    }

}
