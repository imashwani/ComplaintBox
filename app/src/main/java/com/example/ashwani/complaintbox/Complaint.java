package com.example.ashwani.complaintbox;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ashwani on 26/03/2018.
 */

public class Complaint implements Parcelable {
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

    public static final Creator<Complaint> CREATOR = new Creator<Complaint>() {
        @Override
        public Complaint createFromParcel(Parcel in) {
            return new Complaint(in);
        }

        @Override
        public Complaint[] newArray(int size) {
            return new Complaint[size];
        }
    };

    protected Complaint(Parcel in) {
        complaintNo = in.readString();
        userId = in.readString();
        schoolName = in.readString();
        description = in.readString();
        date = in.readString();
        problems = in.readString();
        imageLink = in.readString();
        phoneNumber = in.readString();
        emailId = in.readString();
        status = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(complaintNo);
        parcel.writeString(userId);
        parcel.writeString(schoolName);
        parcel.writeString(date);
        parcel.writeString(problems);
        parcel.writeString(imageLink);
        parcel.writeString(phoneNumber);
        parcel.writeString(emailId);
        parcel.writeString(status);
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

    @Override
    public int describeContents() {
        return 0;
    }


}
