package com.example.hospital.login;

public class Login_ViewModel {

    private String Username;
    private String Passowrd;

    public Login_ViewModel(Login_Model login_model) {
        this.Username=login_model.Username;
        this.Passowrd=login_model.Passowrd;
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
