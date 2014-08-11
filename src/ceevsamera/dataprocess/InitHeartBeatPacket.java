package ceevsamera.dataprocess;

/**
 *
 * @author 基地 杨宸
 */
import ceevsamera.GlobalVar;
import ceevsamera.kaerevmp.Kaer_heartbeat_req;
import ceevsamera.kaerevmp.ProtocolVar;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InitHeartBeatPacket {

    public static ByteArrayOutputStream init(byte[] PUID,byte[] localIP) {
        //初始化 数据包信息
        Kaer_heartbeat_req heartBeat_req = new Kaer_heartbeat_req();
        //设置数据包头
        heartBeat_req.setP_head((short) 0xff);
        heartBeat_req.setP_type((short) ProtocolVar.PacketType.HEARTBEAT_PRO);
        heartBeat_req.setP_length(ProtocolVar.PACKET_HEAD_LENGTH + 12);

        //设置PUID
        heartBeat_req.setPUID(PUID);

        //设置同步时间
        heartBeat_req.setLocalIP(localIP);
        
        heartBeat_req.setAlarmState(GlobalVar.NULL4);
        
        //写入字节流
        try {
            ByteArrayOutputStream bodyStream = heartBeat_req.writeProtocolData();
            if (bodyStream == null) {
                return null;
            }
            return bodyStream;
        } catch (IOException e) {
            Logger.getLogger(InitHeartBeatPacket.class.getName()).log(Level.INFO, null, e);
            return null;
        }
    }
}
