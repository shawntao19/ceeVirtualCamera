/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ceevsamera.dataprocess;

import ceevsamera.GlobalVar;
import ceevsamera.kaerevmp.Kaer_getVideoCodePara_resp;
import ceevsamera.kaerevmp.ProtocolVar;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class InitGetVideoCodeParaPacket {
    public static ByteArrayOutputStream init(byte[] PUID) {
        //初始化 数据包信息
        Kaer_getVideoCodePara_resp getVideoCodePara_resp = new Kaer_getVideoCodePara_resp();
        //设置数据包头
        getVideoCodePara_resp.setP_head((short) 0xff);
        getVideoCodePara_resp.setP_type((short) ProtocolVar.PacketType.GET_VIDEOCODEPARA);
        getVideoCodePara_resp.setP_length(ProtocolVar.PACKET_HEAD_LENGTH + 38);

        getVideoCodePara_resp.setPUID(PUID);
        getVideoCodePara_resp.setClientID(GlobalVar.NULL4);
        getVideoCodePara_resp.setChannel((byte) 1);
        getVideoCodePara_resp.setVideoType(ProtocolVar.IMAGE_TYPE.P720);
        byte[] NULL28 =new byte[28];
        getVideoCodePara_resp.setOtherInfo(NULL28);
        
        //写入字节流
        try {
            ByteArrayOutputStream bodyStream = getVideoCodePara_resp.writeProtocolData();
            if (bodyStream == null) {
                return null;
            }
            return bodyStream;
        } catch (IOException e) {
            Logger.getLogger(InitCapturePacket.class.getName()).log(Level.INFO, null, e);
            return null;
        }
    }
}
