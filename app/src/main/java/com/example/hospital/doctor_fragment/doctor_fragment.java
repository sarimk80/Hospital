package com.example.hospital.doctor_fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toolbar;

import com.example.hospital.R;
import com.example.hospital.databinding.FragmentDoctorFragmentBinding;
import com.example.hospital.patient_fragment.Bluetooth_model;
import com.example.hospital.patient_fragment.Bluetooth_viewModel;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class doctor_fragment extends Fragment {

    private ImageView img_holder;//Initialization of image_view
    private PopupMenu popupMenu;//Initialization of app bar menu
    private Menu menu;//Initialization of menu variable
    FirebaseAuth auth;//Initialization of firebase auth which will give us the  current sign in user
    FragmentDoctorFragmentBinding doctorFragmentBinding; //Initialization of binding
    ArrayList<String> list = new ArrayList<>(); //Initialization of array list which will hold the string values from the data base
    ArrayAdapter<String> arrayAdapter;


    public doctor_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        doctorFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_doctor_fragment, null, false);
        final View view = doctorFragmentBinding.getRoot();
        img_holder = view.findViewById(R.id.placeholder);

        //img_holder.setVisibility(View.VISIBLE);

        popupMenu = new PopupMenu(getContext(), img_holder);

        menu = popupMenu.getMenu();
        //menu.add(0, 0, 0, "Logout");

        popupMenu.getMenuInflater().inflate(R.menu.menu, menu);

        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.logout:
                    auth.getInstance().signOut();//This will sign out the user and navigate to the login page
                    Navigation.findNavController(view).navigate(R.id.action_doctor_fragment2_to_login);
                    return true;
            }

            return false;
        });

        img_holder.setOnClickListener(v -> popupMenu.show());


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Doctor_model doctor_model = new Doctor_model("Name: " + getArguments().getString("User_name"), "" + getArguments().getString("name"));

        doctorFragmentBinding.setDoctor(new Doctor_viewModel(doctor_model));//Name of the doctor binding

        arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.simple_spinner_item, R.id.patient_name, list);


        FirebaseDatabase.getInstance().getReference("Bluetooth").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                /*
                For each loop will loop through the the children's of data snapshot
                and update the bluetooth_model class
                and if statement will check if the patient has selected that particular doctor or not
                 */

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    Bluetooth_model bluetooth_model = postSnapshot.getValue(Bluetooth_model.class);

                    //Log.d("Doctor", String.valueOf(bluetooth_model.patient_name));

                    if (bluetooth_model.doctor_name.equals(getArguments().getString("User_name"))) {


                        doctorFragmentBinding.setPatient(new Bluetooth_viewModel(bluetooth_model));
                        list.add("Patient name: "+bluetooth_model.patient_name + " /n" + bluetooth_model.temp + " /n" + bluetooth_model.pulse);//Populating the list view

                        Log.d("Doctor", bluetooth_model.pulse + bluetooth_model.temp + bluetooth_model.patient_name);
                    }

                }
                doctorFragmentBinding.listview.setAdapter(arrayAdapter);
                arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
