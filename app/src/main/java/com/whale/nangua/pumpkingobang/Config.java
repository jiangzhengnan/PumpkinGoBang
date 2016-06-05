package com.whale.nangua.pumpkingobang;

import java.net.InetAddress;
import java.util.UUID;

/**
 * Created by nangua on 2016/5/31.
 */
public class Config {
    //本机唯一UUID
    public static final UUID UUID = java.util.UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public static InetAddress CONNECTED_OWNER_IP = null;

    public static P2pRole CURRENT_ROLE = P2pRole.NONE;
    public enum P2pRole{
        GROUP_OWNRR,GROUP_MEMBER,NONE
    }
}
