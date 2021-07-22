package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView skip;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            if(msg.what == 0){
                count--;
                if(count == 0){
                    goSurfaceActivity();
                }
                else{
                    skip.setText("跳过"+count);
                    handler.sendEmptyMessageDelayed(0,1000);
                }
            }
            else if(msg.what == 1){
                count = 3;
                skip.setText("跳过"+count);
                handler.sendEmptyMessageDelayed(0,1000);
            }
        }
    };
    private int count = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        skip = findViewById(R.id.skip);
        handler.sendEmptyMessage(1);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacksAndMessages(null);
                goSurfaceActivity();
            }
        });

    }
    private void goSurfaceActivity(){
        Intent intent = new Intent(MainActivity.this,SurfaceActivity.class);
        startActivity(intent);
        finish();
    }
}