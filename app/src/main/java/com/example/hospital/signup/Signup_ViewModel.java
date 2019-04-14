package com.example.hospital.signup;

public class Signup_ViewModel {

    private String Username;
    private String Passowrd;

    public Signup_ViewModel(Signup_Model signup_model) {
        this.Username=signup_model.Username;
        this.Passowrd=signup_model.Passowrd;

    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassowrd() {
        return Passowrd;
    }

    public void setPassowrd(String passowrd) {
        Passowrd = passowrd;
    }
}
