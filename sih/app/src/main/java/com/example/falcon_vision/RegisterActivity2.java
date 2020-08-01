package com.example.falcon_vision;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class RegisterActivity2 extends AppCompatActivity {

    private static final String TAG = "test";
    private static final int CHOOSE_IMAGE =101 ;

    Uri uriProfileImage;
    EditText v_model, v_num, v_dop;
    ImageView signup, car_f,car_b,num_plate;
    TextView login;
    String userID;
    int i =0;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        v_model=findViewById(R.id.car_model);
        v_num = findViewById(R.id.car_num);
        v_dop = findViewById(R.id.car_dop);
        car_f = findViewById(R.id.car_front);
        car_b = findViewById(R.id.car_back);
        num_plate=findViewById(R.id.car_num_plate);

        signup = findViewById(R.id.register_2);
        login = findViewById(R.id.login_link);





//        toolbar.setTitle(R.string.app_name);

        firebaseAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

//        If the user is already logged in, take him to main activity
//        if(firebaseAuth.getCurrentUser() != null) {
//            startActivity(new Intent(RegisterActivity.this,MainActivity.class));
//            finish();
//        }
        final Map<String,Object> user = (Map<String, Object>) getIntent().getSerializableExtra("key");

        car_f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i=0;
                showImageChooser();

            }
        });
        car_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i=1;
                showImageChooser();

            }
        });
        num_plate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i=2;
                showImageChooser();

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String veh_dop = v_dop.getText().toString();
                final String veh_model = v_model.getText().toString();
                final String veh_num = v_num.getText().toString();


                userID = firebaseAuth.getCurrentUser().getUid();

                DocumentReference documentReference = fstore.collection("reg_users").document(userID);

                user.put("veh_model",veh_model);
                user.put("veh_num",veh_num);
                user.put("veh_dop",veh_dop);

                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "SUCCESS! USER PROFILE IS CREATED FOR"+userID);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "FAILUrEEEEEEEEE"+e.toString());
                    }
                });
                startActivity(new Intent(RegisterActivity2.this,MainActivity.class));


            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity2.this,LoginActivity.class));
//                finish();
            }
        });




    }

    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

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
                FirebaseStorage.getInstance().getReference("car_pics/"+ userID + i + ".jpg");


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
                            Toast.makeText(RegisterActivity2.this,e.getMessage(), Toast.LENGTH_SHORT).show();
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
}
