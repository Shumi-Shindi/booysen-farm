package booysenfarm;

import java.sql.*;
import javax.swing.JOptionPane;

public class StockMonitor extends Thread {
    @Override
    public void run() {
        while (true) {
            try {
                Connection conn = FarmDatabase.getConnection();
                // Checks if any inventory item is below its safe threshold
                String sql = "SELECT item_name, quantity FROM inventory WHERE quantity < min_threshold";
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql);

                if (rs.next()) {
                    String item = rs.getString("item_name");
                    int qty = rs.getInt("quantity");
                    // Trigger popup alert
                    JOptionPane.showMessageDialog(null, 
                        "LOW STOCK ALERT: " + item + " is running low (" + qty + " left)!", 
                        "Storehouse Warning", JOptionPane.WARNING_MESSAGE);
                }
                conn.close();
                Thread.sleep(60000); // Wait 1 minute
            } catch (Exception e) {
                System.out.println("Stock Monitor Error: " + e.getMessage());
            }
        }
    }
}