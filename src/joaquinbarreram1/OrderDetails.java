/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package joaquinbarreram1;

/**
 *
 * @author jabar
 */
public class OrderDetails {
    String comments = null;
    double totalSpending;
    
    // Default constructor
    public OrderDetails(){
        this.comments = " ";
        this.totalSpending = 0.00;
    }
    
    // Constructor with parameters
    public OrderDetails(String inComments, double inTotalSpending){
        this.comments = inComments;
        this.totalSpending = inTotalSpending;
    }
    
    // Overide
    @Override
    public String toString(){
        return "\nOrder Details" +
                "\ntotalSpending: " + this.totalSpending +
                "\nComments: " + this.comments;
    }
}
