/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ceevsamera.dataprocess;

import ceevsamera.kaerevmp.KaerGetTimeResp;
import ceevsamera.kaerevmp.KaerSetTimeResp;
import ceevsamera.kaerevmp.ProtocolVar;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class InitDeviceTimePacket {
    public static ByteArrayOutputStream initGetTime(byte[] PUID) {
        //初始化 数据包信息
        KaerGetTimeResp getTime = new KaerGetTimeResp();
        //设置数据包头
        getTime.setP_head((short) 0xff);
        getTime.setP_type((short) ProtocolVar.PacketType.GET_DEVICE_TIME);
        getTime.setP_length(ProtocolVar.PACKET_HEAD_LENGTH + 8);

        getTime.setPUID(PUID);
        getTime.setAnswer(ProtocolVar.RESULT_TYPE.ACK);
        getTime.setTime(PUID);
        //写入字节流
        try {
            ByteArrayOutputStream bodyStream = getTime.writeProtocolData();
            if (bodyStream == null) {
                return null;
            }
            return bodyStream;
        } catch (IOException e) {
            Logger.getLogger(InitDeviceTimePacket.class.getName()).log(Level.INFO, null, e);
            return null;
        }
    }
    
    public static ByteArrayOutputStream initSetTime(byte[] PUID) {
        //初始化 数据包信息
        KaerSetTimeResp setTime = new KaerSetTimeResp();
        //设置数据包头
        setTime.setP_head((short) 0xff);
        setTime.setP_type((short) ProtocolVar.PacketType.SET_DEVICE_TIME);
        setTime.setP_length(ProtocolVar.PACKET_HEAD_LENGTH + 11);

        setTime.setPUID(PUID);
        setTime.setTime(getByteTime());
        //写入字节流
        try {
            ByteArrayOutputStream bodyStream = setTime.writeProtocolData();
            if (bodyStream == null) {
                return null;
            }
            return bodyStream;
        } catch (IOException e) {
            Logger.getLogger(InitDeviceTimePacket.class.getName()).log(Level.INFO, null, e);
            return null;
        }
    }
    
    public static byte[] getByteTime(){
        byte[] time=new byte[3];
        Calendar currentCal = Calendar.getInstance();
        time[0]=(byte)currentCal.get(Calendar.HOUR_OF_DAY);
        time[1]=(byte)currentCal.get(Calendar.MINUTE);
        time[2]=(byte)currentCal.get(Calendar.SECOND);
        return time;
    }
}
