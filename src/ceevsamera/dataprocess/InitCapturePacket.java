package ceevsamera.dataprocess;

import ceevsamera.GlobalVar;
import ceevsamera.kaerevmp.Kaer_capture_resp;
import ceevsamera.kaerevmp.ProtocolVar;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 卡尔 企业视频监控协议 视频请求（实际用途为抓图请求）数据初始化
 *
 * @author 基地 杨宸
 *
 */
public class InitCapturePacket {

    public static ByteArrayOutputStream init(byte[] PUID) {
        //初始化 数据包信息
        Kaer_capture_resp capture_resp= new Kaer_capture_resp();
        //设置数据包头
        capture_resp.setP_head((short) 0xff);
        capture_resp.setP_type((short) ProtocolVar.PacketType.CAPTURE_PRO);
        capture_resp.setP_length(ProtocolVar.PACKET_HEAD_LENGTH + 10);

        capture_resp.setPUID(PUID);
        capture_resp.setClientID(GlobalVar.NULL4);

        capture_resp.setChannel((byte) 1);

        capture_resp.setAnswer(ProtocolVar.RESULT_TYPE.ACK);

        //写入字节流
        try {
            ByteArrayOutputStream bodyStream = capture_resp.writeProtocolData();
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
