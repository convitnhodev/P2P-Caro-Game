package com.example.carofinal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MatchHistory  extends Activity {
    DBHelper DB;
    ArrayList<ArrayList<String>>list=new ArrayList<>();
    ListView listview;
    private ImageView backBtn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_history);
        backBtn=(ImageView) findViewById(R.id.match_history_back_btn);
        DB= new DBHelper(this);
        listview=(ListView) findViewById(R.id.list_item);
        list=DB.getMatchHistory(LoginActivity.user_ID);
        customAdapter adapter=new customAdapter(this,R.layout.item_match_history,list.get(0),list.get(1),list.get(2),list.get(3),list.get(4));
        listview.setAdapter((ListAdapter) adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MatchHistory.this ,ChoiceModeActivity.class);
                startActivity(intent);
            }
        });


    }

}
class customAdapter extends ArrayAdapter<String>
{
    Context context;
    ArrayList<String>username1;
    ArrayList<String>username2;
    ArrayList<String>ketqua;
    ArrayList<String>thoigian;
    ArrayList<String>lichsu;
    ImageButton eye;
    public customAdapter(MatchHistory context, int layoutToBeInlated, ArrayList<String> username1, ArrayList<String> username2, ArrayList<String> ketqua, ArrayList<String>thoigian, ArrayList<String>lichsu)
    {
        super(context,R.layout.activity_match_history,username1);
        this.username1=username1;
        this.context=context;
        this.username2=username2;
        this.ketqua=ketqua;
        this.thoigian=thoigian;
        this.lichsu=lichsu;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=((Activity)context).getLayoutInflater();
        //LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View row=inflater.inflate(R.layout.item_match_history,null);
        //TextView user1=(TextView) row.findViewById(R.id.user_name_1);
        TextView user2=(TextView) row.findViewById(R.id.user_name_2);
        TextView ketqua_match=(TextView) row.findViewById(R.id.ketqua_match);
        TextView thoigian_match=(TextView) row.findViewById(R.id.thoigian_match);
        ImageButton eye=(ImageButton) row.findViewById(R.id.eye_icon) ;

        Integer len=username1.size();
//        .setText(username1.get(len-1-position));
        user2.setText(username2.get(len-1-position));

        ketqua_match.setText(ketqua.get(len-1-position));
        thoigian_match.setText(thoigian.get(len-1-position));

        eye.setImageResource(R.drawable.ic_eye_foreground);
        eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent hehe=new Intent(context,AiGameActivity.class);

                hehe.putExtra("lichsu",lichsu.get(len-1-position));
                hehe.putExtra("ketqua",ketqua.get(len-1-position));
                context.startActivity(hehe);
                // Toast.makeText(context,lichsu.get(len-1-position),Toast.LENGTH_LONG).show();
            }
        });

        return row;
    }
}