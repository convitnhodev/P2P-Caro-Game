package com.example.carofinal;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.skydoves.balloon.ArrowPositionRules;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;
import com.skydoves.balloon.BalloonSizeSpec;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import pl.droidsonroids.gif.GifImageView;

public class Fragment_Caro_with_friend_Bluetooth extends Fragment implements FragmentCallBacks {
    BluetoothActivity main;
    EditText edt1;
    TextView tv1;
    Button bt_send;
    ImageView backBtn;
    String name_player1;
    String name_player2;
    ImageView wifi_profile_friend, btn_wifi_chat;
    DBHelper DB;
    TextView user_name_txt_wifi_player2,user_name_txt_wifi_player1;
    TextView player_two_won_txt_wifi_player1,player_two_won_txt_wifi_player2;
    public static String nuocdi = "";
    final static  int maxN=15;
    private ImageView[][] cell=new ImageView[maxN][maxN];
    private Context context ;
    private Drawable[] drawCell=new Drawable[4];
    private ImageView btnPlay;
    private TextView tvTurn;
    private  int winnerPlay;
    Dialog quitDialog;
    private boolean firstMove;
    private GifImageView settingsGifView;
    LinearLayout boardGame;
    private int xMove,yMove;
    private int[][] valueCell=new int[maxN][maxN];
    private int turnPlay,myTurn=0;
    private Integer chooseSkin=0;
    public static String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    public static Fragment_Caro_with_friend_Bluetooth newInstance(String Arg1)
    {
        Fragment_Caro_with_friend_Bluetooth fragment= new Fragment_Caro_with_friend_Bluetooth();
        Bundle bundle = new Bundle();
        bundle.putString("Arg1",Arg1);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!(getActivity() instanceof  MainCallBacks))
        {
            throw new IllegalStateException("Error");
        }
        // check what is main of fragment
        main=(BluetoothActivity) getActivity();
        context=getActivity();


    }
    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        LinearLayout layout_right=(LinearLayout) inflater.inflate(R.layout.caro_with_friend,null);
        boardGame=(LinearLayout) layout_right.findViewById(R.id.boardGame_wifi);
        //quitDialog = new Dialog(context);
        //edt1=(EditText) layout_right.findViewById(R.id.edt1);
        //tv1=(TextView) layout_right.findViewById(R.id.tv1);
        //bt_send=(Button) layout_right.findViewById(R.id.send);
        backBtn = (ImageView)  layout_right.findViewById(R.id.offline_game_back_btn_p2p);
        btnPlay=(ImageView) layout_right.findViewById(R.id.btn_play_wifi);
        tvTurn=(TextView) layout_right.findViewById(R.id.turn_player_wifi);
        settingsGifView  = (GifImageView)layout_right.findViewById(R.id.ai_game_seting_gifview_wifi);
        user_name_txt_wifi_player2=(TextView) layout_right.findViewById(R.id. user_name_txt_wifi_player2);
        user_name_txt_wifi_player1=(TextView) layout_right.findViewById(R.id. user_name_txt_wifi_player1);
        player_two_won_txt_wifi_player1=(TextView) layout_right.findViewById(R.id.player_two_won_txt_wifi_player1);
        player_two_won_txt_wifi_player2=(TextView) layout_right.findViewById(R.id.player_two_won_txt_wifi_player2);
        wifi_profile_friend=(ImageView) layout_right.findViewById(R.id.wifi_profile_friend);
        btn_wifi_chat=(ImageView) layout_right.findViewById(R.id.btn_wifi_chat);
        DB=new DBHelper(context);
        chooseSkin= DB.getChoosen(LoginActivity.user_ID);
        player_two_won_txt_wifi_player1.setText(String.valueOf(DB.getGold(LoginActivity.user_ID)));// vàng
        user_name_txt_wifi_player1.setText(LoginActivity.user_ID);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                quitDialogfun();
            }
        });
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
                        main.SelectEventFragmenttoMain(6);
                    }
                }, 500);
            }
        });


        loadResources();
        designBoardGame();
        /*bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg=edt1.getText().toString();
                main.ChatFragToMain(msg);
            }
        });*/
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.SelectEventFragmenttoMain(5);
                myTurn=1;
                name_player1 = user_name_txt_wifi_player1.getText().toString();
                name_player2 = user_name_txt_wifi_player2.getText().toString();
                init_game();
                play_game();
                Toast.makeText(context,"Host Innit_game",Toast.LENGTH_SHORT).show();

            }
        });

        btn_wifi_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog=new Dialog(context);
                dialog.setTitle("message");
                dialog.setContentView(R.layout.dialog_custom_chat_wifi);
                Button btn_chat=dialog.findViewById(R.id.btn_chat_wifi_dialog);
                EditText chat=dialog.findViewById(R.id.chat_wifi_dialog);
                btn_chat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        main.ChatFragToMain(chat.getText().toString());
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });



        return layout_right;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //initialize your view here for use view.findViewById("your view id")
        bt_send = (Button) view.findViewById(R.id.btn_chat_wifi_dialog);
    }

    @Override
    public void InitgameMaintoFrag(Boolean check)
    {
        if(check){
            this.myTurn = 2;
            name_player1 = user_name_txt_wifi_player2.getText().toString();
            name_player2 = user_name_txt_wifi_player1.getText().toString();
            init_game();
            play_game();
            Toast.makeText(context,"Client Innit_game",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void PlayFromMainToFragment(Integer x , Integer y) {
       /* if(talk_play) {
            if (text.equals("socket"))myTurn=1;
            else if(text.equals("client"))myTurn=2;
            else tv1.setText(text);
        }
        else
        {
            if(turnPlay==myTurn)Toast.makeText(context,"bug",Toast.LENGTH_SHORT).toString();
            else if(valueCell[x][y]==0) {
                xMove = x;
                yMove = y;
                makeMove();
            }
        }*/
        if(turnPlay==myTurn)Toast.makeText(context,"bug",Toast.LENGTH_SHORT).toString();
        else if(valueCell[x][y]==0) {
            xMove = x;
            yMove = y;
            makeMove();
        }

    }
    @Override
    public void SelectDeviceMaintoFragment(String[]list_device)
    {}
    private void init_game(){
        firstMove=true;
        winnerPlay=0;
        for(int i=0;i<maxN;i++){
            for (int j=0;j<maxN;j++){
                cell[i][j].setImageDrawable(drawCell[0]);
                valueCell[i][j]=0;
            }
        }
    }
    @Override
    public void ChatMainToFrag(String chat)
    {
        if(chat.equals("server"))
        {
            myTurn=1;
            String sendid=user_name_txt_wifi_player1.getText().toString();
//            String send_id="id."+LoginActivity.user_ID+"."+String.valueOf(DB.getGold(LoginActivity.user_ID));
            Toast.makeText(context,sendid,Toast.LENGTH_SHORT).show();
        }
        else if(chat.equals("client"))
        {
            myTurn=2;

            String sendid=user_name_txt_wifi_player1.getText().toString();
//            String send_id="id."+LoginActivity.user_ID+"."+String.valueOf(DB.getGold(LoginActivity.user_ID));
            Toast.makeText(context,sendid,Toast.LENGTH_SHORT).show();
        }
        else
        {
            String[] check = chat.split("./");
            if(check[0].equals("id_gold")) {
                user_name_txt_wifi_player2.setText(check[2]);
                player_two_won_txt_wifi_player2.setText(check[1]);
            }
            else {
                Balloon chat_tmp= chat_p2p(chat);
////            while (true)
                chat_tmp.showAlignBottom(wifi_profile_friend);
            }
        }
       //else tv1.setText(chat);
    }
    private void loadResources(){
        drawCell[0]=null;


        if (turnPlay == 1) {
            if (chooseSkin == 0) {
                drawCell[1] = context.getResources().getDrawable(R.drawable.x);
                drawCell[2] = context.getResources().getDrawable(R.drawable.o);
            } else if (chooseSkin == 1) {
                drawCell[1] = context.getResources().getDrawable(R.drawable.x1);
                drawCell[2] = context.getResources().getDrawable(R.drawable.o1);
            } else if (chooseSkin == 2) {
                drawCell[1] = context.getResources().getDrawable(R.drawable.x2);
                drawCell[2] = context.getResources().getDrawable(R.drawable.o2);
            }

        } else {
            if (chooseSkin == 0) {
                drawCell[1] = context.getResources().getDrawable(R.drawable.o);
                drawCell[2] = context.getResources().getDrawable(R.drawable.x);
            } else if (chooseSkin == 1) {
                drawCell[1] = context.getResources().getDrawable(R.drawable.o1);
                drawCell[2] = context.getResources().getDrawable(R.drawable.x1);
            } else if (chooseSkin == 2) {
                drawCell[1] = context.getResources().getDrawable(R.drawable.o2);
                drawCell[2] = context.getResources().getDrawable(R.drawable.x2);
            }
        }
        drawCell[3] = context.getResources().getDrawable(R.drawable.cell);
    }
    private void designBoardGame(){
        int sizeOfCell=Math.round((ScreenWidth())/maxN);
        LinearLayout.LayoutParams lpRow= new LinearLayout.LayoutParams(sizeOfCell*maxN,sizeOfCell);
        LinearLayout.LayoutParams lpcell=new LinearLayout.LayoutParams(sizeOfCell,sizeOfCell);
        for (int i=0;i<maxN;i++){
            LinearLayout lnRow=new LinearLayout(context);
            for (int j=0;j<maxN;j++){
                cell[i][j]=new ImageView(context);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    cell[i][j].setBackground(drawCell[3]);
                }
                final int x=i;
                final int y=j;
                cell[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override

                    public void onClick(View view) {
                        if(turnPlay!=myTurn)Toast.makeText(context,"bug",Toast.LENGTH_SHORT).toString();
                        else {
                            if(valueCell[x][y]==0) {
                                String text=".//"+String.valueOf(x)+" "+String.valueOf(y);
                                //main.onMsgFromFragToMain("play",1,text);
                                main.ChatFragToMain(text);
                                xMove = x;
                                yMove = y;
                                makeMove();
                            }
                        }
                    }
                });
                lnRow.addView(cell[i][j],lpcell);
            }
            boardGame.addView(lnRow,lpRow);
        }
    }
    private float ScreenWidth(){
        Resources resources=context.getResources();
        DisplayMetrics dm=resources.getDisplayMetrics();
        return dm.widthPixels;
    }
    private void play_game(){
        turnPlay=1;
        if(turnPlay==myTurn){
            Toast.makeText(context,"My Turn",Toast.LENGTH_SHORT).show();
            if(myTurn== 1) {
                player1Turn();
            }
            else {
                player2Turn();
            }
        }else{
            Toast.makeText(context,"--Check--",Toast.LENGTH_SHORT).show();
            if(myTurn== 1) {
                player2Turn();
            }
            else {
                player1Turn();
            }
        }
    }
    private void player1Turn(){
        tvTurn.setText("Turn of:" + name_player1);
        nuocdi+="1"+",";
    }
    private void player2Turn(){
        tvTurn.setText("Turn of:" + name_player2);
        nuocdi+="2"+",";
    }
    private void makeMove(){
        cell[xMove][yMove].setImageDrawable(drawCell[turnPlay]);
        valueCell[xMove][yMove]=turnPlay;
        nuocdi += String.valueOf(xMove);
        nuocdi += ",";
        nuocdi += String.valueOf(yMove);
        nuocdi += "//";
        if(noEmptyCell()){
            Toast.makeText(context,"DRAW!!!",Toast.LENGTH_SHORT).show();
            return;
        }else{
            if(checkWinner()){
                if(winnerPlay == 1) {
                    openDialog(name_player1);


                    if(name_player1.equals(user_name_txt_wifi_player2.getText().toString())) {
                        DB.insertMatch(LoginActivity.user_ID, user_name_txt_wifi_player2.getText().toString(), "Lose", currentDate, nuocdi);
                    }
                    else {
                        DB.updateGold(LoginActivity.user_ID, DB.getGold(LoginActivity.user_ID) + 4);
                        String gold = DB.getGold(LoginActivity.user_ID).toString();
                        String user_id = user_name_txt_wifi_player1.getText().toString();
                        player_two_won_txt_wifi_player1.setText(gold);
                        main.ChatFragToMain("id_gold./" + gold + "./" + user_id);
                        DB.insertMatch(LoginActivity.user_ID, user_name_txt_wifi_player2.getText().toString(), "Win", currentDate, nuocdi);
                    }

                }
                else {
                    openDialog(name_player2);
                    if(name_player2.equals(user_name_txt_wifi_player2.getText().toString())) {
                        DB.insertMatch(LoginActivity.user_ID, user_name_txt_wifi_player2.getText().toString(), "Lose", currentDate, nuocdi);
                    }
                    else {
                        DB.updateGold(LoginActivity.user_ID, DB.getGold(LoginActivity.user_ID) + 4);
                        String gold = DB.getGold(LoginActivity.user_ID).toString();
                        String user_id = user_name_txt_wifi_player1.getText().toString();
                        player_two_won_txt_wifi_player1.setText(gold);
                        main.ChatFragToMain("id_gold./" + gold + "./" + user_id);
                        DB.insertMatch(LoginActivity.user_ID, user_name_txt_wifi_player2.getText().toString(), "Win", currentDate, nuocdi);
                    }

                }
                return;
            }
        }

        if(turnPlay==1){
            turnPlay=3-turnPlay;

            player2Turn();
        }else{
            turnPlay=3-turnPlay;

            player1Turn();
        }
    }
    private boolean checkWinner(){
        if (winnerPlay!=0) return true;
        VectorEnd(xMove,0,0,1,xMove,yMove);
        if (winnerPlay!=0) return true;

        VectorEnd(0,yMove,1,0,xMove,yMove);
        if (winnerPlay!=0) return true;

        if(xMove+yMove>=maxN-1){
            VectorEnd(maxN-1,xMove+yMove-maxN+1,-1,1,xMove,yMove);
        }
        else if(xMove<=yMove){
            VectorEnd(xMove-yMove+maxN-1,maxN-1,-1,-1,xMove,yMove);
        }else{
            VectorEnd(maxN-1,maxN-1-(xMove-yMove),-1,-1,xMove,yMove);
        }
        if(winnerPlay!=0) return  true;
        return false;
    }
    private void VectorEnd(int xx,int yy,int vx,int vy,int rx,int ry){
        if (winnerPlay!=0){
            return;
        }
        final int range=4;
        int i,j;
        int xbelow=rx-range*vx;
        int ybelow=ry-range*vy;
        int xabove=rx+range*vx;
        int yabove=ry+range*vy;
        String st="";
        i=xx;
        j=yy;
        while (!inside(i,xbelow,xabove)|| !inside(j,ybelow,yabove)){
            i+=vx;
            j+=vy;
        }
        while (true){
            st=st+String.valueOf(valueCell[i][j]);
            if(st.length()==5){
                EvalEnd(st);
                if(winnerPlay!=0){
                    break;
                }
                st=st.substring(1,5);
            }
            i+=vx;
            j+=vy;
            if(!inBoard(i,j)|| !inside(i,xbelow,xabove) || !inside(j,ybelow,yabove)){
                break;
            }
        }
    }
    private boolean inBoard(int i,int j){
        if(i<0||i>=maxN|| j<0 || j>=maxN){
            return false;
        }
        return true;
    }
    private void EvalEnd(String st){

        switch (st){
            case "11111":
                winnerPlay=1;
                break;
            case "22222":
                winnerPlay=2;
                break;
            default:
                break;
        }
    }
    private boolean inside(int i,int xd, int xt){
        return (i-xd)*(i-xt)<=0;
    }
    private boolean noEmptyCell(){
        for (int i=0;i<maxN;i++){
            for (int j=0;j<maxN;j++){
                if(valueCell[i][j]==0) return false;
            }
        }
        return true;
    }
    public void openDialog(String winner){
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.winner_status);
        TextView edt = dialog.findViewById(R.id.Dialog_winner);
        edt.setText(winner+ "Win");
        dialog.show();
    }
    public Balloon chat_p2p(String text)
    {
        Balloon tmp;
        tmp=new  Balloon.Builder(context)
            .setWidthRatio(1.0f)
            .setHeight(BalloonSizeSpec.WRAP)
            .setText(text)
            .setTextColorResource(R.color.white_87)
            .setTextSize(15f)
            .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
            .setArrowSize(10)
            .setArrowPosition(0.5f)
            .setPadding(12)
            .setCornerRadius(8f)
            .setBackgroundColorResource(R.color.black)
            .setBalloonAnimation(BalloonAnimation.ELASTIC)
            .build();
        return  tmp;
    }
    private void quitDialogfun() {

        quitDialog= new Dialog(context);
        quitDialog.setContentView(R.layout.quit_dialog);
        quitDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        quitDialog.setCanceledOnTouchOutside(false);


        Button quitBtn = quitDialog.findViewById(R.id.logoutbtn2);
        Button continueBtn = quitDialog.findViewById(R.id.continue_btn);

        quitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitDialog.dismiss();
                main.ChatFragToMain("disconnect..");
                Intent intent = new Intent(context, ChoiceModeActivity.class);
                startActivity(intent);
            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitDialog.dismiss();
            }
        });
        quitDialog.show();
    }
}
