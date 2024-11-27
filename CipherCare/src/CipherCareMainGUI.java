import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

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
