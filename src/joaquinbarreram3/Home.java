package joaquinbarreram3;

    class Home extends Lodging {
    double cost = 0;
    
    // Default constructor
    public Home (){
        super();
        this.cost = 0;
    }
    
    // Constructor with parameters
    public Home (double inCost, int inNumberOfBedrooms,String inName){
        super(inNumberOfBedrooms, inCost, 0, inName);
        this.cost = inCost;
    }
    
    @Override
    public void displayDetails(){
        System.out.println("House:");
        System.out.println("Name: " + this.name);
        System.out.println("Cost: " + this.cost);        
        System.out.println("Number of Bedrooms: " + this.numberOfBedrooms);
    }
}