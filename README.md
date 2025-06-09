# CipherCare: Electronic Medical Record System

## Description

**CipherCare** is a Java-based application designed for efficient patient management. It provides features for logging in, viewing patient details, adding patient reports, managing appointments, and more — making it a complete solution for clinics or small healthcare providers.

## Features

* **User Authentication**
  Secure login interface with username and password validation.

* **Patient Management**
  View, add, edit, delete, and search for patient records.

* **Report Management**
  Add and save patient reports that persist in the database.

* **Appointment Management**
  Manage appointments directly within the GUI.

* **User-Friendly Interface**
  Built using Java Swing with an intuitive layout and responsive design.

## Prerequisites

To run CipherCare, ensure the following are installed:

* Java Development Kit (JDK) 11 or higher
* MySQL Database
* IDE (e.g., IntelliJ IDEA, Eclipse) or a text editor with Java support
* MySQL Connector for Java

## Installation

### Step 1: Clone the Repository

```bash
git clone https://github.com/onik-ahmed/ciphercare.git
cd ciphercare
```

### Step 2: Set Up the Database

1. Open your MySQL client or GUI (e.g., MySQL Workbench).
2. Create and select the database:

   ```sql
   CREATE DATABASE CipherCare;
   USE CipherCare;
   ```
3. Run the SQL script provided in `resources/ciphercare.sql` to create tables and populate sample data.

### Step 3: Configure Database Connection

In `CipherCareSQL.java`, update the following with your MySQL credentials:

```java
private static final String URL = "jdbc:mysql://localhost:3306/CipherCare";
private static final String USER = "your-username"; // your MySQL username
private static final String PASSWORD = "your-password"; // your MySQL password
```

### Step 4: Build and Run

1. Open the project in your preferred IDE.
2. Compile and run the `CipherCareLoginGUI` class.
3. Use your MySQL credentials to log in.

## Application Overview

### Login Page

* Enter valid credentials to log in.
* "CipherCare" is prominently displayed at the top.
* Application credits are listed at the bottom.

### Main Page

* **Add Patient**: Add new patients to the database.
* **View Patients**: Display all existing patients.
* **Edit Patient**: Update patient details.
* **Delete Patient**: Remove patient records.
* **Search Patient**: Search by name, email, or phone number.

### Patient Profile

* View detailed patient info.
* Add, view, and update associated reports.

## File Structure

```
CipherCare/
├── src/
│   ├── CipherCareMainGUI.java         # Main interface
│   ├── CipherCareLoginGUI.java        # Login page
│   ├── CipherCareAddGUI.java          # Add patient interface
│   ├── CipherCareAppointmentGUI.java  # Appointment management
│   ├── CipherCareSQL.java             # DB connection and queries
│   ├── Patient.java                   # Patient model
│   ├── PatientDAO.java                # DAO interface
│   ├── PatientDAOImpl.java            # DAO implementation
│   └── PatientController.java         # Business logic
├── resources/
│   └── ciphercare.sql                 # SQL schema and sample data
└── README.md                          # Project documentation
```

## Technologies Used

* **Java Swing**: GUI development
* **MySQL**: Data storage
* **JDBC**: Java database connectivity

## Credits

Developed by:
Joshua, Jonathan, Andrew, Francisco, Onik

*Group Project for CSC381 class*
