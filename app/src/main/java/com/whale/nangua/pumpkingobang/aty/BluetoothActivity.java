package com.whale.nangua.pumpkingobang.aty;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.whale.nangua.pumpkingobang.R;

import java.util.Iterator;
import java.util.Set;

/**
 * 首先要打开本机蓝牙，然后设置本机蓝牙可见性打开，然后再搜索其他APP
 * Created by nangua on 2016/5/31.
 */
public class BluetoothActivity extends Activity {
    Button saomiao_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_layout);
        initView();

    }

    private void initView() {
        saomiao_btn = (Button) findViewById(R.id.saomiao_btn);
        saomiao_btn.setOnClickListener(new SaomiaoButtonListener());
    }

    boolean isBluetoothOpen = false;

    //扫描按钮监听类
    private class SaomiaoButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //设置手机蓝牙可见性
            //创建一个Intent对象,并且将其action的值设置为BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE也就是蓝牙设备设置为可见状态
            Intent kejianxingIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            //将一个键值对存放到Intent对象当中,主要用于指定可见状态的持续时间,大于300秒,就认为是300秒
            kejianxingIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 500);
            //执行请求
            startActivity(kejianxingIntent);

            //得到BluetoothAdapter对象
            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
            //判断BluetoothAdapter对象是否为空，如果为空，则表明本机没有蓝牙设备
            if (adapter != null) {
                Toast.makeText(BluetoothActivity.this, "本机拥有蓝牙设备", Toast.LENGTH_SHORT).show();
                //调用isEnabled()方法判断当前蓝牙设备是否可用
                if (!adapter.isEnabled()) {
                    //如果蓝牙设备不可用的话,创建一个intent对象,该对象用于启动一个Activity,提示用户启动蓝牙适配器
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivity(intent);
                    if (adapter.isEnabled()) {
                        isBluetoothOpen = true;
                    }
                } else {
                    isBluetoothOpen = true;
                }

                //得到所有已经配对的蓝牙适配器对象
                Set<BluetoothDevice> devices = adapter.getBondedDevices();
                if (devices.size() > 0) {
                    //使用迭代器迭代出每个设备信息
                    for (Iterator iterator = devices.iterator(); iterator.hasNext(); ) {
                        //得到BluetoothDevice对象,也就是说得到配对的蓝牙适配器
                        BluetoothDevice device = (BluetoothDevice) iterator.next();
                        //得到远程蓝牙设备的地址
                        Log.d("mytag", device.getAddress());
                        Log.d("mytag", device.getName());
                    }
                }
            } else {
                Toast.makeText(BluetoothActivity.this, "本机没有蓝牙设备", Toast.LENGTH_SHORT).show();
            }


        }
    }
}
