/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package joaquinbarreram1;

/**
 *
 * @author jabar
 */
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
    
    // Overide
    @Override
    public String toString(){
        return "\nLodging Review: ";
    }
}