package ceevsamera.dataprocess;

/**
 *
 * @author 基地 杨宸
 */
import ceevsamera.GlobalVar;
import ceevsamera.kaerevmp.Kaer_alarmEvent_req;
import ceevsamera.kaerevmp.ProtocolVar;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InitAlarmEventPacket {

    public static ByteArrayOutputStream init(byte[] PUID, byte state) {
        //初始化 数据包信息
        Kaer_alarmEvent_req alarmEvent_req = new Kaer_alarmEvent_req();
        //设置数据包头
        alarmEvent_req.setP_head((short) 0xff);
        alarmEvent_req.setP_type((short) ProtocolVar.PacketType.ALARMEVENT_PRO);
        alarmEvent_req.setP_length(ProtocolVar.PACKET_HEAD_LENGTH + 12);

        alarmEvent_req.setPUID(PUID);
        alarmEvent_req.setChannel((byte) 0x01);    //通道号默认为1

        alarmEvent_req.setState(state);     //状态 0x00

        alarmEvent_req.setTimeStamp(GlobalVar.NULL5);

        alarmEvent_req.setSequence((byte) 0x00);    //序号ox00

        //写入字节流
        try {
            ByteArrayOutputStream bodyStream = alarmEvent_req.writeProtocolData();
            if (bodyStream == null) {
                return null;
            }
            return bodyStream;
        } catch (IOException e) {
            Logger.getLogger(InitAlarmEventPacket.class.getName()).log(Level.INFO, null, e);
            return null;
        }
    }
}
