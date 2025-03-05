package joaquinbarreram2;

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
    public TravelAgencyEmployee (boolean inIsAManager, double inSalary, String inWorkNumber,String inName, String inAddress, int inId, String inPassword, String inLoginName){
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
    public String toString(){
        return super.toString() + "\nIs A Manager: " + this.isAManager +
               "\nSalary: $" + String.format("%.2f", this.salary) +
               "\nWork Number: " + this.workNumber +
               "\nlogin name: " + this.loginName +
               "\npassword: " + this.password;
    }
}
