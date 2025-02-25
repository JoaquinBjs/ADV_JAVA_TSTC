/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package joaquinbarreram1;

/**
 *
 * @author jabar
 */
public class Customer extends Person {
    double balanceOwed;
    // Defualt constructor
    public Customer (){
        this.balanceOwed = 0.00;
    }
    
    // 2nd Constructor
    public Customer (double inBalanceOwed, String inName, String inAddress, String inPhoneNumber, int inId, String inPassword, String inLoginName){
        super(inName, inAddress, inPhoneNumber, inId, inPassword, inLoginName);
        this.balanceOwed = inBalanceOwed;
    }
    
    // Override
    @Override
    public String toString(){
        return super.toString() + "\nBalance Owed: $" + String.format("%.2f", this.balanceOwed);
    }
}
