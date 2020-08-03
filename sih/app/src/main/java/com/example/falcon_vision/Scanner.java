package com.example.falcon_vision;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scanner extends AppCompatActivity {

    private static final String TAG ="100" ;

    SurfaceView surfaceView;
    CameraSource cameraSource;
    TextView resultData;
    BarcodeDetector barcodeDetector;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    String code;

    FirebaseAuth mAuth;
    FirebaseFirestore fstore;
    FirebaseUser user;
    String userID,car_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        surfaceView = findViewById(R.id.cam_prev);
        resultData = findViewById(R.id.qr_code_text);

        mAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        user = mAuth.getCurrentUser();


        DocumentReference docReference = fstore.collection("reg_users").document(userID);
        docReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                car_num=documentSnapshot.getString("veh_num");

            }
        });


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        }

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE).build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(640, 480).build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                try {
                    cameraSource.start(holder);

                }catch(IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();

            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }


            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrcode = detections.getDetectedItems();

                if(qrcode.size()!=0){
                    resultData.post(new Runnable() {
                        @Override
                        public void run() {
                            Vibrator vibrator = (Vibrator) getApplicationContext()
                                    .getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(100);
                            code=(qrcode.valueAt(0).displayValue);

                            final DocumentReference documentReference= fstore.collection(code).document("reg_vehicle").collection("verified").document(car_num);
                            documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                @Override
                                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                                    Map<String,Object> user = new HashMap<>();
                                    user.put("block", "false");
                                    user.put("number", car_num);
                                    user.put("visits", "1");

                                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {

                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(Scanner.this, "SUCCESS! The vehicle has been registered",
                                                    Toast.LENGTH_SHORT).show();

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d(TAG, "FAILUrEEEEEEEEE"+e.toString());
                                        }
                                    });


                                    startActivity(new Intent(Scanner.this, MainActivity.class));

                                    //
                                }
                            });

                        }
                    });
                }
            }
        });



//        final DocumentReference documentReference= fstore.collection(code).document("reg_vehicle").collection("verified").document(car_num);
//        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//
//                Map<String,Object> user = new HashMap<>();
//                user.put("block", "false");
//                user.put("number", car_num);
//                user.put("visits", "1");
//
//                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
//
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.d(TAG, "SUCCESS! THE VEHICLE HAS BEEN REGISTERED");
//
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d(TAG, "FAILUrEEEEEEEEE"+e.toString());
//                    }
//                });
//
//                //
//            }
//        });








    }
}