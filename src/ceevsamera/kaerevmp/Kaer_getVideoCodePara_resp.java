/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ceevsamera.kaerevmp;

import ceevsamera.ConvertTools;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 *
 * @author Administrator
 */
public class Kaer_getVideoCodePara_resp extends Kaer_head {

    private byte[] PUID;         //4B(小端书序) 视频服务器 ID，即设备ID 号，此处注意登录成功后要重新更新设备ID号
    private byte[] ClientID = {0, 0, 0, 0};         //4B(小端书序) 客户端ID
    private byte channel;        //1B    通道号
    private byte videoType;        //1B    视频格式
    private byte[] otherInfo;     //28B   其他无用信息

    /**
     * 将设置报警参数 写入数据流中
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
        if (ClientID == null || ClientID.length != 4) {
            return null;
        }
        outs.write(ClientID);
        outs.write(channel);
        outs.write(videoType);
        outs.write(otherInfo);
        return outs;
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

    public byte getVideoType() {
        return videoType;
    }

    public void setVideoType(byte videoType) {
        this.videoType = videoType;
    }

    public byte[] getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(byte[] otherInfo) {
        this.otherInfo = otherInfo;
    }
    
}
