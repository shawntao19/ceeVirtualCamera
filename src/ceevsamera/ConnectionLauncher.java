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

    public static byte[] polleIPAddress = {(byte) 192, (byte) 168, (byte) 102, (byte) 87};

    /**
     * @param args the command line argumentss
     */
    public static void main(String[] args) {
        CameraUtil camearUtil;
        long connect_StartTime = System.currentTimeMillis();   //获取开始时间

        String[] devSeqs = new String[10];
        devSeqs[0] = "04:00:00:00:00:01";
        devSeqs[1] = "04:00:00:00:00:02";
        devSeqs[2] = "04:00:00:00:00:03";
        devSeqs[3] = "04:00:00:00:00:04";
        devSeqs[4] = "04:00:00:00:00:05";
        devSeqs[5] = "04:00:00:00:00:06";
        devSeqs[6] = "04:00:00:00:00:07";
        devSeqs[7] = "04:00:00:00:00:08";
        devSeqs[8] = "04:00:00:00:00:09";
        devSeqs[9] = "04:00:00:00:00:0A";


        for (int i = 0; i < devSeqs.length; i++) {
            Camera camera = new Camera();
            camearUtil = new CameraUtil();
            camearUtil.setServerIP(polleIPAddress);
            camearUtil.setDevSeq(devSeqs[i]);
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
