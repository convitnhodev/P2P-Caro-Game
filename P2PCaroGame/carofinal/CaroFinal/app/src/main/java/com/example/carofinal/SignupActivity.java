package com.example.carofinal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity implements View.OnTouchListener{
    public static final int STARTUP_DELAY = 300;
    public static final int ANIM_ITEM_DURATION = 1000;
    public static final int ITEM_DELAY = 300;

    public int SCREEN_SIZE;
    public int SET_TRANSLATE;
    private boolean animationStarted = false;
    private ImageView back;
    private Button signup;
    private EditText ID,PASSWORD,CONFIRMPASSWORD;


    DBHelper DB;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ID = (EditText) findViewById(R.id.signup_username);
        PASSWORD = (EditText) findViewById(R.id.signup_password);
        CONFIRMPASSWORD=(EditText) findViewById(R.id.confirm);
        signup=(Button) findViewById(R.id.signupbtn2);
        back=(ImageView)findViewById(R.id.back_btn) ;

        DB=new DBHelper(this);
        // Chuyển màn hình sign in -> Lưu vào database
        signup.setOnTouchListener(this);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user=ID.getText().toString();
                String pass= PASSWORD.getText().toString();
                String confirmPassword=CONFIRMPASSWORD.getText().toString();

                if(user.equals("")||pass.equals("")||confirmPassword.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Please enter all fields ", Toast.LENGTH_SHORT).show();
                }
                else if (pass.equals(confirmPassword)==false)
                {
                    Toast.makeText(getApplicationContext(),"Password not matching",Toast.LENGTH_SHORT).show();;
                }
                else
                {
                    Boolean checkUser=DB.checkUsername(user);
                    if(checkUser==true)
                    {
                        Toast.makeText(getApplicationContext(),"User already exists",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Boolean insert = DB.insertData(user,pass);
                        if(insert==true)
                        {
                            Toast.makeText(getApplicationContext(),"Registered successfully",Toast.LENGTH_SHORT).show();
                            Intent login_page= new Intent(SignupActivity.this,LoginActivity.class);
                            startActivity(login_page);

                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Registered failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }});

        //Đoạn này chuyển intent LoginActivity
        //back.setOnTouchListener(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });



        SCREEN_SIZE =getScreenResolution(this);

        if(SCREEN_SIZE >1500)
        {
            SET_TRANSLATE = -560;
        }
        else if(SCREEN_SIZE <=1500)
        {
            SET_TRANSLATE = -300;
        }





    }



    private int getScreenResolution(Context context)
    {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        //  Toast.makeText(SplashActivity.this , "Screen height is : "+ height , Toast.LENGTH_SHORT).show();

        return height ;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        if (!hasFocus || animationStarted) {
            return;
        }


        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v == signup) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                v.setAlpha(0.5f);
            } else {
                v.setAlpha(1f);
            }
        }
        else  if (v == back) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                v.setAlpha(0.5f);
            } else {
                v.setAlpha(1f);
            }
        }
        return false;
    }
}
