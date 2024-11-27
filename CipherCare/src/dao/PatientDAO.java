package dao;

import java.util.List;
import models.Patient;

public interface PatientDAO {
    boolean addPatient(Patient patient);
    Patient getPatientById(int patientID);
    List<Patient> getAllPatients();
    boolean updatePatient(Patient patient);
    boolean deletePatient(int patientID);
}
