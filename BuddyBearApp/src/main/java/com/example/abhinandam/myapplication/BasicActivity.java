package com.example.abhinandam.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

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
