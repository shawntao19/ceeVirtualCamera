package ceevsamera.dataprocess;

import ceevsamera.ConvertTools;
import ceevsamera.GlobalVar;
import ceevsamera.kaerevmp.Kaer_SecretKey_req;
import ceevsamera.kaerevmp.ProtocolVar;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 初始化 回复密钥 数据包
 *
 * @author 基地 杨宸
 *
 *
 */
public class InitReqSecretKeyPacket {

    public static ByteArrayOutputStream init(byte[] devSeq) {
        //初始化 数据包信息
        Kaer_SecretKey_req secretKey_req = new Kaer_SecretKey_req();
        //设置数据包头
        secretKey_req.setP_head((short) 0xff);
        secretKey_req.setP_type((short) ProtocolVar.PacketType.SCRETKEY_PRO);
        secretKey_req.setP_length(ProtocolVar.PACKET_HEAD_LENGTH + 24);
        
        secretKey_req.setPUID(GlobalVar.NULL4);

        secretKey_req.setDevSeq(devSeq);
        secretKey_req.setManufacId(GlobalVar.NULL4);
        
        //写入字节流
        try {
            ByteArrayOutputStream bodyStream = secretKey_req.writeToByteArr();
            if (bodyStream == null) {
                return null;
            }
            return bodyStream;
        } catch (IOException e) {
            Logger.getLogger(InitReqSecretKeyPacket.class.getName()).log(Level.INFO, ConvertTools.devSeqByteArr2Str(devSeq), e);
            return null;
        }
    }
}
