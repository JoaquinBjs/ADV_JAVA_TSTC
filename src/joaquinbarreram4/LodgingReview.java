package joaquinbarreram4;

public class LodgingReview {
    String comments = null;
    String date = null;
    String rating = null;
    
    // Default constructor
    public LodgingReview(){
        this.comments = " ";
        this.date = " ";
        this.rating = " ";
    }
    
    // Constructor with parameters
    public LodgingReview(String inComments, String inDate, String inRating){
        this.comments = inComments;
        this.date = inDate;
        this.rating = inRating;        

    }
    
    // Override 
    @Override
    public String toString(){
        return "\nLodging Review: " + this.comments +
                "\nrating: " + this.rating +
                "\nDate: " + this.date;
        
    }
}