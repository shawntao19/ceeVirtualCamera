package ceevsamera.dataprocess;

/**
 *
 * @author 基地 杨宸
 */
import ceevsamera.GlobalVar;
import ceevsamera.kaerevmp.Kaer_alarmTime_resp;
import ceevsamera.kaerevmp.ProtocolVar;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InitAlarmTimePacket {
    
    public static ByteArrayOutputStream init(byte[] PUID) {
        //初始化 数据包信息
        Kaer_alarmTime_resp alarmTime_resp = new Kaer_alarmTime_resp();
        //设置数据包头
        alarmTime_resp.setP_head((short) 0xff);
        alarmTime_resp.setP_type((short) ProtocolVar.PacketType.ALARMTIME_PRO);
        alarmTime_resp.setP_length(ProtocolVar.PACKET_HEAD_LENGTH + 11);
        
        alarmTime_resp.setPUID(PUID);
        alarmTime_resp.setClientID(GlobalVar.NULL4);
        
        alarmTime_resp.setChannel((byte) 1);    //通道号默认为1
        alarmTime_resp.setAnswer(ProtocolVar.RESULT_TYPE.ACK);
        alarmTime_resp.setType((byte) 0);    ///类型   为0 表示输入开关量

        //写入字节流
        try {
            ByteArrayOutputStream bodyStream = alarmTime_resp.writeProtocolData();
            if (bodyStream == null) {
                return null;
            }
            return bodyStream;
        } catch (IOException e) {
            Logger.getLogger(InitAlarmTimePacket.class.getName()).log(Level.INFO, null, e);
            return null;
        }
    }
}
