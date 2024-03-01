package payroll;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class MyConnection {
    private Connection con;

    public Connection connect() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/motor_ph", "root", "dmns!!404");

            // Check if the connection is successful before showing the dialog
            if (con != null && !con.isClosed()) {
                showSuccessDialog();
            }

        } catch (ClassNotFoundException | SQLException ex) {
            // Display error dialog
            JOptionPane.showMessageDialog(null, "Database connection error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            throw new SQLException("Database connection error: " + ex.getMessage());
        }
        return con;
    }

    public void closeConnection() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException ex) {
            // Display error dialog
            JOptionPane.showMessageDialog(null, "Error closing database connection: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showSuccessDialog() {
        // Display success dialog
        JOptionPane.showMessageDialog(null, "Database connection successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}