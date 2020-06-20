package com.example.mymusic.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mymusic.R;

public class MainActivity extends AppCompatActivity {

    private  static int SPLASH_SCREEN =5000;

    Animation topAnim,botAnim;
    ImageView image;
    TextView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        topAnim= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        botAnim=AnimationUtils.loadAnimation(this,R.anim.botton_animation);

        image=findViewById(R.id.musicIw);
        image.setAnimation(topAnim);

        logo=findViewById(R.id.logo);
        logo.setAnimation(botAnim);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(MainActivity.this,IntroActivity.class);
                startActivity(i);
                finish();
            }
        },SPLASH_SCREEN);
    }

}
