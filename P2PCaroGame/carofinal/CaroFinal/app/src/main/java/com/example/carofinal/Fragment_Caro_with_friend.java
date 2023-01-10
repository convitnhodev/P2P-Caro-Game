package com.example.carofinal;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
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

import com.skydoves.balloon.ArrowPositionRules;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;
import com.skydoves.balloon.BalloonSizeSpec;

import java.util.Random;
import java.util.zip.Inflater;

import pl.droidsonroids.gif.GifImageView;

public class Fragment_Caro_with_friend extends Fragment implements FragmentCallBacks {
    Caro_p2p main;
    EditText edt1;
    TextView tv1;
    Button bt_send;
    ImageView wifi_profile_friend,btn_wifi_chat;
    DBHelper DB;
    TextView user_name_txt_wifi_player2,user_name_txt_wifi_player1;
    TextView player_two_won_txt_wifi_player1,player_two_won_txt_wifi_player2;
    final static  int maxN=15;
    private ImageView[][] cell=new ImageView[maxN][maxN];
    private Context context ;
    private Drawable[] drawCell=new Drawable[4];
    private ImageView btnPlay;
    private TextView tvTurn;
    private  int winnerPlay;
    private boolean firstMove;
    private GifImageView settingsGifView;
    LinearLayout boardGame;
    private int xMove,yMove;
    private int[][] valueCell=new int[maxN][maxN];
    private int turnPlay,myTurn=0;
    public static Fragment_Caro_with_friend newInstance(String Arg1)
    {
        Fragment_Caro_with_friend fragment= new Fragment_Caro_with_friend();
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
        main = (Caro_p2p) getActivity();
        context=getActivity();

    }
    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        LinearLayout layout_right=(LinearLayout) inflater.inflate(R.layout.caro_with_friend,null);
        boardGame=(LinearLayout) layout_right.findViewById(R.id.boardGame_wifi);
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
        player_two_won_txt_wifi_player1.setText(String.valueOf(DB.getGold(LoginActivity.user_ID)));// vàng
        user_name_txt_wifi_player1.setText(LoginActivity.user_ID);


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

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.SelectEventFragmenttoMain(5);
                init_game();
                play_game();

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

        /*bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.ChatFragToMain(tv1.getText().toString());
            }
        });*/
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
            init_game();
            play_game();
            Toast.makeText(context,"Innit_game",Toast.LENGTH_SHORT).show();
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
            Toast.makeText(context,sendid,Toast.LENGTH_SHORT).show();
            //main.ChatFragToMain(sendid);

        }
        else if(chat.equals("client"))
        {
            myTurn=2;
            String sendid=user_name_txt_wifi_player1.getText().toString();
            Toast.makeText(context,sendid,Toast.LENGTH_SHORT).show();
            // main.ChatFragToMain(sendid);
        }
        else
        {
            Balloon chat_tmp= chat_p2p(chat);
            chat_tmp.showAlignBottom(wifi_profile_friend);
        }
       /* else
        {

            if(tmp[0].equals("id"))
            {
                player_two_won_txt_wifi_player2.setText(String.valueOf(tmp[2]));
                user_name_txt_wifi_player2.setText(String.valueOf(tmp[1]));
            }
            else
            {
                Balloon chat_tmp= chat_p2p(chat);
                chat_tmp.showAlignBottom(wifi_profile_friend);
            }
        }*/
        //else tv1.setText(chat);
    }
    private void loadResources(){
        drawCell[0]=null;
        drawCell[1]=context.getResources().getDrawable(R.drawable.x);
        drawCell[2]=context.getResources().getDrawable(R.drawable.o);
        drawCell[3]=context.getResources().getDrawable(R.drawable.cell);
    }
    private void designBoardGame(){
        int sizeOfCell=Math.round((ScreenWidth())/maxN);
        LinearLayout.LayoutParams lpRow= new LinearLayout.LayoutParams(sizeOfCell*maxN,sizeOfCell);
        LinearLayout.LayoutParams lpcell=new LinearLayout.LayoutParams(sizeOfCell,sizeOfCell);
        for (int i=0;i<maxN;i++){
            LinearLayout lnRow=new LinearLayout(context);
            for (int j=0;j<maxN;j++){
                cell[i][j]=new ImageView(context);
                cell[i][j].setBackground(drawCell[3]);
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
        Random r=new Random();
        turnPlay=r.nextInt(2)+1;
        if(turnPlay==myTurn){
            Toast.makeText(context,"My Turn",Toast.LENGTH_SHORT).show();
            player1Turn();
        }else{
            Toast.makeText(context,"--Check--",Toast.LENGTH_SHORT).show();
            player2Turn();
        }
    }
    private void player1Turn(){
        tvTurn.setText("Turn of: Player 1");
    }
    private void player2Turn(){
        tvTurn.setText("Turn of: Player 2");
    }
    private void makeMove(){
        cell[xMove][yMove].setImageDrawable(drawCell[turnPlay]);
        valueCell[xMove][yMove]=turnPlay;
        if(noEmptyCell()){
            Toast.makeText(context,"DRAW!!!",Toast.LENGTH_SHORT).show();
            return;
        }else{
            if(checkWinner()){
                if (winnerPlay==myTurn) {
                    Toast.makeText(context, "Winner"  , Toast.LENGTH_SHORT).show();
                    openDialog(true);

                }else {
                    Toast.makeText(context, "Loser" , Toast.LENGTH_SHORT).show();
                    openDialog(false);

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
    public void openDialog(boolean isWin){
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.winner_status);
        TextView edt = dialog.findViewById(R.id.Dialog_winner);
        if(isWin) {
            edt.setText( "You Win");
        }
        else {
            edt.setText( "You Lose");
        }
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
}
