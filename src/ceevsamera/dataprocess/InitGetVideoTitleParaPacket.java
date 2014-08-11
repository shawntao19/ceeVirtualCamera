/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ceevsamera.dataprocess;

import ceevsamera.GlobalVar;
import ceevsamera.kaerevmp.Kaer_getVideoTitlePara_req;
import ceevsamera.kaerevmp.ProtocolVar;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class InitGetVideoTitleParaPacket {

    public static ByteArrayOutputStream init(byte[] PUID) {
        //初始化 数据包信息
        Kaer_getVideoTitlePara_req getVideoTitlePara_req = new Kaer_getVideoTitlePara_req();
        //设置数据包头
        getVideoTitlePara_req.setP_head((short) 0xff);
        getVideoTitlePara_req.setP_type((short) ProtocolVar.PacketType.GET_VIDEOTITLEPARA);
        getVideoTitlePara_req.setP_length(ProtocolVar.PACKET_HEAD_LENGTH + 9);

        getVideoTitlePara_req.setPUID(PUID);
        getVideoTitlePara_req.setClientID(GlobalVar.NULL4);
        getVideoTitlePara_req.setChannel((byte) 1);

        //写入字节流
        try {
            ByteArrayOutputStream bodyStream = getVideoTitlePara_req.writeProtocolData();
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
