package ceevsamera.kaerevmp;

import ceevsamera.ConvertTools;
import ceevsamera.GlobalVar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 卡尔 企业视频监控协议 申请密钥消息回复
 *
 * @author 基地 杨宸
 *
 */
public class Kaer_SecretKey_resp extends Kaer_head {

    private byte[] PUID;         //4B(小端书序) 视频服务器 ID，即设备ID 号，此处注意登录成功后要重新更新设备ID号
    private byte[] secretKey;  //密钥  8B

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
        //获取 数据包secretKey
        this.secretKey = new byte[8];
        ConvertTools.copyByteArr(packet, this.secretKey, ProtocolVar.PACKET_HEAD_LENGTH + 4 + 0, 0, 8);
        
        return GlobalVar.SUCCESSED;
    }

    public byte[] getPUID() {
        return PUID;
    }

    public void setPUID(byte[] pUID) {
        PUID = pUID;
    }

    public byte[] getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(byte[] secretKey) {
        this.secretKey = secretKey;
    }

}
