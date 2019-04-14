package com.example.hospital.patient_fragment;

public class Bluetooth_viewModel {
    String temp;
    String pulse;
    String doctor_name;
    String patient_name;

    public Bluetooth_viewModel(Bluetooth_model bluetooth_model) {
        this.temp=bluetooth_model.temp;
        this.pulse=bluetooth_model.pulse;
        this.doctor_name=bluetooth_model.doctor_name;
        this.patient_name=bluetooth_model.patient_name;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public String getPatient_name() {
        return patient_name;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getPulse() {
        return pulse;
    }

    public void setPulse(String pulse) {
        this.pulse = pulse;
    }
}
