package com.example.carofinal;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class FragHome extends Fragment implements FragmentCallBacks {
   Caro_p2p main;
   Context context = null;
   TextView tv_list_device;
   String msg = "";
   Integer pos;
   ListView listView_device;
   Button Discovery,Create_Room;
   public static FragHome newInstance(String Arg) {
      FragHome fragment = new FragHome();
      Bundle args = new Bundle();
      args.putString("Arg1", Arg);
      fragment.setArguments(args);
      return fragment;
   }
   @Override
   public void onCreate( Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      try {
         context = getActivity();
         main = (Caro_p2p) getActivity();
      } catch (IllegalStateException e) {
         throw new IllegalStateException("Error");
      }
   }

   @Override
   public void onStop() {
      main.SelectEventFragmenttoMain(3);
      super.onStop();


   }

   @Override
   public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
      LinearLayout layout_left = (LinearLayout) inflater.inflate(R.layout.activity_caro_p2p, null);
      listView_device=(ListView) layout_left.findViewById(R.id.list_device);
      listView_device.setOnItemClickListener(new AdapterView.OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            main.SelectDeviceFragmenttoMain(position);
         }
      });

      Discovery= (Button) layout_left.findViewById(R.id.Btn_Discovery);
      Discovery.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            tv_list_device.setText("List Device");
            main.SelectEventFragmenttoMain(1);
         }
      });

      Create_Room=(Button) layout_left.findViewById(R.id.Btn_Tao_Phong);
      Create_Room.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            main.SelectEventFragmenttoMain(2);
         }
      });
      tv_list_device=(TextView) layout_left.findViewById(R.id.tv_list_device);
      return layout_left;
   }
   @Override
   public void PlayFromMainToFragment(Integer x ,Integer y) {
   }
   @Override
   public void InitgameMaintoFrag(Boolean check)
   {

   }
   @Override
   public void SelectDeviceMaintoFragment(String[] list_device)
   {
      if(list_device.length!=0)
      {
         ArrayAdapter<String> device=new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,list_device);
         listView_device.setAdapter((device));
      }
      else Toast.makeText(context,"device trống",Toast.LENGTH_SHORT).show();
   }
   @Override
   public void ChatMainToFrag(String chat)
   {

   }

}



