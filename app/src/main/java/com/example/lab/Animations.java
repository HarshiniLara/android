package com.example.lab;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Animations extends AppCompatActivity {

    private Button btnRotate, btnBlink, btnFadeIn, btnFadeOut, btnSlideUp, btnSlideDown;
    private TextView textViewToAnimate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animation);

        textViewToAnimate = findViewById(R.id.textViewToAnimate);
        btnRotate = findViewById(R.id.btnRotate);
        btnBlink = findViewById(R.id.btnBlink);
        btnFadeIn = findViewById(R.id.btnFade);
        btnSlideUp = findViewById(R.id.btnSlideUp);
        btnSlideDown = findViewById(R.id.btnSlideDown);

        final Animation rotate = AnimationUtils.loadAnimation(this, R.anim.rotate);
        final Animation blink = AnimationUtils.loadAnimation(this, R.anim.blink);
        final Animation fade = AnimationUtils.loadAnimation(this, R.anim.fade);
        final Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slideup);
        final Animation slideDown = AnimationUtils.loadAnimation(this, R.anim.slidedown);

        btnRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewToAnimate.startAnimation(rotate);
            }
        });

        btnBlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewToAnimate.startAnimation(blink);
            }
        });

        btnFadeIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewToAnimate.startAnimation(fade);
            }
        });


        btnSlideUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewToAnimate.startAnimation(slideUp);
            }
        });

        btnSlideDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewToAnimate.startAnimation(slideDown);
            }
        });
    }
}
