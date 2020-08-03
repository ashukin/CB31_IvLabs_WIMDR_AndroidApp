package com.example.falcon_vision;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class Logs extends AppCompatActivity {

    TextView place, date, time;
    FirebaseAuth mAuth;
    FirebaseFirestore fstore;
    FirebaseUser user;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);

        place = findViewById(R.id.place);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);

        mAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        user = mAuth.getCurrentUser();


//        DocumentReference docReference = fstore.collection("MH31FA4640").document("Jh8eNyeMIwFh2Os1Nrhk");
//        docReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//                place.setText(documentSnapshot.getString("place"));
//                date.setText(documentSnapshot.getString("date"));
//                time.setText(documentSnapshot.getString("time"));
//
//            }
//        });

    }
}