package joaquinbarreram4;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

    class Home extends Lodging {
    double cost = 0;
    
    // Default constructor
    public Home (){
        super();
        this.cost = 0;
    }
    
    // Constructor with parameters
    public Home (String inName, double inCost, int inNumberOfBedrooms){
        super(inNumberOfBedrooms, inCost, 0, inName);
        this.cost = inCost;
    }
    
    // Adds a home to the txt file
    @Override
    public void registerLodging() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src\\joaquinbarreram4\\lodgingInfo.txt", true));
            writer.write(String.format("\n%s,%.2f,%d", name, cost, numberOfBedrooms));
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Override
    public void displayDetails(){
        System.out.println("House:");
        System.out.println("Name: " + this.name);
        System.out.println("Cost: " + this.cost);        
        System.out.println("Number of Bedrooms: " + this.numberOfBedrooms);
    }
    @Override
    public String getDetailsString() {
        return String.format("Home: %s\nBedrooms: %d\nCost: $%.2f",
            name, numberOfBedrooms, cost);
    }
}