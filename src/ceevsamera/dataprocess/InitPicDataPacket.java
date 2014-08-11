/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ceevsamera.dataprocess;

import ceevsamera.ConvertTools;
import ceevsamera.GlobalVar;
import ceevsamera.kaerevmp.Kaer_pictureData_req;
import ceevsamera.kaerevmp.ProtocolVar;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class InitPicDataPacket {
    public static ByteArrayOutputStream init(byte[] PUID,int piece_seq,byte[] data) {
        //初始化 数据包信息
        Kaer_pictureData_req pictureData_req = new Kaer_pictureData_req();
        //设置数据包头
        pictureData_req.setP_head((short) 0xff);
        pictureData_req.setP_type((short) ProtocolVar.PacketType.UPLOADPIC_PRO);
        int picLen=data.length;
        pictureData_req.setP_length(25+picLen);
        
        pictureData_req.setPUID(PUID);
        pictureData_req.setVideoOutput_channel((byte)1);
        pictureData_req.setFrame_seq((byte)0x0b);
        pictureData_req.setPiece_seq(piece_seq);
        pictureData_req.setTimeStamp_s(GlobalVar.NULL4);
        pictureData_req.setTimeStamp_ms((byte)0);
        pictureData_req.setImg_size((byte)0);
        pictureData_req.setImg_length(ConvertTools.longToByteArr_LittleEnd_(picLen));
        
        pictureData_req.setData(data);
        
        //写入字节流
        try {
            ByteArrayOutputStream bodyStream = pictureData_req.writeProtocolData();
            if (bodyStream == null) {
                return null;
            }
            return bodyStream;
        } catch (IOException e) {
            Logger.getLogger(InitReqSecretKeyPacket.class.getName()).
                    log(Level.INFO, String.valueOf(ConvertTools.byteArrToInt(PUID)), e);
            return null;
        }
    }
}
