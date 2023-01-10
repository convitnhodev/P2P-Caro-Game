package com.example.carofinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ChooseConnectionActivity extends AppCompatActivity implements View.OnTouchListener {


    private ImageView BackBtn , WifiImg , WifiRadioImg , BluetoothImg , BlueToothRadioImg;
    private Button ContinueBtn;

    int PICK_SIDE=1 ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FIRST_APPLICATION_WINDOW
        ); //enable full screen
        setContentView(R.layout.connect_activity);


        BackBtn= (ImageView) findViewById(R.id.connect_back_btn);
        WifiImg= (ImageView) findViewById(R.id.wifi);
        BluetoothImg= (ImageView) findViewById(R.id.bluetooth);
        WifiRadioImg= (ImageView) findViewById(R.id.wifi_radio);
        BlueToothRadioImg= (ImageView) findViewById(R.id.bluetooth_radio);

        ContinueBtn = (Button) findViewById(R.id.connect_continue_btn);

        // CrossRadioImg.setOnTouchListener(this);
        WifiRadioImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PICK_SIDE = 1;
                WifiRadioImg.setImageResource(R.drawable.radio_button_checked);
                BlueToothRadioImg.setImageResource(R.drawable.radio_button_unchecked);
                BluetoothImg.setAlpha(0.3f);
                WifiImg.setAlpha(1.0f);
                //Intent intent = new Intent(.this,Ch.class);
                // startActivity(intent);
            }
        });

        // CircleRadioImg.setOnTouchListener(this);
        BlueToothRadioImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                PICK_SIDE= 2;
                BlueToothRadioImg.setImageResource(R.drawable.radio_button_checked);
                WifiRadioImg.setImageResource(R.drawable.radio_button_unchecked);
                WifiImg.setAlpha(0.3f);
                BluetoothImg.setAlpha(1.0f);

                //Intent intent = new Intent(.this,Ch.class);
                // startActivity(intent);
            }
        });

        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                onBackPressed();
                //Intent intent = new Intent(.this,Ch.class);
                // startActivity(intent);
            }
        });

        ContinueBtn.setOnTouchListener(this);
        ContinueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PICK_SIDE==1)
                {
                    Intent intent=new Intent(ChooseConnectionActivity.this,Caro_p2p.class);
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(ChooseConnectionActivity.this,BluetoothActivity.class);
                    startActivity(intent);
                }
            }
        });
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v == ContinueBtn) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                v.setAlpha(0.5f);
            } else {
                v.setAlpha(1f);
            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}