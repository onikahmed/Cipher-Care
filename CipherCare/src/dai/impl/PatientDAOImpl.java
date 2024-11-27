package dao.impl;

import dao.PatientDAO;
import models.Patient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDAOImpl implements PatientDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/CipherCare";
    private String username;
    private String password;

    public PatientDAOImpl(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public boolean addPatient(Patient patient) {
        String query = "INSERT INTO Patient(PatientDoB, PatientEmail, PatientPhone, PatientAddress) VALUES(?,?,?,?)";
        try (Connection con = DriverManager.getConnection(URL, username, password);
             PreparedStatement statement = con.prepareStatement(query)) {

            statement.setString(1, patient.getDob());
            statement.setString(2, patient.getEmail());
            statement.setString(3, patient.getPhone());
            statement.setString(4, patient.getAddress());

            int updateRows = statement.executeUpdate();
            return updateRows > 0;  // Return true if the insertion was successful
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;  // Return false if the insertion failed
    }

    @Override
    public Patient getPatientById(int patientID) {
        String query = "SELECT * FROM Patient WHERE patientID = ?";
        try (Connection con = DriverManager.getConnection(URL, username, password);
             PreparedStatement statement = con.prepareStatement(query)) {

            statement.setInt(1, patientID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Patient(
                        resultSet.getInt("patientID"),
                        resultSet.getString("PatientDoB"),
                        resultSet.getString("PatientEmail"),
                        resultSet.getString("PatientPhone"),
                        resultSet.getString("PatientAddress")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        String query = "SELECT * FROM Patient";
        try (Connection con = DriverManager.getConnection(URL, username, password);
             Statement statement = con.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                patients.add(new Patient(
                        resultSet.getInt("patientID"),
                        resultSet.getString("PatientDoB"),
                        resultSet.getString("PatientEmail"),
                        resultSet.getString("PatientPhone"),
                        resultSet.getString("PatientAddress")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }

    @Override
    public boolean updatePatient(Patient patient) {
        String query = "UPDATE Patient SET PatientDoB = ?, PatientEmail = ?, PatientPhone = ?, PatientAddress = ? WHERE patientID = ?";
        try (Connection con = DriverManager.getConnection(URL, username, password);
             PreparedStatement statement = con.prepareStatement(query)) {

            statement.setString(1, patient.getDob());
            statement.setString(2, patient.getEmail());
            statement.setString(3, patient.getPhone());
            statement.setString(4, patient.getAddress());
            statement.setInt(5, patient.getId());

            int updateRows = statement.executeUpdate();
            return updateRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deletePatient(int patientID) {
        String query = "DELETE FROM Patient WHERE patientID = ?";
        try (Connection con = DriverManager.getConnection(URL, username, password);
             PreparedStatement statement = con.prepareStatement(query)) {

            statement.setInt(1, patientID);
            int updateRows = statement.executeUpdate();
            return updateRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
