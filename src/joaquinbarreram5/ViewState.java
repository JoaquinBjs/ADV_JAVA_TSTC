package joaquinbarreram5;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

abstract class ViewState {
    static JFrame frame;
    static JPanel pContainer = new JPanel();;
    static CardLayout cLayout;
    public static Connection con = null;

    // Display menu method
    abstract void enter();
    
    // Start method
    abstract void update();
    
    // Save method
    abstract void save();
    
    // Load method
    abstract void load();
    
   // Creates a JFrame ", sets its size to 1080x720 pixels, and puts basic window stuff.
    public static void initializeFrame() {
        frame = new JFrame("JetSetters");
        frame.setSize(1080, 720);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        cLayout = new CardLayout();
        pContainer = new JPanel(cLayout);
        frame.add(pContainer);

        frame.setVisible(true);
    }
    
    
    // Adds a new view state to the card layout container.

    public static void addState(String name, ViewState state) {
        pContainer.add(state.getPanel(), name);
    }
    
    // Gets the panel with the view state.
    public abstract JPanel getPanel();
    
    // Displays the view state in the card layout.
    public static void showState(String name) {
        cLayout.show(pContainer, name);
        
    }
    // M5
    // conenct method
    public static void connect (){
        String username = "root";
        String password = "student";
        if (con == null) {
            try {
                String url = "jdbc:mysql://127.0.0.1:3306/jetsetters";
                con = DriverManager.getConnection(url, username, password);
                Class.forName("com.mysql.cj.jdbc.Driver");
                System.out.println("Database Connected Sucessfully!");
            } catch (SQLException e) {
                System.out.println("DB down, switching to file I/O.\n" + e.getMessage());
                con = null;
            } catch (ClassNotFoundException ex) {
                System.out.println("Database unavailable: " + ex.getMessage());
            }
        }
    }
    
    // close connection method
    public static void closeConnection() throws SQLException {
        if (con != null) {
            con.close();
            con = null;
        }
    }
    
    // save lodge to database
    public static void saveLodge(Lodging inLodge) {
        try {
            // If the lodging is a hotel then the hotel will be written to the database if not then it will be the home
            if (inLodge instanceof Hotel) {
                Hotel hotel = (Hotel) inLodge; // Convert lodge to hotel
                String sql = "INSERT INTO Hotel(name,vacancies,bedroomNum,basePricePerNight,maxOccupants) "
                        + "VALUES(?,?,?,?,?) "
                        + "ON DUPLICATE KEY UPDATE "
                        + "vacancies = VALUES(vacancies), "
                        + "bedroomNum = VALUES(bedroomNum), "
                        + "basePricePerNight = VALUES(basePricePerNight), "
                        + "maxOccupants = VALUES(maxOccupants)";
                try (PreparedStatement ps = ViewState.con.prepareStatement(sql)){
                    ps.setString(1, hotel.name);
                    ps.setInt(2, hotel.vacancies);
                    ps.setInt(3, hotel.numberOfBedrooms);
                    ps.setDouble(4, hotel.basePricePerNight);
                    ps.setInt(5, hotel.maxOccupants);
                    ps.executeUpdate();
                }
            } else if (inLodge instanceof Home) {
                Home home = (Home) inLodge; // convert to house
                String sql = "INSERT INTO Houses(name, cost, bedroomNum) "
                        + "VALUES(?,?,?) "
                        + "ON DUPLICATE KEY UPDATE "
                        + "cost = VALUES(cost), "
                        + "bedroomNum = (bedRoomNum)";
                try(PreparedStatement ps = ViewState.con.prepareStatement(sql)){
                    ps.setString(1, home.name);
                    ps.setDouble(2, home.cost);
                    ps.setInt(3, home.numberOfBedrooms);
                    ps.executeUpdate();
                }
            } else {
                System.out.println("Niether is valid");
            }
                backupToFile();
        } catch (SQLException e){
            System.out.println("ERROR IN SAVELODGE METHOD: " + e);
        }
    }
    
    // Back up file
    public static void backupToFile() {
        try (BufferedWriter writer = new BufferedWriter(
                 new FileWriter("src/joaquinbarreram5/lodgingInfo.txt"))) {
            for (Lodging lodge : Lodging.allLodgings) {
                if (lodge instanceof Hotel) {
                    Hotel h = (Hotel) lodge;
                    writer.write(String.format("%s,%d,%d,%.2f,%d\n",
                        h.name, h.vacancies, h.numberOfBedrooms, h.basePricePerNight, h.maxOccupants));
                } else if (lodge instanceof Home) {
                    Home ho = (Home) lodge;
                    writer.write(String.format("%s,%.2f,%d\n",
                        ho.name, ho.cost, ho.numberOfBedrooms));
                }
            }
        } catch (IOException ex) {
            System.err.println("LINE 142 Failed to backup lodgings.txt: " + ex.getMessage());
        }
    }
    // View lodgings
    public static void viewLodgings() throws SQLException{
        String query = "SELECT * FROM Hotel";
        try (var statement = con.createStatement()){
            var resultSet = statement.executeQuery(query);
            while (resultSet.next()){
//                System.out.println(resultSet.getString("name"));
            
            }
        }
    }
    
    // Add order
    public static void addOrder(){
    
    }
}
