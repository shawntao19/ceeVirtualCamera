package ceevsamera.kaerevmp;

import ceevsamera.ConvertTools;
import ceevsamera.GlobalVar;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 卡尔 企业视频监控协议 设置某一服务器输入报警
 *
 * @author 基地 杨宸
 *
 */
public class Kaer_alarmset_req extends Kaer_head {

    private byte[] PUID;         //4B(小端书序) 视频服务器 ID，即设备ID 号，此处注意登录成功后要重新更新设备ID号
    private byte[] ClientID = {0, 0, 0, 0};         //4B(小端书序) 客户端ID
    private byte inputAlarm;     //1B 输入报警号ID
    private byte typeAlarm;          //1B 报警器类型
    private byte alarmSwitch;     //1B 报警通知中心开关
    private byte typeSound;     //1B 声音报警方式
    private byte typeOut;     //1B 外部报警方式  0/1  默认1 	1 报警开
    private byte channel_output;     //1B 报警输出通道选择
    private byte capNum;     //1B 抓拍图像张数
    private byte capInterval;     //1B 抓拍间隔
    private byte[] capChannel;     //4B 触发抓拍通道选择
    private byte[] videoChannel;     //4B 触发录像通道
    private byte videoTime;     //1B 报警触发录像时间
    private byte videoPreTime;     //1B 预录像时间
    private byte isOpen;     //1B 是否启用
    private byte[] name;     //39B 名称
    private byte[] allChanels;     //16B 联动响应通道
    private byte[] allChanelsDefault;     //16B 联动响应通道预置点
    private byte delay;     //1B 传感器延迟

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
        //获取 数据包resp
        this.typeAlarm = packet[ProtocolVar.PACKET_HEAD_LENGTH + 9];

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

    public byte getInputAlarm() {
        return inputAlarm;
    }

    public void setInputAlarm(byte inputAlarm) {
        this.inputAlarm = inputAlarm;
    }

    public byte getTypeAlarm() {
        return typeAlarm;
    }

    public void setTypeAlarm(byte typeAlarm) {
        this.typeAlarm = typeAlarm;
    }

    public byte getAlarmSwitch() {
        return alarmSwitch;
    }

    public void setAlarmSwitch(byte alarmSwitch) {
        this.alarmSwitch = alarmSwitch;
    }

    public byte getTypeSound() {
        return typeSound;
    }

    public void setTypeSound(byte typeSound) {
        this.typeSound = typeSound;
    }

    public byte getTypeOut() {
        return typeOut;
    }

    public void setTypeOut(byte typeOut) {
        this.typeOut = typeOut;
    }

    public byte getChannel_output() {
        return channel_output;
    }

    public void setChannel_output(byte channel_output) {
        this.channel_output = channel_output;
    }

    public byte getCapNum() {
        return capNum;
    }

    public void setCapNum(byte capNum) {
        this.capNum = capNum;
    }

    public byte getCapInterval() {
        return capInterval;
    }

    public void setCapInterval(byte capInterval) {
        this.capInterval = capInterval;
    }

    public byte[] getCapChannel() {
        return capChannel;
    }

    public void setCapChannel(byte[] capChannel) {
        this.capChannel = capChannel;
    }

    public byte[] getVideoChannel() {
        return videoChannel;
    }

    public void setVideoChannel(byte[] videoChannel) {
        this.videoChannel = videoChannel;
    }

    public byte getVideoTime() {
        return videoTime;
    }

    public void setVideoTime(byte videoTime) {
        this.videoTime = videoTime;
    }

    public byte getVideoPreTime() {
        return videoPreTime;
    }

    public void setVideoPreTime(byte videoPreTime) {
        this.videoPreTime = videoPreTime;
    }

    public byte getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(byte isOpen) {
        this.isOpen = isOpen;
    }

    public byte[] getName() {
        return name;
    }

    public void setName(byte[] name) {
        this.name = name;
    }

    public byte[] getAllChanels() {
        return allChanels;
    }

    public void setAllChanels(byte[] allChanels) {
        this.allChanels = allChanels;
    }

    public byte[] getAllChanelsDefault() {
        return allChanelsDefault;
    }

    public void setAllChanelsDefault(byte[] allChanelsDefault) {
        this.allChanelsDefault = allChanelsDefault;
    }

    public byte getDelay() {
        return delay;
    }

    public void setDelay(byte delay) {
        this.delay = delay;
    }
}
