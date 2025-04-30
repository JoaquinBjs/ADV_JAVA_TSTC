package joaquinbarreram5;


import java.util.*;
import java.util.GregorianCalendar;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;

public class CustomerViewState extends ViewState {
    // Customer components
    private java.util.Map<Lodging, Integer> lodgeIdMap = new java.util.HashMap<>();
    private JPanel panel;
    private JComboBox<String> lodgeList;
    private JetSettersTextArea lodgeDetails;
    private JComboBox<String> startMonth, startDay, startYear;
    private JComboBox<String> endMonth, endDay, endYear;
    private JLabel thumbnailLabel;
    private JetSettersButton reportBtn = new JetSettersButton("Make Report");
    private JPanel thumbnailPanel;
    private JScrollPane thumbnailScroll;
    private JetSettersButton confirmBtn = new JetSettersButton("Confirm Order");
    private JetSettersButton cancelBtn = new JetSettersButton("Cancel Order");;
//    private java.util.List<ImageIcon> images = new ArrayList<>();
    // Objects
    Customer currCustomer = new Customer();
    LoginState lState;
    OrderDetails order = null;
    
    // Variables
    boolean isRunning = true;
    int numOfNights = 0;
    
    // Constructor to hold the current logged in customer, to be used if I find a use for it
    public CustomerViewState(Customer inCustomer, LoginState inLState) {
        load();
        this.currCustomer = inCustomer;
        this.lState = inLState;
        // Main panel with GridBagLayout
        panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(19, 46, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);


        // Title label
        JLabel titleLabel = new JLabel("Customer View");
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 30));
        titleLabel.setForeground(new Color(249, 249, 255));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;  // Only span 1 column
        titleLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, gbc);
        

        // Content panel using BorderLayout
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBackground(new Color(45, 81, 78));
        contentPane.setPreferredSize(new Dimension(885, 485));
        contentPane.setBorder(BorderFactory.createLineBorder(new Color(39, 69, 67), 4, true));
        gbc.gridy++;
        gbc.gridwidth = 10;
        gbc.fill = GridBagConstraints.BOTH;  // Fill both horizontally and vertically
        gbc.weightx = 9.0;
        gbc.weighty = 1.0;

        // Create top bar panel for dropdown
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false); 
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Top Left for report btn
        JPanel leftTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftTop.setOpaque(false);
        leftTop.add(reportBtn);
        topPanel.add(leftTop, BorderLayout.WEST);
        

        // Label and ComboBox
        JLabel lodgeLabel = new JLabel("Select Lodge:");
        lodgeLabel.setForeground(Color.WHITE); 
        lodgeList = new JComboBox<>();
        for (Lodging i : Lodging.allLodgings) {
            lodgeList.addItem(i.name);
        }

        // Add list to top right
        JPanel rightTop = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        rightTop.setOpaque(false);
        lodgeLabel.setForeground(Color.WHITE);
        rightTop.add(lodgeLabel);
        rightTop.add(lodgeList);
        topPanel.add(rightTop, BorderLayout.EAST);        // Add to top of content pane
        
        // Lodge details text area with scroll pane
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        lodgeDetails = new JetSettersTextArea(6, 40);
        JScrollPane scroll = new JScrollPane(lodgeDetails);
        scroll.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        scroll.setOpaque(false);
        
        // Wrap scroll pane to control size
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setOpaque(false);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        leftPanel.setPreferredSize(new Dimension(400, 300));
        leftPanel.add(scroll, BorderLayout.CENTER);
        contentPane.add(scroll, BorderLayout.WEST);
        
        // bottom panel using horizontal 
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        bottomPanel.setOpaque(false);
        
        // left side Dates and Buttons 
        JPanel leftSide = new JPanel(new GridBagLayout());
        leftSide.setOpaque(false);
        GridBagConstraints leftGbc = new GridBagConstraints();
        leftGbc.gridx = 0;
        leftGbc.anchor = GridBagConstraints.WEST;
        leftGbc.insets = new Insets(10, 10, 10, 10);
        leftGbc.fill = GridBagConstraints.HORIZONTAL;
        leftGbc.weightx = 1.0;
        
        contentPane.add(topPanel, BorderLayout.NORTH);

        // From date row
        leftGbc.gridy = 0;
        leftSide.add(createFromDateRow(), leftGbc);
        
        // To date row
        leftGbc.gridy = 1;
        leftSide.add(createToDateRow(), leftGbc);
        
        // Buttons row
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);
        confirmBtn.setVisible(false);
        cancelBtn.setVisible(false);
        buttonPanel.add(confirmBtn);
        buttonPanel.add(cancelBtn);
        
        // Add report event listener
        reportBtn.addActionListener(e -> {
//                System.out.println("REPORT POPPING OFF!");
                makeCustomerReport(inCustomer);
        });
        
        // Inside the constructor where buttons are initialized
        JetSettersButton viewDetailsBtn = new JetSettersButton("View Lodge Details");
        // View Lodge Details button functionality
        viewDetailsBtn.addActionListener(e -> {
            int selectedIndex = lodgeList.getSelectedIndex();
            if (selectedIndex >= 0 && selectedIndex < Lodging.allLodgings.size()) {
                Lodging selectedLodge = Lodging.allLodgings.get(selectedIndex);
                lodgeDetails.setText(selectedLodge.getDetailsString());
                lodgeDetails.setCaretPosition(0);
//                System.out.println("POPPING OFF!");

                addLodgeImage(Lodging.allLodgings.get(selectedIndex));
            } else {
                lodgeDetails.setText("No lodge selected.");
            }
        });
        
        JetSettersButton addToCartBtn = new JetSettersButton("Add to Cart");
        // Add to car funcitonality
        addToCartBtn.addActionListener(e -> {
            // Check if a lodge is selected
            int selectedIndex = lodgeList.getSelectedIndex();
            if (selectedIndex >= 0 && selectedIndex < Lodging.allLodgings.size()) {
                // Check if any of the date fields are empty
                if (startYear.getSelectedItem() == null || startMonth.getSelectedItem() == null || startDay.getSelectedItem() == null ||
                    endYear.getSelectedItem() == null || endMonth.getSelectedItem() == null || endDay.getSelectedItem() == null) {
                    lodgeDetails.setText("Please fill in all date fields.");
                    return; // Exit the event handler early if validation fails
                }
        
                // Parse start date
                int sYear = Integer.parseInt((String) startYear.getSelectedItem());
                int sMonth = startMonth.getSelectedIndex() + 1; // Jan = 0
                int sDay = Integer.parseInt((String) startDay.getSelectedItem());
        
                // Parse end date
                int eYear = Integer.parseInt((String) endYear.getSelectedItem());
                int eMonth = endMonth.getSelectedIndex() + 1;
                int eDay = Integer.parseInt((String) endDay.getSelectedItem());
        
                // Ensure the start date is before the end date
                LocalDate startDate = LocalDate.of(sYear, sMonth, sDay);
                LocalDate endDate = LocalDate.of(eYear, eMonth, eDay);
        
                if (startDate.isAfter(endDate)) {
                    lodgeDetails.setText("Start date cannot be after end date.");
                    return; // Exit the event handler if the dates are invalid
                } else if (startDate.equals(endDate)){
                    lodgeDetails.setText("Start date cannot be equal to end date.");
                    return;
                }
                Lodging selectedLodge = Lodging.allLodgings.get(selectedIndex);
                long nights = ChronoUnit.DAYS.between(startDate,endDate);
                double totalCost = selectedLodge.basePricePerNight * nights;
                // Proceed with purchasing the lodge
//                Lodging selectedLodge = Lodging.allLodgings.get(selectedIndex);
//                System.out.println(String.format("Purchased Lodge:\n %s |\n From date: %s |\n To date: %s | \n INDEX: %s", selectedLodge.getDetailsString(), startDate, endDate, selectedIndex));
                order = new OrderDetails(selectedLodge, startDate, endDate, totalCost);
                lodgeDetails.setText("Added to Cart!");
                // now reveal Confirm / Cancel
                confirmBtn.setVisible(true);
                cancelBtn .setVisible(true);

                // force Swing to redo layout
                panel.revalidate();
                panel.repaint();
                confirmBtn.addActionListener(a -> {
                    int cid = currCustomer.id;
                    if(cid <= 0 || currCustomer != null){
                        if(order == null){
                            return;
                        }
                        if (ViewState.con != null){
                            try {
                                PreparedStatement prep = ViewState.con.prepareStatement(
                                        "INSERT INTO customerorder(customerId,name,lodgeType,startDate, endDate, totalCost) "
                                                + "VALUES(?,?,?,?,?,?)");
                                prep.setInt(1,currCustomer.id);
                                prep.setString(2, order.lodge.name);
                                prep.setString(3, lodgeType(order.getLodge()));
                                prep.setDate(4, java.sql.Date.valueOf(startDate));
                                prep.setDate(5, java.sql.Date.valueOf(endDate));
                                prep.setDouble(6, order.getTotalSpending());
                                prep.executeUpdate();
                                lodgeDetails.setText("Order confirmed!");
                                confirmBtn.setVisible(false);
                                cancelBtn .setVisible(false);
                            } catch (SQLException ex) {
                                Logger.getLogger(CustomerViewState.class.getName()).log(Level.SEVERE, null, ex);
                            } finally {
                                order = null;
                                confirmBtn.setVisible(false);
                                cancelBtn.setVisible(false);
                            }
                        }
                    } else {
                        lodgeDetails.setText("Must have acount");
                    }
                });
                cancelBtn.addActionListener(a -> {
                    order = null;
                    lodgeDetails.setText("Order canceled.");
                    confirmBtn.setVisible(false);
                    cancelBtn .setVisible(false);
                });
            } else {
                lodgeDetails.setText("No lodge selected.");
            }
        });

        
        JetSettersButton logoutBtn = new JetSettersButton("Logout");
        logoutBtn.addActionListener(e -> {
            inLState.clearStatus();
            ViewState.showState("Login");
        });
        buttonPanel.add(viewDetailsBtn);
        buttonPanel.add(addToCartBtn);
        buttonPanel.add(logoutBtn);
        leftGbc.gridy = 2;
        leftSide.add(buttonPanel, leftGbc);
        
        // right side Fixed Thumbnail
//        thumbnailLabel = new JLabel("Thumbnail");
//        thumbnailLabel.setPreferredSize(new Dimension(100, 200)); 
//        thumbnailLabel.setOpaque(true);
//        thumbnailLabel.setBackground(Color.LIGHT_GRAY); // debug
        

        thumbnailPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        thumbnailPanel.setOpaque(false);
        thumbnailScroll = new JScrollPane(thumbnailPanel,
                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
        );
        thumbnailScroll.setPreferredSize(new Dimension(300, 100));        

        thumbnailPanel.setOpaque(false);

        
        // Add both to main bottomPanel
        bottomPanel.add(leftSide);
        bottomPanel.add(Box.createHorizontalStrut(20)); // space
        bottomPanel.add(thumbnailScroll);
        
        // Add to SOUTH of contentPane
        contentPane.add(bottomPanel, BorderLayout.SOUTH);

        panel.add(contentPane, gbc);
    }
    // Creates a report on the Customers spending
    private void makeCustomerReport(Customer currCustomer) {
        JLabel status = new JLabel(" ");
        if (currCustomer == null) {
            status.setText("Guests must create an account to view order history.");
            return;
        }
    
        try {
            LocalDate start = LocalDate.parse(
                    startMonth.getSelectedItem() + "/" + startDay.getSelectedItem() + "/" + startYear.getSelectedItem(),
                    java.time.format.DateTimeFormatter.ofPattern("MMM/d/yyyy"));
            LocalDate end = LocalDate.parse(
                    endMonth.getSelectedItem() + "/" + endDay.getSelectedItem() + "/" + endYear.getSelectedItem(),
                    java.time.format.DateTimeFormatter.ofPattern("MMM/d/yyyy"));
        
            StringBuilder rows = new StringBuilder();
            String sql = "SELECT startDate,name, lodgeType, DATEDIFF(endDate, startDate) AS nights, totalCost FROM customerOrder "
                    + "WHERE customerId=? AND startDate BETWEEN ? AND ? ORDER BY startDate";
        
            ViewState.connect();
            if (ViewState.con == null) {
                status.setText("No DB connection.");
                return;
            }
        
            PreparedStatement prep = ViewState.con.prepareStatement(sql);
            prep.setInt(1, currCustomer.id);
            prep.setDate(2, java.sql.Date.valueOf(start));
            prep.setDate(3, java.sql.Date.valueOf(end));
            ResultSet rs = prep.executeQuery();
        
            SimpleDateFormat out = new SimpleDateFormat("MMM/d/yyyy");
            while (rs.next()) {
                rows.append("<tr><td>").append(out.format(rs.getDate("startDate"))).append("</td>")
                        .append("<td>").append(rs.getString("name")).append("</td>")
                        .append("<td>").append(rs.getString("lodgeType")).append("</td>")
                        .append("<td>").append(rs.getInt("nights")).append("</td>")
                        .append("<td>$").append(rs.getBigDecimal("totalCost")).append("</td></tr>");
            }
        
            ReportMaker.generateCustomerReport("Order History for " + currCustomer.name, rows.toString());
            status.setText("Report generated: Check browser.");
        
        } catch (SQLException ex) {
            System.out.println("ERROR GENERATING REPORT:\n" + ex);
            status.setText("Report error: " + ex.getMessage());
        } catch (Exception e) {
            System.out.println("DATE PARSE ERROR:\n" + e);
            status.setText("Date parsing error: " + e.getMessage());
        }
    }
    // Add images method
    private void addLodgeImage(Lodging lodge){
        thumbnailPanel.removeAll();
//        System.out.println(lodgeIdMap.get(lodge));
        
        try {
            if(ViewState.con != null && !ViewState.con.isClosed()){
                Integer lodgeId = lodgeIdMap.get(lodge);
                System.out.println("LODGE ID: " + lodgeId);
                if (lodgeId != null){
                    String table = lodgeTypeTable(lodge);
                    String column = lodgeTypeColumn(lodge);
                    PreparedStatement ps = ViewState.con.prepareStatement(
                            "SELECT image FROM " + table
                                    + " WHERE "
                                    + column + " = ?");
                    ps.setInt(1, lodgeId);                    
                    System.out.println(ps);
//                    ps.setInt(1, lodgeId);
                    int i = 0;
                    ResultSet rs = ps.executeQuery();
                    while(rs.next()){
//                        System.out.println("PRINT OUT: " + i);
                        i++;
                        byte[] bytes = rs.getBytes("image");
                        
                        if(bytes != null && bytes.length > 0){
                            BufferedImage img = ImageIO.read(new ByteArrayInputStream(bytes));
//                            System.out.println("BUFFERED IMAGE \n" + bf);

                            if(img != null){
//                                System.out.println(img);
                                JLabel pic = new JLabel(new ImageIcon(img));
                                thumbnailPanel.add(pic);
//                                System.out.println("test: " + img);
                                
                                thumbnailPanel.revalidate();
                                thumbnailPanel.repaint();

                            } else {
                                System.out.println("NO IMAGE FOUND");
                            }
                        }
                    }
                }


//                System.out.println(lodgeId);
            }
        } catch(SQLException e){
            System.out.println("ERROR LINE 299: \n" + e);
        } catch (IOException ex) {
            Logger.getLogger(CustomerViewState.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    // TypeOf method for sql
    private String lodgeTypeTable (Lodging lodge){
        if(lodge instanceof Hotel){
            return "hotelImg";
        } else if (lodge instanceof Home){
            return "houseImg";
        } else {
            return null;
        }
    }
    private String lodgeTypeColumn (Lodging lodge){
        if(lodge instanceof Hotel){
            return "hotelId";
        } else if (lodge instanceof Home){
            return "houseId";
        } else {
            return null;
        }
    }
    
    // TypeOf method
    private String lodgeType (Lodging lodge){
        if(lodge instanceof Hotel){
            return "Hotel";
        } else if (lodge instanceof Home){
            return "House";
        } else {
            return null;
        }
    }
    
    // create the From date row
    private JPanel createFromDateRow() {
        JPanel fromPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        fromPanel.setOpaque(false);
    
        // Label and ComboBoxes for date selection
        JLabel fromLabel = new JLabel("From: ");
        fromLabel.setFont(new Font("Dialog", Font.BOLD, 14));
        fromLabel.setForeground(new Color(249,249,255));
        fromPanel.add(fromLabel);
    
        JComboBox<String> fromMonthBox = new JComboBox<>(new String[]{
            "Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        });
        fromPanel.add(fromMonthBox);
    
    
        JComboBox<String> fromYearBox = new JComboBox<>(getYears());
        fromPanel.add(fromYearBox);
        
        startMonth = fromMonthBox;
        startYear = fromYearBox;
        startDay = new JComboBox<>();
        
        updateDays(startMonth, startYear, startDay);
        fromPanel.add(startDay);
        
        startMonth.addActionListener(e -> 
                updateDays(startMonth, startYear, startDay)
        );
        startYear.addActionListener(e -> 
                updateDays(startMonth, startYear, startDay)
        );
        return fromPanel;
    }    
    
    // create the To date row
    private JPanel createToDateRow() {
        JLabel toLabel = new JLabel("To: ");
        toLabel.setFont(new Font("Dialog", Font.BOLD, 14));
        toLabel.setForeground(new Color(249,249,255));
        
        JPanel toPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 5));
        toPanel.setOpaque(false);
    
        // Label and ComboBoxes for date selection
        toPanel.add(toLabel);
    
        JComboBox<String> toMonthBox = new JComboBox<>(new String[]{
            "Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        });
        
        JComboBox<String> toYearBox = new JComboBox<>(getYears());
        toPanel.add(toMonthBox);
        toPanel.add(toYearBox);        
        endMonth = toMonthBox;
        endYear = toYearBox;
        endDay = new JComboBox<>();
        
        updateDays(endMonth, endYear, endDay);
        toPanel.add(endDay);
        endMonth.addActionListener(e -> 
                updateDays(endMonth, endYear, endDay
                ));
        endYear.addActionListener(e -> 
                updateDays(endMonth, endYear, endDay)
        );

        
        return toPanel;
}
    
    private void updateDays(JComboBox<String> monthBox, JComboBox<String> yearBox, JComboBox<String> dayBox) {
        int month = monthBox.getSelectedIndex() + 1;
        int year = Integer.parseInt((String) yearBox.getSelectedItem());
        GregorianCalendar cal = new GregorianCalendar(year, month - 1, 1);
        int maxDay = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
        dayBox.removeAllItems();
        for (int i = 1; i <= maxDay; i++) {
            dayBox.addItem(String.valueOf(i));
        }
    }

    // Checks for duplicates in my array
    boolean isDuplicate(String name){
        for(int i = 0; i < Lodging.allLodgings.size(); i++){
            if(Lodging.allLodgings.get(i).name.equals(name)){
                return true;
            }
        }
        return false;
    }
    // Returns an array of years starting from the current year
    private String[] getYears() {
        String[] years = new String[6];
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 0; i < 6; i++) {
            years[i] = String.valueOf(thisYear + i);
        }
        return years;
    }
    // enter prompt
    @Override
    void enter() {
        System.out.println("Customer Menu:");
        System.out.println("1. View Lodges With Price Per Night");
        System.out.println("2. View Order");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
    }

    // update view states
    @Override
    void update() {
//        System.out.println("customers array list on signout is: " + lState.customers.size());
    }
    @Override
    public void load() {
        Lodging.allLodgings.clear();
        if (con == null){
            try {
                // Debug message
                System.out.println("Loading available lodgings...");
            
                // CHANGE ONLY THIS LINE - use the correct file path, TO DO
                BufferedReader reader = new BufferedReader(new FileReader("src\\joaquinbarreram5\\lodgingInfo.txt"));
                
                String line;
                
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(",");
                    // Hotel: name,vacancies,bedrooms,basePrice,maxOccupants
                    if (data.length == 5) {
                        String name = data[0].trim();
                        if (!isDuplicate(name)){
                            Lodging.allLodgings.add(new Hotel(
                                data[0].trim(),
                                Integer.parseInt(data[1].trim()),
                                Integer.parseInt(data[2].trim()),
                                Double.parseDouble(data[3].trim()),
                                Integer.parseInt(data[4].trim())
                            ));
//                            System.out.println("Loaded hotel: " + data[0]); // Debug
                        }
                    }
                    // Home: name,cost,bedrooms
                    else if (data.length == 3) {
                        String name = data[0].trim();
                        if (!isDuplicate(name)){
                            Lodging.allLodgings.add(new Home(
                                data[0].trim(),
                                Double.parseDouble(data[1].trim()),
                                Integer.parseInt(data[2].trim())
                            ));
//                        System.out.println("Loaded home: " + data[0]); 
                        }
                    }
                }
                reader.close();
                System.out.println("Lodgings loaded successfully! Total: " + Lodging.allLodgings.size()); 
            } catch (IOException ex) {
                System.out.println("Error loading lodgings: " + ex.getMessage()); 
            }
        } else {
                String queryHm = "SELECT * FROM Hotel";
                try (var statement = con.createStatement()){
                    var resultSet = statement.executeQuery(queryHm);
                    // Fill map
                    while (resultSet.next()) {
                        int id = resultSet.getInt("hotelId");
                        String name = resultSet.getString("name");
                        int vanc = resultSet.getInt("vacancies");
                        int bedNum = resultSet.getInt("bedroomNum");
                        double price = resultSet.getDouble("basePricePerNight");
                        int maxOcc = resultSet.getInt("maxOccupants");
                    
                        Hotel hotel = new Hotel(name, vanc, bedNum, price, maxOcc);
                        Lodging.allLodgings.add(hotel);
                    
                        // fill map here
                        lodgeIdMap.put(hotel,id);
                    }
                    while (resultSet.next()){
                        String name = resultSet.getString("name");
                        int vanc = resultSet.getInt("vacancies");
                        int bedNum = resultSet.getInt("bedroomNum");
                        double basePrice = resultSet.getDouble("basePricePerNight");
                        int maxOcc = resultSet.getInt("maxOccupants");
                        Hotel hotel = new Hotel(name,vanc,bedNum,basePrice,maxOcc);
                        Lodging.allLodgings.add(hotel);
                    }
                } catch (SQLException ex) {
                Logger.getLogger(CustomerViewState.class.getName()).log(Level.SEVERE, null, ex);
            }
                String queryHs = "SELECT * FROM houses";
                try (var statement = con.createStatement()){
                    var resultSet = statement.executeQuery(queryHs);
                    while (resultSet.next()) {
                        int id = resultSet.getInt("houseId");
                        String name = resultSet.getString("name");
                        double cost = resultSet.getDouble("cost");
                        int beds = resultSet.getInt("bedroomNum");
                    
                        Home home = new Home(name, cost, beds);
                        Lodging.allLodgings.add(home);
                    
                        lodgeIdMap.put(home,id);
                    }
                    while (resultSet.next()){
                        String name = resultSet.getString("name");
                        Double cost = resultSet.getDouble("cost");
                        int bedroomNum = resultSet.getInt("bedroomNum");
                        Home home = new Home(name,cost,bedroomNum);
                        Lodging.allLodgings.add(home);
                    }
                } catch (SQLException ex) {
                Logger.getLogger(CustomerViewState.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    @Override
    public void save() { 
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src\\joaquinbarreram5\\lodgingInfo.txt"));
            
            for (int i = 0; i < Lodging.allLodgings.size(); i++) {
                Lodging lodge = Lodging.allLodgings.get(i);
                
                // If the lodging is a hotel then the hotel will be written to the txt file if not then it will be the home
                if (lodge instanceof Hotel) {
                    Hotel hotel = (Hotel) lodge; // Convert lodge to hotel
                    writer.write(String.format("%s,%d,%d,%.2f,%d",
                        hotel.name,
                        hotel.vacancies,
                        hotel.numberOfBedrooms,
                        hotel.basePricePerNight,
                        hotel.maxOccupants
                    ));
                } else if (lodge instanceof Home) {
                    Home home = (Home) lodge; // convert to house
                    writer.write(String.format("%s,%.2f,%d",
                        home.name,
                        home.cost,
                        home.numberOfBedrooms
                    ));
                }
                
                // Add newline after first line
                if (i < Lodging.allLodgings.size() - 1) {
                    writer.write("\n");
                }
            }
            writer.close();
            System.out.println("Lodgings saved successfully!"); 
        } catch (IOException ex) {
//            System.out.println("Error saving lodgings: " + ex.getMessage()); // Debug
        }
    }
    @Override
    public JPanel getPanel(){
        return panel;
    }
}