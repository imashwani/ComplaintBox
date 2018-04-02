package com.example.ashwani.complaintbox;

/**
 * Created by Ashwani on 26/03/2018.
 */

public class Complaint {
    private String complaintNo, userId, schoolName, description, date, problems, imageLink, phoneNumber, emailId, status;

    public Complaint() {
    }

    public Complaint(String complaintNo, String userId, String schoolName, String description, String date, String problems, String imageLink, String phoneNumber, String emailId, String status) {
        this.complaintNo = complaintNo;
        this.userId = userId;
        this.schoolName = schoolName;
        this.description = description;
        this.date = date;
        this.problems = problems;
        this.imageLink = imageLink;
        this.phoneNumber = phoneNumber;
        this.emailId = emailId;
        this.status = status;
    }

    public String getComplaintNo() {
        return complaintNo;
    }

    public void setComplaintNo(String complaintNo) {
        this.complaintNo = complaintNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProblems() {
        return problems;
    }

    public void setProblems(String problems) {
        this.problems = problems;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
