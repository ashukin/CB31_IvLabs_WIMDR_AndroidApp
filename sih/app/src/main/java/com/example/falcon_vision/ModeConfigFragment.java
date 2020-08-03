package com.example.falcon_vision;

import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;


public class ModeConfigFragment extends Fragment {

    SwitchCompat mod_alert, no_alert;
    EditText time_mod_to, time_mod_from;
    static Boolean mod_on = false, no_alert_on = false;

    TextInputLayout mod_from_lay, mod_to_lay;
    TextView mod_text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mode_config, container, false);

        mod_alert = view.findViewById(R.id.switch_moderate);
        no_alert = view.findViewById(R.id.switch_noalert);

        mod_text = view.findViewById(R.id.mod_text);
        mod_from_lay = view.findViewById(R.id.mod_from_layout);
        mod_to_lay = view.findViewById(R.id.mod_to_layout);

        mod_alert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (mod_on) {
                    mod_on = false;
                    mod_text.setVisibility(View.GONE);
                    mod_from_lay.setVisibility(View.GONE);
                    mod_to_lay.setVisibility(View.GONE);
                }
                else
                {
                    mod_on=true;
                    mod_text.setVisibility(View.VISIBLE);
                    mod_from_lay.setVisibility(View.VISIBLE);
                    mod_to_lay.setVisibility(View.VISIBLE);
                    if(no_alert_on){
                        no_alert.toggle();
                    }
                }
                Log.d("isTouched!!!!!!!)!)! = ", String.valueOf(mod_on));
            }
        });

        no_alert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (no_alert_on) {
                    no_alert_on = false;
                }
                else
                {
                    no_alert_on=true;
                    if(mod_on){
                        mod_alert.toggle();
                    }
                }
                Log.d("isTouched!!!!!!!)!)! = ", String.valueOf(mod_on));
            }
        });

        time_mod_from = view.findViewById(R.id.mod_from);
        time_mod_to = view.findViewById(R.id.mod_to);



        time_mod_from.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if(selectedMinute==00){
                            time_mod_from.setText( selectedHour + ":" + selectedMinute+0);
                        }else {
                            time_mod_from.setText(selectedHour + ":" + selectedMinute);
                        }
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        time_mod_to.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if(selectedMinute==00){
                            time_mod_to.setText( selectedHour + ":" + selectedMinute+0);
                        }else {
                            time_mod_to.setText(selectedHour + ":" + selectedMinute);
                        }
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });


        return view;
    }


    public void onBackPressed() {
        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new SettingsFragment()).commit();
    }
}