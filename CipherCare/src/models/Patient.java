package models;

public class Patient {
    private int id;
    private String dob;
    private String email;
    private String phone;
    private String address;

    public Patient(int id, String dob, String email, String phone, String address) {
        this.id = id;
        this.dob = dob;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public Patient(String dob, String email, String phone, String address) {
        this.dob = dob;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
