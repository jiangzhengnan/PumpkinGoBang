package com.whale.nangua.pumpkingobang.bean;

/**
 * Created by nangua on 2016/5/31.
 */
public class Device {
    private String deviceName;

    public Device(String deviceName, String deviceAddress) {
        this.deviceAddress = deviceAddress;
        this.deviceName = deviceName;
    }

    public String getDeviceAddress() {
        return deviceAddress;
    }

    public void setDeviceAddress(String deviceAddress) {
        this.deviceAddress = deviceAddress;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    private String deviceAddress;
}
