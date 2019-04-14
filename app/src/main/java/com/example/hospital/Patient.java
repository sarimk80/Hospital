package com.example.hospital;


import android.app.ProgressDialog;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hospital.signup.User_model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class Patient extends Fragment {

    FirebaseDatabase firebaseDatabase;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private User_model user_model;
    private ProgressDialog progressDialog;


    public Patient() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View view = inflater.inflate(R.layout.fragment_patient, container, false);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        progressDialog.setMessage("Please Wait.............");


        //Creating the instance of user
        user = auth.getInstance().getCurrentUser();
        String userId = user.getUid();

        //Will Listen to data base with the name of Users
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user_model = dataSnapshot.getValue(User_model.class);

                Bundle bundle = new Bundle();//Creating a bundle which will pass value to other fragment
                /*
                Values like name , email, doctor bool and user_name will be send to other fragment
                 */
                bundle.putString("name", user_model.name);
                bundle.putString("email", user_model.email);
                bundle.putString("Doc_name", user_model.doctor);
                bundle.putString("User_name",user_model.User_name);

                /*
                This is a helper class which will determine if the login user in doctor or patient
                 */

                //Log.d("main", user_model.name);
                if (user_model.name.equals("Doctor")) {
                    //Log.d("Doctor", "In Doctor Fragment");
                    progressDialog.dismiss();
                    /*
                    If it is a doctor navigate to action_patient_to_doctor_fragment
                     */
                    Navigation.findNavController(view).navigate(R.id.action_patient_to_doctor_fragment2, bundle);
                } else if (user_model.name.equals("Patient")) {

                    //Log.d("Doctor", "In Patient Fragment");

                    /*
                    If it is a Patient navigate to action_patient_to_patient_fragment
                     */

                    progressDialog.dismiss();
                    Navigation.findNavController(view).navigate(R.id.action_patient_to_patient_fragment2, bundle);
                } else {
                    Log.d("Doctor", "In no fragment");
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
