package ceevsamera.kaerevmp;

import ceevsamera.ConvertTools;
import ceevsamera.GlobalVar;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 卡尔 企业视频监控协议 心跳 设备到服务器请求回复
 *
 * @author 基地 杨宸
 *
 */
public class Kaer_heartbeat_resp extends Kaer_head {

    private byte[] PUID;           //4B(小端书序) 视频服务器 ID，即 设备ID 号，此处注意登录成功后要重新更新设备ID号
    private long standardTime;     //同步时间(long)  	4B  国际标准时间，从1970 年1 月1 号到现在的秒数
    private int intervalTime;      //1B 时间（秒）范围10~3600 秒（低	位在前）默认20

    @Override
    public int parsePacket(byte[] packet) {
        if (packet == null || packet.length < ProtocolVar.PACKET_HEAD_LENGTH) {
            return GlobalVar.FAILED;
        }
        //获取包头 p_head
        this.p_head = packet[0];

        if (this.p_head != ProtocolVar.PacketType.PACKET_HEAD) {
            return GlobalVar.FAILED;
        }

        //获取 类型p_type
        this.p_type = packet[1];
        //获取 数据包长度 p_length
        this.p_length = ConvertTools.byteArrToLong_littleEnd(packet, 2);

        //获取 数据包PUID
        this.PUID = new byte[4];
        ConvertTools.copyByteArr(packet, this.PUID, ProtocolVar.PACKET_HEAD_LENGTH + 0, 0, 4);
        //获取 standTime
        byte[] standTime = new byte[4];
        ConvertTools.copyByteArr(packet, standTime, ProtocolVar.PACKET_HEAD_LENGTH + 4 + 0, 0, 4);
        
        this.intervalTime=packet[ProtocolVar.PACKET_HEAD_LENGTH+8];
        return GlobalVar.SUCCESSED;
    }

    public byte[] getPUID() {
        return PUID;
    }

    public void setPUID(byte[] pUID) {
        PUID = pUID;
    }

    public int getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(short intervalTime) {
        this.intervalTime = intervalTime;
    }

    public long getStandardTime() {
        return standardTime;
    }

    public void setStandardTime(long standardTime) {
        this.standardTime = standardTime;
    }
}
