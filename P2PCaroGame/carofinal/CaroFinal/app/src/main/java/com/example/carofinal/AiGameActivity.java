package com.example.carofinal;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import pl.droidsonroids.gif.GifImageView;

public class AiGameActivity extends AppCompatActivity {
    final static int maxN = 12;
    private ImageView[][] cell = new ImageView[maxN][maxN];
    private Context context;
    private Drawable[] drawCell = new Drawable[4];
    private Button btnPlay;
    private TextView tvTurn;
    DBHelper DB;
    private int winnerPlay;
    private boolean firstMove;
    private int xMove, yMove;
    private int[][] valueCell = new int[maxN][maxN];
    private int turnPlay;
    Dialog quitDialog, statusgame;
    TextView player_two_won_txt_AI, user_name_txt_Ai;
    private Integer PICK_SIDE = 1;
    private ImageView backBtn, restart;
    private GifImageView settingsGifView;
    private int chooseSkin=0;
    public static SimpleDateFormat Date_Format = new SimpleDateFormat("dd/mm/yyyy");
    public static String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    String lichsu = "";
    public static String nuocdi = "";
    LinearLayout layout_lichsudau;
    private Integer count=0;
    String ketqua="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title

        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FIRST_APPLICATION_WINDOW,
                WindowManager.LayoutParams.FIRST_APPLICATION_WINDOW); //enable full screen
        //setContentView(R.layout.activity_settings);
        setContentView(R.layout.activity_with_ai);
        context = this;
        quitDialog = new Dialog(this);
        statusgame = new Dialog(this);
        backBtn = (ImageView) findViewById(R.id.offline_game_back_btn);
        settingsGifView = (GifImageView) findViewById(R.id.ai_game_seting_gifview);
        restart = (ImageView) findViewById(R.id.newgame);
        PICK_SIDE = getIntent().getIntExtra("ps", 1);
        player_two_won_txt_AI=(TextView) findViewById(R.id.player_two_won_txt_AI);


        layout_lichsudau =(LinearLayout) findViewById(R.id.huhuhu);

        Intent hehe = getIntent();

        if (hehe.getStringExtra("lichsu") != null) {
            lichsu = hehe.getStringExtra("lichsu");
            ketqua=hehe.getStringExtra("ketqua");
            // Toast.makeText(context, ahihi[0], Toast.LENGTH_SHORT).show();
        } else {
            lichsu = "";
        }



        settingsGifView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler handler = new Handler(Looper.getMainLooper());
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
                        Intent intent = new Intent(AiGameActivity.this, SettingActivity.class);
                        startActivity(intent);

                    }
                }, 500);

            }
        });
        setListen();
        player_two_won_txt_AI.setText(String.valueOf(DB.getGold(LoginActivity.user_ID)));// vàng
        user_name_txt_Ai.setText(LoginActivity.user_ID);// set ten
        //currentDate=Date_Format.format(new Date());
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitDialogfun();
            }
        });
        loadResources();
        designBoardGame();


        if (lichsu != "") {
            LichSuDau(lichsu);
        }
        else
        {
            init_game();
            play_game();
        }


        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                init_game();
                play_game();
            }
        });

    }

    private void quitDialogfun() {


        quitDialog.setContentView(R.layout.quit_dialog);
        quitDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        quitDialog.setCanceledOnTouchOutside(false);


        Button quitBtn = quitDialog.findViewById(R.id.logoutbtn2);
        Button continueBtn = quitDialog.findViewById(R.id.continue_btn);

        quitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitDialog.dismiss();
                Intent intent = new Intent(AiGameActivity.this, ChoiceModeActivity.class);
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

    private void statusgame() {


        statusgame.setContentView(R.layout.statusgame_dialog);
        statusgame.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        statusgame.setCanceledOnTouchOutside(false);

        TextView status = statusgame.findViewById(R.id.statusgame);
        if (winnerPlay == 1) {
            status.setText("Player win !!!");
        } else if (winnerPlay == 2) {
            status.setText("Bot win !!!");
        }
        Button restart = statusgame.findViewById(R.id.continuegame);
        Button cancel = statusgame.findViewById(R.id.cancel);

        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusgame.dismiss();
                init_game();
                play_game();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusgame.dismiss();
            }
        });
        statusgame.show();
    }

    private void setListen() {

        tvTurn = (TextView) findViewById(R.id.tvTurn1);
        DB = new DBHelper(this);
        chooseSkin = DB.getChoosen(LoginActivity.user_ID);
        user_name_txt_Ai = (TextView) findViewById(R.id.user_name_txt_Ai);
        player_two_won_txt_AI = (TextView) findViewById(R.id.player_two_won_txt_AI);

    }

    private void init_game() {
//        playerOne = getIntent().getStringExtra("p1");

        //  Toast.makeText(this, String.valueOf(PICK_SIDE), Toast.LENGTH_SHORT).show();
//        playerOneName.setText(playerOne);
        firstMove = true;
        winnerPlay = 0;
        for (int i = 0; i < maxN; i++) {
            for (int j = 0; j < maxN; j++) {
                cell[i][j].setImageDrawable(drawCell[0]);
                valueCell[i][j] = 0;
            }
        }
    }

    private void play_game() {

        turnPlay = PICK_SIDE;
        //Player is 1, AI is 2
        if (turnPlay == 1) {
            Toast.makeText(context, "Player first", Toast.LENGTH_SHORT).show();
            player1Turn();
        } else {
            Toast.makeText(context, "Bot first", Toast.LENGTH_SHORT).show();
            player2Turn();
        }
    }

    private void player1Turn() {
        tvTurn.setText("Turn of: Player");
        isClicked = false;
        nuocdi += "1,";
    }

    //
    // Dialog thông báo chiến thắng
    public void openDialog() {
        statusgame();
    }

    //
    private boolean checkWinner() {
        if (winnerPlay != 0) return true;
        VectorEnd(xMove, 0, 0, 1, xMove, yMove);
        if (winnerPlay != 0) return true;

        VectorEnd(0, yMove, 1, 0, xMove, yMove);
        if (winnerPlay != 0) return true;

        if (xMove + yMove >= maxN - 1) {
            VectorEnd(maxN - 1, xMove + yMove - maxN + 1, -1, 1, xMove, yMove);
        } else if (xMove <= yMove) {
            VectorEnd(xMove - yMove + maxN - 1, maxN - 1, -1, -1, xMove, yMove);
        } else {
            VectorEnd(maxN - 1, maxN - 1 - (xMove - yMove), -1, -1, xMove, yMove);
        }
        if (winnerPlay != 0) return true;
        return false;
    }

    private void VectorEnd(int xx, int yy, int vx, int vy, int rx, int ry) {
        if (winnerPlay != 0) {
            return;
        }
        final int range = 4;
        int i, j;
        int xbelow = rx - range * vx;
        int ybelow = ry - range * vy;
        int xabove = rx + range * vx;
        int yabove = ry + range * vy;
        String st = "";
        i = xx;
        j = yy;
        while (!inside(i, xbelow, xabove) || !inside(j, ybelow, yabove)) {
            i += vx;
            j += vy;
        }
        while (true) {
            st = st + String.valueOf(valueCell[i][j]);
            if (st.length() == 5) {
                EvalEnd(st);
                if (winnerPlay != 0) {
                    break;
                }
                st = st.substring(1, 5);
            }
            i += vx;
            j += vy;
            if (!inBoard(i, j) || !inside(i, xbelow, xabove) || !inside(j, ybelow, yabove)) {
                break;
            }
        }
    }

    private boolean inBoard(int i, int j) {
        if (i < 0 || i >= maxN || j < 0 || j >= maxN) {
            return false;
        }
        return true;
    }

    private void EvalEnd(String st) {
        //Toast.makeText(context, st, Toast.LENGTH_SHORT).show();

        switch (st) {
            case "11111":
                winnerPlay = 1;
                break;
            case "22222":
                winnerPlay = 2;
                break;
            default:
                break;
        }
    }

    private boolean inside(int i, int xd, int xt) {
        return (i - xd) * (i - xt) <= 0;
    }

    private boolean noEmptyCell() {
        for (int i = 0; i < maxN; i++) {
            for (int j = 0; j < maxN; j++) {
                if (valueCell[i][j] == 0) return false;
            }
        }
        return true;
    }

    private void setValueCell() {
        for (int i = 0; i < maxN; i++) {
            for (int j = 0; j < maxN; j++) {
                valueCell[i][j] = 0;
            }
        }
    }

    //Bot turn
    private void player2Turn() {
        tvTurn.setText("Turn of: Bot");
        nuocdi += "2,";
        if (firstMove) {
            firstMove = false;
            xMove = 7;
            yMove = 7;

            makeMove();
        } else {
            //Find best move
            findBotMove();
            makeMove();
        }
    }

    private final int[] iRow = {-1, -1, -1, 0, 1, 1, 1, 0};
    private final int[] iCol = {-1, 0, 1, 1, 1, 0, -1, -1};

    private void findBotMove() {
        List<Integer> listX = new ArrayList<>();
        List<Integer> listY = new ArrayList<Integer>();
        //find empty cell can move, and we we only move two cell in range 2
        final int range = 2;
        for (int i = 0; i < maxN; i++) {
            for (int j = 0; j < maxN; j++)
                if (valueCell[i][j] != 0) {//not empty
                    for (int t = 1; t <= range; t++) {
                        for (int k = 0; k < 8; k++) {
                            int x = i + iRow[k] * t;
                            int y = j + iCol[k] * t;
                            if (inBoard(x, y) && valueCell[x][y] == 0) {
                                listX.add(x);
                                listY.add(y);
                            }
                        }
                    }
                }
        }
        int lx = listX.get(0);
        int ly = listY.get(0);
        //bot always find min board_position_value
        int res = Integer.MAX_VALUE - 10;
        for (int i = 0; i < listX.size(); i++) {
            int x = listX.get(i);
            int y = listY.get(i);
            valueCell[x][y] = 2;
            int rr = getValue_Position();
            if (rr < res) {
                res = rr;
                lx = x;
                ly = y;
            }
            valueCell[x][y] = 0;
        }
        xMove = lx;
        yMove = ly;
    }

    private int getValue_Position() {
        //this function will find the board_position_value
        int rr = 0;
        int pl = turnPlay;
        //row
        for (int i = 0; i < maxN; i++) {
            rr += CheckValue(maxN - 1, i, -1, 0, pl);
        }
        //column
        for (int i = 0; i < maxN; i++) {
            rr += CheckValue(i, maxN - 1, 0, -1, pl);
        }
        //cross right to left
        for (int i = maxN - 1; i >= 0; i--) {
            rr += CheckValue(i, maxN - 1, -1, -1, pl);
        }
        for (int i = maxN - 2; i >= 0; i--) {
            rr += CheckValue(maxN - 1, i, -1, -1, pl);
        }
        //cross left to right
        for (int i = maxN - 1; i >= 0; i--) {
            rr += CheckValue(i, 0, -1, 1, pl);
        }
        for (int i = maxN - 1; i >= 1; i--) {
            rr += CheckValue(maxN - 1, i, -1, 1, pl);
        }
        return rr;
    }

    private int CheckValue(int xd, int yd, int vx, int vy, int pl) {
        //comback with check value
        int i, j;
        int rr = 0;
        i = xd;
        j = yd;
        String st = String.valueOf(valueCell[i][j]);
        while (true) {
            i += vx;
            j += vy;
            if (inBoard(i, j)) {
                st = st + String.valueOf(valueCell[i][j]);
                if (st.length() == 6) {
                    rr += Eval(st, pl);
                    st = st.substring(1, 6);
                }
            } else break;
        }
        return rr;

    }

    private void makeMove() {
        cell[xMove][yMove].setImageDrawable(drawCell[turnPlay]);
        nuocdi += String.valueOf(xMove);
        nuocdi += ",";
        nuocdi += String.valueOf(yMove);
        nuocdi += "//";

        valueCell[xMove][yMove] = turnPlay;
        if (noEmptyCell()) {
            Toast.makeText(context, "DRAW!!!", Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (checkWinner()) {
                if (winnerPlay == 1) {
                    Toast.makeText(context, "Winner is Player", Toast.LENGTH_SHORT).show();
                    DB.updateGold(LoginActivity.user_ID, DB.getGold(LoginActivity.user_ID) + 4);
                    player_two_won_txt_AI.setText(String.valueOf(DB.getGold(LoginActivity.user_ID)));

                    DB.insertMatch(LoginActivity.user_ID, "BOT", "Win", currentDate, nuocdi);
                    nuocdi="";

                } else if (winnerPlay == 2) {
                    Toast.makeText(context, "Winner is Bot", Toast.LENGTH_SHORT).show();
                    DB.insertMatch(LoginActivity.user_ID, "BOT", "Lose", currentDate, nuocdi);
                    nuocdi="";
                }
                openDialog();

                return;
            }
        }

        if (turnPlay == 1) {
            turnPlay = 3 - turnPlay;
            //  Toast.makeText(context, "HIHI", Toast.LENGTH_SHORT).show();

            player2Turn();
        } else {
            turnPlay = 3 - turnPlay;
            //  Toast.makeText(context, "Haha", Toast.LENGTH_SHORT).show();

            player1Turn();
        }
    }

    private void loadResources() {
        drawCell[0] = null;
        if (PICK_SIDE == 1) {
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

    private boolean isClicked;

    private void designBoardGame() {
        int sizeOfCell = Math.round(ScreenWidth() / maxN);
        LinearLayout.LayoutParams lpRow = new LinearLayout.LayoutParams(sizeOfCell * maxN, sizeOfCell);
        LinearLayout.LayoutParams lpcell = new LinearLayout.LayoutParams(sizeOfCell, sizeOfCell);
        LinearLayout boardGame = (LinearLayout) findViewById(R.id.boardGame);
        for (int i = 0; i < maxN; i++) {
            LinearLayout lnRow = new LinearLayout(context);
            for (int j = 0; j < maxN; j++) {
                cell[i][j] = new ImageView(context);
                cell[i][j].setBackground(drawCell[3]);
                final int x = i;
                final int y = j;
                cell[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override

                    public void onClick(View view) {
                        //This is player turn
                        if (turnPlay == 1 || !isClicked) {
                            if (valueCell[x][y] == 0) {
                                isClicked = true;
                                xMove = x;
                                yMove = y;

                                makeMove();
                            }
                        }
                    }
                });
                lnRow.addView(cell[i][j], lpcell);
            }
            boardGame.addView(lnRow, lpRow);
        }
    }

    private float ScreenWidth() {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.widthPixels - 45;
    }

    // Evaluate Func
    private int Eval(String st, int pl) {
        int b1 = 1, b2 = 1;
        if (pl == 1) {
            b1 = 2;
            b2 = 1;
        } else {
            b1 = 1;
            b2 = 2;
        }
        switch (st) {
            case "111110":
                return b1 * 100000000;
            case "011111":
                return b1 * 100000000;
            case "211111":
                return b1 * 100000000;
            case "111112":
                return b1 * 100000000;
            case "011110":
                return b1 * 10000000;
            case "101110":
                return b1 * 1002;
            case "011101":
                return b1 * 1002;
            case "011112":
                return b1 * 1000;
            case "011100":
                return b1 * 102;
            case "001110":
                return b1 * 102;
            case "210111":
                return b1 * 100;
            case "211110":
                return b1 * 100;
            case "211011":
                return b1 * 100;
            case "211101":
                return b1 * 100;
            case "010100":
                return b1 * 10;
            case "011000":
                return b1 * 10;
            case "001100":
                return b1 * 10;
            case "000110":
                return b1 * 10;
            case "211000":
                return b1 * 1;
            case "201100":
                return b1 * 1;
            case "200110":
                return b1 * 1;
            case "200011":
                return b1 * 1;
            case "222220":
                return b2 * -100000000;
            case "022222":
                return b2 * -100000000;
            case "122222":
                return b2 * -100000000;
            case "222221":
                return b2 * -100000000;
            case "022220":
                return b2 * -10000000;
            case "202220":
                return b2 * -1002;
            case "022202":
                return b2 * -1002;
            case "022221":
                return b2 * -1000;
            case "022200":
                return b2 * -102;
            case "002220":
                return b2 * -102;
            case "120222":
                return b2 * -100;
            case "122220":
                return b2 * -100;
            case "122022":
                return b2 * -100;
            case "122202":
                return b2 * -100;
            case "020200":
                return b2 * -10;
            case "022000":
                return b2 * -10;
            case "002200":
                return b2 * -10;
            case "000220":
                return b2 * -10;
            case "122000":
                return b2 * -1;
            case "102200":
                return b2 * -1;
            case "100220":
                return b2 * -1;
            case "100022":
                return b2 * -1;
            default:
                break;
        }
        return 0;
    }

    public void LichSuDau(String nuocdi) {
        if (nuocdi != "") {
            for (int i = 0; i < maxN; i++) {
                for (int j = 0; j < maxN; j++) {
                    cell[i][j].setImageDrawable(drawCell[0]);
                    valueCell[i][j] = 0;
                }
            }



            String[] split1 = nuocdi.split("//");
//            drawCell[1] = context.getResources().getDrawable(R.drawable.x);
//            drawCell[2] = context.getResources().getDrawable(R.drawable.o);
            ArrayList<String[]> temp = new ArrayList<String[]>();
            for (int i = 0; i < split1.length; i++) {
                temp.add(split1[i].split(","));
            }

//            for (int i = 0; i < temp.size(); i++) {
            String[] ahihi = temp.get(count);
            //Toast.makeText(context, ahihi[0] +" "+ahihi[1] +" "+ahihi[2], Toast.LENGTH_SHORT).show();
            Integer tempPlay = Integer.valueOf(ahihi[0]);
            Integer x = Integer.valueOf(ahihi[1]);
            Integer y = Integer.valueOf(ahihi[2]);
            cell[x][y].setImageDrawable(drawCell[tempPlay]);
            valueCell[x][y] = tempPlay;
            layout_lichsudau.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    String[] ahihi = temp.get(count);
                    Integer tempPlay = Integer.valueOf(ahihi[0]);
                    Integer x = Integer.valueOf(ahihi[1]);
                    Integer y = Integer.valueOf(ahihi[2]);
                    cell[x][y].setImageDrawable(drawCell[tempPlay]);
                    valueCell[x][y] = tempPlay;
                    count+=1;
                    if(count==temp.size())
                    {
                        Dialog dialog=new Dialog(context);

                        dialog.setContentView(R.layout.dialog_history_end);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.setCanceledOnTouchOutside(false);

                        TextView status_history = dialog.findViewById(R.id.status_history);
                        if (ketqua.equals("Win")) {
                            status_history.setText(" win !!!");
                        } else  {
                            status_history.setText("Lose !!!");
                        }

                        Button return_history = dialog.findViewById(R.id.return_history);
                        return_history.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(AiGameActivity.this,MatchHistory.class);
                                startActivity(intent);
                            }
                        });
                        dialog.show();
                    }
                }
            });


            //  }

        }
    };
}