package payroll;

import java.sql.Connection;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Image;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;



public class AdminPortal extends javax.swing.JFrame {
    private DefaultTableModel tableModel;
    private Employee currentEmployee;
        



    public AdminPortal() {
        initComponents();
    }
    

    public AdminPortal(String untxt) {
        initComponents();
        updateFirstNameFromDatabase(untxt);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateCurrentDateTime();
            }
        }, 0, 1000); // 1000 milliseconds = 1 second

        String[] columnNames = {"EmployeeID", "Last_Name", "First_Name", "Birthday", "Address", "Phone_Number", "SSS", "Philhealth", "Pag_ibig", "TIN"};
        tableModel = new DefaultTableModel(columnNames, 0); 

        setTitle("Employee Information Portal");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set the table model to the existing tblEmployeeInformation
        tblEmployeeInformation.setModel(tableModel);

        // Fetch data from MySQL and populate the table
        fetchDataFromDatabase();

        // Display the frame
        setVisible(true);
    }
    
    private void fetchDataFromDatabase() {
        try {
        MyConnection myConnection = new MyConnection();
        Connection connection = myConnection.connect();

        if (connection != null) {
            String query = "SELECT EmployeeID, Last_Name, First_Name, Birthday, Address, Phone_Number, SSS, Philhealth, Pag_ibig, TIN FROM employee_details";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                // Iterate through the result set and add rows to the table model
                while (resultSet.next()) {
                    Object[] rowData = {
                            resultSet.getInt("EmployeeID"),
                            resultSet.getString("Last_Name"),
                            resultSet.getString("First_Name"),
                            resultSet.getString("Birthday"),
                            resultSet.getString("Address"),
                            resultSet.getString("Phone_Number"),
                            resultSet.getString("SSS"),
                            resultSet.getString("Philhealth"),
                            resultSet.getString("Pag_ibig"),
                            resultSet.getString("TIN"),
                            
                    };
                    tableModel.addRow(rowData);
                }
            }

            // Close the connection
            myConnection.closeConnection();
        }
    } catch (SQLException e) {
        e.printStackTrace();
        System.err.println("Error fetching data from database: " + e.getMessage());  // Add this line
    }
    }

    
        private void saveEmployeeToDatabase(Employee employee) {
    try {
        MyConnection myConnection = new MyConnection();
        Connection connection = myConnection.connect();

        if (connection != null) {
            String insertQuery = "INSERT INTO employee_details (Last_Name, First_Name, Birthday, Address, Phone_Number, SSS, Philhealth, TIN, Pag_ibig) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setString(1, employee.getLastName());
                preparedStatement.setString(2, employee.getFirstName());
                preparedStatement.setString(3, employee.getBirthday());
                preparedStatement.setString(4, employee.getAddress());
                preparedStatement.setString(5, employee.getPhoneNum());
                preparedStatement.setString(6, employee.getSssNum());
                preparedStatement.setString(7, employee.getPhilhealthNum());
                preparedStatement.setString(8, employee.getTinNum());
                preparedStatement.setString(9, employee.getPagibigNum());

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Data inserted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to insert data. No rows affected.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            // Close the connection
            myConnection.closeConnection();
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error saving data to database: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

        
        
    private void updateFirstNameFromDatabase(String username) {
        try {
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/motor_ph", "root", "dmns!!404")) {
                String selectQuery = "SELECT ed.First_Name FROM login l " +
                        "JOIN employee_details ed ON l.EmployeeID = ed.EmployeeID " +
                        "WHERE l.username = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                    preparedStatement.setString(1, username);
                    
                    ResultSet resultSet = preparedStatement.executeQuery();
                    
                    if (resultSet.next()) {
                        lblEmployeeName.setText(resultSet.getString("First_Name") + "!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Employee not found.");
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error retrieving employee details: " + ex.getMessage());
        }
    }
    
    private void updateCurrentDateTime() {
        // Get the current date and time in 12-hour format
        SimpleDateFormat dateFormatDate = new SimpleDateFormat("MMM dd, yyyy");
        SimpleDateFormat dateFormatTime = new SimpleDateFormat("hh:mm:ss a");

        String currentDate = dateFormatDate.format(new Date());
        String currentTime = dateFormatTime.format(new Date());

        // Update the labels
        lblCurrentDate.setText(currentDate);
        lblCurrentTime.setText(currentTime);
    }
    
    private void handleButtonClick(java.awt.event.ActionEvent evt, String panelName, JButton clickedButton) {
    // Switch to the specified panel when a button is clicked
    CardLayout cardLayout = (CardLayout) centerPanel.getLayout();
    cardLayout.show(centerPanel, panelName);

    // Set the background color of the clicked button to a different color
    clickedButton.setBackground(Color.WHITE);

    // Reset the background color of other buttons
    resetButtonColors(clickedButton);
}

private void resetButtonColors(JButton clickedButton) {
    // Reset the background color of other buttons
    JButton[] buttons = {btnEmpDash, btnEmpDetails, btnEmpTimesheet, btnPayroll};
    for (JButton button : buttons) {
        if (button != clickedButton) {
            button.setBackground(null);
        }
    }
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel11 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        mainPanel = new javax.swing.JPanel();
        topPanel = new javax.swing.JPanel();
        btnLogout = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        buttonPanel = new javax.swing.JPanel();
        btnEmpDash = new javax.swing.JButton();
        btnEmpDetails = new javax.swing.JButton();
        btnEmpTimesheet = new javax.swing.JButton();
        btnPayroll = new javax.swing.JButton();
        centerPanel = new javax.swing.JPanel();
        p1Dashboard = new javax.swing.JPanel();
        pnlProfile = new javax.swing.JPanel();
        photopanel = new javax.swing.JDesktopPane();
        img = new javax.swing.JLabel();
        btnAttachPic = new javax.swing.JButton();
        lblGreetings = new javax.swing.JLabel();
        lblEmployeeRole = new javax.swing.JLabel();
        lblEmployeeName = new javax.swing.JLabel();
        pnlAttendance = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel3 = new javax.swing.JPanel();
        lblCurrentTime = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        lblCurrentDate = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        lblCheckIn = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        lblCheckOut = new javax.swing.JLabel();
        btnCheckOut = new javax.swing.JButton();
        btnCheckIn = new javax.swing.JButton();
        p2EmpDet = new javax.swing.JPanel();
        pnlbutton = new javax.swing.JPanel();
        btnJobDetails = new javax.swing.JButton();
        btnEmployeeInformation = new javax.swing.JButton();
        pnlCenter = new javax.swing.JPanel();
        p1EmpInformation = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblEmployeeInformation = new javax.swing.JTable();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txtAddress = new javax.swing.JScrollPane();
        txtAdd = new javax.swing.JTextArea();
        txtLastName = new javax.swing.JTextField();
        txtFirstName = new javax.swing.JTextField();
        txtBirthday = new javax.swing.JTextField();
        txtPNumber = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        txtTinNum = new javax.swing.JTextField();
        txtSSSNum = new javax.swing.JTextField();
        txtPhilhealthNum = new javax.swing.JTextField();
        txtPagibigNum = new javax.swing.JTextField();
        btnAddEmployee = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        p2JobDetails = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        p3EmpTime = new javax.swing.JPanel();
        p4Payroll = new javax.swing.JPanel();

        jLabel11.setText("jLabel11");

        jLabel14.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        jLabel14.setText("Last Name:");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ADMIN");
        setMaximumSize(new java.awt.Dimension(1080, 720));
        setMinimumSize(new java.awt.Dimension(1080, 720));
        setPreferredSize(new java.awt.Dimension(1080, 720));
        setResizable(false);
        setSize(new java.awt.Dimension(1080, 720));

        mainPanel.setForeground(new java.awt.Color(204, 204, 204));
        mainPanel.setMaximumSize(new java.awt.Dimension(1080, 720));
        mainPanel.setMinimumSize(new java.awt.Dimension(1080, 720));
        mainPanel.setPreferredSize(new java.awt.Dimension(1080, 720));
        mainPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        topPanel.setBackground(new java.awt.Color(102, 102, 102));
        topPanel.setMaximumSize(new java.awt.Dimension(1080, 110));
        topPanel.setMinimumSize(new java.awt.Dimension(1080, 110));
        topPanel.setPreferredSize(new java.awt.Dimension(1080, 110));
        topPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnLogout.setBackground(new java.awt.Color(153, 0, 0));
        btnLogout.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        btnLogout.setForeground(new java.awt.Color(255, 255, 255));
        btnLogout.setText("LOGOUT");
        btnLogout.setAlignmentY(0.0F);
        btnLogout.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
        btnLogout.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLogout.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLogout.setInheritsPopupMenu(true);
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });
        topPanel.add(btnLogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 20, 90, 40));

        jLabel2.setFont(new java.awt.Font("Bahnschrift", 0, 50)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(204, 204, 204));
        jLabel2.setText("MOTORPH");
        topPanel.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(482, 33, 230, 70));

        jLabel3.setFont(new java.awt.Font("Bahnschrift", 0, 50)); // NOI18N
        jLabel3.setText("MOTORPH");
        topPanel.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 30, 230, 70));

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pictures/motor.png"))); // NOI18N
        topPanel.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 0, 330, 130));

        mainPanel.add(topPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        buttonPanel.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.borderColor"));
        buttonPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
        buttonPanel.setMaximumSize(new java.awt.Dimension(1080, 50));

        btnEmpDash.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        btnEmpDash.setText("Dashboard");
        btnEmpDash.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
        btnEmpDash.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEmpDash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmpDashActionPerformed(evt);
            }
        });

        btnEmpDetails.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        btnEmpDetails.setForeground(new java.awt.Color(51, 51, 51));
        btnEmpDetails.setText("Employee Details");
        btnEmpDetails.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
        btnEmpDetails.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEmpDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmpDetailsActionPerformed(evt);
            }
        });

        btnEmpTimesheet.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        btnEmpTimesheet.setForeground(new java.awt.Color(51, 51, 51));
        btnEmpTimesheet.setText("Employee Timesheet");
        btnEmpTimesheet.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
        btnEmpTimesheet.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEmpTimesheet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmpTimesheetActionPerformed(evt);
            }
        });

        btnPayroll.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        btnPayroll.setForeground(new java.awt.Color(51, 51, 51));
        btnPayroll.setText("Payroll");
        btnPayroll.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
        btnPayroll.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPayroll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPayrollActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout buttonPanelLayout = new javax.swing.GroupLayout(buttonPanel);
        buttonPanel.setLayout(buttonPanelLayout);
        buttonPanelLayout.setHorizontalGroup(
            buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonPanelLayout.createSequentialGroup()
                .addComponent(btnEmpDash, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnEmpDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnEmpTimesheet, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnPayroll, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        buttonPanelLayout.setVerticalGroup(
            buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnEmpDash, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(btnEmpDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(btnEmpTimesheet, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(btnPayroll, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        mainPanel.add(buttonPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 110, -1, -1));

        centerPanel.setBackground(new java.awt.Color(255, 255, 255));
        centerPanel.setAlignmentX(0.0F);
        centerPanel.setAlignmentY(0.0F);
        centerPanel.setMaximumSize(new java.awt.Dimension(1076, 560));
        centerPanel.setMinimumSize(new java.awt.Dimension(1076, 552));
        centerPanel.setPreferredSize(new java.awt.Dimension(1076, 552));
        centerPanel.setLayout(new java.awt.CardLayout());

        p1Dashboard.setBackground(new java.awt.Color(153, 153, 153));
        p1Dashboard.setAlignmentX(0.0F);
        p1Dashboard.setAlignmentY(0.0F);
        p1Dashboard.setMaximumSize(new java.awt.Dimension(1070, 550));
        p1Dashboard.setMinimumSize(new java.awt.Dimension(1070, 550));
        p1Dashboard.setPreferredSize(new java.awt.Dimension(1070, 550));

        pnlProfile.setBackground(new java.awt.Color(204, 204, 204));
        pnlProfile.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        photopanel.setBackground(new java.awt.Color(255, 255, 255));
        photopanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        img.setFont(new java.awt.Font("Bahnschrift", 0, 12)); // NOI18N
        img.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        img.setText("Photo Here");
        img.setMaximumSize(new java.awt.Dimension(196, 196));
        img.setMinimumSize(new java.awt.Dimension(196, 196));
        img.setPreferredSize(new java.awt.Dimension(196, 196));

        photopanel.setLayer(img, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout photopanelLayout = new javax.swing.GroupLayout(photopanel);
        photopanel.setLayout(photopanelLayout);
        photopanelLayout.setHorizontalGroup(
            photopanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(photopanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(img, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        photopanelLayout.setVerticalGroup(
            photopanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(photopanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(img, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnAttachPic.setFont(new java.awt.Font("Bahnschrift", 0, 10)); // NOI18N
        btnAttachPic.setText("ATTACH PICTURE");
        btnAttachPic.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnAttachPic.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAttachPic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAttachPicActionPerformed(evt);
            }
        });

        lblGreetings.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        lblGreetings.setText("Welcome,");

        lblEmployeeRole.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        lblEmployeeRole.setText("ADMIN");

        lblEmployeeName.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        lblEmployeeName.setText("Name");

        javax.swing.GroupLayout pnlProfileLayout = new javax.swing.GroupLayout(pnlProfile);
        pnlProfile.setLayout(pnlProfileLayout);
        pnlProfileLayout.setHorizontalGroup(
            pnlProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlProfileLayout.createSequentialGroup()
                .addGroup(pnlProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlProfileLayout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(pnlProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(pnlProfileLayout.createSequentialGroup()
                                .addComponent(lblEmployeeRole)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnAttachPic))
                            .addComponent(photopanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlProfileLayout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addComponent(lblGreetings)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblEmployeeName)))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        pnlProfileLayout.setVerticalGroup(
            pnlProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlProfileLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(photopanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAttachPic)
                    .addComponent(lblEmployeeRole))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblGreetings)
                    .addComponent(lblEmployeeName))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlAttendance.setBackground(new java.awt.Color(204, 204, 204));
        pnlAttendance.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));
        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.setMaximumSize(new java.awt.Dimension(240, 52));
        jPanel1.setMinimumSize(new java.awt.Dimension(240, 52));
        jPanel1.setPreferredSize(new java.awt.Dimension(240, 52));

        jLabel6.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("CURRENT TIME");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addContainerGap(64, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(10, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(102, 102, 102));
        jPanel2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel2.setMaximumSize(new java.awt.Dimension(240, 52));
        jPanel2.setMinimumSize(new java.awt.Dimension(240, 52));
        jPanel2.setPreferredSize(new java.awt.Dimension(240, 52));

        jLabel1.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("CURRENT DATE");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(58, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap())
        );

        jSeparator1.setForeground(new java.awt.Color(153, 153, 153));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setMaximumSize(new java.awt.Dimension(274, 52));
        jPanel3.setMinimumSize(new java.awt.Dimension(274, 52));
        jPanel3.setPreferredSize(new java.awt.Dimension(274, 52));

        lblCurrentTime.setFont(new java.awt.Font("Bahnschrift", 0, 30)); // NOI18N
        lblCurrentTime.setForeground(new java.awt.Color(102, 102, 102));
        lblCurrentTime.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCurrentTime.setText("00:00");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addComponent(lblCurrentTime)
                .addContainerGap(143, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblCurrentTime)
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setMaximumSize(new java.awt.Dimension(274, 52));
        jPanel4.setMinimumSize(new java.awt.Dimension(274, 52));
        jPanel4.setPreferredSize(new java.awt.Dimension(274, 52));

        lblCurrentDate.setFont(new java.awt.Font("Bahnschrift", 0, 30)); // NOI18N
        lblCurrentDate.setForeground(new java.awt.Color(102, 102, 102));
        lblCurrentDate.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCurrentDate.setText("MM/DD/YYYY");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(58, Short.MAX_VALUE)
                .addComponent(lblCurrentDate)
                .addGap(48, 48, 48))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(9, Short.MAX_VALUE)
                .addComponent(lblCurrentDate)
                .addContainerGap())
        );

        jPanel5.setBackground(new java.awt.Color(102, 102, 102));
        jPanel5.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel5.setMaximumSize(new java.awt.Dimension(240, 52));
        jPanel5.setMinimumSize(new java.awt.Dimension(240, 52));

        jLabel7.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Employee Timecard");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addGap(17, 17, 17))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.setBackground(new java.awt.Color(102, 102, 102));
        jPanel6.setMaximumSize(new java.awt.Dimension(154, 48));
        jPanel6.setMinimumSize(new java.awt.Dimension(154, 48));
        jPanel6.setPreferredSize(new java.awt.Dimension(154, 48));

        jLabel5.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("CHECK-OUT TIME:");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.setBackground(new java.awt.Color(102, 102, 102));
        jPanel7.setMaximumSize(new java.awt.Dimension(154, 48));
        jPanel7.setMinimumSize(new java.awt.Dimension(154, 48));
        jPanel7.setPreferredSize(new java.awt.Dimension(154, 48));

        jLabel8.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("CHECK-IN TIME:");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8.setBackground(new java.awt.Color(204, 204, 204));
        jPanel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel8.setMaximumSize(new java.awt.Dimension(281, 52));
        jPanel8.setMinimumSize(new java.awt.Dimension(281, 52));
        jPanel8.setPreferredSize(new java.awt.Dimension(281, 52));

        lblCheckIn.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        lblCheckIn.setForeground(new java.awt.Color(255, 255, 255));
        lblCheckIn.setText("You have not check-in yet.");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblCheckIn)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblCheckIn)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jPanel9.setBackground(new java.awt.Color(204, 204, 204));
        jPanel9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel9.setMaximumSize(new java.awt.Dimension(281, 52));
        jPanel9.setMinimumSize(new java.awt.Dimension(281, 52));
        jPanel9.setPreferredSize(new java.awt.Dimension(281, 52));

        lblCheckOut.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        lblCheckOut.setForeground(new java.awt.Color(255, 255, 255));
        lblCheckOut.setText("You have not check-out yet.");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblCheckOut)
                .addContainerGap(82, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblCheckOut)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        btnCheckOut.setBackground(new java.awt.Color(220, 126, 56));
        btnCheckOut.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        btnCheckOut.setForeground(new java.awt.Color(255, 255, 255));
        btnCheckOut.setText("Check-Out");
        btnCheckOut.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnCheckOut.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btnCheckIn.setBackground(new java.awt.Color(87, 108, 167));
        btnCheckIn.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        btnCheckIn.setForeground(new java.awt.Color(255, 255, 255));
        btnCheckIn.setText("Check-In");
        btnCheckIn.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnCheckIn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCheckIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCheckInActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlAttendanceLayout = new javax.swing.GroupLayout(pnlAttendance);
        pnlAttendance.setLayout(pnlAttendanceLayout);
        pnlAttendanceLayout.setHorizontalGroup(
            pnlAttendanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAttendanceLayout.createSequentialGroup()
                .addGroup(pnlAttendanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlAttendanceLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator1))
                    .addGroup(pnlAttendanceLayout.createSequentialGroup()
                        .addGroup(pnlAttendanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlAttendanceLayout.createSequentialGroup()
                                .addGap(116, 116, 116)
                                .addGroup(pnlAttendanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(43, 43, 43)
                                .addGroup(pnlAttendanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(pnlAttendanceLayout.createSequentialGroup()
                                .addGap(74, 74, 74)
                                .addGroup(pnlAttendanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnlAttendanceLayout.createSequentialGroup()
                                        .addGroup(pnlAttendanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(pnlAttendanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE)
                                            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE))
                                        .addGap(51, 51, 51))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlAttendanceLayout.createSequentialGroup()
                                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(75, 75, 75)))
                                .addGroup(pnlAttendanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(btnCheckOut, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnCheckIn, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 53, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlAttendanceLayout.setVerticalGroup(
            pnlAttendanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAttendanceLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(pnlAttendanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlAttendanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addGroup(pnlAttendanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlAttendanceLayout.createSequentialGroup()
                        .addGroup(pnlAttendanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlAttendanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlAttendanceLayout.createSequentialGroup()
                        .addComponent(btnCheckIn, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCheckOut, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(134, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout p1DashboardLayout = new javax.swing.GroupLayout(p1Dashboard);
        p1Dashboard.setLayout(p1DashboardLayout);
        p1DashboardLayout.setHorizontalGroup(
            p1DashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p1DashboardLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlProfile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlAttendance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(11, Short.MAX_VALUE))
        );
        p1DashboardLayout.setVerticalGroup(
            p1DashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p1DashboardLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(p1DashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlProfile, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlAttendance, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        centerPanel.add(p1Dashboard, "card2");

        p2EmpDet.setBackground(new java.awt.Color(153, 153, 153));
        p2EmpDet.setMaximumSize(new java.awt.Dimension(1060, 550));
        p2EmpDet.setMinimumSize(new java.awt.Dimension(1060, 550));
        p2EmpDet.setPreferredSize(new java.awt.Dimension(1060, 550));

        pnlbutton.setBackground(new java.awt.Color(204, 204, 204));

        btnJobDetails.setFont(new java.awt.Font("Bahnschrift", 0, 17)); // NOI18N
        btnJobDetails.setText("Job Details");
        btnJobDetails.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        btnJobDetails.setMaximumSize(new java.awt.Dimension(512, 44));
        btnJobDetails.setMinimumSize(new java.awt.Dimension(512, 44));
        btnJobDetails.setPreferredSize(new java.awt.Dimension(512, 44));
        btnJobDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnJobDetailsActionPerformed(evt);
            }
        });

        btnEmployeeInformation.setFont(new java.awt.Font("Bahnschrift", 0, 17)); // NOI18N
        btnEmployeeInformation.setText("Employee Information");
        btnEmployeeInformation.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        btnEmployeeInformation.setMaximumSize(new java.awt.Dimension(512, 44));
        btnEmployeeInformation.setMinimumSize(new java.awt.Dimension(512, 44));
        btnEmployeeInformation.setPreferredSize(new java.awt.Dimension(512, 44));
        btnEmployeeInformation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmployeeInformationActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlbuttonLayout = new javax.swing.GroupLayout(pnlbutton);
        pnlbutton.setLayout(pnlbuttonLayout);
        pnlbuttonLayout.setHorizontalGroup(
            pnlbuttonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlbuttonLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnEmployeeInformation, javax.swing.GroupLayout.PREFERRED_SIZE, 512, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addComponent(btnJobDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 512, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnlbuttonLayout.setVerticalGroup(
            pnlbuttonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlbuttonLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlbuttonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnJobDetails, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnEmployeeInformation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pnlCenter.setBackground(new java.awt.Color(255, 255, 255));
        pnlCenter.setMaximumSize(new java.awt.Dimension(1068, 496));
        pnlCenter.setMinimumSize(new java.awt.Dimension(1068, 496));
        pnlCenter.setPreferredSize(new java.awt.Dimension(1068, 496));
        pnlCenter.setLayout(new java.awt.CardLayout());

        p1EmpInformation.setMaximumSize(new java.awt.Dimension(1060, 500));
        p1EmpInformation.setMinimumSize(new java.awt.Dimension(1060, 500));
        p1EmpInformation.setPreferredSize(new java.awt.Dimension(1060, 500));

        jLabel9.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        jLabel9.setText("SEARCH:");

        txtSearch.setFont(new java.awt.Font("Bahnschrift", 0, 16)); // NOI18N
        txtSearch.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        tblEmployeeInformation.setFont(new java.awt.Font("Bahnschrift", 0, 16));
        if(tableModel != null) {
            tblEmployeeInformation.setModel(tableModel
            );
        }
        jScrollPane1.setViewportView(tblEmployeeInformation);

        jLabel12.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        jLabel12.setText("Phone Number:");

        jLabel13.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        jLabel13.setText("Last Name:");

        jLabel15.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        jLabel15.setText("First Name:");

        jLabel16.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        jLabel16.setText("Birthday:");

        jLabel17.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        jLabel17.setText("Address:");

        txtAdd.setColumns(20);
        txtAdd.setFont(new java.awt.Font("Bahnschrift", 0, 16)); // NOI18N
        txtAdd.setRows(5);
        txtAddress.setViewportView(txtAdd);

        txtLastName.setFont(new java.awt.Font("Bahnschrift", 0, 16)); // NOI18N

        txtFirstName.setFont(new java.awt.Font("Bahnschrift", 0, 16)); // NOI18N
        txtFirstName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFirstNameActionPerformed(evt);
            }
        });

        txtBirthday.setFont(new java.awt.Font("Bahnschrift", 0, 16)); // NOI18N

        txtPNumber.setFont(new java.awt.Font("Bahnschrift", 0, 16)); // NOI18N

        jLabel18.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        jLabel18.setText("Pag-ibig #:");

        jLabel19.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        jLabel19.setText("SSS #:");

        jLabel20.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        jLabel20.setText("TIN #:");

        jLabel21.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        jLabel21.setText("Philhealth #:");

        txtTinNum.setFont(new java.awt.Font("Bahnschrift", 0, 16)); // NOI18N

        txtSSSNum.setFont(new java.awt.Font("Bahnschrift", 0, 16)); // NOI18N

        txtPhilhealthNum.setFont(new java.awt.Font("Bahnschrift", 0, 16)); // NOI18N

        txtPagibigNum.setFont(new java.awt.Font("Bahnschrift", 0, 16)); // NOI18N

        btnAddEmployee.setFont(new java.awt.Font("Bahnschrift", 1, 16)); // NOI18N
        btnAddEmployee.setText("ADD");
        btnAddEmployee.setAlignmentX(0.5F);
        btnAddEmployee.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        btnAddEmployee.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAddEmployee.setMaximumSize(new java.awt.Dimension(92, 27));
        btnAddEmployee.setMinimumSize(new java.awt.Dimension(92, 27));
        btnAddEmployee.setPreferredSize(new java.awt.Dimension(92, 27));
        btnAddEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddEmployeeActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Bahnschrift", 1, 16)); // NOI18N
        jButton2.setText("UPDATE");
        jButton2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jButton3.setFont(new java.awt.Font("Bahnschrift", 1, 16)); // NOI18N
        jButton3.setText("CLEAR");
        jButton3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setMaximumSize(new java.awt.Dimension(92, 27));
        jButton3.setMinimumSize(new java.awt.Dimension(92, 27));
        jButton3.setPreferredSize(new java.awt.Dimension(92, 27));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout p1EmpInformationLayout = new javax.swing.GroupLayout(p1EmpInformation);
        p1EmpInformation.setLayout(p1EmpInformationLayout);
        p1EmpInformationLayout.setHorizontalGroup(
            p1EmpInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p1EmpInformationLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(p1EmpInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1020, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(p1EmpInformationLayout.createSequentialGroup()
                        .addGroup(p1EmpInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12)
                            .addComponent(jLabel17)
                            .addComponent(jLabel13)
                            .addComponent(jLabel15)
                            .addComponent(jLabel16))
                        .addGap(12, 12, 12)
                        .addGroup(p1EmpInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtAddress)
                            .addComponent(txtLastName)
                            .addComponent(txtFirstName)
                            .addComponent(txtBirthday)
                            .addComponent(txtPNumber))
                        .addGroup(p1EmpInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(p1EmpInformationLayout.createSequentialGroup()
                                .addGap(96, 96, 96)
                                .addGroup(p1EmpInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(p1EmpInformationLayout.createSequentialGroup()
                                        .addComponent(jLabel19)
                                        .addGap(82, 82, 82)
                                        .addComponent(txtSSSNum))
                                    .addGroup(p1EmpInformationLayout.createSequentialGroup()
                                        .addComponent(jLabel21)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtPhilhealthNum, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(p1EmpInformationLayout.createSequentialGroup()
                                        .addComponent(jLabel18)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtPagibigNum, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(p1EmpInformationLayout.createSequentialGroup()
                                        .addComponent(jLabel20)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtTinNum, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, p1EmpInformationLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnAddEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(53, 53, 53)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(53, 53, 53)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(48, 48, 48)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(p1EmpInformationLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 435, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        p1EmpInformationLayout.setVerticalGroup(
            p1EmpInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, p1EmpInformationLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(p1EmpInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(p1EmpInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txtLastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19)
                    .addComponent(txtSSSNum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(p1EmpInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(txtFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21)
                    .addComponent(txtPhilhealthNum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(p1EmpInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(txtBirthday, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18)
                    .addComponent(txtPagibigNum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(p1EmpInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(p1EmpInformationLayout.createSequentialGroup()
                        .addGroup(p1EmpInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17)
                            .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(p1EmpInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(txtPNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(13, 13, 13))
                    .addGroup(p1EmpInformationLayout.createSequentialGroup()
                        .addGroup(p1EmpInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel20)
                            .addComponent(txtTinNum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(p1EmpInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAddEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(21, 21, 21)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(83, 83, 83))
        );

        pnlCenter.add(p1EmpInformation, "card2");

        p2JobDetails.setMaximumSize(new java.awt.Dimension(1080, 496));
        p2JobDetails.setMinimumSize(new java.awt.Dimension(1080, 496));

        jLabel10.setText("jLabel10");

        javax.swing.GroupLayout p2JobDetailsLayout = new javax.swing.GroupLayout(p2JobDetails);
        p2JobDetails.setLayout(p2JobDetailsLayout);
        p2JobDetailsLayout.setHorizontalGroup(
            p2JobDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p2JobDetailsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addContainerGap(1031, Short.MAX_VALUE))
        );
        p2JobDetailsLayout.setVerticalGroup(
            p2JobDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p2JobDetailsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addContainerGap(474, Short.MAX_VALUE))
        );

        pnlCenter.add(p2JobDetails, "card3");

        javax.swing.GroupLayout p2EmpDetLayout = new javax.swing.GroupLayout(p2EmpDet);
        p2EmpDet.setLayout(p2EmpDetLayout);
        p2EmpDetLayout.setHorizontalGroup(
            p2EmpDetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, p2EmpDetLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(p2EmpDetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlbutton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlCenter, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        p2EmpDetLayout.setVerticalGroup(
            p2EmpDetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p2EmpDetLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlbutton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlCenter, javax.swing.GroupLayout.PREFERRED_SIZE, 476, Short.MAX_VALUE)
                .addContainerGap())
        );

        centerPanel.add(p2EmpDet, "card3");

        p3EmpTime.setBackground(new java.awt.Color(153, 153, 153));
        p3EmpTime.setMaximumSize(new java.awt.Dimension(1080, 560));
        p3EmpTime.setMinimumSize(new java.awt.Dimension(1080, 560));

        javax.swing.GroupLayout p3EmpTimeLayout = new javax.swing.GroupLayout(p3EmpTime);
        p3EmpTime.setLayout(p3EmpTimeLayout);
        p3EmpTimeLayout.setHorizontalGroup(
            p3EmpTimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1080, Short.MAX_VALUE)
        );
        p3EmpTimeLayout.setVerticalGroup(
            p3EmpTimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 560, Short.MAX_VALUE)
        );

        centerPanel.add(p3EmpTime, "card4");

        p4Payroll.setBackground(new java.awt.Color(153, 153, 153));
        p4Payroll.setMaximumSize(new java.awt.Dimension(1080, 560));
        p4Payroll.setMinimumSize(new java.awt.Dimension(1080, 560));

        javax.swing.GroupLayout p4PayrollLayout = new javax.swing.GroupLayout(p4Payroll);
        p4Payroll.setLayout(p4PayrollLayout);
        p4PayrollLayout.setHorizontalGroup(
            p4PayrollLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1080, Short.MAX_VALUE)
        );
        p4PayrollLayout.setVerticalGroup(
            p4PayrollLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 560, Short.MAX_VALUE)
        );

        centerPanel.add(p4Payroll, "card5");

        mainPanel.add(centerPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 168, -1, 550));

        getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);
        mainPanel.getAccessibleContext().setAccessibleDescription("");

        getAccessibleContext().setAccessibleDescription("");

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnEmployeeInformationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmployeeInformationActionPerformed
        // TODO add your handling code here:
        handleButtonClick(evt, "p1Empinformation", btnEmployeeInformation);
        p1EmpInformation.setVisible(true);
        p2JobDetails.setVisible(false);
    }//GEN-LAST:event_btnEmployeeInformationActionPerformed

    private void btnJobDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnJobDetailsActionPerformed
        // TODO add your handling code here:
        handleButtonClick(evt, "p2JobDetails", btnJobDetails);
        p1EmpInformation.setVisible(false);
        p2JobDetails.setVisible(true);
    }//GEN-LAST:event_btnJobDetailsActionPerformed

    private void btnCheckInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheckInActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCheckInActionPerformed

    private void btnAttachPicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAttachPicActionPerformed
        // TODO add your handling code here:
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        File f = chooser.getSelectedFile();

        filename = f.getAbsolutePath();
        ImageIcon imageIcon = new ImageIcon (new ImageIcon(filename).getImage().getScaledInstance(img.getWidth(), img.getHeight(), Image.SCALE_DEFAULT));
        img.setIcon(imageIcon);

        try{
            File image = new File (filename);
            FileInputStream fix = new FileInputStream(image);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];

            for(int number; (number = fix.read(buf)) != -1;){
                bos.write(buf, 0, number);
            }
            person_image = bos.toByteArray();

        }catch(IOException e){
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_btnAttachPicActionPerformed

    private void btnPayrollActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPayrollActionPerformed
        // TODO add your handling code here
        handleButtonClick(evt, "p4Payroll", btnPayroll);
        p1Dashboard.setVisible(false);
        p2EmpDet.setVisible(false);
        p3EmpTime.setVisible(false);
        p4Payroll.setVisible(true);

    }//GEN-LAST:event_btnPayrollActionPerformed

    private void btnEmpTimesheetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmpTimesheetActionPerformed
        // TODO add your handling code here:
        handleButtonClick(evt, "p3EmpTime", btnEmpTimesheet);
        p1Dashboard.setVisible(false);
        p2EmpDet.setVisible(false);
        p3EmpTime.setVisible(true);
        p4Payroll.setVisible(false);
    }//GEN-LAST:event_btnEmpTimesheetActionPerformed

    private void btnEmpDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmpDetailsActionPerformed
        // TODO add your handling code here:
        handleButtonClick(evt, "p2EmpDet", btnEmpDetails);
        p1Dashboard.setVisible(false);
        p2EmpDet.setVisible(true);
        p3EmpTime.setVisible(false);
        p4Payroll.setVisible(false);
    }//GEN-LAST:event_btnEmpDetailsActionPerformed

    private void btnEmpDashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmpDashActionPerformed
        // TODO add your handling code here:
        handleButtonClick(evt, "p1Dashboard", btnEmpDash);
        p1Dashboard.setVisible(true);
        p2EmpDet.setVisible(false);
        p3EmpTime.setVisible(false);

    }//GEN-LAST:event_btnEmpDashActionPerformed

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        // TODO add your handling code here:
        // Create a new instance of your login frame
        loginForm loginForm = new loginForm();

        // Set the login frame visible
        loginForm.setVisible(true);

        // Dispose the current admin portal frame
        dispose();

    }//GEN-LAST:event_btnLogoutActionPerformed

    private void txtFirstNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFirstNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFirstNameActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void btnAddEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddEmployeeActionPerformed
        // TODO add your handling code here:
        // Retrieve data from text fields and text area
        String lastName = txtLastName.getText();
        String firstName = txtFirstName.getText();
        String birthday = txtBirthday.getText();
        String address = txtAdd.getText();
        String phoneNum = txtPNumber.getText();
        String sssNum = txtSSSNum.getText();
        String philhealthNum = txtPhilhealthNum.getText();
        String pagibigNum = txtPagibigNum.getText();
        String tinNum = txtTinNum.getText();

        // Create an Employee object
        currentEmployee = new Employee(lastName, firstName, birthday, address, phoneNum, sssNum, philhealthNum, pagibigNum, tinNum);

        // Save employee data to MySQL
        saveEmployeeToDatabase(currentEmployee);
    }//GEN-LAST:event_btnAddEmployeeActionPerformed
 
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
        Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (ClassNotFoundException e) {
        e.printStackTrace(); // Add appropriate handling for ClassNotFoundException
    }

    /* Set the Nimbus look and feel */
    try {
        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                javax.swing.UIManager.setLookAndFeel(info.getClassName());
                break;
            }
        }
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
        ex.printStackTrace(); // Add appropriate handling for the LookAndFeel exceptions
    }

    SwingUtilities.invokeLater(AdminPortal::new);
    }
    
    private ImageIcon format = null;
    String filename = null;
    byte[] person_image = null;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddEmployee;
    private javax.swing.JButton btnAttachPic;
    private javax.swing.JButton btnCheckIn;
    private javax.swing.JButton btnCheckOut;
    private javax.swing.JButton btnEmpDash;
    private javax.swing.JButton btnEmpDetails;
    private javax.swing.JButton btnEmpTimesheet;
    private javax.swing.JButton btnEmployeeInformation;
    private javax.swing.JButton btnJobDetails;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnPayroll;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JPanel centerPanel;
    private javax.swing.JLabel img;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblCheckIn;
    private javax.swing.JLabel lblCheckOut;
    private javax.swing.JLabel lblCurrentDate;
    private javax.swing.JLabel lblCurrentTime;
    private javax.swing.JLabel lblEmployeeName;
    private javax.swing.JLabel lblEmployeeRole;
    private javax.swing.JLabel lblGreetings;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel p1Dashboard;
    private javax.swing.JPanel p1EmpInformation;
    private javax.swing.JPanel p2EmpDet;
    private javax.swing.JPanel p2JobDetails;
    private javax.swing.JPanel p3EmpTime;
    private javax.swing.JPanel p4Payroll;
    private javax.swing.JDesktopPane photopanel;
    private javax.swing.JPanel pnlAttendance;
    private javax.swing.JPanel pnlCenter;
    private javax.swing.JPanel pnlProfile;
    private javax.swing.JPanel pnlbutton;
    private javax.swing.JTable tblEmployeeInformation;
    private javax.swing.JPanel topPanel;
    private javax.swing.JTextArea txtAdd;
    private javax.swing.JScrollPane txtAddress;
    private javax.swing.JTextField txtBirthday;
    private javax.swing.JTextField txtFirstName;
    private javax.swing.JTextField txtLastName;
    private javax.swing.JTextField txtPNumber;
    private javax.swing.JTextField txtPagibigNum;
    private javax.swing.JTextField txtPhilhealthNum;
    private javax.swing.JTextField txtSSSNum;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtTinNum;
    // End of variables declaration//GEN-END:variables

    private String getEmployeeFirstName(String loggedInUsername) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
