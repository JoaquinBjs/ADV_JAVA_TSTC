/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package joaquinbarreram1;

/**
 *
 * @author jabar
 */
public class TravelAgencyEmployee extends Person {
    boolean isAManager;
    String hireDate = null;
    double salary;
    String workNumber = null;
    // Defualt constructor
    public TravelAgencyEmployee (){
    this.isAManager = false;
    this.hireDate = "unknown";
    this.salary = 0.00;
    this.workNumber = "unknown";
    }
    
    // 2nd Constructor
    public TravelAgencyEmployee (boolean inIsAManager, String inHireDate, double inSalary, String inWorkNumber,String inName, String inAddress, String inPhoneNumber, int inId, String inPassword, String inLoginName){
        super(inName, inAddress, inPhoneNumber, inId, inPassword, inLoginName);
        this.isAManager = inIsAManager;
        this.hireDate = inHireDate;
        this.salary = inSalary;
        this.workNumber = inWorkNumber;
    }
    
    // Override
    @Override
    public String toString(){
        return super.toString() + "\nIs A Manager: " + this.isAManager +
               "\nHire Date: " + this.hireDate +
               "\nSalary: $" + String.format("%.2f", this.salary) +
               "\nWork Number: " + this.workNumber;
    }
}
