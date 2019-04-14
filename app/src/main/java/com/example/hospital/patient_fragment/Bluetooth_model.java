package com.example.hospital.patient_fragment;

public class Bluetooth_model {
    public String temp;
    public String pulse;
    public String doctor_name;
    public String patient_name;


    public Bluetooth_model() {
    }

    public Bluetooth_model(String temp, String pulse, String doctor_name, String patient_name) {
        this.temp = temp;
        this.pulse = pulse;
        this.doctor_name=doctor_name;
        this.patient_name=patient_name;
    }
}
