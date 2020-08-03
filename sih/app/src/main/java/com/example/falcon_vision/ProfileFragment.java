package com.example.falcon_vision;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {

    private static final int CHOOSE_IMAGE = 101;

    Uri uriProfileImage;
    ImageView pro_pic, drawer_img;
    EditText pro_name, pro_phone, pro_pwd, pro_v_type, pro_v_num;
    TextView pro_email;
    String profileImageUrl;
    Button save;

    FirebaseAuth mAuth;
    FirebaseFirestore fstore;
    StorageReference storageReference;
    FirebaseUser user;
    String userID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        pro_pic = view.findViewById(R.id.profile_pic);
        drawer_img = view.findViewById(R.id.drawer_img);
        pro_name = view.findViewById(R.id.profile_name);
        pro_email = view.findViewById(R.id.pro_email);
        pro_phone = view.findViewById(R.id.pro_phone);
        pro_pwd = view.findViewById(R.id.pro_pwd);

        mAuth = FirebaseAuth.getInstance();
        save = view.findViewById(R.id.save_profile);

        mAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        user = mAuth.getCurrentUser();


        StorageReference profileRef = FirebaseStorage.getInstance().getReference("profilepics/"+ userID + ".jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(pro_pic);
            }
        });

        final DocumentReference documentReference = fstore.collection("reg_users").document(userID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                pro_phone.setText(documentSnapshot.getString("phone"));
                pro_name.setText(documentSnapshot.getString("name"));
                pro_email.setText(documentSnapshot.getString("email"));
                pro_pwd.setText(documentSnapshot.getString("pwd"));

            }
        });


        pro_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageChooser();

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pro_name.getText().toString().isEmpty() || pro_email.getText().toString().isEmpty() || pro_phone.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "One or many fields are empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                final String email = pro_email.getText().toString();
                user.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        DocumentReference docRef = fstore.collection("reg_users").document(user.getUid());
                        Map<String,Object> edited = new HashMap<>();
                        edited.put("email", email);
                        edited.put("name", pro_name.getText().toString());
                        edited.put("phone", pro_phone.getText().toString());
                        docRef.update(edited);

                        Toast.makeText(getActivity(),"Saved", Toast.LENGTH_SHORT).show();

                        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new ShowProFragment()).commit();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(),e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


        return view;
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CHOOSE_IMAGE && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            uriProfileImage = data.getData();

            try {
                Bitmap bitmap = Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), uriProfileImage);
//                pro_pic.setImageBitmap(bitmap);

                uploadImageToFirebaseStorage();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImageToFirebaseStorage() {
        final StorageReference pro_pic_reference =
                FirebaseStorage.getInstance().getReference("profilepics/"+ userID + ".jpg");

        if(uriProfileImage != null) {
            pro_pic_reference.putFile(uriProfileImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            pro_pic_reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Picasso.get().load(uri).into(pro_pic);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(),e.getMessage(), Toast.LENGTH_SHORT).show();
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
