package com.example.sih;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;

public class PaymentsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);

        int width = Resources.getSystem().getDisplayMetrics().widthPixels;

        int height = Resources.getSystem().getDisplayMetrics().heightPixels;
        getWindow().setLayout((int) (width*.8), (int) (height*.6));
    }
}