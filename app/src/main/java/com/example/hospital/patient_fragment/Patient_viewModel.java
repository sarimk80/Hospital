package com.example.hospital.patient_fragment;

public class Patient_viewModel {

    private String name;
    private String doc_name;
    private String email;

    public Patient_viewModel(Patient_model patient_model) {
        this.name = patient_model.name;
        this.doc_name = patient_model.doc_name;
        this.email = patient_model.email;
    }

    public String getName() {
        return name;
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
