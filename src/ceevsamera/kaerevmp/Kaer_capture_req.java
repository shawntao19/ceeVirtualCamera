package ceevsamera.kaerevmp;

import ceevsamera.ConvertTools;
import ceevsamera.GlobalVar;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 卡尔 企业视频监控协议 视频请求（实际用途为抓图请求）
 *
 * @author 基地 杨宸
 *
 */
public class Kaer_capture_req extends Kaer_head {

    private byte[] PUID;         //4B(小端书序) 视频服务器 ID，即设备ID 号，此处注意登录成功后要重新更新设备ID号
    private byte[] ClientID = {0, 0, 0, 0};         //4B(小端书序) 客户端ID
    private byte channel;        //1B    通道号
    private byte upVieo_Channel;        //1B    上行视频
    private byte upSound_Channel;        //1B   上行音频
    private byte downSound_Channel;        //1B  下行音频
    private byte protocolType;        //1B    协议类型
    private byte[] serverIP;          // 4B 服务器IP

    @Override
    public int parsePacket(byte[] packet) {
        if (packet == null || packet.length < ProtocolVar.PACKET_HEAD_LENGTH) {
            return GlobalVar.FAILED;
        }
        //获取包头 p_head
        this.p_head = packet[0];
        //获取 类型p_type
        this.p_type = packet[1];
        //获取 数据包长度 p_length
        this.p_length = ConvertTools.byteArrToLong_littleEnd(packet, 2);

        //获取 数据包PUID
        this.PUID = new byte[4];
        ConvertTools.copyByteArr(packet, this.PUID, ProtocolVar.PACKET_HEAD_LENGTH + 0, 0, 4);
        //获取 数据包ClientID
        this.ClientID = new byte[4];
        ConvertTools.copyByteArr(packet, this.ClientID, ProtocolVar.PACKET_HEAD_LENGTH + 4, 0, 4);
        //获取 数据包channel
        this.channel = packet[ProtocolVar.PACKET_HEAD_LENGTH + 8];
        //获取 serverIP
        ConvertTools.copyByteArr(packet, this.serverIP, ProtocolVar.PACKET_HEAD_LENGTH + 13, 0, 4);

        return GlobalVar.SUCCESSED;
    }

    public byte[] getPUID() {
        return PUID;
    }

    public void setPUID(byte[] pUID) {
        PUID = pUID;
    }

    public byte[] getClientID() {
        return ClientID;
    }

    public void setClientID(byte[] clientID) {
        ClientID = clientID;
    }

    public byte getChannel() {
        return channel;
    }

    public void setChannel(byte channel) {
        this.channel = channel;
    }

    public byte getUpVieo_Channel() {
        return upVieo_Channel;
    }

    public void setUpVieo_Channel(byte upVieo_Channel) {
        this.upVieo_Channel = upVieo_Channel;
    }

    public byte getUpSound_Channel() {
        return upSound_Channel;
    }

    public void setUpSound_Channel(byte upSound_Channel) {
        this.upSound_Channel = upSound_Channel;
    }

    public byte getDownSound_Channel() {
        return downSound_Channel;
    }

    public void setDownSound_Channel(byte downSound_Channel) {
        this.downSound_Channel = downSound_Channel;
    }

    public byte getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(byte protocolType) {
        this.protocolType = protocolType;
    }

    public byte[] getServerIP() {
        return serverIP;
    }

    public void setServerIP(byte[] serverIP) {
        this.serverIP = serverIP;
    }

}
