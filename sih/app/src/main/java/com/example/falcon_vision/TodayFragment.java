package com.example.falcon_vision;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**

 */
public class TodayFragment extends Fragment {


    SwitchCompat switch_button;
    EditText vig_from, vig_to;
    Spinner spinner;
    static Boolean vig_on = false;

    TextView dist, vig_text;
    TextView avg_dist;
    TextView places_vis;
    TextView most_vis;
    TextView new_places;
    TextView park_cost;

    CardView places_card;

    TextInputLayout vig_layout, vig_layout_2;
    String car_2,car,car_num;
    String car_num_2;

    FirebaseAuth mAuth;
    FirebaseFirestore fstore;
    FirebaseUser user;
    String userID;
    int cost;




//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//
//    }

    public void deductMoney(int money){
        cost-=money;
        park_cost.setText("Rs"+ String.valueOf(cost));
    }

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

        places_card = view.findViewById(R.id.card_3);

        vig_text = view.findViewById(R.id.vig_text);
        spinner = view.findViewById(R.id.spinner);

        vig_layout = view.findViewById(R.id.vig_layout);
        vig_layout_2=view.findViewById(R.id.vig_layout_to);

//        car = view.findViewById(R.id.today_car);
//        car_num=view.findViewById(R.id.today_car_num);

        mAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        user = mAuth.getCurrentUser();

        park_cost.setText("0");

        places_card.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Logs.class));
            }
        });

        final DocumentReference documentReference = fstore.collection("reg_users").document(userID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                car = documentSnapshot.getString("veh_model");
                car_num=documentSnapshot.getString("veh_num");

                car_2 = documentSnapshot.getString("veh_model_2");
                car_num_2=documentSnapshot.getString("veh_num_2");

                List<String> list = new ArrayList<String>();
                list.add(car + "\n"+ car_num);
                list.add(car_2 + "\n"+ car_num_2);

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.multiline_spinner_row, list);
                dataAdapter.setDropDownViewResource(R.layout.multiline_spinner_row);
                spinner.setAdapter(dataAdapter);

            }
        });

//        List<String> list = new ArrayList<String>();
////        list.add(car + "\n" + car_num);
//        list.add(car_2 + "\n"+ car_num_2);
//
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.multiline_spinner_row, list);
//        dataAdapter.setDropDownViewResource(R.layout.multiline_spinner_row);
//        spinner.setAdapter(dataAdapter);

        switch_button = view.findViewById(R.id.switch_vigilance);

        switch_button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (vig_on) {
                    vig_on = false;
                    vig_text.setVisibility(View.GONE);
                    vig_layout.setVisibility(View.GONE);
                    vig_layout_2.setVisibility(View.GONE);
                }
                else
                {
                    vig_on=true;
                    vig_text.setVisibility(View.VISIBLE);
                    vig_layout.setVisibility(View.VISIBLE);
                    vig_layout_2.setVisibility(View.VISIBLE);
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
