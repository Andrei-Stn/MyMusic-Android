package com.example.mymusic.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymusic.R;
import com.example.mymusic.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    EditText emailID,password,userName,fullName;
    Button btnSignUp;
    TextView tvSignIN;
    FirebaseAuth mFirebaseAuth;

    FirebaseDatabase database;
    DatabaseReference reference;

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().setTitle("Register");

        mFirebaseAuth=FirebaseAuth.getInstance();
        emailID=findViewById(R.id.emailEdTxt);
        password=findViewById(R.id.passwordEdTxt);
        userName=findViewById(R.id.userNameEt);
        fullName=findViewById(R.id.fullNameEt);
        btnSignUp=findViewById(R.id.btnRegister);
        tvSignIN=findViewById(R.id.txtViewSign);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=emailID.getText().toString();
                String pwd=password.getText().toString();
                if(email.isEmpty()){
                    emailID.setError("Please enter email id");
                    emailID.requestFocus();

                }
                else if (pwd.isEmpty()){
                    password.setError("Please enter your password");
                    password.requestFocus();
                }
                else if(email.isEmpty()  &&  pwd.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Fiedls are empty", Toast.LENGTH_SHORT).show();
                }
                else if(!(email.isEmpty() && pwd.isEmpty())){
                    mFirebaseAuth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "Sign Up Unsuccessful, Please try again", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                sharedPref = getSharedPreferences("DisplayName", MODE_PRIVATE);
                                editor = sharedPref.edit().putString("UserNameKey", userName.getText().toString());
                                editor.apply();
                                startActivity(new Intent(RegisterActivity.this,HomeActivity.class));
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(RegisterActivity.this, "Error Ocurred!", Toast.LENGTH_SHORT).show();
                }
                String name=fullName.getText().toString();
                String username=userName.getText().toString();
                String emaili=emailID.getText().toString();
                String pwds=password.getText().toString();



                User user= new User(name,username,email,pwds);

                database=FirebaseDatabase.getInstance("https://mymusic-b3f98.firebaseio.com/");
                reference=database.getReference("mail");
                reference.child(username).setValue(user);

            }
        });

        tvSignIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i =new Intent(RegisterActivity.this, LogInActivity.class);
                startActivity(i);
            }
        });

    }
}
