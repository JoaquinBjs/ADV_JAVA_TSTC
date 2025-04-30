package joaquinbarreram5;

import java.time.*;

public class OrderDetails {
    Lodging lodge;
    LocalDate startDate;
    LocalDate endDate;
    double totalSpending;
    
    // Default constructor
    public OrderDetails(){
        this.lodge = null;
        this.totalSpending = 0.00;
    }
    
    // Constructor with parameters
    public OrderDetails(Lodging lodge, LocalDate instartDate, LocalDate inendDate, double inTotalSpending){
        this.lodge = lodge;
        this.totalSpending = inTotalSpending;
        this.startDate = instartDate;
        this.endDate = inendDate;
    }
    
    // Getters
    public Lodging getLodge(){
        return lodge;
    }
    
    public double getTotalSpending(){
        return totalSpending;
    }
    
    // Override
    @Override
    public String toString(){
        return String.format(
                "Order Details\nLodge: %s\nFrom: %s\nTo: %s\nTotal: $%.2f",
                lodge.getDetailsString(),
                startDate, endDate,
                totalSpending
        );
    }
}
