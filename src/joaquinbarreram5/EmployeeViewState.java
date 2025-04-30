package joaquinbarreram5;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;


public final class EmployeeViewState extends ViewState {
    // is running boolean for the update method
    private JetSettersButton reportBtn = new JetSettersButton("Make Report");

    // Employee components
    private Map<String, java.util.List<String>> hotelImages = new HashMap<>();
    private Map<String, java.util.List<String>> houseImages = new HashMap<>();
    private JPanel thumbnailPanel;
    private java.util.Map<Lodging, Integer> lodgeIdMap = new java.util.HashMap<>();
    // house form fields
    private JTextField houseCostField;
    private JTextField houseBedroomsField;
    private JTextField houseNameField;
    // hotel form fields
    private JTextField hotelCostField;
    private JTextField hotelBedroomsField;
    private JTextField hotelVacanciesField;
    private JTextField hotelMaxOccField;
    private JTextField hotelNameField;
    
    private JComboBox<String> lodgeList;
    private JPanel panel;
    private JPanel mainMenuPanel = new JPanel();
    private JPanel addPanel;
    private JPanel removePanel;
    private JPanel listPanel;
    private JPanel editPanel;
    private JetSettersTextArea lodgeDetails = new JetSettersTextArea(0,0);
    private JPanel lcPanel;
    private CardLayout lcLayout = new CardLayout();
    private JetSettersButton uploadImageBtn = new JetSettersButton("Upload Image");
    private JetSettersButton refresh = new JetSettersButton("Refresh");
    JPanel formFieldHotel = hotelEdit();
    JPanel formFieldHouse = houseEdit();
    private JComboBox<String> reportStartMonth, reportStartDay, reportStartYear;
    private JComboBox<String> reportEndMonth, reportEndDay, reportEndYear;
    private JLabel status = new JLabel(" ");
    // Objects
    Scanner sc = new Scanner(System.in);
    TravelAgencyEmployee currEmployee = null;
    LoginState lstate;
    // Constructor to hold the logged in employee
    public EmployeeViewState(TravelAgencyEmployee inEmployee, LoginState inLState){
        this.currEmployee = inEmployee;
        this.lstate = inLState;
        load(); // ensures all existing lodges are loaded in
        houseImages.clear();
        hotelImages.clear();
        uploadImageBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setMultiSelectionEnabled(true);
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int result = chooser.showOpenDialog(panel);
            if (result == JFileChooser.APPROVE_OPTION) {
                String selectedName = (String) lodgeList.getSelectedItem();
                if (selectedName == null) return;
                File[] files = chooser.getSelectedFiles();
                Map<String,java.util.List<String>> target = getCurrentImageMap();
                target.computeIfAbsent(selectedName, k -> new ArrayList<>());
                for (File f : files) target.get(selectedName).add(f.getAbsolutePath());
                // now push them into your DB:
                uploadImages();
            }
        });
        reportStartMonth = new JComboBox<>(new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"});
        reportStartDay = new JComboBox<>(generateDays());
        reportStartYear = new JComboBox<>(generateYears());

        reportEndMonth = new JComboBox<>(new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"});
        reportEndDay = new JComboBox<>(generateDays());
        reportEndYear = new JComboBox<>(generateYears());
        // M4 CODE 
        // Main panel with GridBagLayout
        panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(19, 46, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        

        // Title label
        JLabel titleLabel = new JLabel("Employee View");
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
        panel.add(contentPane, gbc);
        
        // Card layout
        lcPanel = new JPanel(lcLayout);
        lcPanel.setBackground(new Color(45, 81, 78));

        contentPane.add(lcPanel, BorderLayout.CENTER);
//        lcPanel.setPreferredSize(new Dimension(800, 400));
        lcPanel.add(mainMenuPanel, "MAIN");
        mainMenuPanel.setBackground(new Color(45, 81, 78));
        
        // Button Pane
        JPanel buttonPane = new JPanel();
        buttonPane.setBackground(new Color(19, 46, 50));
        
        // Buttons
        JetSettersButton addBtn = new JetSettersButton("Add Lodge");
        JetSettersButton removeBtn = new JetSettersButton("Delete Lodge");
        JetSettersButton listBtn = new JetSettersButton("List Lodge");
        JetSettersButton editBtn = new JetSettersButton("Edit Lodge");
        JetSettersButton logoutBtn = new JetSettersButton("Log Out");
        JetSettersButton exitBtn = new JetSettersButton("Exit");
        Dimension BUTTON_SIZE = new Dimension(140, 40); // width, height
        
        addBtn.setPreferredSize(BUTTON_SIZE);
        removeBtn.setPreferredSize(BUTTON_SIZE);
        listBtn.setPreferredSize(BUTTON_SIZE);
        editBtn.setPreferredSize(BUTTON_SIZE);
        logoutBtn.setPreferredSize(BUTTON_SIZE);
        exitBtn.setPreferredSize(BUTTON_SIZE);

        buttonPane.add(addBtn);
        buttonPane.add(removeBtn);
        buttonPane.add(listBtn);
        buttonPane.add(editBtn);
        buttonPane.add(logoutBtn);
        buttonPane.add(exitBtn);
        buttonPane.add(reportBtn);
        
        // Add functionality
        reportBtn.addActionListener(e -> {
            generateEmployeeReport();
        });
        addBtn.addActionListener(e -> {
        contentPane.setLayout(new BorderLayout());
        addPanel = addLodgePanel();
        lcPanel.add(addPanel, "ADD");
        lcLayout.show(lcPanel, "ADD");
        });
        removeBtn.addActionListener(e -> {
            removePanel = removeLodgePanel();
            lcPanel.add(removePanel, "REMOVE");
            lcLayout.show(lcPanel, "REMOVE");
        });
        listBtn.addActionListener(e ->{
            listPanel = displayLodges();
            lcPanel.add(listPanel, "LIST");
            lcLayout.show(lcPanel, "LIST");
        });
        editBtn.addActionListener(e -> {
            editPanel = makeEditForm();
            lcPanel.add(editPanel, "EDIT");
            lcLayout.show(lcPanel, "EDIT");
        });
        logoutBtn.addActionListener(e -> {
            currEmployee = null;
            inLState.clearStatus();
            ViewState.showState("Login");
        });
        exitBtn.addActionListener(e -> {
            System.exit(0);
        });
       
        contentPane.add(buttonPane, BorderLayout.SOUTH);
    }
    
    // MAKE YEARS AND DAYS METHODS
    private String[] generateDays() {
        String[] days = new String[31];
        for (int i = 0; i < 31; i++) {
            days[i] = String.valueOf(i + 1);
        }
        return days;
    }
    
    private String[] generateYears() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        String[] years = new String[10]; // Example: current year + next 9 years
        for (int i = 0; i < 10; i++) {
            years[i] = String.valueOf(currentYear + i);
        }
        return years;
    }

    // DISPLAY LODGES METHOD
    public JPanel displayLodges(){
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(152, 172, 173));
        panel.setPreferredSize(new Dimension(800, 400));        
        
        // Top pane
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topBar.setOpaque(false);
        JetSettersLabel lodgeLabel = new JetSettersLabel("Select Lodge:");
        lodgeLabel.setForeground(Color.WHITE);
        topBar.add(lodgeLabel);
        
        lodgeList = new JComboBox<>();
        for (Lodging i : Lodging.allLodgings) {
            lodgeList.addItem(i.name);
        }
        topBar.add(lodgeList);
        panel.add(topBar, BorderLayout.NORTH);
        
        // Upload image button
        topBar.add(uploadImageBtn);
        panel.add(topBar, BorderLayout.NORTH);
        
        topBar.add(refresh);
        refresh.addActionListener(e -> {
            // exactly the same as the view lodge button
            int idx = lodgeList.getSelectedIndex();
            if (idx >= 0 && idx < Lodging.allLodgings.size()) {
                Lodging sel = Lodging.allLodgings.get(idx);
                lodgeDetails.setText(sel.getDetailsString());
                lodgeDetails.setCaretPosition(0);
                addLodgeImage(sel);
            } else {
                lodgeDetails.setText("No lodge selected.");
                thumbnailPanel.removeAll();
                thumbnailPanel.revalidate();
                thumbnailPanel.repaint();
            }
        });
        // Center
        JScrollPane detailsScroll = new JScrollPane(lodgeDetails);
        detailsScroll.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        detailsScroll.setOpaque(false);
        panel.add(detailsScroll, BorderLayout.CENTER);
        
        // South
        thumbnailPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        thumbnailPanel.setOpaque(false);
        JScrollPane thumbnailScroll = new JScrollPane (thumbnailPanel,
            JScrollPane.VERTICAL_SCROLLBAR_NEVER,
            JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
        );
        thumbnailScroll.setPreferredSize(new Dimension(0, 150));
        panel.add(thumbnailScroll, BorderLayout.SOUTH);
        
        // Event listener
        lodgeList.addActionListener(e -> {
            int idx = lodgeList.getSelectedIndex();
            if (idx >= 0 && idx < Lodging.allLodgings.size()) {
                Lodging sel = Lodging.allLodgings.get(idx);
                lodgeDetails.setText(sel.getDetailsString());
                lodgeDetails.setCaretPosition(0);
                addLodgeImage(sel);
            } else {
                lodgeDetails.setText("No lodge selected.");
                thumbnailPanel.removeAll();
                thumbnailPanel.revalidate();
                thumbnailPanel.repaint();
            }
        });
        
        return panel;
    }
    
    // MAKE REMPLOYEE REPORT
    private void generateEmployeeReport() {
        try {
            ViewState.connect();
            SimpleDateFormat sdf = new SimpleDateFormat("MMM/dd/yyyy");
            Date start = sdf.parse(reportStartMonth.getSelectedItem() + "/" + reportStartDay.getSelectedItem() + "/" + reportStartYear.getSelectedItem());
            Date end = sdf.parse(reportEndMonth.getSelectedItem() + "/" + reportEndDay.getSelectedItem() + "/" + reportEndYear.getSelectedItem());
        
            StringBuilder rows = new StringBuilder();
            String sql = "SELECT c.customerId, c.name, SUM(o.totalCost) AS total_spent FROM customerOrder AS o JOIN customerAccount AS c ON o.customerId = c.customerId WHERE o.startDate BETWEEN ? AND ? GROUP BY c.customerId, c.name";
        
//            System.out.println("CONNECTION working LINE 297");
            if (ViewState.con == null) {
//                System.out.println("CONNECTION FAILED LINE 300");
                status.setText("Database connection failed.");
                return;
            }
        
            PreparedStatement prep = ViewState.con.prepareStatement(sql);
            prep.setDate(1, new java.sql.Date(start.getTime()));
            prep.setDate(2, new java.sql.Date(end.getTime()));
            ResultSet res = prep.executeQuery();
            
            // For rows
            while (res.next()) {
                rows.append("<tr><td>").append(res.getInt("customerId")).append("</td>")
                    .append("<td>").append(res.getString("name")).append("</td>")
                    .append("<td>$").append(res.getBigDecimal("total_spent")).append("</td></tr>");
            }
            
            ReportMaker.generateEmployeeReport("Customer Spending Report", rows.toString());
            status.setText("Report generated successfully!");
//            
//            System.out.println("START: "  + start.getTime());
//            System.out.println("END: " + end.getTime());
        } catch (Exception ex) {
//            System.out.println("Report generation error: " + ex);
            status.setText("Report error: " + ex.getMessage());
        }
    }
    
    public JPanel removeLodgePanel ()  {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(152, 172, 173));
        panel.setPreferredSize(new Dimension(800, 400));
        
        GridBagConstraints gbc = new GridBagConstraints();
        // Content panel using GBC
        JPanel contentPane = new JPanel(new GridBagLayout());
        contentPane.setBackground(new Color(249, 249, 255));
        contentPane.setPreferredSize(new Dimension(600, 300));
        contentPane.setBorder(BorderFactory.createLineBorder(new Color(249, 249, 255), 4, true));

        // Label and ComboBox
        JLabel lodgeLabel = new JLabel("Select Lodge:");
        lodgeLabel.setForeground(Color.WHITE); 
        lodgeList = new JComboBox<>();
        for (Lodging i : Lodging.allLodgings) {
            lodgeList.addItem(i.name);
        }

        // text field
        JScrollPane scroll = new JScrollPane(lodgeDetails);
        scroll.setBackground(new Color(152,172,173));
       
        scroll.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        scroll.setOpaque(false);
        
        // Display lodging
            lodgeList.addActionListener(e -> {
            int selectedIndex = lodgeList.getSelectedIndex();
            if (selectedIndex >= 0 && selectedIndex < Lodging.allLodgings.size()) {
                Lodging selectedLodge = Lodging.allLodgings.get(selectedIndex);
                lodgeDetails.setText(selectedLodge.getDetailsString());
                lodgeDetails.setCaretPosition(0);
            } else {
                lodgeDetails.setText("No lodge selected.");
            }
        });
        
        // Buttons
        JetSettersButton quit = new JetSettersButton("Quit");
        JetSettersButton delete = new JetSettersButton("Delete");

        // For lodgeLabel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 2;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contentPane.add(lodgeLabel, gbc);
        
        // For drop box
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        contentPane.add(lodgeList, gbc);
        
        // For text field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        contentPane.add(scroll, gbc);
        
        // For button
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setOpaque(false);
        // Add buttons to panel
        buttonPanel.add(quit, BorderLayout.SOUTH);
        buttonPanel.add(delete, BorderLayout.NORTH);
        // Use GBC to position buttons to south
        gbc.gridy = 1;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(buttonPanel, gbc);
        
        // BUTTON FUNCTIONALITY
        quit.addActionListener(e ->{
//            System.out.println("POPPING OFF!");
            lcLayout.show(lcPanel, "MAIN");
            mainMenuPanel.setBackground(new Color(45, 81, 78));

        });
        delete.addActionListener(e ->{
//            System.out.println("POPPING OFF!");
            removeLodge();
            lodgeList.removeAllItems(); 
            for (Lodging i : Lodging.allLodgings) {
                System.out.println("ADDING: " + i.name);
                lodgeList.addItem(i.name);
            }
            lodgeDetails.setText("");
        });
        
        // Use GBC on panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(contentPane, gbc);
        
        return panel;
    }
    public JPanel addLodgePanel(){
        System.out.println("ADD LODGE PANEL POPPING OFF!");
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(45,81,78));
        
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Content panel using BorderLayout
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBackground(new Color(249, 249, 255));
        contentPane.setBorder(BorderFactory.createLineBorder(new Color(249, 249, 255), 4, true));
        
        // Prompt for lodge type
        JetSettersLabel selectLabel = new JetSettersLabel("Select Lodge Type: ");
        selectLabel.setForeground(new Color(37,68,65));
        
        // Radio buttons for home and hotel
        JRadioButton homeBtn = new JRadioButton ("House");
        JRadioButton hotelBtn = new JRadioButton ("Hotel");  
        homeBtn.setOpaque(true);
        homeBtn.setBackground(Color.white);
        homeBtn.setForeground(Color.black);
        hotelBtn.setOpaque(true);
        hotelBtn.setBackground(Color.white);
        hotelBtn.setForeground(Color.black);
        
        // Top panel
        JPanel topPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPane.add(selectLabel);
        contentPane.add(topPane, BorderLayout.WEST);
        
        // group radio buttons
        ButtonGroup lodgeGroupBtn = new ButtonGroup();
        lodgeGroupBtn.add(homeBtn);
        lodgeGroupBtn.add(hotelBtn);
        JPanel lodgeGroup = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lodgeGroup.setBackground(new Color(249, 249, 255));
        lodgeGroup.add(homeBtn);
        lodgeGroup.add(hotelBtn);
        contentPane.add(lodgeGroup, BorderLayout.EAST);
        
        // Bottom Panel
        JPanel bottomPane = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPane.setBackground(new Color(255,0,255,1)); // Set background to false, Src: https://stackoverflow.com/questions/1801580/what-is-the-hex-code-for-transparent-color
        
        // Exit button
        JetSettersButton exitBtn = new JetSettersButton("Exit");
        bottomPane.add(exitBtn);
        
        // Add bottom pane to content pane
        contentPane.add(bottomPane, BorderLayout.SOUTH);
        
        // House and Hotel card
        JPanel formCards = new JPanel(new CardLayout());
        formCards.add(makeHouseForm(), "HOUSE");
        formCards.add(makeHotelForm(), "HOTEL");                                                                     
        contentPane.add(formCards, BorderLayout.CENTER);
        
        // Functionality to buttons
        homeBtn.addActionListener(e -> {
            CardLayout cl = (CardLayout)formCards.getLayout();
            cl.show(formCards, "HOUSE");
//            houseInputPanel.setVisible(true);
//            hotelInputPanel.setVisible(false);
        });
        
        hotelBtn.addActionListener(e -> {
            CardLayout cl = (CardLayout)formCards.getLayout();
            cl.show(formCards, "HOTEL");
//            hotelInputPanel.setVisible(true);            
//            houseInputPanel.setVisible(false);
        });
        exitBtn.addActionListener(e -> {
            lcLayout.show(lcPanel, "MAIN");
            mainMenuPanel.setBackground(new Color(45, 81, 78));
        });

        // Main panel and content panel
        contentPane.setPreferredSize(new Dimension(885, 485));        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
//        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(contentPane, gbc);
        return panel;
    }
    public JPanel makeHouseForm(){
        // House Panel
        JPanel houseInputPanel = new JPanel(new GridBagLayout());
        houseInputPanel.setBackground(new Color(249, 249, 255));
        houseInputPanel.setVisible(false); // Hidden by default
        
        GridBagConstraints hgbc = new GridBagConstraints();
        hgbc.insets = new Insets(10, 10, 10, 10);
        hgbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Form Fields Panel
        JPanel formFields = new JPanel(new GridBagLayout());
        formFields.setOpaque(false); // Transparent background
        GridBagConstraints innerGbc = new GridBagConstraints();
        innerGbc.insets = new Insets(15, 15, 15, 15);
        innerGbc.fill = GridBagConstraints.HORIZONTAL;
        innerGbc.weightx = 1.0;
        
        // Cost Field
        JetSettersLabel costLabel = new JetSettersLabel("Cost: ");
        costLabel.setFont(new Font("Dialog", Font.BOLD, 20));
        costLabel.setForeground(new Color(37, 68, 65));
        innerGbc.gridx = 0;
        innerGbc.gridy = 0;
        innerGbc.weightx = 0;
        formFields.add(costLabel, innerGbc);
        
        JTextField costField = new JTextField(40);
        costField.setMinimumSize(costField.getPreferredSize()); 
        innerGbc.gridx = 1;
        innerGbc.weightx = 1;
        formFields.add(costField, innerGbc);
        
        // Bedrooms Field
        JetSettersLabel bedroomsLabel = new JetSettersLabel("Number of Bedrooms: ");
        bedroomsLabel.setFont(new Font("Dialog", Font.BOLD, 20));
        bedroomsLabel.setForeground(new Color(37, 68, 65));
        innerGbc.gridx = 0;
        innerGbc.gridy = 1;
        innerGbc.weightx = 0;
        formFields.add(bedroomsLabel, innerGbc);
        
        JTextField bedroomsField = new JTextField(40);
        bedroomsField.setMinimumSize(bedroomsField.getPreferredSize()); // Locks textbox size
        innerGbc.gridx = 1;
        innerGbc.weightx = 1;
        formFields.add(bedroomsField, innerGbc);
        
        // Name Field
        JetSettersLabel nameLabel = new JetSettersLabel("Name: ");
        nameLabel.setFont(new Font("Dialog", Font.BOLD, 20));
        nameLabel.setForeground(new Color(37, 68, 65));
        innerGbc.gridx = 0;
        innerGbc.gridy = 2;
        innerGbc.weightx = 0;
        formFields.add(nameLabel, innerGbc);
        
        JTextField nameField = new JTextField(40);
        nameField.setMinimumSize(nameField.getPreferredSize());
        innerGbc.gridx = 1;
        innerGbc.weightx = 1;
        formFields.add(nameField, innerGbc);
        
        // Status Label
        JLabel statusLabel = new JLabel(" ");
        statusLabel.setForeground(Color.RED);
        houseInputPanel.add(statusLabel);
        
        // Submit button
        JetSettersButton submitBtn = new JetSettersButton("Submit");
        innerGbc.gridx = 0;
        innerGbc.gridy = 3;
        innerGbc.gridwidth = 2;
        formFields.add(submitBtn, innerGbc);
        submitBtn.addActionListener(e -> {
            try {
                double cost = Double.parseDouble(costField.getText());
                int bedrooms = Integer.parseInt(bedroomsField.getText());
                String name = nameField.getText().trim();
            
                if (name.isEmpty()) {
                    statusLabel.setForeground(Color.RED);
                    statusLabel.setText("Field is either empty or invalid");
                } else {
                    Home house = new Home(name, cost, bedrooms);
                    house.registerLodging();
                    Lodging.allLodgings.add(house);
                    if (ViewState.con != null){
                        saveLodge(house);
                    }
                    statusLabel.setText("House added sucessfully!");
                    statusLabel.setForeground(Color.black);
            
                    // Clear fields
                    costField.setText("");
                    bedroomsField.setText("");
                    nameField.setText("");
                }
            } catch (NumberFormatException ex) {
                statusLabel.setText("Field is either empty or invalid");
            }
        });
        // Add the form fields to the house input panel
        hgbc.gridx = 0;
        hgbc.gridy = 0;
        houseInputPanel.add(formFields, hgbc);
        
        return houseInputPanel;
    }
    public JPanel makeHotelForm() {
        // Hotel Panel
        JPanel hotelInputPanel = new JPanel(new GridBagLayout());
        hotelInputPanel.setBackground(new Color(249, 249, 255));
        hotelInputPanel.setVisible(false); // Hidden by default
    
        GridBagConstraints hgbc = new GridBagConstraints();
        hgbc.insets = new Insets(10, 10, 10, 10);
        hgbc.fill = GridBagConstraints.HORIZONTAL;
    
        // Form Fields Panel
        JPanel formFields = new JPanel(new GridBagLayout());
        formFields.setOpaque(false); // Transparent background
        GridBagConstraints innerGbc = new GridBagConstraints();
        innerGbc.insets = new Insets(15, 15, 15, 15);
        innerGbc.fill = GridBagConstraints.HORIZONTAL;
        innerGbc.weightx = 1.0;
    
        // Cost Field (base price per night)
        JetSettersLabel costLabel = new JetSettersLabel("Base Price/Night: ");
        costLabel.setFont(new Font("Dialog", Font.BOLD, 20));
        costLabel.setForeground(new Color(37, 68, 65));
        innerGbc.gridx = 0;
        innerGbc.gridy = 0;
        innerGbc.weightx = 0;
        formFields.add(costLabel, innerGbc);
    
        JTextField costField = new JTextField(40);
        costField.setMinimumSize(costField.getPreferredSize());
        innerGbc.gridx = 1;
        innerGbc.weightx = 1;
        formFields.add(costField, innerGbc);
    
        // Bedrooms Field
        JetSettersLabel bedroomsLabel = new JetSettersLabel("Number of Bedrooms: ");
        bedroomsLabel.setFont(new Font("Dialog", Font.BOLD, 20));
        bedroomsLabel.setForeground(new Color(37, 68, 65));
        innerGbc.gridx = 0;
        innerGbc.gridy = 1;
        innerGbc.weightx = 0;
        formFields.add(bedroomsLabel, innerGbc);
    
        JTextField bedroomsField = new JTextField(40);
        bedroomsField.setMinimumSize(bedroomsField.getPreferredSize());
        innerGbc.gridx = 1;
        innerGbc.weightx = 1;
        formFields.add(bedroomsField, innerGbc);
    
        // Vacancies Field
        JetSettersLabel vacanciesLabel = new JetSettersLabel("Vacancies: ");
        vacanciesLabel.setFont(new Font("Dialog", Font.BOLD, 20));
        vacanciesLabel.setForeground(new Color(37, 68, 65));
        innerGbc.gridx = 0;
        innerGbc.gridy = 2;
        innerGbc.weightx = 0;
        formFields.add(vacanciesLabel, innerGbc);
    
        JTextField vacanciesField = new JTextField(40);
        vacanciesField.setMinimumSize(vacanciesField.getPreferredSize());
        innerGbc.gridx = 1;
        innerGbc.weightx = 1;
        formFields.add(vacanciesField, innerGbc);
    
        // Max Occupants Field
        JetSettersLabel maxOccLabel = new JetSettersLabel("Max Occupants: ");
        maxOccLabel.setFont(new Font("Dialog", Font.BOLD, 20));
        maxOccLabel.setForeground(new Color(37, 68, 65));
        innerGbc.gridx = 0;
        innerGbc.gridy = 3;
        innerGbc.weightx = 0;
        formFields.add(maxOccLabel, innerGbc);
    
        JTextField maxOccField = new JTextField(40);
        maxOccField.setMinimumSize(maxOccField.getPreferredSize());
        innerGbc.gridx = 1;
        innerGbc.weightx = 1;
        formFields.add(maxOccField, innerGbc);
    
        // Name Field
        JetSettersLabel nameLabel = new JetSettersLabel("Name: ");
        nameLabel.setFont(new Font("Dialog", Font.BOLD, 20));
        nameLabel.setForeground(new Color(37, 68, 65));
        innerGbc.gridx = 0;
        innerGbc.gridy = 4;
        innerGbc.weightx = 0;
        formFields.add(nameLabel, innerGbc);
    
        JTextField nameField = new JTextField(40);
        nameField.setMinimumSize(nameField.getPreferredSize());
        innerGbc.gridx = 1;
        innerGbc.weightx = 1;
        formFields.add(nameField, innerGbc);
    
        // Status Label
        JLabel statusLabel = new JLabel(" ");
        statusLabel.setForeground(Color.RED);
        hotelInputPanel.add(statusLabel);
    
        // Submit button
        JetSettersButton submitBtn = new JetSettersButton("Submit");
        innerGbc.gridx = 0;
        innerGbc.gridy = 5;
        innerGbc.gridwidth = 2;
        formFields.add(submitBtn, innerGbc);
        submitBtn.addActionListener(e -> {
            try {
                double basePrice = Double.parseDouble(costField.getText());
                int bedrooms = Integer.parseInt(bedroomsField.getText());
                int vacancies = Integer.parseInt(vacanciesField.getText());
                int maxOcc = Integer.parseInt(maxOccField.getText());
                String name = nameField.getText().trim();
            
                if (name.isEmpty()) {
                    statusLabel.setForeground(Color.RED);
                    statusLabel.setText("Field is either empty or invalid");
                } else {
                    Hotel hotel = new Hotel(name, vacancies, bedrooms, basePrice, maxOcc);
                    hotel.registerLodging();
                    Lodging.allLodgings.add(hotel);
                    if (ViewState.con != null){
                        saveLodge(hotel);
//                        save();
                    }
                    statusLabel.setText("Hotel added successfully!");
                    statusLabel.setForeground(Color.BLACK);
                
                    // Clear fields
                    costField.setText("");
                    bedroomsField.setText("");
                    vacanciesField.setText("");
                    maxOccField.setText("");
                    nameField.setText("");
                }
            } catch (NumberFormatException ex) {
                statusLabel.setText("Field is either empty or invalid");
            }
        });
    
        // Add the form fields to the hotel input panel
        hgbc.gridx = 0;
        hgbc.gridy = 0;
        hotelInputPanel.add(formFields, hgbc);
    
        return hotelInputPanel;
    }
    public JPanel makeEditForm(){
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(152, 172, 173));

//        panel.setPreferredSize(new Dimension(800, 400));
        
//        // Content panel using GBC
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBackground(new Color(249, 249, 255));
        contentPane.setBorder(BorderFactory.createLineBorder(new Color(249, 249, 255), 4, true));

        // Label and ComboBox
        JLabel lodgeLabel = new JLabel("Select Lodge:");
        lodgeLabel.setForeground(Color.WHITE); 
        lodgeList = new JComboBox<>();
        for (Lodging i : Lodging.allLodgings) {
            lodgeList.addItem(i.name);
        }
        
        // Top psne
        JPanel topPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        // Buttons
        JetSettersButton edit = new JetSettersButton("Edit");
        
        // event listener
        edit.addActionListener(e -> {
            contentPane.remove(formFieldHotel);
            contentPane.remove(formFieldHouse);
            formFieldHouse = houseEdit();
            formFieldHotel = hotelEdit();

//            System.out.println("EDIT BUTTON POPPING OFF!");
            int selectedIndex = lodgeList.getSelectedIndex();
            if (selectedIndex >= 0 && selectedIndex < Lodging.allLodgings.size()) {
                Lodging selectedLodge = Lodging.allLodgings.get(selectedIndex);
                if (selectedLodge instanceof Hotel){
                    contentPane.add(formFieldHotel, BorderLayout.CENTER);
                    hotelPop((Hotel) selectedLodge);
//                    System.out.println(selectedLodge.name + " is a hotel");
                } else {
                    contentPane.add(formFieldHouse, BorderLayout.CENTER);
                    housePop((Home) selectedLodge);
//                  System.out.println(selectedLodge.name + " is a house");
                }
//            lodgeDetails.setText(selectedLodge.getDetailsString());
//            lodgeDetails.setCaretPosition(0);
            } else {
                lodgeDetails.setText("No lodge selected.");
            }
            
            // src: https://stackoverflow.com/questions/1097366/java-swing-revalidate-vs-repaint
            // contentpane was refusing to show my text fields, so i added revalidate and repaint
            contentPane.revalidate();
            contentPane.repaint();
        });
        lodgeList.addActionListener(e -> {
        int selectedIndex = lodgeList.getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < Lodging.allLodgings.size()) {
            Lodging selectedLodge = Lodging.allLodgings.get(selectedIndex);
            lodgeDetails.setText(selectedLodge.getDetailsString());
            lodgeDetails.setCaretPosition(0);
        } else {
            lodgeDetails.setText("No lodge selected.");
        }
        });

        // Panels
        topPane.add(edit);
        topPane.add(lodgeList);
        contentPane.add(topPane, BorderLayout.NORTH);
        GridBagConstraints gbc = new GridBagConstraints();
        contentPane.setPreferredSize(new Dimension(885, 485));        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1; 
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(contentPane, gbc);
        return panel;
    }
    public JPanel houseEdit(){
        // Form Fields Panel
        JPanel formFields = new JPanel(new GridBagLayout());
        formFields.setOpaque(false); // Transparent background
        GridBagConstraints innerGbc = new GridBagConstraints();
        innerGbc.insets = new Insets(15, 15, 15, 15);
        innerGbc.fill = GridBagConstraints.HORIZONTAL;
        innerGbc.weightx = 1.0;
        
        // Cost Field
        JetSettersLabel costLabel = new JetSettersLabel("Cost: ");
        costLabel.setFont(new Font("Dialog", Font.BOLD, 20));
        costLabel.setForeground(new Color(37, 68, 65));
        innerGbc.gridx = 0;
        innerGbc.gridy = 0;
        innerGbc.weightx = 0;
        formFields.add(costLabel, innerGbc);
        
        JTextField costField = new JTextField(40);
        houseCostField = costField;
        costField.setMinimumSize(costField.getPreferredSize()); 
        innerGbc.gridx = 1;
        innerGbc.weightx = 1;
        formFields.add(costField, innerGbc);
        
        // Bedrooms Field
        JetSettersLabel bedroomsLabel = new JetSettersLabel("Number of Bedrooms: ");
        bedroomsLabel.setFont(new Font("Dialog", Font.BOLD, 20));
        bedroomsLabel.setForeground(new Color(37, 68, 65));
        innerGbc.gridx = 0;
        innerGbc.gridy = 1;
        innerGbc.weightx = 0;
        formFields.add(bedroomsLabel, innerGbc);
        
        JTextField bedroomsField = new JTextField(40);
        houseBedroomsField = bedroomsField;
        bedroomsField.setMinimumSize(bedroomsField.getPreferredSize()); // Locks textbox size
        innerGbc.gridx = 1;
        innerGbc.weightx = 1;
        formFields.add(bedroomsField, innerGbc);
        
        // Name Field
        JetSettersLabel nameLabel = new JetSettersLabel("Name: ");
        nameLabel.setFont(new Font("Dialog", Font.BOLD, 20));
        nameLabel.setForeground(new Color(37, 68, 65));
        innerGbc.gridx = 0;
        innerGbc.gridy = 2;
        innerGbc.weightx = 0;
        formFields.add(nameLabel, innerGbc);
        
        JTextField nameField = new JTextField(40);
        houseNameField = nameField;
        nameField.setMinimumSize(nameField.getPreferredSize());
        innerGbc.gridx = 1;
        innerGbc.weightx = 1;
        formFields.add(nameField, innerGbc);
        
        // Status label
        JetSettersLabel statusLabel = new JetSettersLabel(" ");
        statusLabel.setFont(new Font("Dialog", Font.BOLD, 12));
        statusLabel.setVisible(false);
        statusLabel.setText(" ");
        formFields.add(statusLabel);

        
        //  BUTTON WITH EVENT LISTENER
        JetSettersButton submitHouseBtn = new JetSettersButton("Submit LODGE");
        submitHouseBtn.addActionListener(e -> {
            if(ViewState.con == null){
                try {
                    statusLabel.setVisible(false);
                    double houseCostFieldH = Double.parseDouble(houseCostField.getText());
                    int houseBedroomsFieldH = Integer.parseInt(houseBedroomsField.getText());
                    String name = houseNameField.getText().trim();
                
                    if (name.isEmpty()) {
                        // Handle invalid input
                        statusLabel.setForeground(Color.RED);
                        statusLabel.setText("Field is either empty or invalid");
                        statusLabel.setVisible(true);
                    } else {
                        // Find the selected hotel and update its details
                        int selectedIndex = lodgeList.getSelectedIndex();
                        if (selectedIndex >= 0 && selectedIndex < Lodging.allLodgings.size()) {
                            Home selectedHouse = (Home) Lodging.allLodgings.get(selectedIndex);
                            selectedHouse.cost = houseCostFieldH;
                            selectedHouse.numberOfBedrooms = houseBedroomsFieldH;
                            selectedHouse.name = name;
                            save();
                        }
                        lodgeList.removeAllItems();
                        for (Lodging l : Lodging.allLodgings) {
                            lodgeList.addItem(l.name);
                        }
                        statusLabel.setText("LODGE updated successfully!");
                        statusLabel.setForeground(Color.BLACK);
                        statusLabel.setVisible(true);
                    }
                } catch (NumberFormatException ex) {
                    statusLabel.setText("Field is either empty or invalid");
                    statusLabel.setVisible(true);
                }
            } else{
                statusLabel.setVisible(false);
                int selectedIndex = lodgeList.getSelectedIndex();
                double houseCostFieldH = Double.parseDouble(houseCostField.getText());
                int houseBedroomsFieldH = Integer.parseInt(houseBedroomsField.getText());
                String name = houseNameField.getText().trim();
                
                Home home = (Home) Lodging.allLodgings.get(selectedIndex);
                String oldName = home.name;
                if(name.isEmpty()){
                    // Handle invalid input
                    statusLabel.setForeground(Color.RED);
                    statusLabel.setText("Field is either empty or invalid");
                    statusLabel.setVisible(true);
                } else {
                    String sql = "UPDATE houses "
                            + "SET name = ?, cost = ?, bedroomNum = ? "
                            + "WHERE name = ?";
                    try(PreparedStatement ps = ViewState.con.prepareStatement(sql)){
                        ps.setString(1, name);
                        ps.setDouble(2, houseCostFieldH);
                        ps.setInt(3, houseBedroomsFieldH);
                        ps.setString(4, oldName);
                        ps.executeUpdate();
                    } catch (SQLException ex) {
                        Logger.getLogger(EmployeeViewState.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    home.name = name;
                    home.cost = houseCostFieldH;
                    home.numberOfBedrooms = houseBedroomsFieldH;                    
                    lodgeList.removeAllItems();
                    for (Lodging l : Lodging.allLodgings) {
                        lodgeList.addItem(l.name);
                    }
                    statusLabel.setText("LODGE updated successfully!");
                    statusLabel.setForeground(Color.BLACK);
                    statusLabel.setVisible(true);
                }
            }
        });
        formFields.add(submitHouseBtn);
//        formFields.add(statusLabel);
        return formFields;
    }
    public JPanel hotelEdit(){
        GridBagConstraints hgbc = new GridBagConstraints();
        hgbc.insets = new Insets(10, 10, 10, 10);
        hgbc.fill = GridBagConstraints.HORIZONTAL;

        // Form Fields Panel
        JPanel formFields = new JPanel(new GridBagLayout());
        formFields.setOpaque(false); // Transparent background
        GridBagConstraints innerGbc = new GridBagConstraints();
        innerGbc.insets = new Insets(15, 15, 15, 15);
        innerGbc.fill = GridBagConstraints.HORIZONTAL;
        innerGbc.weightx = 1.0;
        
        // Cost Field (base price per night)
        JetSettersLabel costLabel = new JetSettersLabel("Base Price/Night: ");
        costLabel.setFont(new Font("Dialog", Font.BOLD, 20));
        costLabel.setForeground(new Color(37, 68, 65));
        innerGbc.gridx = 0;
        innerGbc.gridy = 0;
        innerGbc.weightx = 0;
        formFields.add(costLabel, innerGbc);
        
        JTextField costField = new JTextField(40);
        hotelCostField = costField;
        costField.setMinimumSize(costField.getPreferredSize());
        innerGbc.gridx = 1;
        innerGbc.weightx = 1;
        formFields.add(costField, innerGbc);
        
        // Bedrooms Field
        JetSettersLabel bedroomsLabel = new JetSettersLabel("Number of Bedrooms: ");
        bedroomsLabel.setFont(new Font("Dialog", Font.BOLD, 20));
        bedroomsLabel.setForeground(new Color(37, 68, 65));
        innerGbc.gridx = 0;
        innerGbc.gridy = 1;
        innerGbc.weightx = 0;
        formFields.add(bedroomsLabel, innerGbc);
        
        JTextField bedroomsField = new JTextField(40);
        hotelBedroomsField = bedroomsField;
        bedroomsField.setMinimumSize(bedroomsField.getPreferredSize());
        innerGbc.gridx = 1;
        innerGbc.weightx = 1;
        formFields.add(bedroomsField, innerGbc);
        
        // Vacancies Field
        JetSettersLabel vacanciesLabel = new JetSettersLabel("Vacancies: ");
        vacanciesLabel.setFont(new Font("Dialog", Font.BOLD, 20));
        vacanciesLabel.setForeground(new Color(37, 68, 65));
        innerGbc.gridx = 0;
        innerGbc.gridy = 2;
        innerGbc.weightx = 0;
        formFields.add(vacanciesLabel, innerGbc);
        
        JTextField vacanciesField = new JTextField(40);
        hotelVacanciesField = vacanciesField;
        vacanciesField.setMinimumSize(vacanciesField.getPreferredSize());
        innerGbc.gridx = 1;
        innerGbc.weightx = 1;
        formFields.add(vacanciesField, innerGbc);
        
        // Max Occupants Field
        JetSettersLabel maxOccLabel = new JetSettersLabel("Max Occupants: ");
        maxOccLabel.setFont(new Font("Dialog", Font.BOLD, 20));
        maxOccLabel.setForeground(new Color(37, 68, 65));
        innerGbc.gridx = 0;
        innerGbc.gridy = 3;
        innerGbc.weightx = 0;
        formFields.add(maxOccLabel, innerGbc);
        
        JTextField maxOccField = new JTextField(40);
        hotelMaxOccField = maxOccField;
        maxOccField.setMinimumSize(maxOccField.getPreferredSize());
        innerGbc.gridx = 1;
        innerGbc.weightx = 1;
        formFields.add(maxOccField, innerGbc);
        
        // Name Field
        JetSettersLabel nameLabel = new JetSettersLabel("Name: ");
        nameLabel.setFont(new Font("Dialog", Font.BOLD, 20));
        nameLabel.setForeground(new Color(37, 68, 65));
        innerGbc.gridx = 0;
        innerGbc.gridy = 4;
        innerGbc.weightx = 0;
        formFields.add(nameLabel, innerGbc);
        
        JTextField nameField = new JTextField(40);
        hotelNameField = nameField;
        nameField.setMinimumSize(nameField.getPreferredSize());
        innerGbc.gridx = 1;
        innerGbc.weightx = 1;
        formFields.add(nameField, innerGbc);
        
        // Bottom Pane
        JPanel bottomP = new JPanel(new BorderLayout());
        
        // Status label
        JetSettersLabel statusLabel = new JetSettersLabel(" ");
        statusLabel.setFont(new Font("Dialog", Font.BOLD, 12));
        bottomP.add(statusLabel, BorderLayout.SOUTH);
        innerGbc.anchor = GridBagConstraints.SOUTH;
        innerGbc.insets = new Insets(20,10,10,10);
        innerGbc.gridy = 5;
        formFields.add(bottomP, innerGbc);        
//        statusLabel.setVisible(false);
        // Submit button with even listner
        JetSettersButton submitHotelBtn = new JetSettersButton("Submit Hotel");
        submitHotelBtn.addActionListener(e -> {
            if (ViewState.con == null){
                try {
                    bottomP.setVisible(false);
                    double basePrice = Double.parseDouble(hotelCostField.getText());
                    int bedrooms = Integer.parseInt(hotelBedroomsField.getText());
                    int vacancies = Integer.parseInt(hotelVacanciesField.getText());
                    int maxOccupants = Integer.parseInt(hotelMaxOccField.getText());
                    String name = hotelNameField.getText().trim();
                
                    if (name.isEmpty()) {
                        // Handle invalid input
                        statusLabel.setForeground(Color.RED);
                        statusLabel.setText("Field is either empty or invalid");
                        bottomP.setVisible(true);
                    } else {
                        // Find the selected hotel and update its details
                        int selectedIndex = lodgeList.getSelectedIndex();
                        if (selectedIndex >= 0 && selectedIndex < Lodging.allLodgings.size()) {
                            Hotel selectedHotel = (Hotel) Lodging.allLodgings.get(selectedIndex);
                            selectedHotel.basePricePerNight = basePrice;
                            selectedHotel.numberOfBedrooms = bedrooms;
                            selectedHotel.vacancies = vacancies;
                            selectedHotel.maxOccupants = maxOccupants;
                            selectedHotel.name = name;
                        statusLabel.setText("Hotel updated successfully!");
                        statusLabel.setForeground(Color.BLACK);
                        bottomP.setVisible(true);
                        save();
                        } 
                       
                    }
                        lodgeList.removeAllItems();
                        for (Lodging l : Lodging.allLodgings) {
                            lodgeList.addItem(l.name);
                        }
                } catch (NumberFormatException ex) {
                    statusLabel.setText("Field is either empty or invalid");
                    bottomP.setVisible(true);
                }
            } else {
                bottomP.setVisible(false);
                int selectedIndex = lodgeList.getSelectedIndex();
                double basePrice = Double.parseDouble(hotelCostField.getText());
                int bedrooms = Integer.parseInt(hotelBedroomsField.getText());
                int vacancies = Integer.parseInt(hotelVacanciesField.getText());
                int maxOccupants = Integer.parseInt(hotelMaxOccField.getText());
                String name = hotelNameField.getText().trim();
                
                Hotel Shotel = (Hotel) Lodging.allLodgings.get(selectedIndex);
                String oldName = Shotel.name;
                if (name.isEmpty()) {
                    // Handle invalid input
                    statusLabel.setForeground(Color.RED);
                    statusLabel.setText("Field is either empty or invalid");
                    bottomP.setVisible(true);
                } else {
                    // Find the selected hotel and update its details
                    String sql = "UPDATE Hotel "
                            + "SET name = ?, "
                            + "vacancies = ?, "
                            + "bedroomNum = ?, "
                            + "basePricePerNight = ?, "
                            + "maxOccupants = ? "
                            + "WHERE name = ?";
                    
                    try(PreparedStatement ps = ViewState.con.prepareStatement(sql)){
                        ps.setString(1, name);
                        ps.setInt(2, vacancies);
                        ps.setInt(3, bedrooms);
                        ps.setDouble(4, basePrice);
                        ps.setInt(5, maxOccupants);
                        ps.setString(6, oldName);
                        ps.executeUpdate();
                    } catch (SQLException ex) {
                        Logger.getLogger(EmployeeViewState.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    Shotel.basePricePerNight = basePrice;
                    Shotel.maxOccupants = maxOccupants;
                    Shotel.name = name;
                    Shotel.numberOfBedrooms = bedrooms;
                    Shotel.vacancies = vacancies;
                    
                    try {
                        BufferedWriter writer = new BufferedWriter(new FileWriter("src\\joaquinbarreram5\\lodgingInfo.txt"));
                        // Gets updated array, and then rewrites the file with the contents of the updated array, which no longer contains the removed item
                        for (int i = 0; i < Lodging.allLodgings.size(); i++) {
                            Lodging lodge = Lodging.allLodgings.get(i);
                            if (lodge instanceof Hotel) {
                                Hotel hotel = (Hotel)lodge;
                                writer.write(String.format("%s,%d,%d,%.2f,%d",
                                    hotel.name,
                                    hotel.vacancies,
                                    hotel.numberOfBedrooms,
                                    hotel.basePricePerNight,
                                    hotel.maxOccupants
                                ));
//                                saveLodge(hotel);
                            } else {
                                Home home = (Home)lodge;
                                writer.write(String.format("%s,%.2f,%d",
                                    home.name,
                                    home.cost,
                                    home.numberOfBedrooms
                                ));
//                                saveLodge(home);
                            }
                            if (i < Lodging.allLodgings.size() - 1) {
                                writer.newLine();
                            }
                        }
                    //            System.out.println("ARRAY SIZE TRY CATCH REMOVE: " + Lodging.allLodgings.size());
                        writer.close();
                    } catch (IOException ex) {
                        System.out.println("Error updating file: " + ex.getMessage());
                    }
                    lodgeList.removeAllItems();
                    for (Lodging l : Lodging.allLodgings) {
                        lodgeList.addItem(l.name);
                    }
                    statusLabel.setText("Hotel updated successfully!");
                    statusLabel.setForeground(Color.BLACK);
                    bottomP.setVisible(true);
                }
            }
        });
        formFields.add(submitHotelBtn);
        return formFields;
    }
    // Fill the house form’s fields
    private void housePop(Home h) {
      houseCostField.setText(String.valueOf(h.cost));
      houseBedroomsField.setText(String.valueOf(h.numberOfBedrooms));
      houseNameField.setText(h.name);
    }
    // Fill the hotel form’s fields
    private void hotelPop(Hotel h) {
      hotelCostField.setText(String.valueOf(h.basePricePerNight));
      hotelBedroomsField.setText(String.valueOf(h.numberOfBedrooms));
      hotelVacanciesField.setText(String.valueOf(h.vacancies));
      hotelMaxOccField.setText(String.valueOf(h.maxOccupants));
      hotelNameField.setText(h.name);
    }
    public void removeLodge(){
        int selectedIndex = lodgeList.getSelectedIndex();
        
        if (selectedIndex >= 0 && selectedIndex < Lodging.allLodgings.size()) {
            System.out.println(Lodging.allLodgings.get(selectedIndex).name + " removed successfully");
//            System.out.println("ARRAY SIZE BEFORE: " + Lodging.allLodgings.size());
//            System.out.println("ARRAY SIZE AFTER: " + Lodging.allLodgings.size());
        
        // Remove from database
        Lodging chosenLodge = Lodging.allLodgings.get(selectedIndex);
        if (ViewState.con != null){
            String sql = " ";
            if(chosenLodge instanceof Hotel){
                sql = "DELETE FROM Hotel WHERE name = ?";
            } else {
                sql = "DELETE FROM Houses WHERE name = ?";
            }
            try(PreparedStatement ps = ViewState.con.prepareStatement(sql)){
                ps.setString(1, chosenLodge.name);
                ps.executeUpdate();
            }catch (SQLException ex) {
                    Logger.getLogger(EmployeeViewState.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        // remove from array
        Lodging.allLodgings.remove(selectedIndex);
        // Update file, to remove text
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src\\joaquinbarreram5\\lodgingInfo.txt"));
            // Gets updated array, and then rewrites the file with the contents of the updated array, which no longer contains the removed item
            for (int i = 0; i < Lodging.allLodgings.size(); i++) {
                Lodging lodge = Lodging.allLodgings.get(i);
                if (lodge instanceof Hotel) {
                    Hotel hotel = (Hotel)lodge;
                    writer.write(String.format("%s,%d,%d,%.2f,%d",
                        hotel.name,
                        hotel.vacancies,
                        hotel.numberOfBedrooms,
                        hotel.basePricePerNight,
                        hotel.maxOccupants
                    ));
//                    saveLodge(hotel);
                } else {
                    Home home = (Home)lodge;
                    writer.write(String.format("%s,%.2f,%d",
                        home.name,
                        home.cost,
                        home.numberOfBedrooms
                    ));
//                    saveLodge(home);
                }
                if (i < Lodging.allLodgings.size() - 1) {
                    writer.newLine();
                }
            }
        //            System.out.println("ARRAY SIZE TRY CATCH REMOVE: " + Lodging.allLodgings.size());
            writer.close();
        } catch (IOException ex) {
            System.out.println("Error updating file: " + ex.getMessage());
        }
        lodgeList.removeAllItems();
        for (Lodging l : Lodging.allLodgings) {
            lodgeList.addItem(l.name);
        }
        } else {
            System.out.println("Invalid selection");
        }
        
    }
    public void editLodge() {
        System.out.println("Select a lodging to edit:");
        Lodging.displayAllLodging();
        System.out.print("Enter choice (1-" + Lodging.allLodgings.size() + "): ");
        int editChoice = Integer.parseInt(sc.nextLine());
        
        if (editChoice > 0 && editChoice <= Lodging.allLodgings.size()) {
            Lodging lodge = Lodging.allLodgings.get(editChoice - 1);
            
            System.out.println("\nEditing: " + lodge.name);
            System.out.println("1. Edit name");
            System.out.println("2. Edit price");
            
            // Type-specific options
            if (lodge instanceof Hotel) {
                System.out.println("3. Edit vacancies");
                System.out.println("4. Edit max occupants");
            } else if (lodge instanceof Home) {
                System.out.println("3. Edit bedroom count");
            }
            
            System.out.println("0. Cancel");
            System.out.print("Select option: ");
            String editOpt = sc.nextLine();
            
            switch (editOpt) {
                case "1": // Edit name
                    System.out.print("Enter new name: ");
                    lodge.name = sc.nextLine();
                    break;
                    
                case "2": // Edit price
                    if (lodge instanceof Home) {
                        System.out.print("Enter new home cost: $");
                        ((Home)lodge).cost = Double.parseDouble(sc.nextLine());
                    } else {
                        System.out.print("Enter new base price per night: $");
                        ((Hotel)lodge).basePricePerNight = Double.parseDouble(sc.nextLine());
                    }
                    break;
                    
                case "3": // Type-specific options
                    if (lodge instanceof Hotel) {
                        System.out.print("Enter new vacancy count: ");
                        ((Hotel)lodge).vacancies = Integer.parseInt(sc.nextLine());
                    } else {
                        System.out.print("Enter new bedroom count: ");
                        lodge.numberOfBedrooms = Integer.parseInt(sc.nextLine());
                    }
                    break;
                    
                case "4": // Hotel-only option
                    if (lodge instanceof Hotel) {
                        System.out.print("Enter new max occupants: ");
                        ((Hotel)lodge).maxOccupants = Integer.parseInt(sc.nextLine());
                    } else {
                        System.out.println("Invalid option!");
                    }
                    break;
                    
                case "0":
                    System.out.println("Edit cancelled.");
                    return;
                    
                default:
                    System.out.println("Invalid option!");
                    return;
            }
            
//            save();
            System.out.println("Lodge updated successfully!");
        } else {
            System.out.println("Invalid selection!");
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
//                    int i = 0;
                    var rs = ps.executeQuery();
                    while(rs.next()){
//                        System.out.println("PRINT OUT: " + i);
//                        i++;
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
            return "Houses";
        } else {
            return null;
        }
    }
    
    private boolean isSelectedLodgeAHotel() {
        int idx = lodgeList.getSelectedIndex();
        if (idx < 0 || idx >= Lodging.allLodgings.size()) {
            return false;
        }
        return Lodging.allLodgings.get(idx) instanceof Hotel;
    }
    
    private void uploadImages() {
        String name = (String) lodgeList.getSelectedItem();
        if (name == null) return;
    
        // finds the matching lodge
        Lodging lodge = null;
        for (Lodging l : Lodging.allLodgings) {
            if (l.name.equals(name)){
                lodge = l; 
                break;
            }
        }
        if (lodge == null || ViewState.con == null){
            return;
        }
    
        // HOTEL
        try {
            ViewState.connect();
            String[] hotels = hotelImages.keySet().toArray(new String[0]);
            for (String hotelName : hotels) {
                PreparedStatement sqlId = ViewState.con.prepareStatement(""
                        + "SELECT hotelId FROM hotel WHERE name = ?");
                sqlId.setString(1,hotelName);
                var rs = sqlId.executeQuery();
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
                var rs = sqlId.executeQuery();
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
    }
    
    private Map<String, java.util.List<String>> getCurrentImageMap() {
        int idx = lodgeList.getSelectedIndex();
        if (idx < 0 || idx >= Lodging.allLodgings.size()) {
            return null;
        }
        Lodging selected = Lodging.allLodgings.get(idx);
        if (selected instanceof Hotel) {
            return hotelImages;
        } else if (selected instanceof Home) {
            return houseImages;
        } else {
            return null;
        }
    }

    @Override
    void enter() {
        System.out.println("====================");
        System.out.println("1. Add Lodge");
        System.out.println("2. Remove Lodge");
        System.out.println("3. List Lodges");
        System.out.println("4. Edit Lodges");
        System.out.println("5. Log out");
        if (currEmployee.isAManager) {
            System.out.println("6. Manager View");
        }
        System.out.println("====================");
    }

    @Override
    void update() {
        // Not needed
    }
    @Override
    public void load() {
        Lodging.allLodgings.clear();

        try {
            if (con == null){
                // Load lodgings from file
                BufferedReader reader = new BufferedReader(new FileReader("src\\joaquinbarreram5\\lodgingInfo.txt"));
                String line;
                Lodging.allLodgings.clear(); // Clear existing before loading
//                System.out.println("POPPING OFF!");
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data.length == 5) { // Hotel
                        Lodging.allLodgings.add(new Hotel(
                        data[0], 
                        Integer.parseInt(data[1]), 
                        Integer.parseInt(data[2]), 
                        Double.parseDouble(data[3]), 
                        Integer.parseInt(data[4])
                    ));
                } else if (data.length == 3) { // Home
                        Lodging.allLodgings.add(new Home(
                        data[0],
                        Double.parseDouble(data[1]),
                        Integer.parseInt(data[2])
                    ));
                }
            }
                viewLodgings();
                reader.close();
            } else {
                lodgeIdMap.clear();
                
                // load hotels
                String queryHm = "SELECT hotelId,name,vacancies,bedroomNum,basePricePerNight,maxOccupants FROM Hotel";
                try (var stmt = con.createStatement(); var rs = stmt.executeQuery(queryHm)) {
                    while (rs.next()) {
                        int id = rs.getInt("hotelId");
                        String name = rs.getString("name");
                        int vac = rs.getInt("vacancies");
                        int beds = rs.getInt("bedroomNum");
                        double price = rs.getDouble("basePricePerNight");
                        int maxOcc = rs.getInt("maxOccupants"); 
                        Hotel h = new Hotel(name, vac, beds, price, maxOcc);
                        Lodging.allLodgings.add(h);
                        lodgeIdMap.put(h, id);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(EmployeeViewState.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                // load houses
                String queryHs = "SELECT houseId,name,cost,bedroomNum FROM Houses";
                try (var stmt = con.createStatement(); var rs = stmt.executeQuery(queryHs)) {
                    while (rs.next()) {
                        int id = rs.getInt("houseId");
                        String name = rs.getString("name");
                        double cost = rs.getDouble("cost");
                        int beds = rs.getInt("bedroomNum");
                
                        Home ho = new Home(name, cost, beds);
                        Lodging.allLodgings.add(ho);
                        lodgeIdMap.put(ho, id);
                    }
                } catch (SQLException ex) {
                  Logger.getLogger(EmployeeViewState.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            System.out.println("Lodgings loaded successfully!"); // Debug
        } catch (IOException ex) {
            System.out.println("Error loading lodgings: " + ex.getMessage()); // Debug
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeViewState.class.getName()).log(Level.SEVERE, null, ex);
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
                
                // Add newline except after last entry
                if (i < Lodging.allLodgings.size() - 1) {
                    writer.write("\n");
                }
            }
            writer.close();
            System.out.println("Lodgings saved successfully!"); // Debug
        } catch (IOException ex) {
            System.out.println("Error saving lodgings: " + ex.getMessage()); // Debug
        }
        // M5 
        // Save to databases
        try {
            System.out.println("TRY CATCH EMPLOYEEVIEWSTATE SAVE POPPING OFF");
            if(ViewState.con != null && !ViewState.con.isClosed()){
                for(int i = 0; i < Lodging.allLodgings.size(); i++){
                    Lodging lodge = Lodging.allLodgings.get(i);
                    saveLodge(lodge);
                }
            }
        } catch(Exception e){
            System.out.println("LINE 1234 Problem with saving to database: " + e);
        }
    }
    @Override
    public JPanel getPanel(){
        return panel;
    }
}