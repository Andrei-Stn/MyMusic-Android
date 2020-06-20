package com.example.mymusic.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mymusic.R;

public class IntroActivity extends AppCompatActivity {
    Button btn_sign_in;
    Button btn_sign_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        getSupportActionBar().hide();
        btn_sign_up=findViewById(R.id.btn_sign_up_intro);
        btn_sign_in=findViewById(R.id.btn_sign_in_intro);

        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(IntroActivity.this,LogInActivity.class);
                startActivity(i);
            }
        });

        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(IntroActivity.this,RegisterActivity.class);
                startActivity(i);
            }
        });
    }
}
