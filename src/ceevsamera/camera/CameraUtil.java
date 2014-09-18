/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ceevsamera.camera;

import ceevsamera.ConnectionLauncher;

/**
 *
 * @author Administrator
 */
public class CameraUtil {

    //摄像头参数
    private boolean on_off = false;    //摄像头开关
    private byte[] serverIP;
    private boolean online = false;  //是否在线
    private String devSeq = "00:00:00:00:00:00";
    private int imgType;      //拍照图片分辨率

    private int tag = 0;

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public void initDevSeq(int tag, int number) {
        StringBuilder sb = new StringBuilder();
        String tagHead = Integer.toHexString(tag);
        if (tagHead.length() < 2) {
            sb.append("0");
        }
        sb.append(tagHead);
        sb.append(":");

        String numberTagStr = Integer.toHexString(number);
        if (numberTagStr.length() % 2 == 1) {
            numberTagStr = numberTagStr.replaceFirst(String.valueOf(numberTagStr.charAt(0)), "0" + numberTagStr.charAt(0));
        }
        int digit = numberTagStr.length();
        for (int i = 0; i < 5 - digit / 2; i++) {
            sb.append("00:");
        }

        for (int j = 0; j < digit; j += 2) {
            if (j + 2 >= numberTagStr.length()) {
                sb.append(numberTagStr.substring(j));
            } else {
                sb.append(numberTagStr.substring(j, j + 2));
            }

            if (j != digit - 2) {
                sb.append(":");
            }
        }
        devSeq = sb.toString().toUpperCase();
    }

    public boolean isOn_off() {
        return on_off;
    }

    public void setOn_off(boolean on_off) {
        this.on_off = on_off;
    }

    public byte[] getServerIP() {
        return serverIP;
    }

    public void setServerIP(byte[] serverIP) {
        this.serverIP = serverIP;
    }

    public String getDevSeq() {
        return devSeq;
    }

    public void setDevSeq(String devSeq) {
        this.devSeq = devSeq;
    }

    public int getImgType() {
        return imgType;
    }

    public void setImgType(int imgType) {
        this.imgType = imgType;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

}
