package com.example.falcon_vision;


import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


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
import com.squareup.picasso.Picasso;



public class ShowProFragment extends Fragment {


    ImageView pro_pic, edit_profile;
    TextView pro_name, pro_email, pro_phone, pro_v_type, pro_v_num, pro_v_type_2, pro_v_num_2;


    FirebaseAuth mAuth;
    FirebaseFirestore fstore;
    FirebaseUser user;
    String userID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_pro, container, false);

        pro_pic = view.findViewById(R.id.show_profile_pic);
        pro_name = view.findViewById(R.id.show_pro_name);
        pro_email = view.findViewById(R.id.show_pro_email);
        pro_phone = view.findViewById(R.id.show_pro_phone);
        pro_v_type = view.findViewById(R.id.show_pro_v_type);
        pro_v_num = view.findViewById(R.id.show_pro_v_num);
        pro_v_type_2 = view.findViewById(R.id.show_pro_v_type_2);
        pro_v_num_2 = view.findViewById(R.id.show_pro_v_num_2);


        edit_profile = (ImageView) view.findViewById(R.id.edit_profile_pen);


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
                pro_v_type.setText(documentSnapshot.getString("veh_model"));
                pro_v_num.setText(documentSnapshot.getString("veh_num").toUpperCase());

                if(documentSnapshot.getString("veh_model_2")!=null || documentSnapshot.getString("veh_num_2")!=null){
                    pro_v_type_2.setText(documentSnapshot.getString("veh_model_2"));
                    pro_v_num_2.setText(documentSnapshot.getString("veh_num_2").toUpperCase());
                }else {
                    pro_v_type_2.setText("-");
                    pro_v_num_2.setText("-");
                }

            }
        });


        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfileFragment()).commit();


            }
        });

        return view;
    }

//    public void onBackPressed(){
//        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                new DashboardFragment()).commit();
//
//    }
}
