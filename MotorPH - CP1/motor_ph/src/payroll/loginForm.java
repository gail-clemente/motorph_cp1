package payroll;

import java.awt.HeadlessException;
import javax.swing.JOptionPane;
import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;



public class loginForm extends javax.swing.JFrame {
    
MyConnection con;
PreparedStatement pst;
ResultSet rs;

    public loginForm() {
        initComponents();
        clearbtn.addActionListener(this::clearbtnActionPerformed);
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        mainPanel = new javax.swing.JPanel();
        header = new javax.swing.JPanel();
        titlename = new javax.swing.JLabel();
        footer = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        rolelbl = new javax.swing.JLabel();
        loginlbl = new javax.swing.JLabel();
        usernamelbl = new javax.swing.JLabel();
        passwordlbl = new javax.swing.JLabel();
        usernametxt = new javax.swing.JTextField();
        clearbtn = new javax.swing.JButton();
        loginbtn = new javax.swing.JButton();
        rolecmb = new javax.swing.JComboBox<>();
        passwordtxt = new javax.swing.JPasswordField();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jList1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        mainPanel.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(102, 102, 102)));
        mainPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        header.setBackground(new java.awt.Color(102, 102, 102));

        titlename.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        titlename.setForeground(new java.awt.Color(255, 255, 255));
        titlename.setLabelFor(header);
        titlename.setText("MotorPH's Employee and Payroll System");

        javax.swing.GroupLayout headerLayout = new javax.swing.GroupLayout(header);
        header.setLayout(headerLayout);
        headerLayout.setHorizontalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, headerLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(titlename)
                .addGap(31, 31, 31))
        );
        headerLayout.setVerticalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(titlename, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(38, Short.MAX_VALUE))
        );

        mainPanel.add(header, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 1, 398, 100));

        footer.setBackground(new java.awt.Color(102, 102, 102));

        jLabel1.setFont(new java.awt.Font("Bahnschrift", 0, 10)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Office Hours: 8:30am–5:30pm, Monday through Saturday");

        jLabel2.setFont(new java.awt.Font("Bahnschrift", 0, 10)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Office Address: 7 Jupiter Avenue cor. F. Sandoval Jr., Bagong Nayon, Quezon City");

        jLabel3.setFont(new java.awt.Font("Bahnschrift", 0, 10)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Phone: (028) 911-5071 / (028) 911-5072 / (028) 911-5073");

        jLabel4.setFont(new java.awt.Font("Bahnschrift", 0, 10)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Email: corporate@motorph.com");

        javax.swing.GroupLayout footerLayout = new javax.swing.GroupLayout(footer);
        footer.setLayout(footerLayout);
        footerLayout.setHorizontalGroup(
            footerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(footerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(footerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        footerLayout.setVerticalGroup(
            footerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, footerLayout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addGap(14, 14, 14))
        );

        mainPanel.add(footer, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 300, 400, 100));

        rolelbl.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        rolelbl.setText("Role:");
        mainPanel.add(rolelbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 200, -1, 23));

        loginlbl.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        loginlbl.setText("Login");
        mainPanel.add(loginlbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 110, -1, -1));

        usernamelbl.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        usernamelbl.setText("Username:");
        mainPanel.add(usernamelbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 140, 70, 23));

        passwordlbl.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        passwordlbl.setText("Password:");
        mainPanel.add(passwordlbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 170, -1, 23));

        usernametxt.setFont(new java.awt.Font("Bahnschrift", 0, 12)); // NOI18N
        usernametxt.setToolTipText("");
        usernametxt.setName(""); // NOI18N
        usernametxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usernametxtActionPerformed(evt);
            }
        });
        mainPanel.add(usernametxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 140, 190, 25));

        clearbtn.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.borderColor"));
        clearbtn.setFont(new java.awt.Font("Bahnschrift", 0, 13)); // NOI18N
        clearbtn.setForeground(new java.awt.Color(51, 51, 51));
        clearbtn.setText("Clear");
        clearbtn.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        clearbtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        clearbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearbtnActionPerformed(evt);
            }
        });
        mainPanel.add(clearbtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 240, 66, 24));

        loginbtn.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.borderColor"));
        loginbtn.setFont(new java.awt.Font("Bahnschrift", 0, 13)); // NOI18N
        loginbtn.setForeground(new java.awt.Color(51, 51, 51));
        loginbtn.setText("Login");
        loginbtn.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        loginbtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        loginbtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                loginbtnMouseClicked(evt);
            }
        });
        loginbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginbtnActionPerformed(evt);
            }
        });
        mainPanel.add(loginbtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 240, 66, 24));

        rolecmb.setFont(new java.awt.Font("Bahnschrift", 0, 12)); // NOI18N
        rolecmb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Admin", "Non-admin" }));
        rolecmb.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rolecmb.setName(""); // NOI18N
        mainPanel.add(rolecmb, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 200, 190, 25));

        passwordtxt.setFont(new java.awt.Font("Bahnschrift", 0, 12)); // NOI18N
        passwordtxt.setName(""); // NOI18N
        mainPanel.add(passwordtxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 170, 190, 25));

        getContentPane().add(mainPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 400, 400));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void usernametxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usernametxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_usernametxtActionPerformed

    private void clearbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearbtnActionPerformed
        // TODO add your handling code here:
            // Clear the text fields
    usernametxt.setText("");
    passwordtxt.setText("");
    rolecmb.setSelectedItem("Admin");
    }//GEN-LAST:event_clearbtnActionPerformed

    private void loginbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginbtnActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_loginbtnActionPerformed

    private void loginbtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_loginbtnMouseClicked
         // TODO add your handling code here:
    String untxt = usernametxt.getText();
    String pass = String.valueOf(passwordtxt.getPassword());
    String role = rolecmb.getSelectedItem().toString();

    if (untxt.equals("") || pass.equals("") || role.equals("Select")) {
        JOptionPane.showMessageDialog(rootPane, "Some Fields are Empty.", "Error", JOptionPane.ERROR_MESSAGE);
    } else {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            MyConnection myConnection = new MyConnection();
            Connection con = myConnection.connect();
            pst = con.prepareStatement("select * from login where username = ? and password = ? and role = ?");

            pst.setString(1, untxt);
            pst.setString(2, pass);
            pst.setString(3, role);

            rs = pst.executeQuery();

            if (rs.next()) {
                String userRole = rs.getString("role");
                if (role.equalsIgnoreCase("Admin") && userRole.equalsIgnoreCase("Admin")) {
                    AdminPortal ad = new AdminPortal(untxt);
                    ad.setVisible(true);
                    setVisible(false);

                    // Notify the user about successful login
                    JOptionPane.showMessageDialog(rootPane, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    
                } else if (role.equalsIgnoreCase("Non-admin") && userRole.equalsIgnoreCase("Non-admin")) {
                    NonAdminPortal nonAdminPortal = new NonAdminPortal(untxt); 
                    nonAdminPortal.setVisible(true);
                    setVisible(false);

                    // Notify the user about successful login
                    JOptionPane.showMessageDialog(rootPane, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // Notify the user about invalid role
                    JOptionPane.showMessageDialog(rootPane, "Invalid role for the selected user.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // Notify the user about unsuccessful login
                JOptionPane.showMessageDialog(rootPane, "Invalid credentials.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (HeadlessException | ClassNotFoundException | SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, "Error during login: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            // Close the connection after usage
            new MyConnection().closeConnection();
        }
    }
    }//GEN-LAST:event_loginbtnMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(loginForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(loginForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(loginForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(loginForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new loginForm().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton clearbtn;
    private javax.swing.JPanel footer;
    private javax.swing.JPanel header;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JList<String> jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton loginbtn;
    private javax.swing.JLabel loginlbl;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JLabel passwordlbl;
    private javax.swing.JPasswordField passwordtxt;
    private javax.swing.JComboBox<String> rolecmb;
    private javax.swing.JLabel rolelbl;
    private javax.swing.JLabel titlename;
    private javax.swing.JLabel usernamelbl;
    private javax.swing.JTextField usernametxt;
    // End of variables declaration//GEN-END:variables
}
