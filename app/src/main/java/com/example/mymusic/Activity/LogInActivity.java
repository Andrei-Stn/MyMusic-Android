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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LogInActivity extends AppCompatActivity {
    EditText emailID,password;
    Button btnSignIn;
    TextView tvSignUp;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Log in");

        mFirebaseAuth=FirebaseAuth.getInstance();
        emailID=findViewById(R.id.emailTxt);
        password=findViewById(R.id.passwordTxt);
        btnSignIn=findViewById(R.id.btnLogIn);
        tvSignUp=findViewById(R.id.textView);

        mAuthStateListener= new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFireBaseUser=mFirebaseAuth.getCurrentUser();
                if(mFireBaseUser!=null){
                    Toast.makeText(LogInActivity.this, "You are logged in ", Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(LogInActivity.this,HomeActivity.class);
                    sharedPref = getSharedPreferences("DisplayName", MODE_PRIVATE);
                    editor = sharedPref.edit().putString("emailKey", mFirebaseAuth.getCurrentUser().getEmail());
                    editor.apply();
                    startActivity(i);

                }
                else {
                    Toast.makeText(LogInActivity.this, "Please Login", Toast.LENGTH_SHORT).show();
                }
            }
        };
        btnSignIn.setOnClickListener(new View.OnClickListener() {
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
                else if(email.isEmpty() && pwd.isEmpty()){
                    Toast.makeText(LogInActivity.this, "Fiedls are empty", Toast.LENGTH_SHORT).show();
                }
                else if(!(email.isEmpty() && pwd.isEmpty())){
                    mFirebaseAuth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(LogInActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(LogInActivity.this,"Login Error,Please Login Again",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Intent intToHome=new Intent(LogInActivity.this,HomeActivity.class);
                                startActivity(intToHome);
                            }

                        }
                    });
                }
                else{
                    Toast.makeText(LogInActivity.this, "Error Ocurred!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intSignUp=new Intent(LogInActivity.this, RegisterActivity.class);
                startActivity(intSignUp);
            }
        });
    }
    @Override
    protected void onStart(){
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

 /*   private void isUser(){
       final String userEnteredEmail=emailID.getText().toString();
       final String userEnteredPassword=password.getText().toString();

        DatabaseReference reference = FirebaseDatabase.getInstance("https://mymusic-b3f98.firebaseio.com/").getReference("mail");

        Query checkUser=reference.orderByChild("email").equalTo(userEnteredEmail);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    String passwordFromDb=dataSnapshot.child(userEnteredEmail).child("password").getValue(String.class);

                    if(passwordFromDb.equals(userEnteredPassword)){
                        String fullNameFromDb=dataSnapshot.child(userEnteredEmail).child("fullName").getValue(String.class);
                        String emailFromDb=dataSnapshot.child(userEnteredEmail).child("email").getValue(String.class);
                        String usernameFromDb=dataSnapshot.child(userEnteredEmail).child("username").getValue(String.class);

                        Intent intent =new Intent (getApplicationContext(), UserActivity.class);
                        intent.putExtra("fullName",fullNameFromDb);
                        intent.putExtra("username",usernameFromDb);
                        intent.putExtra("email",emailFromDb);
                        intent.putExtra("password",passwordFromDb);

                        startActivity(intent);
                    }
                    else {
                        password.setError("Wrong password");
                        password.requestFocus();
                    }
                }
                else{
                    emailID.setError("No such Email exist");
                    emailID.requestFocus();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/

}
