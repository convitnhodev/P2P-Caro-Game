package com.example.carofinal;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class ChoiceModeActivity extends AppCompatActivity implements View.OnTouchListener {


    public static final int STARTUP_DELAY = 300;
    public static final int ANIM_ITEM_DURATION = 1000;
    public static final int ITEM_DELAY = 300;

    public int SCREEN_SIZE;
    public int SET_TRANSLATE;
    private boolean animationStarted = false;
    DBHelper DB;
    private GifImageView settingsGifView;
    private Button WithAFriendBtn , WithAi,MatchHistory,Shopping;
    private ImageView logout;
    Dialog quitdialog;
    TextView name,gold;
    Dialog shop;
    Integer chooseSkin=0;
    Integer choosen=0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choicemode);


        settingsGifView  = (GifImageView) findViewById(R.id.seting_gifview_offline_menu);
        WithAFriendBtn = (Button) findViewById(R.id.playerbtn);
        WithAi = (Button) findViewById(R.id.aibtn);
        MatchHistory=(Button) findViewById(R.id.matchbtn);
        Shopping=(Button) findViewById(R.id.shopbtn);
        logout=(ImageView) findViewById(R.id.logoutbtn) ;

        quitdialog = new Dialog(this);
        shop=new Dialog(this);
        DB=new DBHelper(this);
        choosen=DB.getChoosen(LoginActivity.user_ID);

        name=(TextView) findViewById(R.id.user_name_txt);
        name.setText(LoginActivity.user_ID);
        gold=(TextView) findViewById(R.id.player_two_won_txt);


        //DB.updateGold(LoginActivity.user_ID,4);
        LoginActivity.user_Gold=DB.getGold(LoginActivity.user_ID);
        gold.setText(String.valueOf(LoginActivity.user_Gold));



        // settingsGifView.getBackground().Stop();

        // settingsGifView.getAnimation().hasEnded();


        Drawable drawable = settingsGifView.getDrawable();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).stop();
        }

        SCREEN_SIZE =getScreenResolution(this);

        if(SCREEN_SIZE >1500)
        {
            SET_TRANSLATE = -560;
        }
        else if(SCREEN_SIZE <=1500)
        {
            SET_TRANSLATE = -300;
        }
        //Play with player
        WithAFriendBtn.setOnTouchListener(this);
        WithAFriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChoiceModeActivity.this,ChooseConnectionActivity.class);
                startActivity(intent);
            }
        });
        //Play with ai
        WithAi.setOnTouchListener(this);
        WithAi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(    ChoiceModeActivity.this,ChooseSymbolActivity.class);
                intent.putExtra("skin",DB.getChoosen(LoginActivity.user_ID));
                startActivity(intent);
            }
        });
        //Match history
        MatchHistory.setOnTouchListener(this);
        MatchHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(    ChoiceModeActivity.this,MatchHistory.class);
                startActivity(intent);
            }
        });
        //Shopping
        Shopping.setOnTouchListener(this);
        Shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopDialogFun();
            }
        });

        logout.setOnTouchListener(this);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quitDialogfun();
            }
        });

        //settingsGifView.setOnTouchListener(this);
        settingsGifView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler handler =  new Handler(Looper.getMainLooper());
                Drawable drawable = settingsGifView.getDrawable();
                if (drawable instanceof Animatable) {
                    ((Animatable) drawable).start();

                }


                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Drawable drawable = settingsGifView.getDrawable();
                        if (drawable instanceof Animatable) {
                            ((Animatable) drawable).stop();

                        }
                        Intent intent = new Intent(ChoiceModeActivity.this,SettingActivity.class);
                        startActivity(intent);

                    }
                }, 500);

            }
        });


    }
    @Override
    protected void onResume() {
        LoginActivity.user_Gold=DB.getGold(LoginActivity.user_ID);
        gold.setText(String.valueOf(LoginActivity.user_Gold));
        super.onResume();

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
        if (v == WithAFriendBtn) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                v.setAlpha(0.5f);
            } else {
                v.setAlpha(1f);
            }
        }
        else  if (v == WithAi) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                v.setAlpha(0.5f);
            } else {
                v.setAlpha(1f);
            }
        }
        else  if (v == MatchHistory) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                v.setAlpha(0.5f);
            } else {
                v.setAlpha(1f);
            }
        }
        else  if (v == Shopping) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                v.setAlpha(0.5f);
            } else {
                v.setAlpha(1f);
            }
        }
        else  if (v ==logout ) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                v.setAlpha(0.5f);
            } else {
                v.setAlpha(1f);
            }
        }
        return false;
    }
    boolean checkInArray(int[] arr, int toCheckValue)
    {

        for (int i=0;i<arr.length;i++){
            if (arr[i]==toCheckValue){
                return true;
            }
        }
        return false;

    }
    private void  shopDialogFun() {


        shop.setContentView(R.layout.shop_dialog);
        shop.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        shop.setCanceledOnTouchOutside(false);

        TextView user_gold=(TextView) shop.findViewById(R.id.user_gold);
        user_gold.setText(String.valueOf(DB.getGold(LoginActivity.user_ID)));
        String listSkinDB=DB.getListSkin(LoginActivity.user_ID);

        List<Integer> listSkin=new ArrayList<>();

        if (listSkinDB.length()==1)
        {
            int temp= Integer.parseInt(listSkinDB);
            listSkin.add(temp);

        }
        else
        {
            String[] list=listSkinDB.split(",");
            for (int i=0;i<list.length;i++)
            {
                listSkin.add(Integer.parseInt(list[i]));
            }
        }

        Button skin0=shop.findViewById(R.id.skin0);
        Button skin1 = shop.findViewById(R.id.skin1);
        Button skin2=shop.findViewById(R.id.skin2);
        for (int i=0;i<listSkin.size();i++){
            if(listSkin.get(i)==0){
                skin0.setText("Choose");
            }else if(listSkin.get(i)==1){
                skin1.setText("Choose");
            }else if (listSkin.get(i)==2)
            {
                skin2.setText("Choose");
            }
        }
        if (choosen==0){
            skin0.setText("Choosen");

        }else if (choosen==1){
            skin1.setText("Choosen");
        }else if (choosen==2){
            skin2.setText("Choosen");
        }
        ImageView exit=shop.findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shop.dismiss();

            }
        });
        skin0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chooseSkin=0;
                if (listSkin.indexOf(1)>=0) {
                    skin1.setText("Choose");
                }
                if (listSkin.indexOf(2)>=0) {
                    skin2.setText("Choose");
                }
                skin0.setText("Choosen");
                DB.updateChoosen(LoginActivity.user_ID,0);
                quitdialog.dismiss();
            }
        });
        if(listSkin.indexOf(1)<0){
            skin1.setText("Buy: 10");
        }
        if(listSkin.indexOf(2)<0){
            skin2.setText("Buy: 20");
        }
        skin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listSkin.indexOf(1)<0){
                    if(LoginActivity.user_Gold>=10)
                    {
                        LoginActivity.user_Gold-=10;
                        DB.updateGold(LoginActivity.user_ID,LoginActivity.user_Gold);
                        user_gold.setText(String.valueOf(DB.getGold(LoginActivity.user_ID)));
                        listSkin.add(1);
                        DB.updatelistSkin(LoginActivity.user_ID,1);
                        skin1.setText("Choose");
                        LoginActivity.user_Gold=DB.getGold(LoginActivity.user_ID);
                        gold.setText(String.valueOf(LoginActivity.user_Gold));

                    }
                    else {
                        Toast.makeText(ChoiceModeActivity.this,"not enough money",Toast.LENGTH_SHORT).show();

                    }
                }else {
                    choosen=1;
                    chooseSkin = 1;
                    skin1.setText("Choosen");
                    if (listSkin.indexOf(2)>=0){
                        skin2.setText("Choose");
                    }

                    skin0.setText("Choose");
                    DB.updateChoosen(LoginActivity.user_ID,1);

                }
                quitdialog.dismiss();
            }
        });
        skin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listSkin.indexOf(2)<0){
                    if(LoginActivity.user_Gold>=20)
                    {
                        LoginActivity.user_Gold-=20;
                        listSkin.add(2);
                        DB.updateGold(LoginActivity.user_ID,LoginActivity.user_Gold);
                        user_gold.setText(String.valueOf(DB.getGold(LoginActivity.user_ID)));
                        DB.updatelistSkin(LoginActivity.user_ID,2);
                        skin2.setText("Choose");
                        LoginActivity.user_Gold=DB.getGold(LoginActivity.user_ID);
                        gold.setText(String.valueOf(LoginActivity.user_Gold));

                    }
                    else {
                        Toast.makeText(ChoiceModeActivity.this,"not enough money",Toast.LENGTH_SHORT).show();
                    }

                }else {
                    choosen=2;
                    chooseSkin = 2;
                    if (listSkin.indexOf(1)>=0) {
                        skin1.setText("Choose");
                    }
                    skin2.setText("Choosen");
                    skin0.setText("Choose");
                    DB.updateChoosen(LoginActivity.user_ID,2);

                }

                quitdialog.dismiss();
            }
        });

        shop.show();
    }
    private void  quitDialogfun() {


        quitdialog.setContentView(R.layout.quit_dialog);
        quitdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        quitdialog.setCanceledOnTouchOutside(false);

        Button continueBtn = quitdialog.findViewById(R.id.continue_btn);
        Button logout2=quitdialog.findViewById(R.id.logoutbtn2);

        logout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitdialog.dismiss();
                Intent intent = new Intent(ChoiceModeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitdialog.dismiss();
            }
        });
        quitdialog.show();
    }
}