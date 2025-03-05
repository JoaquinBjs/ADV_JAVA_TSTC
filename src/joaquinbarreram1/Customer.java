package joaquinbarreram1;

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
    public String toString(){
        return super.toString() + "\nBalance Owed: $" + String.format("%.2f", this.balanceOwed);
    }
}
