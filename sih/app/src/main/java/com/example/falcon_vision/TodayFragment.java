package com.example.falcon_vision;

import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Calendar;


/**

 */
public class TodayFragment extends Fragment {


    SwitchCompat switch_button;
    EditText vig_from, vig_to;
    static Boolean vig_on = false;

    TextView dist, avg_dist, places_vis, most_vis, new_places, park_cost, car, car_num;

    FirebaseAuth mAuth;
    FirebaseFirestore fstore;
    FirebaseUser user;
    String userID;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_today, container, false);

        dist = view.findViewById(R.id.distance);
        avg_dist = view.findViewById(R.id.avg_distance);
        places_vis = view.findViewById(R.id.places_visited);
        most_vis = view.findViewById(R.id.most_visited);
        new_places = view.findViewById(R.id.new_places);
        park_cost = view.findViewById(R.id.park_cost);

        car = view.findViewById(R.id.today_car);
        car_num = view.findViewById(R.id.today_car_num);

        mAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        user = mAuth.getCurrentUser();

        final DocumentReference documentReference = fstore.collection("reg_users").document(userID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                car.setText(documentSnapshot.getString("veh_model"));
                car_num.setText(documentSnapshot.getString("veh_num"));

            }
        });

        switch_button = view.findViewById(R.id.switch_vigilance);
        switch_button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (vig_on) {
                    vig_on = false;
                }
                else
                {
                    vig_on=true;
                }
                Log.d("isTouched!!!!!!!)!)! = ", String.valueOf(vig_on));
            }
        });


        vig_from = view.findViewById(R.id.vig_from);
        vig_to = view.findViewById(R.id.vig_to);

        vig_from.setOnClickListener(new View.OnClickListener() {

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
                            vig_from.setText( selectedHour + ":" + selectedMinute+0);
                        }else {
                            vig_from.setText(selectedHour + ":" + selectedMinute);
                        }
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        vig_to.setOnClickListener(new View.OnClickListener() {

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
                            vig_to.setText( selectedHour + ":" + selectedMinute+0);
                        }else {
                            vig_to.setText(selectedHour + ":" + selectedMinute);
                        }
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });



        return view;
    }
}
