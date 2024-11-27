package controllers;

import dao.PatientDAO;
import dao.impl.PatientDAOImpl;
import models.Patient;

import javax.swing.*;
import java.util.List;

public class PatientController {
    private PatientDAO patientDAO;

    public PatientController(String username, String password) {
        // Instantiate DAO with credentials
        this.patientDAO = new PatientDAOImpl(username, password);
    }

    // Method to add a patient
    public boolean addPatient(String dob, String email, String phone, String address) {
        if (dob.isEmpty() || address.isEmpty() || (phone.isEmpty() && email.isEmpty())) {
            JOptionPane.showMessageDialog(null, "Please fill in all required fields: Date of Birth, Address, Phone Number, and Email.");
            return false;
        }

        Patient patient = new Patient(dob, email, phone, address);
        boolean success = patientDAO.addPatient(patient);

        if (success) {
            JOptionPane.showMessageDialog(null, "Data inserted successfully");
        } else {
            JOptionPane.showMessageDialog(null, "Failed to insert data");
        }
        return success;
    }

    // Method to get patient by ID
    public Patient getPatientById(int patientID) {
        return patientDAO.getPatientById(patientID);
    }

    // Method to retrieve all patients
    public List<Patient> getAllPatients() {
        return patientDAO.getAllPatients();
    }

    // Other CRUD operations (update, delete) can also be added similarly.
}
