package com.whale.nangua.pumpkingobang.aty;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.whale.nangua.pumpkingobang.Config;
import com.whale.nangua.pumpkingobang.R;
import com.whale.nangua.pumpkingobang.adapter.DeviceshowAdapter;
import com.whale.nangua.pumpkingobang.bean.Device;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by nangua on 2016/5/31.
 */
public class FindOthersAty extends Activity {
    Button btn_saomiao;
    ListView saomiao_lv;
    ArrayList<Device> devices;
    DeviceshowAdapter deviceshowAdapter;
    private BluetoothAdapter bluetoothAdapter = null;
    private BluetoothReceiver bluetoothReceiver = null;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_layout);

        //设置手机蓝牙可见性
        //创建一个Intent对象,并且将其action的值设置为BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE也就是蓝牙设备设置为可见状态
        Intent kejianxingIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        //将一个键值对存放到Intent对象当中,主要用于指定可见状态的持续时间,大于300秒,就认为是300秒
        kejianxingIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 500);
        //执行请求
        startActivity(kejianxingIntent);

        //得到扫描周围蓝牙设备按钮
        btn_saomiao = (Button) findViewById(R.id.saomiao_btn);
        saomiao_lv = (ListView) findViewById(R.id.saomiao_lv);
        devices = new ArrayList<>();
        deviceshowAdapter = new DeviceshowAdapter(this,devices);
        saomiao_lv.setAdapter(deviceshowAdapter);

        //绑定扫描周围蓝牙设备按钮监听器

        btn_saomiao.setOnClickListener(new SaoMiaoButtonListener());

        //得到本机蓝牙设备
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        String benjiname = bluetoothAdapter.getName();//本机名称
        String benjidizhi = bluetoothAdapter.getAddress();//本机地址
        bluetoothAdapter.getState();//获得本地蓝牙设备状态，如果是打开状态可以判断进入下一个界面
        /**
         *  getState()获取本地蓝牙适配器当前状态（感觉可能调试的时候更需要）
             isDiscovering()判断当前是否正在查找设备，是返回true
             isEnabled()判断蓝牙是否打开，已打开返回true，否则，返回false
         */

        //创建一个IntentFilter对象,将其action指定为BluetoothDevice.ACTION_FOUND
        //IntentFilter它是一个过滤器,只有符合过滤器的Intent才会被我们的BluetoothReceiver所接收
        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        //创建一个BluetoothReceiver对象
        bluetoothReceiver = new BluetoothReceiver();
        //设置广播的优先级
        intentFilter.setPriority(Integer.MAX_VALUE);
        //注册广播接收器 注册完后每次发送广播后，BluetoothReceiver就可以接收到这个广播了
        registerReceiver(bluetoothReceiver, intentFilter);
    }

    //扫描周围的蓝牙设备按钮监听器

    private class SaoMiaoButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Toast.makeText(FindOthersAty.this,"开始扫描",Toast.LENGTH_SHORT).show();
            //扫描周围的可见的蓝牙设备一次要消耗12秒，废电池电量
            //扫描到了后结果我们怎么接收呢,扫描周围的蓝牙设备每扫描到一个蓝牙设备就会发送一个广播,我们就需要BroadcastReceiver来接收这个广播,这个函数是异步的调用,并不是扫描12之后才返回结果的,只要一调用这个函数马上返回,不会等12秒
            bluetoothAdapter.startDiscovery();
        }
    }

    //接收广播

    /**
     * 接受广播，并显示尚未配对的可用的周围所有蓝牙设备
     */
    private class BluetoothReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //只要BluetoothReceiver接收到来自于系统的广播,这个广播是什么呢,是我找到了一个远程蓝牙设备
                //Intent代表刚刚发现远程蓝牙设备适配器的对象,可以从收到的Intent对象取出一些信息
                BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // 如果该设备已经被配对，则跳过
                if (bluetoothDevice.getBondState() != BluetoothDevice.BOND_BONDED) {

                }

                devices.add(new Device(bluetoothDevice.getName(),bluetoothDevice.getAddress()));
                deviceshowAdapter.notifyDataSetChanged();

                //根据名称，UUID创建并返回BluetoothServerSocket，这是创建BluetoothSocket服务器端的第一步
                try {
                    BluetoothServerSocket serverSocket = bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(bluetoothAdapter.getName(), Config.UUID);
                    Log.d("whalea","2333");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //发送完成之后的操作
                Toast.makeText(FindOthersAty.this,"扫描完成",Toast.LENGTH_SHORT).show();
            }
        }
    }

}
