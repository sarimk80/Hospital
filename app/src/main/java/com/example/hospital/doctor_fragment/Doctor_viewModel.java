package com.example.hospital.doctor_fragment;

public class Doctor_viewModel {
    private String email;
    private String doctor;

    public Doctor_viewModel(Doctor_model doctor_model) {
        this.email=doctor_model.email;
        this.doctor=doctor_model.doctor;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }
}
