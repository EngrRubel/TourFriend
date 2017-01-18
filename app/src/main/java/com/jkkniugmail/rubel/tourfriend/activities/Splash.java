package com.jkkniugmail.rubel.tourfriend.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.jkkniugmail.rubel.tourfriend.R;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final ImageView iv = (ImageView)findViewById(R.id.imageSplash);

        final Animation an = AnimationUtils.loadAnimation(getBaseContext(),R.anim.zoom_in);
        //for fade out
        final Animation an2 = AnimationUtils.loadAnimation(getBaseContext(),R.anim.fade_in);


        iv.startAnimation(an2);
        an2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                iv.startAnimation(an);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        an.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
