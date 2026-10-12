package booysenfarm;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Login extends JFrame {
    private JTextField txtUser = new JTextField(15);
    private JPasswordField txtPass = new JPasswordField(15);
    private JButton btnLogin = new JButton("Login");

    public Login() {
        setTitle("Booysen Farm Login");
        setSize(300, 320);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Vertical Layout Setup
        JPanel mainPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        mainPanel.add(new JLabel("Username:"));
        mainPanel.add(txtUser);
        mainPanel.add(new JLabel("Password:"));
        mainPanel.add(txtPass);
        mainPanel.add(btnLogin);

        add(mainPanel);
        btnLogin.addActionListener(e -> authenticate());
    }

    private void authenticate() {
        String user = txtUser.getText().trim();
        String pass = new String(txtPass.getPassword());

        try (Connection conn = FarmDatabase.getConnection()) {
            String sql = "SELECT role FROM users WHERE username=? AND password=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user);
            pstmt.setString(2, pass);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");
                
                // Open the updated Dashboard with role AND username
                new Dashboard(role, user).setVisible(true);
                this.dispose(); 
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Username or Password!", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
        }
    }
}