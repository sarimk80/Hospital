package com.example.hospital.signup;

public class User_model {

    public String name;
    public String email;
    public String doctor;
    public String User_name;
    public String phone_no;

    public User_model() {
    }

    public User_model(String name, String email, String doctor,String User_name,String phone_no) {
        this.name = name;
        this.email = email;
        this.doctor = doctor;
        this.User_name=User_name;
        this.phone_no=phone_no;
    }


    public User_model(String name, String email,String User_name) {
        this.name = name;
        this.email = email;
        this.User_name=User_name;
    }
}
