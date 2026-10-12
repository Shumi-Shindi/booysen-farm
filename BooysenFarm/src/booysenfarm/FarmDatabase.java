package booysenfarm;

import java.sql.*;
import java.util.ArrayList; // Requirement: Collections

public class FarmDatabase {
    // 1. DATABASE CREDENTIALS
    private static final String URL = "jdbc:mysql://localhost:3306/booysen_farm";
    private static final String USER = "root";
    private static final String PASS = "YOUR_DB_PASSWORD";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    // 2. REQUIREMENT 1: ANIMAL RECORD KEEPING (JDBC & OOP)
    public void saveAnimal(Animal a) throws SQLException {
        String sql = "INSERT INTO animals VALUES (?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE status=?, weight=?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, a.getTagId()); 
            pstmt.setString(2, a.getSpecies());
            pstmt.setString(3, a.getBreed()); 
            pstmt.setString(4, a.getGender());
            pstmt.setString(5, a.getStatus()); 
            pstmt.setDouble(6, a.getWeight());
            pstmt.setString(7, a.getLastVac()); 
            
            // Updates if animal already exists
            pstmt.setString(8, a.getStatus());
            pstmt.setDouble(9, a.getWeight());
            pstmt.executeUpdate();
        }
    }

    // 3. REQUIREMENT 3: HEALTH & TASK MANAGEMENT (Staff Tracking)
    public void addMedicalRecord(String tag, String staff, String treat) throws SQLException {
        String sql = "INSERT INTO medical_records (animal_tag, staff_member, treatment) VALUES (?,?,?)";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tag); 
            pstmt.setString(2, staff); 
            pstmt.setString(3, treat);
            pstmt.executeUpdate();
        }
    }

    // 4. REQUIREMENT 2: INVENTORY TRACKING
    public void updateStock(String item, int qtyUsed) throws SQLException {
        String sql = "UPDATE inventory SET quantity = quantity - ? WHERE item_name = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, qtyUsed); 
            pstmt.setString(2, item);
            pstmt.executeUpdate();
        }
    }

    // 5. REQUIREMENT: SECURITY & ATTENDANCE
    public void markAttendance(String user) {
        try (Connection conn = getConnection(); PreparedStatement p = conn.prepareStatement("INSERT INTO attendance (username) VALUES (?)")) {
            p.setString(1, user); 
            p.executeUpdate();
        } catch (Exception e) {
            System.out.println("Attendance Error: " + e.getMessage());
        }
    }

    // --- INCORPORATING ARRAYLIST (TECHNICAL REQUIREMENT) ---
    public String getAttendanceLog() {
        // We use an ArrayList here because the log size is dynamic (Requirement: Collections)
        ArrayList<String> logEntries = new ArrayList<>();
        
        String sql = "SELECT * FROM attendance ORDER BY check_in_time DESC";
        try (Connection conn = getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                String record = rs.getTimestamp("check_in_time") + " - User: " + rs.getString("username");
                logEntries.add(record); // Adding to dynamic collection
            }
        } catch (Exception e) { 
            return "Error loading log."; 
        }

        // Processing the ArrayList into a single string for the Dashboard UI
        StringBuilder finalLog = new StringBuilder("--- WORKER ATTENDANCE LOG ---\n");
        for (String entry : logEntries) {
            finalLog.append(entry).append("\n");
        }
        return finalLog.toString();
    }

    // 6. ANIMAL HISTORY SEARCH (Vet Requirement)
    public String getAnimalHistory(String tag) {
        try (Connection conn = getConnection()) {
            PreparedStatement p = conn.prepareStatement("SELECT * FROM animals WHERE tag_id=?");
            p.setString(1, tag); 
            ResultSet rs = p.executeQuery();
            if(rs.next()) {
                return "Tag: " + rs.getString("tag_id") + 
                       "\nSpecies: " + rs.getString("species") +
                       "\nBreed: " + rs.getString("breed") +
                       "\nStatus: " + rs.getString("status") +
                       "\nLast Vac: " + rs.getString("last_vac");
            }
        } catch (Exception e) {
            return "Database search error.";
        }
        return "Animal not found.";
    }
}