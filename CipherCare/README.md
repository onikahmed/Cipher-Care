

CipherCare: Electronic Medical Record System.

Description: CipherCare is a Java-based application designed for efficient
patient management. It provides features for logging in, viewing patient details,
adding patient reports, managing appointments, and more, making it a complete 
solution for clinics or small healthcare providers.

Features
-User Authentication:
Secure login interface with username and password validation.
-Patient Management:
View, add, edit, delete, and search for patient records.
-Report Management:
Add and save patient reports that persist in the database.
-Appointment Management:
Manage appointments directly within the GUI.
-User-Friendly Interface:
Built using Java Swing with an intuitive layout and responsive design.

Prerequisites
To run CipherCare, ensure the following are installed:

-Java Development Kit (JDK) (version 11 or higher)
-MySQL Database
-IDE (e.g., IntelliJ IDEA, Eclipse) or a text editor with Java support
-MySQL Connector for Java

Installation
Step 1: Clone the Repository
Clone the project from your repository:
git clone https://github.com/onik-ahmed/ciphercare.git
cd ciphercare

Step 2: Setup the Database
Open your MySQL client or GUI (e.g., MySQL Workbench).
Create a database:
CREATE DATABASE CipherCare;
USE CipherCare;
Run the SQL script included in the project (CipherCareSQL.java) to create the necessary
tables and populate sample data.

Step 3: Configure Database Connection
Update the CipherCareSQL class with your database credentials:
private static final String URL = "jdbc:mysql://localhost:3306/CipherCare";
private static final String USER = "your-username";//same as yourMYSQL
private static final String PASSWORD = "your-password";//same as yourMYSQL

Step 4: Build and Run
Open the project in your IDE.
Compile and run the CipherCareLoginGUI class.
Use the following credentials to log in (or update them as needed):
Username: (same as you MYSQL)
Password: (same as you MYSQL)

Login Page
    Enter valid credentials to log in.
"   CipherCare" is prominently displayed at the top, and the application credits are listed at the bottom.
Main Page
    Access patient records:
        Add Patient: Add new patients to the database.
        View Patients: View a list of all patients.
        Edit Patient: Update patient details.
        Delete Patient: Remove a patient from the database.
        Search Patient: Search for a patient by name, email, or phone.
Patient Profile
    View detailed patient information.
    Add, view, and update reports associated with each patient.

File Structure:
CipherCare/
├── src/
│   ├── CipherCareMainGUI.java         # Main interface for managing patients
│   ├── CipherCareLoginGUI.java        # Login interface
│   ├── CipherCareAddGUI.java          # Interface to add patients
│   ├── CipherCareAppointmentGUI.java  # Appointment management interface
│   ├── CipherCareSQL.java             # Database connection and queries
│   ├── Patient.java                   # Patient model class
│   ├── PatientDAO.java                # Data Access Object interface
│   ├── PatientDAOImpl.java            # DAO implementation
│   └── PatientController.java         # Business logic controller
├── resources/
│   └── ciphercare.sql                 # SQL script to set up the database
└── README.md                          # Project documentation (this file)

Technologies Used
-Java Swing: For building the graphical user interface.
-MySQL: For data storage and retrieval.
-JDBC: To connect Java to the MySQL database

Credits
Developed by:
Joshua
Jonathan
Andrew
Francisco
Onik

Group Project for CSC381 class.
