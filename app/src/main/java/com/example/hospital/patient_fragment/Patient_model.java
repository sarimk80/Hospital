package com.example.hospital.patient_fragment;

public class Patient_model {

    public String name;
    public String doc_name;
    public String email;
    public String phone_no;

    public Patient_model(String name, String doc_name, String email,String phone_no) {
        this.name = name;
        this.doc_name = doc_name;
        this.email = email;
        this.phone_no=phone_no;
    }
}
