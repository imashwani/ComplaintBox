package com.example.ashwani.complaintbox;

/**
 * Created by Ashwani on 26/03/2018.
 */

public class Complaint {
    String ComplaintNo, UserId, SchoolName, Description, Date, Problems, PhoneNumber, EmailId, Status;

    public Complaint(String complaintNo, String userId, String schoolName, String description, String date, String problems, String phoneNumber, String emailId, String status) {
        ComplaintNo = complaintNo;
        UserId = userId;
        SchoolName = schoolName;
        Description = description;
        Date = date;
        Problems = problems;
        PhoneNumber = phoneNumber;
        EmailId = emailId;
        Status = status;
    }

    public String getComplaintNo() {
        return ComplaintNo;
    }

    public void setComplaintNo(String complaintNo) {
        ComplaintNo = complaintNo;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getSchoolName() {
        return SchoolName;
    }

    public void setSchoolName(String schoolName) {
        SchoolName = schoolName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getProblems() {
        return Problems;
    }

    public void setProblems(String problems) {
        Problems = problems;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getEmailId() {
        return EmailId;
    }

    public void setEmailId(String emailId) {
        EmailId = emailId;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
