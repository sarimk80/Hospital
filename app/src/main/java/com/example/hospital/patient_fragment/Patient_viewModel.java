package com.example.hospital.patient_fragment;

public class Patient_viewModel {

    private String name;
    private String doc_name;
    private String email;
    private String phone_no;

    public Patient_viewModel(Patient_model patient_model) {
        this.name = patient_model.name;
        this.doc_name = patient_model.doc_name;
        this.email = patient_model.email;
        this.phone_no=patient_model.phone_no;
    }

    public String getName() {
        return name;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDoc_name() {
        return doc_name;
    }

    public void setDoc_name(String doc_name) {
        this.doc_name = doc_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
