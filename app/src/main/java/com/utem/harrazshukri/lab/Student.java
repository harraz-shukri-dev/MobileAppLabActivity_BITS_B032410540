package com.utem.harrazshukri.lab;

public class Student {

    private String strFullName, strStudNo, strEmail, strGender, strBirthdate, strState;

    public Student(String strFullName, String strStudNo, String strEmail, String strGender, String strBirthdate, String strState) {
        this.strFullName = strFullName;
        this.strStudNo = strStudNo;
        this.strEmail = strEmail;
        this.strGender = strGender;
        this.strBirthdate = strBirthdate;
        this.strState = strState;
    }

    public String getStrFullName() {
        return strFullName;
    }

    public void setStrFullName(String strFullName) {
        this.strFullName = strFullName;
    }

    public String getStrStudNo() {
        return strStudNo;
    }

    public void setStrStudNo(String strStudNo) {
        this.strStudNo = strStudNo;
    }

    public String getStrEmail() {
        return strEmail;
    }

    public void setStrEmail(String strEmail) {
        this.strEmail = strEmail;
    }

    public String getStrGender() {
        return strGender;
    }

    public void setStrGender(String strGender) {
        this.strGender = strGender;
    }

    public String getStrBirthdate() {
        return strBirthdate;
    }

    public void setStrBirthdate(String strBirthdate) {
        this.strBirthdate = strBirthdate;
    }

    public String getStrState() {
        return strState;
    }

    public void setStrState(String strState) {
        this.strState = strState;
    }
}
