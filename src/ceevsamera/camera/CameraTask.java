/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ceevsamera.camera;

import ceevsamera.ConvertTools;
import ceevsamera.GlobalVar;
import ceevsamera.dataprocess.InitAlarmEventPacket;
import ceevsamera.dataprocess.InitAlarmSettingPacket;
import ceevsamera.dataprocess.InitAlarmTimePacket;
import ceevsamera.dataprocess.InitCapturePacket;
import ceevsamera.dataprocess.InitDeviceTimePacket;
import ceevsamera.dataprocess.InitGetVideoCodeParaPacket;
import ceevsamera.dataprocess.InitHeartBeatPacket;
import ceevsamera.dataprocess.InitLoginPacket;
import ceevsamera.dataprocess.InitReqSecretKeyPacket;
import ceevsamera.dataprocess.InitSetVideoCodeParaPacket;
import ceevsamera.kaerevmp.Kaer_Login_resp;
import ceevsamera.kaerevmp.Kaer_SecretKey_resp;
import ceevsamera.kaerevmp.Kaer_alarmTime_req;
import ceevsamera.kaerevmp.Kaer_alarmset_req;
import ceevsamera.kaerevmp.Kaer_capture_req;
import ceevsamera.kaerevmp.Kaer_heartbeat_resp;
import ceevsamera.kaerevmp.ProtocolVar;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class CameraTask {

    private CameraUtil cameraUtil;

    private Socket cameraSocket = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;
    private int port;

    byte[] PUID = null, devSeq = null, MD5Key = null, localIP = null, serverIP = null;  //视频服务器ID,设备标志码,设备对应的密钥
    private String devSeqStr = null;
    private int heartbeatFreq = 10;   //默认心跳为30秒
    private Timer heartbeatTimer = null, alarmTimer = null;

    private boolean isDestoryed = false;  //task 对象是否被销毁
    public static final int CLEAR_LEVEL_1 = 1;
    public static final int CLEAR_LEVEL_2 = 2;
    public static final int CLEAR_LEVEL_3 = 3;

    public CameraTask(CameraUtil cameraParam) {
        this.cameraUtil = cameraParam;
    }

    public Socket getSocket() {
        return this.cameraSocket;
    }

    public void connect() {
        try {
            devSeqStr = cameraUtil.getDevSeq();
            devSeq = ConvertTools.devSeqStr2ByteArr(devSeqStr);
            serverIP = cameraUtil.getServerIP();

            cameraSocket = new Socket();
            SocketAddress sa = new InetSocketAddress(ConvertTools.ipByteArr2Str(serverIP), 22616);
            cameraSocket.connect(sa, 60 * 1000);
            
            
            port = cameraSocket.getLocalPort();
            cameraUtil.setOnline(true);
            localIP = cameraSocket.getInetAddress().getAddress();

            in = new DataInputStream(cameraSocket.getInputStream());
            out = new DataOutputStream(cameraSocket.getOutputStream());

            heartbeatTimer = new Timer();
            alarmTimer = new Timer();

        } catch (IOException ex) {
            Logger.getLogger(CameraTask.class.getName()).log(Level.INFO, "设备：" + devSeqStr + " 端口：" + port, ex);
            destoryTask(CLEAR_LEVEL_3);
            return;
        }

        //---------先登录注册 建立可发送指令的连接
        if (proKeyReq() == GlobalVar.SUCCESSED) {
            try {
                if (proLoginReq() == GlobalVar.SUCCESSED) {
                    Camera.addCameraTask(devSeqStr, this);

                    receivePacket();
                }
            } catch (IOException ex) {
                Logger.getLogger(CameraTask.class.getName()).log(Level.SEVERE, devSeqStr, ex);
                destoryTask(CLEAR_LEVEL_3);
            }
        }
        destoryTask(CLEAR_LEVEL_3);
    }

    private void sendPacket(byte[] packet) {
        synchronized (out) {
            try {
                out.write(packet);
                out.flush();
            } catch (IOException ex) {
                Logger.getLogger(CameraTask.class.getName()).
                        log(Level.INFO, devSeqStr + "连接断开，sendPacket socket异常", ex);
                //断开连接，回收所有资源
                destoryTask(CLEAR_LEVEL_3);
            }
        }
    }

    private byte[] readPacket() throws IOException {
        byte[] reslutPacket = null;
        byte p_head, p_type;
        byte[] p_lenByte = new byte[4];
        int p_lenInt;
        int index, buf;
        while (true) {
            try {
                p_head = in.readByte();
                if (p_head == ProtocolVar.PacketType.PACKET_HEAD) {
                    p_type = in.readByte();
                    in.read(p_lenByte);
                    p_lenInt = (int) ConvertTools.byteArrToLong_littleEnd(p_lenByte, 0);
                    reslutPacket = new byte[p_lenInt];
                    reslutPacket[0] = p_head;
                    reslutPacket[1] = p_type;
                    ConvertTools.copyByteArr(p_lenByte, reslutPacket, 0, 2, 4);
                    index = 6;
                    /**
                     * 直到收到所有数据
                     */
                    while (index < p_lenInt) {
                        if (in.available() != 0) {
                            buf = in.read(reslutPacket, index, p_lenInt - index);
                            if (buf != -1) {
                                index += buf;
                            }
                        }
                    }
                    break;
                }
            } catch (ArrayIndexOutOfBoundsException ex) {
                Logger.getLogger(CameraTask.class.getName()).
                        log(Level.INFO, devSeqStr, ex);
            }
        }
        return reslutPacket;
    }

    /**
     * 数据包接收
     *
     * @param in
     */
    private void receivePacket() {
        // 先发送一个心跳数据包
        setTimingHeartbeatTask();
        byte[] receivedPacket;
        while (true) {
            try {
                receivedPacket = readPacket();
                processPacket(receivedPacket);
            } catch (IOException ex) {
                Logger.getLogger(CameraTask.class.getName()).
                        log(Level.INFO, devSeqStr + "连接断开，receivePacket socket异常", ex);
                break;
            }
        }
        destoryTask(CLEAR_LEVEL_3);
    }

    /**
     *
     * 对接收到的数据包进行分类处理
     */
    private void processPacket(byte[] receivedPacket) {
        byte packet_head = receivedPacket[0];   //数据包类型
        if (packet_head == ProtocolVar.PacketType.PACKET_HEAD) {
            int packet_type = receivedPacket[1];   //数据包类型
            switch (packet_type) {
                //处理接收到的心跳请求
                case ProtocolVar.PacketType.HEARTBEAT_PRO:
                    ///打印心跳数据包
//                    Logger.getLogger(CameraTask.class.getName()).
//                            log(Level.INFO, devSeqStr + "---心跳回复", devSeqStr);
//                    PrintHelper.printByteArr(receivedPacket, 0);
                    Kaer_heartbeat_resp heartbeat_resp = new Kaer_heartbeat_resp();
                    heartbeat_resp.parsePacket(receivedPacket);
                    heartbeatFreq = heartbeat_resp.getIntervalTime();
                    break;
                //处理接收到的报警设置请求
                case ProtocolVar.PacketType.ALARMSET_PRO:
//                    Logger.getLogger(CameraTask.class.getName()).
//                            log(Level.INFO, devSeqStr + "---报警设置 req", devSeqStr);
//                    PrintHelper.printByteArr(receivedPacket, 0);
                    Kaer_alarmset_req alarmsetReq = new Kaer_alarmset_req();
                    alarmsetReq.parsePacket(receivedPacket);

                    //发送报警设置确认数据包
                    byte[] alarmSetRespPacet = InitAlarmSettingPacket.init(PUID).toByteArray();
                    sendPacket(alarmSetRespPacet);
//                    Logger.getLogger(CameraTask.class.getName()).
//                            log(Level.INFO, devSeqStr + "---报警设置 resp", devSeqStr);
//                    PrintHelper.printByteArr(alarmSetRespPacet, 0);
                    if (alarmsetReq.getTypeAlarm() == ProtocolVar.ALARM_OFF) {
                        //上传报警开始数据包
                        byte[] respPacket = InitAlarmEventPacket.init(PUID, (byte) 0).toByteArray();

                        sendPacket(respPacket);
//                        Logger.getLogger(CameraTask.class.getName()).
//                                log(Level.INFO, devSeqStr + "---报警事件上传 req  报警开始", devSeqStr);
//                        PrintHelper.printByteArr(receivedPacket, 0);
                        //20秒后发送报警结束
                        setAlarmOffTask();
                    } else {
                        //do nothing
                    }
                    break;
                //处理布放时间设置请求
                case ProtocolVar.PacketType.ALARMTIME_PRO:
//                    Logger.getLogger(CameraTask.class.getName()).
//                            log(Level.INFO, devSeqStr + "---时间设置 req", devSeqStr);
//                    PrintHelper.printByteArr(receivedPacket, 0);
                    Kaer_alarmTime_req alarmTimeReq = new Kaer_alarmTime_req();
                    alarmTimeReq.parsePacket(receivedPacket);

                    byte[] respPacket = InitAlarmTimePacket.init(PUID).toByteArray();
//                    Logger.getLogger(CameraTask.class.getName()).
//                            log(Level.INFO, devSeqStr + "---时间设置 resp", devSeqStr);
//                    PrintHelper.printByteArr(respPacket, 0);
                    sendPacket(respPacket);
                    break;
                //处理报警事件上传请求
                case ProtocolVar.PacketType.ALARMEVENT_PRO:
//                    Logger.getLogger(CameraTask.class.getName()).
//                            log(Level.INFO, devSeqStr + "---报警事件上传 resp", devSeqStr);
//                    PrintHelper.printByteArr(receivedPacket, 0);
//                    Kaer_alarmEvent_resp alarmEventResp = new Kaer_alarmEvent_resp();
//                    alarmEventResp.parsePacket(receivedPacket);
                    break;
                //处理接视频请求的回复
                case ProtocolVar.PacketType.CAPTURE_PRO:
//                    Logger.getLogger(CameraTask.class.getName()).
//                            log(Level.INFO, devSeqStr + "---视频请求 req", devSeqStr);
//                    PrintHelper.printByteArr(receivedPacket, 0);
                    Kaer_capture_req captureReq = new Kaer_capture_req();
                    captureReq.parsePacket(receivedPacket);

                    byte[] respCapturePacket = InitCapturePacket.init(PUID).toByteArray();
//                    Logger.getLogger(CameraTask.class.getName()).
//                            log(Level.INFO, devSeqStr + "---视频请求 resp", devSeqStr);
//                    PrintHelper.printByteArr(respCapturePacket, 0);
                    sendPacket(respCapturePacket);
                    //--------------------发送图片
                    sendPicture();
                    break;
                //处理获取视频编码参数的回复
                case ProtocolVar.PacketType.GET_VIDEOCODEPARA:
//                    Logger.getLogger(CameraTask.class.getName()).
//                            log(Level.INFO, devSeqStr + "---获取视频编码参数 req", devSeqStr);
//                    PrintHelper.printByteArr(receivedPacket, 0);

                    byte[] getVideoParaRespPacket = InitGetVideoCodeParaPacket.init(PUID).toByteArray();
//                    Logger.getLogger(CameraTask.class.getName()).
//                            log(Level.INFO, devSeqStr + "---获取视频编码参数 resp", devSeqStr);
//                    PrintHelper.printByteArr(getVideoParaRespPacket, 0);
                    sendPacket(getVideoParaRespPacket);
                    break;
                //处理设置视频编码参数的回复
                case ProtocolVar.PacketType.SET_VIDEOCODEPARA:
//                    Logger.getLogger(CameraTask.class.getName()).
//                            log(Level.INFO, devSeqStr + "---设置视频编码参数 req", devSeqStr);
//                    PrintHelper.printByteArr(receivedPacket, 0);

                    byte[] setVideoParaRespPacket = InitSetVideoCodeParaPacket.init(PUID).toByteArray();
//                    Logger.getLogger(CameraTask.class.getName()).
//                            log(Level.INFO, devSeqStr + "---设置视频编码参数 resp", devSeqStr);
//                    PrintHelper.printByteArr(setVideoParaRespPacket, 0);
                    sendPacket(setVideoParaRespPacket);
                    break;
                case ProtocolVar.PacketType.GET_DEVICE_TIME:
                    byte[] getTimeResp = InitDeviceTimePacket.initGetTime(PUID).toByteArray();
//                    Logger.getLogger(CameraTask.class.getName()).
//                            log(Level.INFO, devSeqStr + "---GET_DEVICE_TIME resp", devSeqStr);
//                    PrintHelper.printByteArr(setVideoParaRespPacket, 0);
                    sendPacket(getTimeResp);
                    break;
                default:
//                    Logger.getLogger(CameraTask.class.getName()).log(Level.INFO, ConvertTools.devSeqByteArr2Str(devSeq) + "  的不知名数据包");
//                    PrintHelper.printByteArr(receivedPacket, 0);
//                    System.out.println("");
            }
        }
    }

    public void sendPicture() {
        try {
            PicSendTask sendPicTask = new PicSendTask(PUID, devSeqStr, cameraUtil.getTag());
            sendPicTask.start();
        } catch (IOException ex) {
            Logger.getLogger(CameraTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 发送 申请密钥 数据包
     *
     * @return
     * @throws IOException
     */
    private int proKeyReq() {
        //-------------------------------------发送 申请密钥 数据包
        byte[] keyReqByteArr, keyRespByteArr;
        try {
            keyReqByteArr = InitReqSecretKeyPacket.
                    init(ConvertTools.devSeqStr2ByteArr(cameraUtil.getDevSeq())).toByteArray();

//            Logger.getLogger(CameraTask.class.getName()).log(Level.INFO, "申请密钥数据包:");
//            PrintHelper.printByteArr(keyReqByteArr, 0);
            sendPacket(keyReqByteArr);

            //-------------------------------------接收 申请密钥 数据包
            keyRespByteArr = readPacket();

//            Logger.getLogger(CameraTask.class.getName()).log(Level.INFO, "回复密钥数据包:");
//            PrintHelper.printByteArr(keyRespByteArr, 0);
            Kaer_SecretKey_resp keyResp = new Kaer_SecretKey_resp();

            if (GlobalVar.FAILED == keyResp.parsePacket(keyRespByteArr)) {
                Logger.getLogger(CameraTask.class.getName()).log(Level.INFO, "申请密钥数据包解析错误");
                return GlobalVar.FAILED;
            }

            //判断是不是申请密钥 请求类型
            if (keyResp.getP_type() != ProtocolVar.PacketType.SCRETKEY_PRO) {
                Logger.getLogger(CameraTask.class.getName()).log(Level.INFO, "申请密钥数据包类型错误");
                return GlobalVar.FAILED;
            }
            PUID = keyResp.getPUID();
            MD5Key = keyResp.getSecretKey();
        } catch (IOException ex) {
            Logger.getLogger(CameraTask.class.getName()).log(Level.SEVERE, devSeqStr + "申请密钥操作失败  端口：" + port, ex);
            return GlobalVar.FAILED;
        }
        return GlobalVar.SUCCESSED;
    }

    /**
     * 发送并处理 登录 数据包
     *
     * @param in
     * @param out
     * @param PUID
     * @param MD5Key
     * @return
     * @throws IOException
     */
    private int proLoginReq() throws IOException {
        //-------------------------------------接收 登录申请 数据包
        byte[] loginReqByteArr, loginRespByteArr;
        loginReqByteArr = InitLoginPacket.
                init(PUID, MD5Key, serverIP).toByteArray();

//        Logger.getLogger(CameraTask.class.getName()).log(Level.INFO, "登录请求数据包:");
//        PrintHelper.printByteArr(loginReqByteArr, 0);
        sendPacket(loginReqByteArr);

        loginRespByteArr = readPacket();

//        Logger.getLogger(CameraTask.class.getName()).log(Level.INFO, "登录回复数据包:");
//        PrintHelper.printByteArr(loginRespByteArr, 0);
        Kaer_Login_resp kaer_Login_resp = new Kaer_Login_resp();

        if (GlobalVar.FAILED == kaer_Login_resp.parsePacket(loginRespByteArr)) {
            Logger.getLogger(CameraTask.class.getName()).log(Level.INFO, "登录回复数据包解析错误");
            return GlobalVar.FAILED;
        }
        //判断是不是申请密钥 请求类型
        if (kaer_Login_resp.getP_type() != ProtocolVar.PacketType.LOGIN_PRO) {
            Logger.getLogger(CameraTask.class.getName()).log(Level.INFO, "申请登录数据包类型错误");
            return GlobalVar.FAILED;
        }
        if (kaer_Login_resp.getState() == ProtocolVar.RESULT_TYPE.ACK) {
            return GlobalVar.SUCCESSED;
        } else {
            Logger.getLogger(CameraTask.class.getName()).log(Level.INFO, "------------登录失败-------------" + devSeqStr + " 端口:" + port);
            return GlobalVar.FAILED;
        }
    }

    /**
     * 设置定时发送心跳数据包
     */
    public void setTimingHeartbeatTask() {
        Date nextSendDate;
        //进行查询
        nextSendDate = calculateNextTime(heartbeatFreq);
        //定义timer
        heartbeatTimer.purge();

        heartbeatTimer.schedule(new HeartbeatTask(), nextSendDate);
    }

    public void setAlarmOffTask() {
        Date nextSendDate;
        //进行查询
        nextSendDate = calculateNextTime(20);
        //定义timer
        alarmTimer.purge();

        alarmTimer.schedule(new AlarmOffTask(), nextSendDate);
    }

    class HeartbeatTask extends TimerTask {

        @Override
        public void run() {
            //向平台发送心跳数据请求
            byte[] hearbeatPacket = InitHeartBeatPacket.init(PUID, localIP).toByteArray();
            sendPacket(hearbeatPacket);
//            Logger.getLogger(CameraTask.class.getName()).
//                    log(Level.INFO, devSeqStr + "---心跳请求", devSeqStr);
//            PrintHelper.printByteArr(hearbeatPacket, 0);

            if (!isDestoryed) {
                setTimingHeartbeatTask();
            }

        }
    }

    class AlarmOffTask extends TimerTask {

        @Override
        public void run() {
            byte[] respPacket = InitAlarmEventPacket.init(PUID, (byte) 1).toByteArray();
//            Logger.getLogger(CameraTask.class.getName()).
//                    log(Level.INFO, devSeqStr + "---ALARMEVENT_PRO req 报警结束", devSeqStr);
//            PrintHelper.printByteArr(respPacket, 0);
            sendPacket(respPacket);
        }
    }

    public synchronized void destoryTask(int clearLevel) {
        if (!isDestoryed) {
            if (clearLevel >= CLEAR_LEVEL_1) {
                clearStreamResource();
            }

            if (clearLevel >= CLEAR_LEVEL_2) {

            }
            if (clearLevel >= CLEAR_LEVEL_3) {

            }
            isDestoryed = true;
            cameraUtil.setOnline(false);
        }
    }

    private void clearStreamResource() {
        if (in != null) {
            try {
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(CameraTask.class.getName()).
                        log(Level.INFO, ConvertTools.devSeqByteArr2Str(devSeq) + "inputStream close 异常 in closeLink()Method", ex);
            }
        }
        if (out != null) {
            try {
                out.close();
            } catch (IOException ex) {
                Logger.getLogger(CameraTask.class.getName()).
                        log(Level.INFO, ConvertTools.devSeqByteArr2Str(devSeq) + "outputStream close 异常 in closeLink()Method", ex);
            }
        }
        if (cameraSocket != null && !cameraSocket.isClosed()) {
            try {
                cameraSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(CameraTask.class.getName()).log(Level.SEVERE, null, ex);
            }
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

    public CameraUtil getCameraUtil() {
        return cameraUtil;
    }

    public void setCameraUtil(CameraUtil cameraUtil) {
        this.cameraUtil = cameraUtil;
    }

}
