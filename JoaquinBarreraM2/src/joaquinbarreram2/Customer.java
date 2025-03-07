package joaquinbarreram2;

public class Customer extends Person {
    double balanceOwed;
    
    // Defualt constructor
    public Customer (){
        this.balanceOwed = 0.00;
    }
    
    // 2nd Constructor
    public Customer (double inBalanceOwed, String inName, String inAddress, int inId){
        super(inName, inAddress, inId);
        this.balanceOwed = inBalanceOwed;
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
