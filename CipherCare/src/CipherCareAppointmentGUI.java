import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

public class CipherCareAppointmentGUI {
    private JFrame frame;
    private JTable appointmentTable;
    private DefaultTableModel appointmentModel;

    public CipherCareAppointmentGUI() {
        // Set up Nimbus Look and Feel
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Set up the frame
        frame = new JFrame("CipherCare - Manage Appointments");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        initializeUI();
        frame.setVisible(true);
    }

    private void initializeUI() {
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add Appointment Button
        JButton addAppointmentButton = new JButton("Add Appointment");
        addAppointmentButton.addActionListener(e -> addAppointment());

        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonPanel.add(addAppointmentButton, gbc);

        // Edit Appointment Button
        JButton editAppointmentButton = new JButton("Edit Appointment");
        editAppointmentButton.addActionListener(e -> editAppointment());

        gbc.gridy = 1;
        buttonPanel.add(editAppointmentButton, gbc);

        // Delete Appointment Button
        JButton deleteAppointmentButton = new JButton("Delete Appointment");
        deleteAppointmentButton.addActionListener(e -> deleteAppointment());

        gbc.gridy = 2;
        buttonPanel.add(deleteAppointmentButton, gbc);

        frame.add(buttonPanel, BorderLayout.WEST);

        // Table to display appointments
        appointmentModel = new DefaultTableModel();
        String[] columnNames = {"Appointment ID", "Record ID", "Telehealth ID", "Date", "Start Time", "End Time"};
        appointmentModel.setColumnIdentifiers(columnNames);
        appointmentTable = new JTable(appointmentModel);
        JScrollPane scrollPane = new JScrollPane(appointmentTable);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Load appointments initially
        loadAppointments();
    }

    private void loadAppointments() {
        appointmentModel.setRowCount(0); // Clear existing rows

        try (Connection connection = CipherCareSQL.getConnection()) {
            String query = "SELECT * FROM Appointment";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    int appointmentID = resultSet.getInt("appointmentID");
                    int recordID = resultSet.getInt("recordID");
                    int telehealthID = resultSet.getInt("telehealthID");
                    String date = resultSet.getString("date");
                    String startTime = resultSet.getString("startTime");
                    String endTime = resultSet.getString("endTime");

                    appointmentModel.addRow(new Object[]{appointmentID, recordID, telehealthID, date, startTime, endTime});
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Failed to load appointments: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void addAppointment() {
        // Implement add appointment logic (e.g., show dialog for input, insert into database)
    }

    private void editAppointment() {
        // Implement edit appointment logic (e.g., select row, show details in dialog, update database)
    }

    private void deleteAppointment() {
        int selectedRow = appointmentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select an appointment to delete.", "Delete Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int appointmentID = (int) appointmentModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this appointment?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection connection = CipherCareSQL.getConnection()) {
                String query = "DELETE FROM Appointment WHERE appointmentID = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setInt(1, appointmentID);
                    preparedStatement.executeUpdate();
                    appointmentModel.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(frame, "Appointment deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(frame, "Failed to delete appointment: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
}
