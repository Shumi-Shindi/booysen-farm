package booysenfarm;

import java.io.*;
import java.sql.*;

public class ReportGenerator {

    public static void generateMonthlyBankReport() throws Exception {
        // This creates/overwrites the file Bank_Report.txt in the project folder
        try (PrintWriter writer = new PrintWriter(new FileWriter("Bank_Report.txt"))) {
            Connection conn = FarmDatabase.getConnection();
            
            writer.println("==========================================");
            writer.println("      GREEN PASTURES - MONTHLY REPORT     ");
            writer.println("==========================================");
            writer.println("Generated on: " + new java.util.Date());
            writer.println("Owner: Mr. Van Booysen");
            writer.println();

            // 1. LIVESTOCK TOTALS (Requirement 4.2)
            Statement stmt = conn.createStatement();
            ResultSet rsTotal = stmt.executeQuery("SELECT COUNT(*) FROM animals");
            if (rsTotal.next()) {
                writer.println("TOTAL ANIMALS CURRENTLY ON FARM: " + rsTotal.getInt(1));
            }
            writer.println("------------------------------------------");

            // 2. STATUS REPORT - DIED, LOST, STOLEN (Requirement 4.3)
            writer.println("LIVESTOCK STATUS SUMMARY:");
            String[] statuses = {"Healthy", "Sick", "Sold", "Died", "Lost", "Stolen"};
            for (String s : statuses) {
                // We use a PreparedStatement for security and accuracy
                PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM animals WHERE status = ?");
                ps.setString(1, s);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    writer.println("- Total " + s + ": " + rs.getInt(1));
                }
            }

            // 3. INVENTORY LEVELS (Requirement 2)
            writer.println("\nSTOREHOUSE INVENTORY STATUS:");
            writer.println("------------------------------------------");
            ResultSet rsInv = stmt.executeQuery("SELECT item_name, quantity FROM inventory");
            while (rsInv.next()) {
                writer.println("- " + rsInv.getString("item_name") + ": " + rsInv.getInt("quantity") + " units");
            }

            // 4. SIGN-OFF
            writer.println("\n==========================================");
            writer.println("Verified by: __________________________");
            writer.println("             Farm Management System");
            
            conn.close();
            System.out.println("SUCCESS: Bank_Report.txt generated.");
        }
    }
}