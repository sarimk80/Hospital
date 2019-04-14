package com.example.hospital.patient_fragment;


import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import es.dmoral.toasty.Toasty;
import me.aflak.bluetooth.Bluetooth;
import me.aflak.bluetooth.BluetoothCallback;
import me.aflak.bluetooth.DeviceCallback;


import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.hospital.R;
import com.example.hospital.databinding.FragmentPatientFragmentBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class Patient_fragment extends Fragment implements LocationListener {

    private ImageView img_holder;
    private PopupMenu popupMenu;
    private Menu menu;
    FirebaseAuth auth;
    FragmentPatientFragmentBinding patientFragmentBinding;
    Bluetooth bluetooth;
    ProgressDialog progressDialog;
    LocationManager locationManager;
    String phone;


    public Patient_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        checkBTPermissions();
//        CheckPermission();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        patientFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_patient_fragment, null, false);

        final View view = patientFragmentBinding.getRoot();


        img_holder = view.findViewById(R.id.placeholder);


        //img_holder.setVisibility(View.VISIBLE);

        popupMenu = new PopupMenu(getContext(), img_holder);

        menu = popupMenu.getMenu();
        //menu.add(0, 0, 0, "Logout");

        popupMenu.getMenuInflater().inflate(R.menu.menu, menu);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.logout:
                        auth.getInstance().signOut();
                        Navigation.findNavController(view).navigate(R.id.action_patient_fragment2_to_login);
                        return true;
                }

                return false;
            }
        });

        img_holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu.show();
            }
        });


        bluetooth = new Bluetooth(getContext());
        progressDialog = new ProgressDialog(getContext());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getLocation();

        /*
        This patient_model is getting its data from pervious activity that is
        patient class  and setting these values to the binding data
         */

        Patient_model patient_model = new Patient_model("Patient name: " + getArguments().getString("User_name"), "Email id:  " + getArguments().getString("email"), "Doctor name " + getArguments().getString("Doc_name"));


        patientFragmentBinding.setPatient(new Patient_viewModel(patient_model));


        bluetooth.onStart(); // Enabling the bluetooth
        bluetooth.enable();

        /*
        BLUETOOTH override methods
        Reading the method names can determine what each method is doing
         */
        bluetooth.setBluetoothCallback(new BluetoothCallback() {
            @Override
            public void onBluetoothTurningOn() {

                progressDialog.setMessage("Bluetooth is turning on");
                progressDialog.show();
            }

            @Override
            public void onBluetoothOn() {
                progressDialog.dismiss();
                try {
                    bluetooth.connectToAddress("00:18:E4:40:00:06");
                    toastSuccess("Connected");
                } catch (Exception e) {
                    toastError("Bluetooth is not in range");
                }


            }

            @Override
            public void onBluetoothTurningOff() {

            }

            @Override
            public void onBluetoothOff() {

            }

            @Override
            public void onUserDeniedActivation() {
                toastError("Permission is not granted");

            }
        });

        bluetooth.setDeviceCallback(new DeviceCallback() {
            @Override
            public void onDeviceConnected(BluetoothDevice device) {


            }

            @Override
            public void onDeviceDisconnected(BluetoothDevice device, String message) {

            }

            @Override
            public void onMessage(String message) {
                String string = message;
                String[] parts = string.split(",");
                String part1 = "Temperature: " + parts[0]; // 004
                String part2 = "Pulse: " + parts[1]; // 034556

                Bluetooth_model bluetooth_model = new Bluetooth_model(part1, part2, getArguments().getString("Doc_name"), getArguments().getString("User_name"));
                patientFragmentBinding.setBluetooth(new Bluetooth_viewModel(bluetooth_model));


                FirebaseDatabase.getInstance().getReference("Bluetooth").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(bluetooth_model).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                    } else {

                    }

                });


                Log.d("Patient", message);


            }

            @Override
            public void onError(String message) {
                toastError("Error");
            }

            @Override
            public void onConnectError(BluetoothDevice device, String message) {

            }
        });


    }

    /*
    Getting the permission from the user
    1) Access_fine_location
    2) Access_coarse_location
    3)Send sms permission
     */
    private void checkBTPermissions() {


        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            int permissionCheck = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                permissionCheck = getActivity().checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                permissionCheck += getActivity().checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                permissionCheck += getActivity().checkSelfPermission("Manifest.permission.SEND_SMS");
            }
            if (permissionCheck != 0) {

                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.SEND_SMS}, 1001); //Any number
            }

        } else {
            // Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (bluetooth.isConnected() || bluetooth.isEnabled()) {
            bluetooth.onStop();
            bluetooth.disable();
            bluetooth.disconnect();

        } else {

        }

        locationManager.removeUpdates(this);
    }

    void toastError(String msg) {
        Toasty.error(getContext(), msg, Toast.LENGTH_LONG).show();
    }

    void toastSuccess(String msg) {
        Toasty.success(getContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLocationChanged(Location location) {


//        Log.d("Patient", String.valueOf(location.getLongitude()) + String.valueOf(location.getLatitude()));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    public void getLocation() {
        try {


            /*
            Geting the location from the location manager and storing the
            longitude and latitude in loc object.
             */
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            Location loc = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            patientFragmentBinding.setEvent(new Patient_event() {
                @Override
                public void Send_location() {
                    phone = patientFragmentBinding.call.getText().toString();

                    // Log.d("Patient", "Click on Button to send Sms");
                    String latitude = String.valueOf(loc.getLatitude());
                    String longitude = String.valueOf(loc.getLongitude());
                    String user_location = "http://maps.google.com/maps?q=" + latitude + "," + longitude;
                    if (phone.isEmpty()) {
                        toastError("Enter phone no.");
                    } else {
                        //Send_SMS method takes two parameters
                        // 1) YOU have to send which is in string formate
                        // 2) Phone no.

                        Send_SMS(user_location, phone);
                    }


                }
            });

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }


    void Send_SMS(String user_location, String phone_no) {

        try {
            /*
            Calling the smsManager and smsManager contains a sendTextMassage
            method that do what the name suggest , it takes a phone no. and a string
            as message
             */
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phone_no, null, user_location, null, null);

            toastSuccess("SMS is send");


        } catch (Exception e) {
            toastError("Phone no. is not correct");

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1001: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    /*
                    Checking of the permission is granted
                    if it is granted call the method getLOocation()
                     */
                    Log.d("Patient", "Permission is granted");
                    getLocation();
                } else {

                }
            }
        }
    }
}
