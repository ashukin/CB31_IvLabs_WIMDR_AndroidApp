package com.example.falcon_vision;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class AddCarActivity extends AppCompatActivity {

    private static final String TAG = "test";
    private static final int CHOOSE_IMAGE =101 ;
    private DatabaseReference reff;

    Uri uriProfileImage;
    EditText v_model, v_num, v_dop;
    ImageView  car_f,car_b,num_plate;

    Button save;
    String userID;
    int i =0, c=0;

    String veh_num, phone, name, email, veh_model,  veh_dop, pwd;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore fstore;
    final Calendar myCalendar = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        v_model=findViewById(R.id.add_car_model);
        v_num = findViewById(R.id.add_car_num);
        v_dop = findViewById(R.id.add_car_dop);
        car_f = findViewById(R.id.add_car_front);
        car_b = findViewById(R.id.add_car_back);
        num_plate=findViewById(R.id.add_car_num_plate);

        save = findViewById(R.id.save_car);


        firebaseAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        userID = firebaseAuth.getCurrentUser().getUid();

//        final Map<String,Object> user = (Map<String, Object>) getIntent().getSerializableExtra("key");

        car_f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i=0;
                showImageChooser();
                c+=1;


            }
        });
        car_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i=1;
                showImageChooser();
                c+=1;

            }
        });
        num_plate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i=2;
                showImageChooser();
                c+=1;

            }
        });

        userID = firebaseAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fstore.collection("reg_users").document(userID);
        final Map<String,Object> user = new HashMap<>();

        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                phone = documentSnapshot.getString("phone");
                name = documentSnapshot.getString("name");
                email = documentSnapshot.getString("email");
                veh_model = documentSnapshot.getString("veh_model");
                veh_num=documentSnapshot.getString("veh_num");
                veh_dop=documentSnapshot.getString("veh_dop");
                pwd=documentSnapshot.getString("pwd");

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String veh_dop_2 = v_dop.getText().toString();
                final String veh_model_2 = v_model.getText().toString();
                final String veh_num_2 = v_num.getText().toString().toUpperCase();


//                userID = firebaseAuth.getCurrentUser().getUid();
                DocumentReference documentReference = fstore.collection("reg_users").document(userID);
//                final Map<String,Object> user = new HashMap<>();

//                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//
//                        phone = documentSnapshot.getString("phone");
//                        name = documentSnapshot.getString("name");
//                        email = documentSnapshot.getString("email");
//                        veh_model = documentSnapshot.getString("veh_model");
//                        veh_num=documentSnapshot.getString("veh_num");
//                        veh_dop=documentSnapshot.getString("veh_dop");
//                        pwd=documentSnapshot.getString("pwd");
//
//                    }
//                });

//                reff = FirebaseDatabase.getInstance().getReference();
////                DatabaseReference userRef = ref.child("reg_users").child(userID).push();
////                String key = userRef.getKey();

                user.put("phone", phone);
                user.put("name", name);
                user.put("email", email);
                user.put("veh_model", veh_model);
                user.put("veh_num", veh_num);
                user.put("veh_dop", veh_dop);
                user.put("pwd", pwd);
                user.put("veh_model_2",veh_model_2);
                user.put("veh_num_2",veh_num_2);
                user.put("veh_dop_2",veh_dop_2);

//                reff.child("reg_users").child(userID)
//                        .addListenerForSingleValueEvent(new ValueEventListener() {
//                                @Override
//                                public void onDataChange (DataSnapshot dataSnapshot) {
//                                    Map<String, Object> postValues = new HashMap<String,Object>();
//                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                                        postValues.put(snapshot.getKey(),snapshot.getValue());
//                                    }
//                                    postValues.put("veh_model_2",veh_model_2);
//                                    postValues.put("veh_num_2",veh_num_2);
//                                    postValues.put("veh_dop_2",veh_dop_2);
//                                    reff.child("reg_users").child(userID).updateChildren(postValues);
//                                }
//
//                                @Override
//                                public void onCancelled(DatabaseError databaseError) {}
//                            }
//                        );

//                ref.child("reg_users").child(userID).child("veh_model_2").setValue(veh_model_2);
//                ref.child("reg_users").child(userID).child("veh_num_2").setValue(veh_num_2);
//                ref.child("reg_users").child(userID).child("veh_dop_2").setValue(veh_dop_2);





                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "SUCCESS! USER PROFILE IS CREATED FOR)))"+userID);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "FAILUrEEEEEEEEE"+e.toString());
                    }
                });

                if(c==3) {

                    getSupportFragmentManager().beginTransaction()
                            .add(android.R.id.content, new SettingsFragment ()).commit();
                    Toast.makeText(AddCarActivity.this, "Registered Successfully",
                            Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(AddCarActivity.this, "Upload all the images to register",
                            Toast.LENGTH_LONG).show();
                }




            }


        });



        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        v_dop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddCarActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CHOOSE_IMAGE && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            uriProfileImage = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), uriProfileImage);
//                pro_pic.setImageBitmap(bitmap);

                uploadImageToFirebaseStorage();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImageToFirebaseStorage() {
        userID = firebaseAuth.getCurrentUser().getUid();

        final StorageReference pro_pic_reference =
                FirebaseStorage.getInstance().getReference("car_pics/"+ userID +veh_num+ i + ".jpg");


        if(uriProfileImage != null) {
            pro_pic_reference.putFile(uriProfileImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            pro_pic_reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    if(i==0) {
                                        Picasso.get().load(uri).into(car_f);
                                    }else if(i==1){
                                        Picasso.get().load(uri).into(car_b);
                                    }else{
                                        Picasso.get().load(uri).into(num_plate);
                                    }
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddCarActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), CHOOSE_IMAGE);
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        v_dop.setText(sdf.format(myCalendar.getTime()));
    }
}
