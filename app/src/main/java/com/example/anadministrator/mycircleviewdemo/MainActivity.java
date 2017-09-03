package com.example.anadministrator.mycircleviewdemo;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private CircleBar circleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        System.out.println(absolutePath);
        File file=new File(absolutePath,"aaaa.text");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        circleBar = (CircleBar) findViewById(R.id.circle);
        circleBar.setSweepAngle(360);

        circleBar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                circleBar.startCustomAnimation();
            }
        });
        new Handler().postDelayed(new Runnable() {
            public void run() {
                circleBar.setText("100");
            }
        }, 2000);

    }


}

