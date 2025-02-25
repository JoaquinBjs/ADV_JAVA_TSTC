/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package joaquinbarreram1;

/**
 *
 * @author jabar
 */
public class Person {
    String name = null, phoneNumber = null, address = null;
    int id = 0;
    private String loginName = null;
    private String password = null;
    // Defualt constructor
    public Person (){
        this.name = "unknown";
        this.address = "unknown";
        this.phoneNumber = "unknown";
        this.id = 0;
        this.password = "unknown";
        this.loginName = "unknown";
    }
    
    // 2nd Constructor
    public Person (String inName, String inAddress, String inPhoneNumber, int inId, String inPassword, String inLoginName){
        this.name = inName;
        this.address = inAddress;
        this.phoneNumber = inPhoneNumber;
        this.id = inId;
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
        return "\nname: " + this.name +
               "\naddress: " + this.address +
               "\nphone number: " + this.phoneNumber +
               "\nid: " + this.id +
               "\nlogin name: " + this.loginName +
               "\npassword: " + this.password;
    }
}
