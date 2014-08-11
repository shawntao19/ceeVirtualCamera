package ceevsamera.dataprocess;

/**
 *
 * @author 基地 杨宸
 */
import ceevsamera.GlobalVar;
import ceevsamera.kaerevmp.Kaer_alarmset_resp;
import ceevsamera.kaerevmp.ProtocolVar;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InitAlarmSettingPacket {

    public static ByteArrayOutputStream init(byte[] PUID) {
        //初始化 数据包信息
        Kaer_alarmset_resp alarmset_resp = new Kaer_alarmset_resp();
        //设置数据包头
        alarmset_resp.setP_head((short) 0xff);
        alarmset_resp.setP_type((short) ProtocolVar.PacketType.ALARMSET_PRO);
        alarmset_resp.setP_length(ProtocolVar.PACKET_HEAD_LENGTH + 9);

        alarmset_resp.setPUID(PUID);             //设置PUID
        alarmset_resp.setClientID(GlobalVar.NULL4);             

        alarmset_resp.setResp(ProtocolVar.RESULT_TYPE.ACK);

        //写入字节流
        try {
            ByteArrayOutputStream bodyStream = alarmset_resp.writeProtocolData();
            if (bodyStream == null) {
                return null;
            }
            return bodyStream;
        } catch (IOException e) {
            Logger.getLogger(InitAlarmSettingPacket.class.getName()).log(Level.INFO, null, e);
            return null;
        }
    }
}
