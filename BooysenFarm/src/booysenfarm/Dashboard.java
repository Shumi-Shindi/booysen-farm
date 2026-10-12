package booysenfarm;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Dashboard extends JFrame {
    private String currentUsername, userRole;
    
    // Components
    private JTextField tTag = new JTextField(20), tBreed = new JTextField(20), tWeight = new JTextField(20), tVac = new JTextField(20);
    private JTextField tSearchTag = new JTextField(20), tStaff = new JTextField(20), tTreat = new JTextField(20), tStockQty = new JTextField(20);
    private JComboBox<String> cSpecies = new JComboBox<>(new String[]{"Cattle", "Sheep", "Poultry"});
    private JComboBox<String> cGen = new JComboBox<>(new String[]{"Male", "Female"});
    private JComboBox<String> cStat = new JComboBox<>(new String[]{"Healthy", "Sick", "Sold", "Died", "Lost", "Stolen"});
    private JComboBox<String> cInv = new JComboBox<>(new String[]{"Cattle Feed", "Vaccine A", "Sheep Dip", "Tools"});
    
    private JButton btnSave = new JButton("Register Animal Record"), btnSearch = new JButton("Search History");
    private JButton btnLogout = new JButton("Logout"), btnCheckIn = new JButton("Check-In");
    private JButton btnMed = new JButton("Log Medical Treatment"), btnStock = new JButton("Update Storehouse");
    private JButton btnRep = new JButton("Generate Bank Report"), btnView = new JButton("View Last Report");

    public Dashboard(String role, String username) {
        this.userRole = role; 
        this.currentUsername = username;
        
        setTitle("Booysen Farm System - Portal: " + role);
        setSize(600, 900);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Root Panel
        JPanel root = new JPanel(new BorderLayout());
        
        // 1. Header
        JPanel header = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        header.add(new JLabel("User: " + username + " (" + role + ") "));
        if(role.equalsIgnoreCase("Employee")) header.add(btnCheckIn);
        header.add(btnLogout);
        root.add(header, BorderLayout.NORTH);

        // 2. Main Content (Scrollable)
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));

        // Search Section
        mainContent.add(createSection("Search History", new JComponent[]{new JLabel("Tag ID:"), tSearchTag, btnSearch}));
        
        // Registration Section
        mainContent.add(createSection("Livestock Registration", new JComponent[]{
            new JLabel("Tag:"), tTag, new JLabel("Breed:"), tBreed, 
            new JLabel("Weight:"), tWeight, new JLabel("Vac Date:"), tVac,
            new JLabel("Species:"), cSpecies, new JLabel("Gender:"), cGen, 
            new JLabel("Status:"), cStat, new JLabel("Action:"), btnSave
        }));

        // Medical Section
        mainContent.add(createSection("Medical Tracking", new JComponent[]{
            new JLabel("Staff:"), tStaff, new JLabel("Treatment:"), tTreat, new JLabel("Action:"), btnMed
        }));

        // Inventory Section
        mainContent.add(createSection("Inventory Management", new JComponent[]{
            new JLabel("Item:"), cInv, new JLabel("Qty Used:"), tStockQty, new JLabel("Action:"), btnStock
        }));

        // --- THE FIX: OWNER PORTAL ---
        if (role.equalsIgnoreCase("Owner") || role.equalsIgnoreCase("admin")) {
            JPanel ownerPanel = new JPanel(new GridLayout(1, 2, 10, 10));
            ownerPanel.setBorder(BorderFactory.createTitledBorder("Owner Management Portal"));
            ownerPanel.setBackground(new Color(220, 240, 255));
            ownerPanel.add(btnRep);
            ownerPanel.add(btnView);
            mainContent.add(ownerPanel);
        }

        root.add(new JScrollPane(mainContent), BorderLayout.CENTER);
        add(root);
        
        setupActions();
    }

    private JPanel createSection(String title, JComponent[] comps) {
        JPanel p = new JPanel(new GridLayout(0, 2, 5, 5));
        p.setBorder(BorderFactory.createTitledBorder(title));
        for (JComponent c : comps) p.add(c);
        return p;
    }

    private void setupActions() {
        btnLogout.addActionListener(e -> { new Login().setVisible(true); this.dispose(); });
        
        btnSearch.addActionListener(e -> JOptionPane.showMessageDialog(this, new FarmDatabase().getAnimalHistory(tSearchTag.getText().trim())));

        btnSave.addActionListener(e -> {
            try {
                Animal a; String s = (String)cSpecies.getSelectedItem();
                if(s.equals("Cattle")) a = new Cattle(tTag.getText(), tBreed.getText(), (String)cGen.getSelectedItem(), (String)cStat.getSelectedItem(), Double.parseDouble(tWeight.getText()), tVac.getText());
                else if(s.equals("Sheep")) a = new Sheep(tTag.getText(), tBreed.getText(), (String)cGen.getSelectedItem(), (String)cStat.getSelectedItem(), Double.parseDouble(tWeight.getText()), tVac.getText());
                else a = new Poultry(tTag.getText(), tBreed.getText(), (String)cGen.getSelectedItem(), (String)cStat.getSelectedItem(), Double.parseDouble(tWeight.getText()), tVac.getText());
                new FarmDatabase().saveAnimal(a);
                JOptionPane.showMessageDialog(this, "Success: Animal Registered!");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error: Check your inputs."); }
        });

        btnMed.addActionListener(e -> {
            try {
                new FarmDatabase().addMedicalRecord(tTag.getText(), tStaff.getText(), tTreat.getText());
                JOptionPane.showMessageDialog(this, "Medical Record Added!");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
        });

        btnStock.addActionListener(e -> {
            try {
                new FarmDatabase().updateStock((String)cInv.getSelectedItem(), Integer.parseInt(tStockQty.getText().trim()));
                JOptionPane.showMessageDialog(this, "Inventory Updated!");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Quantity Error!"); }
        });

        btnCheckIn.addActionListener(e -> { new FarmDatabase().markAttendance(currentUsername); btnCheckIn.setEnabled(false); });
        
        btnRep.addActionListener(e -> {
            try {
                ReportGenerator.generateMonthlyBankReport();
                JOptionPane.showMessageDialog(this, "Report Created in project folder!");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Report Error!"); }
        });

        btnView.addActionListener(e -> {
            try {
                String c = new String(Files.readAllBytes(Paths.get("Bank_Report.txt")));
                JOptionPane.showMessageDialog(this, new JScrollPane(new JTextArea(c, 20, 50)), "Farm Status Report", JOptionPane.PLAIN_MESSAGE);
            } catch (IOException ex) { JOptionPane.showMessageDialog(this, "No report file found. Click Generate first."); }
        });
    }
}