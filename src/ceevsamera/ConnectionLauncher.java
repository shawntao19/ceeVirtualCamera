/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ceevsamera;

import ceevsamera.camera.Camera;
import ceevsamera.camera.CameraUtil;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class ConnectionLauncher {

    public static int tag = 4, startNo = 0, endNo = 10000;
    public static byte[] polleIPAddress = {(byte) 172, (byte) 10, (byte) 2, (byte) 2};

    /**
     * @param args the command line argumentss
     */
    public static void main(String[] args) {
        if (args != null) {
            if (args.length >= 2) {
                startNo = Integer.valueOf(args[0]);
                endNo = Integer.valueOf(args[1]);
            }
            if (args.length >= 3) {
                String polleIPAddressStr = args[2];
                polleIPAddress = StrIPToByteArr(polleIPAddressStr);
            }
        }

        CameraUtil camearUtil;
        long connect_StartTime = System.currentTimeMillis();   //获取开始时间
        for (int i = startNo; i <= endNo; i++) {
            Camera camera = new Camera();
            camearUtil = new CameraUtil();
            camearUtil.setServerIP(polleIPAddress);
            camearUtil.setTag(i);
            camearUtil.initDevSeq(tag, i);
            camera.setCameraUtil(camearUtil);
            try {
                camera.turnOn();
            } catch (IOException ex) {
                Logger.getLogger(ConnectionLauncher.class.getName()).log(Level.SEVERE, String.valueOf(i), ex);
            }
        }
        long connect_OverTime = System.currentTimeMillis();   //获取开始时间
        Logger.getLogger(ConnectionLauncher.class.getSimpleName()).
                log(Level.INFO, "\u6240\u6709\u8bbe\u5907\u8fde\u63a5\u7528\u65f6\uff1a{0}\u6beb\u79d2", (connect_OverTime - connect_StartTime));
    }

    public static byte[] StrIPToByteArr(String ipStr) {
        String[] sArr = ipStr.split("\\.");
        byte[] ipArr = new byte[4];
        int temp;
        for (int i = 0; i < 4; i++) {
            temp = Integer.valueOf(sArr[i]);
            ipArr[i] = (byte) temp;
        }
        return ipArr;
    }
}
