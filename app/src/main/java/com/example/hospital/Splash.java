package com.example.hospital;


import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.navigation.Navigation;


/**
 * A simple {@link Fragment} subclass.
 */
public class Splash extends Fragment {


    public Splash() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {


        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                //Navigation.findNavController(view).navigate(R.id.action_splash_to_login);
            }
        }, 200);

        super.onViewCreated(view, savedInstanceState);
    }
}
