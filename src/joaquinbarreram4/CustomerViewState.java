package joaquinbarreram4;


import java.util.*;
import java.util.GregorianCalendar;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.*;
import java.time.temporal.ChronoUnit;
import javax.swing.*;

public class CustomerViewState extends ViewState {
    // Customer components
    private JPanel panel;
    private JPanel cCPanel;
    private CardLayout cCLayout;
    private JPanel cViewPanel;
    private JComboBox<String> lodgeList;
    private JetSettersTextArea lodgeDetails;
    private JComboBox<String> startMonth, startDay, startYear;
    private JComboBox<String> endMonth, endDay, endYear;
    private JLabel thumbnailLabel;
    private JPanel thumbnailFrame;
    // Objects
    Scanner sc = new Scanner(System.in);
    Lodging lodgeOrder = null;
    Customer currCustomer = new Customer();
    LoginState lState;
    
    
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
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setOpaque(false); 
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Label and ComboBox
        JLabel lodgeLabel = new JLabel("Select Lodge:");
        lodgeLabel.setForeground(Color.WHITE); 
        lodgeList = new JComboBox<>();
        for (Lodging i : Lodging.allLodgings) {
            lodgeList.addItem(i.name);
        }
        
        topPanel.add(lodgeLabel);
        topPanel.add(lodgeList);
        
        // Add to top of content pane
        contentPane.add(topPanel, BorderLayout.NORTH);
        
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
        
        // From date row
        leftGbc.gridy = 0;
        leftSide.add(createFromDateRow(), leftGbc);
        
        // To date row
        leftGbc.gridy = 1;
        leftSide.add(createToDateRow(), leftGbc);
        
        // Buttons row
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);
        
        // Inside the constructor where buttons are initialized
        JetSettersButton viewDetailsBtn = new JetSettersButton("View Lodge Details");
        // View Lodge Details button functionality
        viewDetailsBtn.addActionListener(e -> {
            int selectedIndex = lodgeList.getSelectedIndex();
            if (selectedIndex >= 0 && selectedIndex < Lodging.allLodgings.size()) {
                Lodging selectedLodge = Lodging.allLodgings.get(selectedIndex);
                lodgeDetails.setText(selectedLodge.getDetailsString());
                lodgeDetails.setCaretPosition(0);
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
                }
        
                // Proceed with purchasing the lodge
                Lodging selectedLodge = Lodging.allLodgings.get(selectedIndex);
        
                System.out.println(String.format("Purchased Lodge:\n %s |\n From date: %s |\n To date: %s | \n INDEX: %s", selectedLodge.getDetailsString(), startDate, endDate, selectedIndex));
        
            } else {
                lodgeDetails.setText("No lodge selected.");
            }
        });

        
        JetSettersButton logoutBtn = new JetSettersButton("Logout");
        logoutBtn.addActionListener(e -> {
            ViewState.showState("Login");
        });
        buttonPanel.add(viewDetailsBtn);
        buttonPanel.add(addToCartBtn);
        buttonPanel.add(logoutBtn);
        leftGbc.gridy = 2;
        leftSide.add(buttonPanel, leftGbc);
        
        // right side Fixed Thumbnail
        thumbnailLabel = new JLabel("Thumbnail");
        thumbnailLabel.setPreferredSize(new Dimension(100, 200)); 
        thumbnailLabel.setOpaque(true);
        thumbnailLabel.setBackground(Color.LIGHT_GRAY); // debug
        
        JPanel thumbnailPanel = new JPanel(new BorderLayout());
        thumbnailPanel.setPreferredSize(new Dimension(100, 200));
//        thumbnailPanel.setMaximumSize(new Dimension(120, 200));
//        thumbnailPanel.setMinimumSize(new Dimension(120, 200));
        thumbnailPanel.setOpaque(false);
        thumbnailPanel.add(thumbnailLabel, BorderLayout.CENTER);
        
        // Add both to main bottomPanel
        bottomPanel.add(leftSide);
        bottomPanel.add(Box.createHorizontalStrut(20)); // space
        bottomPanel.add(thumbnailPanel);
        
        // Add to SOUTH of contentPane
        contentPane.add(bottomPanel, BorderLayout.SOUTH);

        panel.add(contentPane, gbc);
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
        
        JPanel toPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 5))
                ;
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
    
    //  Display Lodgings
    private void viewLodgings() {
        System.out.println("Displaying available lodgings...");
        if (Lodging.allLodgings.isEmpty()) {
            load();
        }
        
        for (int i = 0; i < Lodging.allLodgings.size(); i++) {
            System.out.println((1 + i) + ". " + Lodging.allLodgings.get(i).name + " || price: $" + String.format("%.2f", Lodging.allLodgings.get(i).basePricePerNight));
        }
        
        // Loops forever until a valid option is chosen
        while (true) {
            // Prompts user to select a lodge
            System.out.println("=================================");
            System.out.println("a. Select Lodge");
            System.out.println("b. Return");
            System.out.println("=================================");
            char input = sc.nextLine().charAt(0);
            if (input == 'a' || input == 'A') {
                System.out.println("Select a Lodge: ");
                String lodgeInput = sc.nextLine();
                int lodgeIndex = Integer.parseInt(lodgeInput) - 1;
                if (lodgeIndex >= 0 && lodgeIndex < Lodging.allLodgings.size()) {
                    lodgeOrder = Lodging.allLodgings.get(lodgeIndex);
                    System.out.println("Lodge Selected: " + lodgeOrder.name);
                    System.out.println("Select Number Of Nights: ");
                    numOfNights = Integer.parseInt(sc.nextLine());
                    break;
                } else {
                    System.out.println("Invalid selection");
                }
            } else if (input == 'b' || input == 'B') {
                break;
            } else {
                System.out.println("Select a valid option");
            }
        }
    }

    // Method to book a lodging
    private void bookLodging() {
        if (lodgeOrder == null) {
            System.out.println("No lodging selected");
        } else {
            // Book lodging prompt
            System.out.println("============================");
            System.out.println("Booking a lodging...");
            System.out.println("============================");
            lodgeOrder.displayDetails();
            System.out.println("Number of Nights: " + numOfNights);
            System.out.println("Total Cost: $" + String.format("%.2f", lodgeOrder.basePricePerNight * numOfNights));
            System.out.println("============================");
            System.out.println("a. Confirm");
            System.out.println("b. Cancel");
            System.out.println("c. Exit");
            System.out.println("============================");
            System.out.println("Select an option: ");
            char option = sc.nextLine().charAt(0);
            if (option == 'a' || option == 'A') {
                System.out.println("Order confirmed!");
                lodgeOrder = null;
                numOfNights = 0;
            } else if (option == 'b' || option == 'B') {
                System.out.println("Order canceled");
                lodgeOrder = null;
                numOfNights = 0;
            } else if (option == 'c' || option == 'C') {
                System.out.println("Exited");
            }
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
        while (isRunning) {
            enter();
            String input = sc.nextLine();
            if (input.equals("1")) {
                viewLodgings();
            } else if (input.equals("2")) {
                bookLodging();
            } else if (input.equals("3")) {
                System.out.println("Exiting Customer Menu.");
                break;
            } else {
                System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }
    @Override
    public void load() {
        try {
            // Debug message
            System.out.println("Loading available lodgings...");
        
            // CHANGE ONLY THIS LINE - use the correct file path, TO DO
            BufferedReader reader = new BufferedReader(new FileReader("src\\joaquinbarreram4\\lodgingInfo.txt"));
            
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
//                        System.out.println("Loaded hotel: " + data[0]); // Debug
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
//                    System.out.println("Loaded home: " + data[0]); 
                    }
                }
            }
            reader.close();
            System.out.println("Lodgings loaded successfully! Total: " + Lodging.allLodgings.size()); 
        } catch (IOException ex) {
            System.out.println("Error loading lodgings: " + ex.getMessage()); 
        }
    }
    @Override
    public void save() { 
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src\\joaquinbarreram4\\lodgingInfo.txt"));
            
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