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
public class KaerSetTimeResp extends Kaer_head {

    private byte[] PUID;         //4B(小端书序) 视频服务器 ID，即设备ID 号，此处注意登录成功后要重新更新设备ID号
    private byte[] ClientID = {0, 0, 0, 0};         //4B(小端书序) 客户端ID
    private byte[] time;         //3B   HH:MM:SS

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
        if (time == null || time.length != 3) {
            return null;
        }
        outs.write(time);
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

    public byte[] getTime() {
        return time;
    }

    public void setTime(byte[] time) {
        this.time = time;
    }
    
}
