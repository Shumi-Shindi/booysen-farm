package booysenfarm;

import javax.swing.JOptionPane;

public class BooysenFarm {
    public static void main(String[] args) {
        // 1. Initial Connection Test
        try {
            if (FarmDatabase.getConnection() != null) {
                System.out.println("SYSTEM STATUS: Database Connection Successful!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Database Connection Failed! Check if MySQL is running.");
            System.exit(0);
        }

        // 2. Start Background Thread (Stock Monitoring)
        StockMonitor monitor = new StockMonitor();
        monitor.setDaemon(true); 
        monitor.start();

        // 3. Launch Login Window
        java.awt.EventQueue.invokeLater(() -> {
            new Login().setVisible(true);
        });
    }
}