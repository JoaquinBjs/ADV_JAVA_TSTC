// Color Palette: https://colordesigner.io/color-palette-builder#132E32-2D514E-516669-98ACAD-F9F9FF
// 132E32 - dark green
// 2D514E - deep teal
// 516669 - muted steel
// 98ACAD - misty blue
// F9F9FF - off white

package joaquinbarreram5;

import java.io.*;
import javax.swing.JFrame;
import java.sql.*;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JoaquinBarreraM5 {
    public static void main(String[] args) {
        ViewState.connect();
        ViewState.initializeFrame();

        Map<String, String[]> hotelImages = Map.of(
            "4 seasons", new String[]{
                "images/lodge1.png",
                "images/lodge2.png",
                "images/lodge3.png",
                "images/lodge4.png",
                "images/lodge5.png",
                "images/lodge6.png",
                "images/lodge7.png"
            },
            "956 Hotel", new String[]{
                "images/lodge8.png",
                "images/lodge9.png",
                "images/lodge10.png"
            }
        );
        Map<String, String[]> houseImages = Map.of(
            "Blue House", new String[]{
                "images/lodge11.png",
                "images/lodge12.png",
                "images/lodge13.png"
            },
            "Mansion", new String[]{
                "images/lodge14.png",
                "images/lodge15.png",
                "images/lodge16.png"
            },
            "Big Home", new String[]{
                "images/lodge17.png",
                "images/lodge18.png",
                "images/lodge19.png"
            }
        );
        
        // HOTEL
        try {
            ViewState.connect();
            String[] hotels = hotelImages.keySet().toArray(new String[0]);
            for (String hotelName : hotels) {
                PreparedStatement sqlId = ViewState.con.prepareStatement(""
                        + "SELECT hotelId FROM hotel WHERE name = ?");
                sqlId.setString(1,hotelName);
                ResultSet rs = sqlId.executeQuery();
                if(rs.next()){
                    int hotelId = rs.getInt("hotelId");
                    for (String imgPath : hotelImages.get(hotelName)) {
                        File imgFile = new File(imgPath);
                        if (!imgFile.exists()) {
                            System.out.println("Debug: file not found " + imgFile.getAbsolutePath());
                        }
                        try (FileInputStream fis = new FileInputStream(imgFile);
                             PreparedStatement ps = ViewState.con.prepareStatement(
                                     "INSERT INTO hotelImg(image,hotelId) VALUES(?,?)")) {
                            ps.setBinaryStream(1, fis, (int) imgFile.length());
                            ps.setInt(2, hotelId);
                            ps.executeUpdate();
                            System.out.println("Debug: uploaded " + imgPath);
                        } catch (IOException ex) {
                            Logger.getLogger(JoaquinBarreraM5.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        } catch (SQLException e){
            System.out.println("\n" + e);
        }
        // HOUSE
        try {
            ViewState.connect();
            String[] house = houseImages.keySet().toArray(new String[0]);
            for (String houseName : house) {
                PreparedStatement sqlId = ViewState.con.prepareStatement(""
                        + "SELECT houseId FROM houses WHERE name = ?");
                sqlId.setString(1,houseName);
                ResultSet rs = sqlId.executeQuery();
                if(rs.next()){
                    int houseId = rs.getInt("houseId");
                    for (String imgPath : houseImages.get(houseName)) {
                        File imgFile = new File(imgPath);
                        if (!imgFile.exists()) {
                            System.out.println("Debug: file not found " + imgFile.getAbsolutePath());
                        }
                        try (FileInputStream fis = new FileInputStream(imgFile);
                             PreparedStatement ps = ViewState.con.prepareStatement(
                                     "INSERT INTO houseImg(image,houseId) VALUES(?,?)")) {
                            ps.setBinaryStream(1, fis, (int) imgFile.length());
                            ps.setInt(2, houseId);
                            ps.executeUpdate();
                            System.out.println("Debug: uploaded " + imgPath);
                        } catch (IOException ex) {
                            Logger.getLogger(JoaquinBarreraM5.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        } catch (SQLException e){
            System.out.println("\n" + e);
        }
//      
//        JFrame testFrame = new JFrame("Test ADD LODGE PANEL View");
//        testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        LoginState lS = new LoginState();
//        EmployeeViewState cView = new EmployeeViewState(null,null);
//        testFrame.add(cView.displayLodges());
//        testFrame.pack();
//        testFrame.setLocationRelativeTo(null);
//        testFrame.setVisible(true);
//
        // Initialize all states
        LoginState loginState = new LoginState();
        CustomerViewState customerState = new CustomerViewState(new Customer(), loginState);
        EmployeeViewState employeeState = new EmployeeViewState(null, null);
        
        // Register states
        ViewState.addState("Login", loginState);
        ViewState.addState("CustomerView", customerState);
        ViewState.addState("EmployeeView", employeeState);
        
        // Start with login screen
        ViewState.showState("Login");
    }
}
