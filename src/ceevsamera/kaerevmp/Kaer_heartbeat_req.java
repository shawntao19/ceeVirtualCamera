package ceevsamera.kaerevmp;

import ceevsamera.ConvertTools;
import ceevsamera.GlobalVar;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 卡尔 企业视频监控协议 心跳 设备到服务器请求
 *
 * @author 基地 杨宸
 *
 */
public class Kaer_heartbeat_req extends Kaer_head {

    private byte[] PUID;         //4B(小端书序) 视频服务器 ID，即设备ID 号，此处注意登录成功后要重新更新设备ID号
    private byte[] localIP;    //本机ip 4B
    private byte[] alarmState;   //布撤防状态 4B 布撤防状态以4 个0 代替；

    /**
     * 将回复心跳信息 加入数据流中
     *
     * @return 
     * @throws IOException
     */
    public ByteArrayOutputStream writeProtocolData() throws IOException {
        ByteArrayOutputStream outs = new ByteArrayOutputStream();
        outs.write((byte) p_head);
        outs.write((byte) p_type);
        outs.write(ConvertTools.longToByteArr_LittleEnd_(p_length));

        //写入登录参数
        if (PUID == null || PUID.length != 4) {
            return null;
        }
        outs.write(PUID);

        if (localIP == null || localIP.length != 4) {
            return null;
        }
        outs.write(localIP);

        if (alarmState == null || alarmState.length != 4) {
            return null;
        }
        outs.write(alarmState);
        return outs;
    }

    public byte[] getPUID() {
        return PUID;
    }

    public void setPUID(byte[] pUID) {
        PUID = pUID;
    }

    public byte[] getLocalIP() {
        return localIP;
    }

    public void setLocalIP(byte[] localIP) {
        this.localIP = localIP;
    }

    public byte[] getAlarmState() {
        return alarmState;
    }

    public void setAlarmState(byte[] alarmState) {
        this.alarmState = alarmState;
    }

}
