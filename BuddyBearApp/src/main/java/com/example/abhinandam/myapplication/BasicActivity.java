package com.example.abhinandam.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class BasicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent cameraPage = new Intent(BasicActivity.this, CameraActivity.class);
                BasicActivity.this.startActivity(cameraPage);
            }
        }, 2000);


    }

}
