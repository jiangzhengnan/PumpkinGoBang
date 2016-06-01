package com.whale.nangua.pumpkingobang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.whale.nangua.pumpkingobang.R;
import com.whale.nangua.pumpkingobang.bean.Device;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by nangua on 2016/5/31.
 */
public class DeviceshowAdapter extends BaseAdapter {
    public ArrayList<Device> getDevices() {
        return devices;
    }

    public void setDevices(ArrayList<Device> devices) {
        this.devices = devices;
    }

    private ArrayList<Device> devices;
    private Context context;
    private TextView devicename;
    private TextView deviceaddress;
    public DeviceshowAdapter(Context context,ArrayList<Device> devices) {
        this.context = context;
        this.devices = devices;
    }

    @Override
    public int getCount() {
        return devices.size();
    }

    @Override
    public Object getItem(int position) {
        return devices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = LayoutInflater.from(context).inflate(R.layout.device_cell_layout,null);
        Device device = devices.get(position);
        devicename = (TextView) v.findViewById(R.id.device_name);
        deviceaddress = (TextView) v.findViewById(R.id.device_address);
        devicename.setText(device.getDeviceName());
        deviceaddress.setText(device.getDeviceAddress());
        return v;
    }
}
