import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;
import javax.swing.Timer;
import javax.swing.Timer;

public class CipherCareMainGUI {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel model;

    public CipherCareMainGUI(String username, String password) {
        // Set up Nimbus Look and Feel
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Set up the frame
        frame = new JFrame("CipherCare - Main");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        initializeUI();
        frame.setVisible(true);
    }
    private void disableTableEditOnDoubleClick() {
        table.setDefaultEditor(Object.class, null); // Disable cell editing
    }

    private void initializeUI() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add Patient Button
        JButton addPatientButton = new JButton("Add Patient");
        addPatientButton.addActionListener(e -> new CipherCareAddGUI());

        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(addPatientButton, gbc);

        // View Patients Button
        JButton viewPatientsButton = new JButton("View Patients");
        viewPatientsButton.addActionListener(e -> viewPatients());

        gbc.gridy = 1;
        mainPanel.add(viewPatientsButton, gbc);

        // Edit Patient Button
        JButton editPatientButton = new JButton("Edit Patient");
        editPatientButton.addActionListener(e -> editPatient());

        gbc.gridy = 2;
        mainPanel.add(editPatientButton, gbc);

        // Delete Patient Button
        JButton deletePatientButton = new JButton("Delete Patient");
        deletePatientButton.addActionListener(e -> deletePatient());

        gbc.gridy = 3;
        mainPanel.add(deletePatientButton, gbc);

        // Search Patient Button
        JButton searchPatientButton = new JButton("Search Patient");
        searchPatientButton.addActionListener(e -> searchPatient());

        gbc.gridy = 4;
        mainPanel.add(searchPatientButton, gbc);

        // Manage Appointments Button
        JButton manageAppointmentsButton = new JButton("Manage Appointments");
        manageAppointmentsButton.addActionListener(e -> new CipherCareAppointmentGUI());

        gbc.gridy = 5;
        mainPanel.add(manageAppointmentsButton, gbc);

        frame.add(mainPanel, BorderLayout.WEST);

        // Table to display patients
        model = new DefaultTableModel();
        String[] columnNames = {"Patient ID", "Date of Birth", "Address", "Email", "Phone Number"};
        model.setColumnIdentifiers(columnNames);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);


        table.addMouseListener(new java.awt.event.MouseAdapter() {
            private long lastClickTime = 0;

            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                long currentTime = System.currentTimeMillis();
                //System.out.println("Mouse clicked! Current time: " + currentTime);

                // Check if the time between clicks is within the double-click threshold
                if (currentTime - lastClickTime < 400) { // 400ms threshold
                    //System.out.println("Double-click detected!");
                    int selectedRow = table.getSelectedRow();
                    //System.out.println("Double-click detected! Selected Row: " + selectedRow);

                    if (selectedRow >= 0) {
                        try {
                            openPatientProfile(selectedRow);
                        } catch (Exception e) {
                            System.err.println("Error while opening patient profile: " + e.getMessage());
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("No valid row selected.");
                    }

                    // Reset lastClickTime after a double-click
                    lastClickTime = 0;
                } else {
                    //System.out.println("Single click detected.");
                    lastClickTime = currentTime; // Update last click time
                }
            }
        });
        disableTableEditOnDoubleClick();

    }
    private void openPatientProfile(int selectedRow) {
        try {
            // Retrieve the data for the selected row
            Object patientID = model.getValueAt(selectedRow, 0);
            Object dob = model.getValueAt(selectedRow, 1);
            Object address = model.getValueAt(selectedRow, 2);
            Object email = model.getValueAt(selectedRow, 3);
            Object phone = model.getValueAt(selectedRow, 4);

            // Fetch the report for this patient from the database
            String patientReport = "";
            try (Connection connection = CipherCareSQL.getConnection()) {
                String query = "SELECT patientReport FROM Patient WHERE patientID = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setInt(1, Integer.parseInt(patientID.toString()));
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        if (resultSet.next()) {
                            patientReport = resultSet.getString("patientReport");
                        }
                    }
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Error retrieving patient report: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }

            // Create the profile window
            JFrame profileFrame = new JFrame("Patient Profile");
            profileFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            profileFrame.setSize(frame.getSize());
            profileFrame.setLocation(frame.getLocation());

            // Use BorderLayout for the frame
            profileFrame.setLayout(new BorderLayout());

            // Create a panel for the patient details
            JPanel detailsPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5); // Small padding
            gbc.anchor = GridBagConstraints.NORTHWEST; // Align to the top-left corner

            // Add labels and values
            gbc.gridx = 0;
            gbc.gridy = 0;
            detailsPanel.add(new JLabel("Patient ID:"), gbc);
            gbc.gridx = 1;
            detailsPanel.add(new JLabel(String.valueOf(patientID)), gbc);

            gbc.gridx = 0;
            gbc.gridy++;
            detailsPanel.add(new JLabel("Date of Birth:"), gbc);
            gbc.gridx = 1;
            detailsPanel.add(new JLabel(dob.toString()), gbc);

            gbc.gridx = 0;
            gbc.gridy++;
            detailsPanel.add(new JLabel("Address:"), gbc);
            gbc.gridx = 1;
            detailsPanel.add(new JLabel(address.toString()), gbc);

            gbc.gridx = 0;
            gbc.gridy++;
            detailsPanel.add(new JLabel("Email:"), gbc);
            gbc.gridx = 1;
            detailsPanel.add(new JLabel(email.toString()), gbc);

            gbc.gridx = 0;
            gbc.gridy++;
            detailsPanel.add(new JLabel("Phone Number:"), gbc);
            gbc.gridx = 1;
            detailsPanel.add(new JLabel(phone.toString()), gbc);

            // Add detailsPanel to the top of the frame
            profileFrame.add(detailsPanel, BorderLayout.NORTH);

            // Create a panel for the text area with a title
            JPanel reportPanel = new JPanel(new BorderLayout());
            JLabel reportTitle = new JLabel("Patient Reports");
            reportTitle.setHorizontalAlignment(SwingConstants.LEFT); // Align the title to the left
            reportTitle.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Add some padding

            // Add the text area for patient report
            JTextArea reportTextArea = new JTextArea(10, 40); // 10 rows, 40 columns
            reportTextArea.setLineWrap(true);
            reportTextArea.setWrapStyleWord(true);
            reportTextArea.setText(patientReport); // Set the fetched report
            JScrollPane scrollPane = new JScrollPane(reportTextArea); // Add scrolling support

            // Add title and text area to the report panel
            reportPanel.add(reportTitle, BorderLayout.NORTH);
            reportPanel.add(scrollPane, BorderLayout.CENTER);

            // Add the report panel to the frame
            profileFrame.add(reportPanel, BorderLayout.CENTER);


            JButton saveButton = new JButton("Save");
            saveButton.setPreferredSize(new Dimension(80, 30)); // Small button dimensions
            saveButton.addActionListener(e -> {
                String report = reportTextArea.getText();
                if (!report.trim().isEmpty()) {
                    try (Connection connection = CipherCareSQL.getConnection()) {
                        String query = "UPDATE Patient SET patientReport = ? WHERE patientID = ?";
                        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                            preparedStatement.setString(1, report); // Set the report text
                            preparedStatement.setInt(2, Integer.parseInt(patientID.toString())); // Set the patient ID
                            int rowsUpdated = preparedStatement.executeUpdate();
                            if (rowsUpdated > 0) {
                                JOptionPane.showMessageDialog(profileFrame, "Report saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(profileFrame, "Failed to save report.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(profileFrame, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(profileFrame, "Report cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            // Add Save button to a smaller JPanel at the bottom
            JPanel buttonPanel = new JPanel();
            buttonPanel.add(saveButton); // Center the button in the panel
            profileFrame.add(buttonPanel, BorderLayout.SOUTH);

            // Display the profile window
            profileFrame.setVisible(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame,
                    "An error occurred while opening the patient profile.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }





    private void viewPatients() {
        model.setRowCount(0); // Clear existing rows

        try (Connection connection = CipherCareSQL.getConnection()) {
            String query = "SELECT patientID, PatientDoB, patientAddress, patientEmail, patientPhone FROM Patient";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    int id = resultSet.getInt("patientID");
                    String dob = resultSet.getString("PatientDoB");
                    String address = resultSet.getString("patientAddress");
                    String email = resultSet.getString("patientEmail");
                    String phone = resultSet.getString("patientPhone");

                    // Log the retrieved row for debugging
                    //System.out.println("Row: " + id + ", " + dob + ", " + address + ", " + email + ", " + phone);

                    model.addRow(new Object[]{id, dob, address, email, phone});
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Failed to retrieve patient records: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    private void editPatient() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a patient to edit.", "Edit Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int patientID = (int) model.getValueAt(selectedRow, 0);
        String dob = (String) model.getValueAt(selectedRow, 1);
        String address = (String) model.getValueAt(selectedRow, 2);
        String email = (String) model.getValueAt(selectedRow, 3);
        String phone = (String) model.getValueAt(selectedRow, 4);

        JTextField dobField = new JTextField(dob);
        JTextField addressField = new JTextField(address);
        JTextField emailField = new JTextField(email);
        JTextField phoneField = new JTextField(phone);

        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("Date of Birth:"));
        panel.add(dobField);
        panel.add(new JLabel("Address:"));
        panel.add(addressField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Phone Number:"));
        panel.add(phoneField);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Edit Patient", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String newDob = dobField.getText().trim();
            String newAddress = addressField.getText().trim();
            String newEmail = emailField.getText().trim();
            String newPhone = phoneField.getText().trim();

            try (Connection connection = CipherCareSQL.getConnection()) {
                String query = "UPDATE Patient SET PatientDoB = ?, patientAddress = ?, patientEmail = ?, patientPhone = ? WHERE patientID = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, newDob);
                    preparedStatement.setString(2, newAddress);
                    preparedStatement.setString(3, newEmail);
                    preparedStatement.setString(4, newPhone);
                    preparedStatement.setInt(5, patientID);
                    preparedStatement.executeUpdate();
                    JOptionPane.showMessageDialog(frame, "Patient updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    viewPatients(); // Refresh the patient list
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(frame, "Failed to update patient: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void deletePatient() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a patient to delete.", "Delete Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int patientID = (int) model.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this patient?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection connection = CipherCareSQL.getConnection()) {
                String query = "DELETE FROM Patient WHERE patientID = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setInt(1, patientID);
                    preparedStatement.executeUpdate();
                    model.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(frame, "Patient deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(frame, "Failed to delete patient: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void searchPatient() {
        String searchTerm = JOptionPane.showInputDialog(frame, "Enter patient name, email, or phone number to search:", "Search Patient", JOptionPane.QUESTION_MESSAGE);
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return;
        }

        model.setRowCount(0); // Clear existing rows

        try (Connection connection = CipherCareSQL.getConnection()) {
            String query = "SELECT patientID, PatientDoB, patientAddress, patientEmail, patientPhone FROM Patient WHERE patientEmail LIKE ? OR patientPhone LIKE ? OR patientAddress LIKE ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, "%" + searchTerm + "%");
                preparedStatement.setString(2, "%" + searchTerm + "%");
                preparedStatement.setString(3, "%" + searchTerm + "%");
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int id = resultSet.getInt("patientID");
                        String dob = resultSet.getString("PatientDoB");
                        String address = resultSet.getString("patientAddress");
                        String email = resultSet.getString("patientEmail");
                        String phone = resultSet.getString("patientPhone");

                        model.addRow(new Object[]{id, dob, address, email, phone});
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Failed to search patient records: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
