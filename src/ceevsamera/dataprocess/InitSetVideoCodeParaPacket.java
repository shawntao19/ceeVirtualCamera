/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ceevsamera.dataprocess;

import ceevsamera.GlobalVar;
import ceevsamera.kaerevmp.Kaer_setVideoCodePara_resp;
import ceevsamera.kaerevmp.ProtocolVar;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class InitSetVideoCodeParaPacket {
    public static ByteArrayOutputStream init(byte[] PUID) {
        //初始化 数据包信息
        Kaer_setVideoCodePara_resp setVideoCodePara_resp = new Kaer_setVideoCodePara_resp();
        //设置数据包头
        setVideoCodePara_resp.setP_head((short) 0xff);
        setVideoCodePara_resp.setP_type((short) ProtocolVar.PacketType.GET_VIDEOCODEPARA);
        setVideoCodePara_resp.setP_length(ProtocolVar.PACKET_HEAD_LENGTH + 10);

        setVideoCodePara_resp.setPUID(PUID);
        setVideoCodePara_resp.setClientID(GlobalVar.NULL4);
        setVideoCodePara_resp.setChannel((byte) 1);
        setVideoCodePara_resp.setAnswer(ProtocolVar.RESULT_TYPE.ACK);

        
        //写入字节流
        try {
            ByteArrayOutputStream bodyStream = setVideoCodePara_resp.writeProtocolData();
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
