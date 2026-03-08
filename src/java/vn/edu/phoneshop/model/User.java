package vn.edu.phoneshop.model;

<<<<<<< HEAD
import java.util.Date;

public class User {

=======
import java.sql.Date;

public class User {
>>>>>>> e7b45a906d28bff2867a45721aa3c05ab0f4cc4d
    private int userID;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;
    private String password;
    private String role;
    private Date createDate;
<<<<<<< HEAD
=======
    private int rewardPoints;
    private String customerType;
>>>>>>> e7b45a906d28bff2867a45721aa3c05ab0f4cc4d

    public User() {
    }

<<<<<<< HEAD
    public User(int userID, String fullName, String email, String phoneNumber,
            String address, String password, String role, Date createDate) {
=======
    public User(int userID, String fullName, String email, String phoneNumber, String address, String password,
            String role, Date createDate, int rewardPoints, String customerType) {
>>>>>>> e7b45a906d28bff2867a45721aa3c05ab0f4cc4d
        this.userID = userID;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.password = password;
        this.role = role;
        this.createDate = createDate;
<<<<<<< HEAD
    }

    public User(String fullName, String email, String phoneNumber,
            String address, String password, String role, Date createDate) {
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.password = password;
        this.role = role;
        this.createDate = createDate;
=======
        this.rewardPoints = rewardPoints;
        this.customerType = customerType;
>>>>>>> e7b45a906d28bff2867a45721aa3c05ab0f4cc4d
    }

    public int getUserID() {
        return userID;
    }

<<<<<<< HEAD
    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public Date getCreateDate() {
        return createDate;
    }

=======
>>>>>>> e7b45a906d28bff2867a45721aa3c05ab0f4cc4d
    public void setUserID(int userID) {
        this.userID = userID;
    }

<<<<<<< HEAD
=======
    public String getFullName() {
        return fullName;
    }

>>>>>>> e7b45a906d28bff2867a45721aa3c05ab0f4cc4d
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

<<<<<<< HEAD
=======
    public String getEmail() {
        return email;
    }

>>>>>>> e7b45a906d28bff2867a45721aa3c05ab0f4cc4d
    public void setEmail(String email) {
        this.email = email;
    }

<<<<<<< HEAD
=======
    public String getPhoneNumber() {
        return phoneNumber;
    }

>>>>>>> e7b45a906d28bff2867a45721aa3c05ab0f4cc4d
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

<<<<<<< HEAD
=======
    public String getAddress() {
        return address;
    }

>>>>>>> e7b45a906d28bff2867a45721aa3c05ab0f4cc4d
    public void setAddress(String address) {
        this.address = address;
    }

<<<<<<< HEAD
=======
    public String getPassword() {
        return password;
    }

>>>>>>> e7b45a906d28bff2867a45721aa3c05ab0f4cc4d
    public void setPassword(String password) {
        this.password = password;
    }

<<<<<<< HEAD
=======
    public String getRole() {
        return role;
    }

>>>>>>> e7b45a906d28bff2867a45721aa3c05ab0f4cc4d
    public void setRole(String role) {
        this.role = role;
    }

<<<<<<< HEAD
=======
    public Date getCreateDate() {
        return createDate;
    }

>>>>>>> e7b45a906d28bff2867a45721aa3c05ab0f4cc4d
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

<<<<<<< HEAD
    @Override
    public String toString() {
        return "User{"
                + "userID=" + userID
                + ", fullName='" + fullName + '\''
                + ", email='" + email + '\''
                + ", phoneNumber='" + phoneNumber + '\''
                + ", address='" + address + '\''
                + ", password='" + password + '\''
                + ", role='" + role + '\''
                + ", createDate=" + createDate
                + '}';
=======
    public int getRewardPoints() {
        return rewardPoints;
    }

    public void setRewardPoints(int rewardPoints) {
        this.rewardPoints = rewardPoints;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
>>>>>>> e7b45a906d28bff2867a45721aa3c05ab0f4cc4d
    }
}
