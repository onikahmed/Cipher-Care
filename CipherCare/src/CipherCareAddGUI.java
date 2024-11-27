import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CipherCareAddGUI {
    private JFrame frame;
    private JTextField dobField;
    private JTextField addressField;
    private JTextField emailField;
    private JTextField phoneField;

    // No-argument constructor
    public CipherCareAddGUI() {
        setupGUI();
    }

    // Constructor with arguments to pre-fill data
    public CipherCareAddGUI(String dob, String address, String email) {
        setupGUI();
        dobField.setText(dob);
        addressField.setText(address);
        emailField.setText(email);
    }

    // Method to initialize GUI components
    private void setupGUI() {
        // Set up Nimbus Look and Feel
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Set up the frame
        frame = new JFrame("CipherCare - Add Patient");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new GridBagLayout());

        initializeUI();
        frame.setVisible(true);
    }

    private void initializeUI() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Patient Date of Birth Field (small size)
        JLabel dobLabel = new JLabel("Patient Date of Birth (dd-mm-yyyy):");
        dobField = new JTextField(10); // Smaller size for Date of Birth

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        frame.add(dobLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        frame.add(dobField, gbc);

        // Patient Address Field (larger size)
        JLabel addressLabel = new JLabel("Patient Address:");
        addressField = new JTextField(20); // Larger size for Address

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        frame.add(addressLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        frame.add(addressField, gbc);

        // Patient Email Field (larger size)
        JLabel emailLabel = new JLabel("Patient Email:");
        emailField = new JTextField(20); // Larger size for Email

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        frame.add(emailLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        frame.add(emailField, gbc);

        // Patient Phone Number Field (larger size)
        JLabel phoneLabel = new JLabel("Patient Phone Number:");
        phoneField = new JTextField(15); // Larger size for Phone Number

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.LINE_END;
        frame.add(phoneLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        frame.add(phoneField, gbc);

        // Add Patient Button
        JButton addPatientButton = new JButton("Add Patient");
        addPatientButton.setToolTipText("Click to add a new patient");
        addPatientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPatient();
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        frame.add(addPatientButton, gbc);
    }

    private void addPatient() {
        // Get input data from fields
        String dob = dobField.getText().trim();
        String address = addressField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();

        // Validate and process the data
        if (dob.isEmpty() || address.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "All fields must be filled out.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Format date of birth to YYYY-MM-DD (required by MySQL)
            String formattedDob = dob.replaceAll("-", "/");
            String[] dateParts = formattedDob.split("/");
            if (dateParts.length == 3) {
                formattedDob = dateParts[2] + "-" + dateParts[1] + "-" + dateParts[0];
            } else {
                JOptionPane.showMessageDialog(frame, "Date format should be dd-mm-yyyy.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Save the patient information to the database
            Connection connection = CipherCareSQL.getConnection(); // Assuming CipherCareSQL has a method to get DB connection
            String query = "INSERT INTO Patient (PatientDoB, patientAddress, patientEmail, patientPhone) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, formattedDob);
            preparedStatement.setString(2, address);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, phone);
            preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();

            JOptionPane.showMessageDialog(frame, "Patient added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            // Clear the fields after successful addition
            dobField.setText("");
            addressField.setText("");
            emailField.setText("");
            phoneField.setText("");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Failed to add patient. Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CipherCareAddGUI());
    }
}
