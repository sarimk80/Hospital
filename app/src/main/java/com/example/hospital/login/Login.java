package com.example.hospital.login;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import es.dmoral.toasty.Toasty;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;

import com.example.hospital.R;
import com.example.hospital.databinding.FragmentLoginBinding;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class Login extends Fragment {

    /*
    Log in fragment will be the first fragment you will see when the application is open

    To learn the routing of fragment go to res/navigation/nav

    from Login fragment the user to either go the sign up page ot can login with doctor ot patient
     */

    private FragmentLoginBinding loginBinding;// Initialization of binding
    private FirebaseAuth mAuth;
    //private EditText email, password;
    private ProgressDialog progressDialog;


    public Login() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        loginBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, null, false);

        View view = loginBinding.getRoot();

//        email = view.findViewById(R.id.email);
//        password = view.findViewById(R.id.password);


        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(getContext());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginBinding.setEvent(new Presenter() {
            @Override
            public void Signup_Event() {
                Navigation.findNavController(view).navigate(R.id.action_login_to_signup);
            }

            @Override
            public void Signin_Event() {
                String _email = loginBinding.email.getText().toString();
                String _password = loginBinding.password.getText().toString();

                progressDialog.show();
                progressDialog.setMessage("Please wait.....");
                if (_email.isEmpty() || _password.isEmpty()) {
                    Toasty.error(Objects.requireNonNull(getContext()), "Enter email and password", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                } else {

                    /*
                    Will get email and password from edit text and will create a firebase auth with email and password
                     */

                    mAuth.signInWithEmailAndPassword(_email, _password).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            progressDialog.dismiss();
                            Navigation.findNavController(view).navigate(R.id.action_login_to_patient);
                        } else {
                            Toasty.error(Objects.requireNonNull(getContext()), "Error Try again!!!!", Toast.LENGTH_LONG).show();
                            loginBinding.email.setText("");
                            loginBinding.password.setText("");
                            progressDialog.dismiss();
                        }
                    });
                }


            }
        });


    }
}
