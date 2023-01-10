package com.example.carofinal;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class SettingActivity extends AppCompatActivity {
    Switch darkMode;
    Switch soundSwitch;
    private ImageView backBtn;

    MediaPlayer music;

    @Override
    protected void onCreate(@NonNull Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title

        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FIRST_APPLICATION_WINDOW); //enable full screen
        setContentView(R.layout.activity_settings);

        darkMode = (Switch)  findViewById(R.id.darkmode_switch);
        soundSwitch = (Switch)  findViewById(R.id.sound_switch);

        backBtn = (ImageView) findViewById(R.id.settings_back_btn);

        if(Services.DARKMODE_CHECK)
        {
            darkMode.setChecked(true);
        }
        else if(!Services.DARKMODE_CHECK)
        {
            darkMode.setChecked(false);
        }

        //Change darkmode handling here
        darkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    Services.DARKMODE_CHECK=true;
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

                }else {
                    Services.DARKMODE_CHECK=false;
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });

        //Sound check - handling here
        if(Services.SOUND_CHECK)
        {
            soundSwitch.setChecked(true);
        }
        else if(!Services.SOUND_CHECK)
        {
            soundSwitch.setChecked(false);
        }


        soundSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    Services.SOUND_CHECK =true;
                    startMusic();

                }
                else {
                    Services.SOUND_CHECK= false;
                    stopMusic();
                }
            }

        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void stopMusic() {

        MusicGame.player.stop();
        MusicGame.player.release();
        MusicGame.player = null;
    }

    private void startMusic() {

        MusicGame.player = MediaPlayer.create(getApplicationContext(), R.raw.night_run);
        MusicGame.player.start();
    }

}
