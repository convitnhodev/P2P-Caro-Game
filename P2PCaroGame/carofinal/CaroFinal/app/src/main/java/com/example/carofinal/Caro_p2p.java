package com.example.carofinal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Caro_p2p extends FragmentActivity implements MainCallBacks {
    boolean isSuccess=false;
    ListView DeviceList;
    private WifiP2pManager manager;
    boolean isWifiP2pEnabled = false;
    boolean retryChannel = false;
    private WifiP2pManager.Channel channel;
    private BroadcastReceiver receiver = null;
    private final IntentFilter intentFilter = new IntentFilter();
    List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
    String[] deviceNameArray={};
    WifiP2pDevice[] deviceArray;
    Socket socket;
    ServerClass serverClass;
    ClientClass clientClass;
    DBHelper DB;
    FragHome fragHome;
    Fragment_Caro_with_friend fragCaro;
    boolean isHost;
    String deviceName;
    FragmentTransaction ft;
    public void setIsWifiP2pEnabled(boolean isWifiP2pEnabled) {
        this.isWifiP2pEnabled = isWifiP2pEnabled;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_p2p);

        // add necessary intent values to be matched.
        ft = getSupportFragmentManager().beginTransaction();
        fragHome = FragHome.newInstance("");
        ft.replace(R.id.frame_home, fragHome);
        ft.commit();
        ft = getSupportFragmentManager().beginTransaction();
        fragCaro = Fragment_Caro_with_friend.newInstance("");
        DB=new DBHelper(this);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        if (!initP2P()) {
            finish();
        }

    }




    private boolean initP2P() {
        // Device capability definition check
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_WIFI_DIRECT)) {
            Toast.makeText(this, "Wi-Fi Direct is not supported by this device.", Toast.LENGTH_SHORT).show();
            return false;
        }
        // Hardware capability check
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager == null) {
            Toast.makeText(this, "Cannot get Wi-Fi system service.", Toast.LENGTH_SHORT).show();
            return false;
        }
        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        if (manager == null) {
            Toast.makeText(this, "Cannot get Wi-Fi P2P system service.", Toast.LENGTH_SHORT).show();
            return false;
        }

        channel = manager.initialize(this, getMainLooper(), null);
        if (channel == null) {
            Toast.makeText(this, "Cannot initialize Wi-Fi P2P.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {
            if (!wifiP2pDeviceList.equals(peers)) {
                peers.clear();
                peers.addAll(wifiP2pDeviceList.getDeviceList());
                deviceNameArray = new String[wifiP2pDeviceList.getDeviceList().size()];
                deviceArray = new WifiP2pDevice[wifiP2pDeviceList.getDeviceList().size()];
                int index = 0;
                for (WifiP2pDevice device : wifiP2pDeviceList.getDeviceList()) {
                    deviceNameArray[index] = device.deviceName;
                    deviceArray[index] = device;
                    index++;
                }
            }
            if(peers.size()==0)
            {
                Toast.makeText(Caro_p2p.this, "Empty", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    };

    WifiP2pManager.ConnectionInfoListener connectionInfoListener=new WifiP2pManager.ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {
            final InetAddress groupOwnerAddress = wifiP2pInfo.groupOwnerAddress;
            if(wifiP2pInfo.groupFormed && wifiP2pInfo.isGroupOwner)
            {
                Toast.makeText(Caro_p2p.this, "Connected ", Toast.LENGTH_SHORT).show();
                isHost =true;
                serverClass = new ServerClass();
                serverClass.start();
                //fragCaro.onMsgFromMainToFragment(true,"socket",0,0);
                replaceFragment(fragCaro);
                fragCaro.ChatMainToFrag("server");
                //String send_id="id."+LoginActivity.user_ID+"."+String.valueOf(DB.getGold(LoginActivity.user_ID));
                //send(send_id);


            }else if(wifiP2pInfo.groupFormed)
            {
                Toast.makeText(Caro_p2p.this, "connected ", Toast.LENGTH_SHORT).show();
                isHost =false;
                clientClass = new ClientClass(groupOwnerAddress);
                clientClass.start();
                //fragCaro.onMsgFromMainToFragment(true,"client",0,0);
                fragCaro.ChatMainToFrag("client");
                replaceFragment(fragCaro);
                //String send_id="id."+String.valueOf(LoginActivity.user_ID)+"."+String.valueOf(DB.getGold(LoginActivity.user_ID));
                //send(send_id);
            }
        }
    };





    /** register the BroadcastReceiver with the intent values to be matched */
    @Override
    protected void onResume() {
        super.onResume();
        receiver = new WifiDirectBroadcastReceiver(manager, channel, this);
        registerReceiver(receiver,intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);

    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public class  ServerClass extends Thread {
        ServerSocket serverSocket;
        private InputStream inputStream;

        private OutputStream outputStream;
        public  void write(byte[] bytes)
        {
            try {
                outputStream.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(8888);
                socket = serverSocket.accept();
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    byte[] buffer = new byte[1024];
                    int bytes;
                    while (socket!=null)
                    {
                        try {
                            bytes =inputStream.read(buffer);
                            if(bytes>0)
                            {
                                int finalBytes=bytes;
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        String tempMSG=new String(buffer,0,finalBytes);
                                        //Toast.makeText(HomeActivity.this,tempMSG,Toast.LENGTH_SHORT).show();
                                        check(tempMSG);
                                    }
                                });
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }






    public class ClientClass extends Thread
    {
        String hostAdd;
        private InputStream inputStream;
        private OutputStream outputStream;
        public  void write(byte[] bytes)
        {
            try {
                outputStream.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public ClientClass(InetAddress hostAddress)
        {
            hostAdd=hostAddress.getHostAddress();
            socket=new Socket();
        }

        @Override
        public void run() {
            try {
                socket.connect(new InetSocketAddress(hostAdd,8888),500);
                inputStream=socket.getInputStream();
                outputStream=socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Handler handler=new Handler(Looper.getMainLooper());
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    byte[] buffer = new byte[1024];
                    int bytes;
                    while (socket!=null)
                    {
                        try {
                            bytes =inputStream.read(buffer);
                            if(bytes>0)
                            {
                                int finalBytes=bytes;
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        String tempMSG=new String(buffer,0,finalBytes);
                                        check(tempMSG);
                                    }
                                });
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }
    private  void innitFragment() {
        FragHome fragHome = new FragHome() ;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_home,fragHome);
        fragmentTransaction.commit();
    }
    private  void replaceFragment(Fragment fragment){
        if(fragment!=null)
        {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_home,fragment);
            fragmentTransaction.commit();
        }
    }
    @Override
    public void SelectDeviceFragmenttoMain(int x)
    {
        final WifiP2pDevice device =deviceArray[x];
        WifiP2pConfig config=new WifiP2pConfig();
        config.deviceAddress=device.deviceAddress;
        manager.connect(channel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                // Toast.makeText(HomeActivity.this, "Connected to "+device.deviceName, Toast.LENGTH_SHORT).show();
                replaceFragment(fragCaro);
            }

            @Override
            public void onFailure(int i) {
                Toast.makeText(Caro_p2p.this, "Connect failed. Retry.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void ChatFragToMain(String chat)
    {
        send(chat);
    }
    @Override
    public void SelectEventFragmenttoMain(int ma)
    {
        if(ma==1)
        {
            manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(Caro_p2p.this, "Discovery Initiated", Toast.LENGTH_SHORT).show();
                    if(deviceNameArray.length!=0)fragHome.SelectDeviceMaintoFragment(deviceNameArray);
                    else Toast.makeText(Caro_p2p.this,"Device Empty",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(int reason) {
                    Toast.makeText(Caro_p2p.this, "Discovery Failed ", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else if(ma==2)
        {
            manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    replaceFragment(fragCaro);
                }

                @Override
                public void onFailure(int reason) {
                }
            });
        }
        else if(ma==6)//setting
        {
            Intent intent = new Intent(Caro_p2p.this,SettingActivity.class);
            startActivity(intent);
        }
        else if(ma==5)// mã play game
        {
            send("playgame");
        }
    }
    private  void send(String msg)
    {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                if(msg!=null&& isHost)
                {
                    serverClass.write(msg.getBytes());

                }else if(msg!=null && !isHost)
                {
                    clientClass.write(msg.getBytes());
                }
                else
                {
                    Toast.makeText(Caro_p2p.this, "in-connect 3", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
    private void check(String msg)
    {
        if(msg.equals(("playgame")))
        {
            fragCaro.InitgameMaintoFrag(true);
        }
        else
        {
            String[] tmp=msg.split("//");
            if(tmp.length==1)
            {
                fragCaro.ChatMainToFrag(msg);
            }
            else// chỗ này là đánh cờ
            {
                String[] tmp_x_y=tmp[1].split(" ");
                fragCaro.PlayFromMainToFragment(Integer.parseInt(tmp_x_y[0]),Integer.parseInt(tmp_x_y[1]));
            }
        }

    }
}