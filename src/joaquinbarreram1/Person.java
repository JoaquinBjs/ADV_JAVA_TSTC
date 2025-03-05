package joaquinbarreram1;

public class Person {
    String name = null, address = null;
    int id = 0;

    // Defualt constructor
    public Person (){
        this.name = "unknown";
        this.address = "unknown";
        this.id = 0;
    }
    
    // 2nd Constructor
    public Person (String inName, String inAddress, int inId){
        this.name = inName;
        this.address = inAddress;
        this.id = inId;
    }
    

    
    // Override
    @Override
    public String toString(){
        return "\nname: " + this.name +
               "\naddress: " + this.address +
               "\nid: " + this.id;
    }
}
