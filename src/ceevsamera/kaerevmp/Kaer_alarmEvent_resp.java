package ceevsamera.kaerevmp;

import ceevsamera.ConvertTools;
import ceevsamera.GlobalVar;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 卡尔 企业视频监控协议 上传传感器报警事件回复 指令类型 0x0f
 *
 * @author 基地 杨宸
 *
 */
public class Kaer_alarmEvent_resp extends Kaer_head {

    private byte[] PUID;         //4B(小端书序) 视频服务器 ID，即设备ID 号，此处注意登录成功后要重新更新设备ID号
    private byte channel;        //1B  通道号
    private byte state;          //1B状态  0开始   1结束
    private byte answer;         //1B回复 
    private byte sequence;       //1B序号

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
        //获取 数据包channel
        this.channel = packet[ProtocolVar.PACKET_HEAD_LENGTH + 4];
        //获取 数据包state
        this.state = packet[ProtocolVar.PACKET_HEAD_LENGTH + 5];
        //获取 answer
        this.answer = packet[ProtocolVar.PACKET_HEAD_LENGTH + 6];
        //获取 数据包sequence
        this.sequence = packet[ProtocolVar.PACKET_HEAD_LENGTH + 7];

        return GlobalVar.SUCCESSED;
    }

    public byte[] getPUID() {
        return PUID;
    }

    public void setPUID(byte[] pUID) {
        PUID = pUID;
    }

    public byte getChannel() {
        return channel;
    }

    public void setChannel(byte channel) {
        this.channel = channel;
    }

    public byte getState() {
        return state;
    }

    public void setState(byte state) {
        this.state = state;
    }

    public byte getAnswer() {
        return answer;
    }

    public void setAnswer(byte answer) {
        this.answer = answer;
    }

    public byte getSequence() {
        return sequence;
    }

    public void setSequence(byte sequence) {
        this.sequence = sequence;
    }
}
