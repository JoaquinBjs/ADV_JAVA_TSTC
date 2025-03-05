package joaquinbarreram2;

public class Home extends Lodging {
    double cost = 0;
    
    // Default constructor
    public Home (){
        super();
        this.cost = 0;
    }
    
    // Constructor with parameters
    public Home (double inCost, int inNumberOfBedrooms){
        super(inNumberOfBedrooms, 0,0);
        this.cost = inCost;
    }
    
    @Override
    public String toString(){
        return super.toString() + "\nTotal price of the home: " + this.cost;
    }
    
}
