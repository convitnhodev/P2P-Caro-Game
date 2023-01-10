package com.example.carofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Select_direct extends AppCompatActivity {

    Button btn_bluetooth,btn_wifi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_direct);
        btn_bluetooth=(Button) findViewById(R.id.btn_bluetooth);
        btn_bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Select_direct.this,BluetoothActivity.class);
                startActivity(intent);

            }
        });
        btn_wifi=(Button) findViewById(R.id.btn_wifi);
        btn_wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Select_direct.this,Caro_p2p.class);
                startActivity(intent);
            }
        });
    }
}