package joaquinbarreram2;

public class Customer extends Person {
    double balanceOwed;
    private String username = null;
    private String password = null;
    
    // Defualt constructor
    public Customer (){
        this.balanceOwed = 0.00;
        this.username = null;
        this.password = null; 
    }
    
    // 2nd Constructor
    public Customer (double inBalanceOwed, String inName, String inAddress, int inId, String inUsername, String inPass){
        super(inName, inAddress, inId);
        this.balanceOwed = inBalanceOwed;
        this.username = inUsername;
        this.password = inPass;
    }
    
    
    // Getters an setters
    String getUsername (){
        return username;
    }
    String getPassword (){
        return password;
    }
    void setUsername (String inUsername){
        this.username = inUsername;
    }
    void setPassword (String inPassword){
        this.password = inPassword;
    }

    
    // Override
    @Override
    public void displayDetails(){
        System.out.println("Customer");
        System.out.println("Id: " + this.id);
        System.out.println("Name: " + this.name);
        System.out.println("Address: " + this.address);
        System.out.println("Balance owed: $" + String.format("%.2f", this.balanceOwed));
    }
}
