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

    public static int tag = 4, startNo = 0, endNo = 4000;
    public static byte[] polleIPAddress= {(byte) 192, (byte) 168, (byte) 102, (byte) 87};

    /**
     * @param args the command line argumentss
     */
    public static void main(String[] args) {
        if (args != null) {
            if (args.length >= 2) {
                startNo = Integer.valueOf(args[0]);
                endNo = Integer.valueOf(args[1]);
            } else if (args.length >= 3) {
                String polleIPAddressStr=args[2];
                polleIPAddress=StrIPToByteArr(polleIPAddressStr);
            }
        }

        CameraUtil camearUtil;
        long connect_StartTime = System.currentTimeMillis();   //获取开始时间
        for (int i = startNo; i <= endNo; i++) {
            Camera camera = new Camera();
            camearUtil = new CameraUtil();
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
                log(Level.INFO, "所有设备连接用时：" + (connect_OverTime - connect_StartTime) + "毫秒");
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
