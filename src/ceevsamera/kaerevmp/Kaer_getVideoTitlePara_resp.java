/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ceevsamera.kaerevmp;

import ceevsamera.ConvertTools;
import ceevsamera.GlobalVar;

/**
 *
 * @author Administrator
 */
public class Kaer_getVideoTitlePara_resp extends Kaer_head{
    private byte[] PUID;         //4B(小端书序) 视频服务器 ID，即设备ID 号，此处注意登录成功后要重新更新设备ID号
    private byte[] ClientID = {0, 0, 0, 0};         //4B(小端书序) 客户端ID
    private byte channel;        //1B    通道号
    private byte answer;        //1B    应答
    private byte[] data;     //未知大小   需要的数据
    
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
        ConvertTools.copyByteArr(packet, this.ClientID, ProtocolVar.PACKET_HEAD_LENGTH + 4 + 0, 0, 4);
        //获取 数据包channel
        this.channel = packet[ProtocolVar.PACKET_HEAD_LENGTH + 8];

        this.answer = packet[ProtocolVar.PACKET_HEAD_LENGTH + 9];
        
        ConvertTools.copyByteArr(packet, this.data, ProtocolVar.PACKET_HEAD_LENGTH + 10, 0, (int)this.p_length-12);

        return GlobalVar.SUCCESSED;
    }

    public byte[] getPUID() {
        return PUID;
    }

    public void setPUID(byte[] PUID) {
        this.PUID = PUID;
    }

    public byte[] getClientID() {
        return ClientID;
    }

    public void setClientID(byte[] ClientID) {
        this.ClientID = ClientID;
    }

    public byte getChannel() {
        return channel;
    }

    public void setChannel(byte channel) {
        this.channel = channel;
    }

    public byte getAnswer() {
        return answer;
    }

    public void setAnswer(byte answer) {
        this.answer = answer;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
    
    
}
