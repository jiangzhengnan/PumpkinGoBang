package com.whale.nangua.pumpkingobang.aty;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ListView;

import com.whale.nangua.pumpkingobang.Config;
import com.whale.nangua.pumpkingobang.R;
import com.whale.nangua.pumpkingobang.utils.AlertUtils;
import com.whale.nangua.pumpkingobang.utils.WiFiDirectBroadcastReceiver;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nangua on 2016/6/5.
 */
public class WifiFindOtherAty extends Activity {
    //wifi设备list
    public List<WifiP2pDevice> devices = new ArrayList<>();
    //wifi p2p管理器
    private WifiP2pManager mManager;
    //wifi p2p通道
    private WifiP2pManager.Channel mChannel;
    //意图过滤器
    IntentFilter mIntentFilter;
    //广播接收器
    WiFiDirectBroadcastReceiver mReceiver;

    ImageButton wifichoice_saomiaobtn;
    ListView wifi_saomiao_lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_layout);
        initView();
    }

    private void initView() {
        wifi_saomiao_lv = (ListView) findViewById(R.id.wifi_saomiao_lv);
        wifichoice_saomiaobtn = (ImageButton) findViewById(R.id.wifichoice_saomiaobtn);

        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);

        mManager.removeGroup(mChannel, null);
        //搜索周边设备
        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                AlertUtils.toastMess(WifiFindOtherAty.this, "加载成功");
            }

            @Override
            public void onFailure(int reason) {
                AlertUtils.toastMess(WifiFindOtherAty.this, "加载失败" + reason);
            }
        });
    }

    boolean stop = false;
    boolean accepted = false;

    /**
     * 连接到owner
     */
    public void connectToOwner() {
            Log.d("connect", "执行了连接到Owner");
            new Thread() {
                boolean success = false;

            @Override
            public void run() {
                while (!success && !stop && !accepted) {
                    try {
                        sleep(3000);
                        Log.e("ownerAddress", Config.CONNECTED_OWNER_IP.toString());
                        Socket clientSocket = new Socket();
                        clientSocket.bind(null);
                        clientSocket.connect(new InetSocketAddress(Config.CONNECTED_OWNER_IP, 8889));
                        success = true;
                        serverConnected(clientSocket);
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                        success = false;
                    }
                }
            }
        }.start();
    }


    /**
     * 连接上了服务器
     *
     * @param socket
     */
    private void serverConnected(Socket socket) {


    }


    /* unregister the broadcast receiver */
    @Override
    protected void onPause() {
        super.onPause();
        stop = true;

    }

    @Override
    protected void onDestroy() {
        //解绑服务
//        unbindService(connection);
        unregisterReceiver(mReceiver);
        super.onDestroy();

    }
}
