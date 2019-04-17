package com.example.hospital.doctor_fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.hospital.R;
import com.example.hospital.patient_fragment.Bluetooth_model;

import java.util.ArrayList;


public class CustomAdapter extends BaseAdapter {

    Context context;
    ArrayList<Bluetooth_model> bluetooth_modelList;
    LayoutInflater layoutInflater;

    public CustomAdapter(Context context, ArrayList<Bluetooth_model> bluetooth_modelList) {
        this.context = context;
        this.bluetooth_modelList = bluetooth_modelList;
    }

    @Override
    public int getCount() {
        return bluetooth_modelList.size();
    }

    @Override
    public Object getItem(int position) {
        return bluetooth_modelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.simple_spinner_item,parent,false);
        }

        TextView pulse, temp, patient_name;
        pulse = convertView.findViewById(R.id.pulse);
        temp = convertView.findViewById(R.id.temp);
        patient_name = convertView.findViewById(R.id.patient_name);

        pulse.setText(bluetooth_modelList.get(position).pulse);
        temp.setText(bluetooth_modelList.get(position).temp);
        patient_name.setText("Patient: "+bluetooth_modelList.get(position).patient_name);

        return convertView;
    }
}
