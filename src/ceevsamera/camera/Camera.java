/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ceevsamera.camera;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class Camera {

    public static Map<String, CameraTask> cameraTaskContainer = new HashMap<>();
    public static Map<String, PicSendTask> sendPicTaskContainer = new HashMap<>();

    public static int connectedCounter = 0, sendPicCounter = 0;

    private CameraUtil cameraUtil = new CameraUtil();
    private CameraTask cameraTask;

    private Timer hearbeatTimer = null;
    Date nextRequestDate;

    
    public synchronized static void addCameraTask(String devSeq, CameraTask cameraTask) {
        cameraTaskContainer.put(devSeq, cameraTask);
        connectedCounter = cameraTaskContainer.size();
        Logger.getLogger(Camera.class.getSimpleName()).
                log(Level.INFO, "当前连接数：" + connectedCounter + "    tag:" + cameraTask.getCameraUtil().getTag()+"    devSeq:"+devSeq);
    }

    public synchronized static void addSendPicTask(String devSeq, PicSendTask picSendTask) {
        sendPicTaskContainer.put(devSeq, picSendTask);

        sendPicCounter = sendPicTaskContainer.size();
        Logger.getLogger(Camera.class.getSimpleName()).
                log(Level.INFO, "已发送图片的摄像头数目：" + sendPicCounter);
    }

    public static void deleteCameraTask(String devSeq) {
        cameraTaskContainer.remove(devSeq);
        System.out.println("断开连接：" + devSeq + "  现有连接数：" + cameraTaskContainer.size());
    }

    public void setCameraUtil(CameraUtil cameraUtil) {
        this.cameraUtil = cameraUtil;
    }

    public void turnOn() throws IOException {
        hearbeatTimer = new Timer();
        setTimingTask(1);
    }

    private void setTimingTask(int freq) {
        nextRequestDate = calculateNextTime(freq);
        hearbeatTimer.purge();
        hearbeatTimer.schedule(new TimingRequest(), nextRequestDate);
    }

    class TimingRequest extends TimerTask {

        @Override
        public void run() {
            if (!cameraUtil.isOnline()) {
                cameraTask = new CameraTask(cameraUtil);
                cameraTask.connect();
            }
//            setTimingTask(1);
        }
    }

    /**
     * 计算下次拍照时间 reqFreq 间隔时间 单位秒
     */
    private Date calculateNextTime(int reqFreqSecond) {
        Calendar currentCal = Calendar.getInstance();
        currentCal.add(Calendar.SECOND, reqFreqSecond);
        return currentCal.getTime();
    }
}
