package com.example.hospital.signup;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import es.dmoral.toasty.Toasty;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.hospital.R;
import com.example.hospital.databinding.FragmentSignupBinding;
import com.example.hospital.login.Presenter;
import com.example.hospital.patient_fragment.Patient_fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class Signup extends Fragment implements AdapterView.OnItemSelectedListener {

    private FragmentSignupBinding signupBinding;//Binding
    private FirebaseAuth auth;//Firebase auth for authentication
    private ProgressDialog progressDialog;//Progress spinner
    //private EditText email, password;
    private Spinner select_spi, doc_spi;//Initialization spinners

    private User_model user_model; // Data model
    // List will be use to add data
    private List<String> _name = new ArrayList<>();


    public Signup() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        /*
        Creating the binding
         */
        signupBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_signup, null, false);
        View view = signupBinding.getRoot();
//        email = view.findViewById(R.id.email);
//        password = view.findViewById(R.id.password);
        select_spi = view.findViewById(R.id.spi_select);
        doc_spi = view.findViewById(R.id.spi_doc);


        auth = FirebaseAuth.getInstance();

        doc_spi.setVisibility(View.INVISIBLE);//Seating the doc_spin to invisible

        progressDialog = new ProgressDialog(getContext());
        select_spi.setOnItemSelectedListener(this);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressDialog.setMessage("Please wait.......");
        progressDialog.show();


        /*
        Geting the data from the database and checking if the selected person if doctor and patient , if it is doctor then adding the name of the doctor to the doc_spinner
         */

        FirebaseDatabase.getInstance().getReference("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot postsnapshot : dataSnapshot.getChildren()) {

                    User_model user_model = postsnapshot.getValue(User_model.class);
                    //Log.d("User_model", user_model.name + " " + user_model.User_name);
                    if (user_model.name.equals("Doctor")) {

                        /*
                        _name is an array string which will add all the names that is
                        in the user_model.user_name array
                         */
                        _name.add(user_model.User_name);

                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, _name);
                /*
                Initialization of string array adapter which will hold the array (_name) and layout
                 */
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//Seating the layout of drop out menu
                signupBinding.spiDoc.setAdapter(adapter);//Seating the adpater in spinner
                adapter.notifyDataSetChanged();

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                progressDialog.dismiss();
                Toasty.error(getContext(), "Error " + databaseError, Toast.LENGTH_LONG).show();
            }
        });


    }


    /*
     This override ItemSelector select what category is selected on the select_spinner
     and signup the user according to the information selected in the spinner whether
     it is a doctor or patient
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0: {

                /*
                If case 0 that is (doctor) is selected that the doc_name spinner
                will be invisible and when you sign in it will call the sign up method
                that takes two parameters :
                1) The The user qualification (doctor or patient)
                2) The doctor name (in the case of the doctor it will be null).
                 */
                doc_spi.setVisibility(View.GONE);
                signupBinding.setEvent(new Presenter() {
                    @Override
                    public void Signup_Event() {
                        Navigation.findNavController(getView()).navigate(R.id.action_signup_to_login);
                    }

                    @Override
                    public void Signin_Event() {
                        UserSignup(select_spi.getSelectedItem().toString(), "null");
                        /*
                        After the 5 sec of pressing the sign up button the user will be taken to sign in fragment
                         */
                        final Handler handler = new Handler();
                        handler.postDelayed(() -> {
                            // Do something after 5s = 5000ms
                            Navigation.findNavController(getView()).navigate(R.id.action_signup_to_login);

                        }, 4000);


                    }
                });
                break;
            }
            case 1: {

                 /*
                If case 1 that is (patient) is selected that the doc_name spinner
                will be visible and when you sign in it will call the sign up method
                that takes two parameters :
                1) The The user qualification (doctor or patient)
                2) The doctor name from the doc_spi.
                 */
                doc_spi.setVisibility(View.VISIBLE);
                signupBinding.setEvent(new Presenter() {
                    @Override
                    public void Signup_Event() {
                        Navigation.findNavController(getView()).navigate(R.id.action_signup_to_login);
                    }

                    @Override
                    public void Signin_Event() {
                        UserSignup(select_spi.getSelectedItem().toString(), doc_spi.getSelectedItem().toString());
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Do something after 5s = 5000ms
                                Navigation.findNavController(getView()).navigate(R.id.action_signup_to_login);

                            }
                        }, 4000);

                    }
                });

                break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /*
    This method takes two parameters the "person" whether if the person is doctor
    or patient and "doc_name" if it is a patient then the doc name is whatever he
    has selected and if a doctor then the doc_name is null.This method takes gmail
    and password from the edit text and creates TWO databases one for auth and other
    for user other data like name, doctor , patient, doc_name etc.
     */
    public void UserSignup(final String person, final String doc_name) {

        /*
        Validating the text from the edit text
         */

        String _email = signupBinding.email.getText().toString();//Getting the text from email edit text
        String _password = signupBinding.password.getText().toString();//Getting the text from password edit text
        String _User_name = signupBinding.name.getText().toString(); //Getting the text from user_name edit text

        /*
        Checking of email , password and user name field is empty if it is empty error toast will display
         */
        if (_email.isEmpty() || _password.isEmpty() || _User_name.isEmpty()) {
            Toasty.error(getContext(), "Enter email and password", Toast.LENGTH_LONG).show();
        } else {


            progressDialog.show();
            progressDialog.setMessage("Please wait......");

            /*
            this method will create a user with email address and password
            Go to firebase auth to see your sign up user
             */
            auth.createUserWithEmailAndPassword(_email, _password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        /*
                        Storing the data in user_model , the user_model takes 4
                        parameters
                        1)  Name (doctor or patient)
                        2) Doc_name (the name of the doctor that the patient
                        has selected)
                        3) Email address
                        5) User_name
                         */

                        user_model = new User_model(person, String.valueOf(signupBinding.email.getText()), doc_name, _User_name);

                        /*
                        Will create a data base in firebase with the name of
                        "Users" and the child will be the Uid of the current
                        sign in user.

                         */
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user_model).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                /*
                                If the sign in is complete success toast will be
                                displayed
                                else Error toast will be displayed
                                 */

                                if (task.isSuccessful()) {
                                    Toasty.success(getContext(), "You can sign in now!!!!", Toast.LENGTH_LONG, true).show();
                                    progressDialog.dismiss();
                                    signupBinding.email.setText("");
                                    signupBinding.password.setText("");
                                    signupBinding.name.setText("");
                                } else if (task.isCanceled()) {
                                    Toasty.error(getContext(), "Error!!!!", Toast.LENGTH_LONG, true).show();
                                    progressDialog.dismiss();
                                    signupBinding.email.setText("");
                                    signupBinding.password.setText("");
                                    signupBinding.name.setText("");
                                }


                            }
                        });

                    }

                    if (task.isCanceled()) {
                        Toasty.warning(getContext(), "Some thing went wrong", Toast.LENGTH_LONG, true).show();
                        progressDialog.dismiss();
                    }
                    if (task.isComplete()) {
                        Toasty.success(Objects.requireNonNull(getContext()), "You can Login now", Toast.LENGTH_LONG, true).show();
                        progressDialog.dismiss();
                    }
                }

            });
        }

    }

}
