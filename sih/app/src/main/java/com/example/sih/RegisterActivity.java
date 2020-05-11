package com.example.sih;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    public static int DEKHO ;

    EditText name;
    EditText email;
    EditText phone;
    EditText pwd;
    EditText c_pwd;
    ImageView signup;
    TextView login;


    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.v(String.valueOf(RegisterActivity.this),"!!!!!!!!!!!!!!!!!!!Restart karne me value = " + DEKHO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        pwd = findViewById(R.id.pwd);
        c_pwd = findViewById(R.id.confirm_pwd);
        signup = findViewById(R.id.register);
        login = findViewById(R.id.login_link);


//        toolbar.setTitle(R.string.app_name);

        firebaseAuth = FirebaseAuth.getInstance();

//        If the user is already logged in, take him to main activity
        if(firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(RegisterActivity.this,MainActivity.class));
            finish();
        }



        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = email.getText().toString().trim();
                String password = pwd.getText().toString().trim();
                String c_password = c_pwd.getText().toString().trim();

                if(TextUtils.isEmpty(mail)) {
                    email.setError("Email is required");
                    return;
                }

                if (!mail.contains("@")) {
                    email.setError("Enter valid e-mail address");
                }

                if(phone.length()!=10) {
                    phone.setError("Enter valid phone number");
                }

                if(TextUtils.isEmpty(password)) {
                    pwd.setError("Password is required");
                    return;
                }

                if (password.length()<6){
                    pwd.setError("Password must be greater than 6 characters");
                }

                if (!password.equals(c_password)) {
                    pwd.setError("Password doesn't match. Re-enter password.");
                    pwd.setText("");
                    c_pwd.setText("");
                    return;
                }


                firebaseAuth.createUserWithEmailAndPassword(mail, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, "Registered Successfully",
                                            Toast.LENGTH_LONG).show();

                                    startActivity(new Intent(RegisterActivity.this,MainActivity.class));
//                                    finish();

                                }else{
                                    Toast.makeText(RegisterActivity.this, task.getException().getMessage(),
                                            Toast.LENGTH_LONG).show();

                                }

                            }
                        });
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
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
}
