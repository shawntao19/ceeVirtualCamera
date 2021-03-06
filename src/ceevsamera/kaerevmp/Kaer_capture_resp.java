package ceevsamera.kaerevmp;

import ceevsamera.ConvertTools;
import ceevsamera.GlobalVar;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 卡尔 企业视频监控协议 视频请求回复（实际用途为抓图请求）
 *
 * @author 基地 杨宸
 *
 */
public class Kaer_capture_resp extends Kaer_head {

    private byte[] PUID;         //4B(小端书序) 视频服务器 ID，即设备ID 号，此处注意登录成功后要重新更新设备ID号
    private byte[] ClientID;         //4B(小端书序) 客户端ID
    private byte channel;        //1B    通道号
    private byte answer;        //1B    应答信息

    /**
     * 将设置报警参数 写入数据流中
     *
     * @return 
     * @throws IOException
     */
    public ByteArrayOutputStream writeProtocolData() throws IOException {
        ByteArrayOutputStream outs = new ByteArrayOutputStream();
        outs.write((byte) p_head);
        outs.write((byte) p_type);
        outs.write(ConvertTools.longToByteArr_LittleEnd_(p_length));

        //写入登录参数
        if (PUID == null || PUID.length != 4) {
            return null;
        }
        outs.write(PUID);
        if (ClientID == null || ClientID.length != 4) {
            return null;
        }
        outs.write(ClientID);

        outs.write(channel);
        outs.write(answer);
        return outs;
    }

    public byte[] getPUID() {
        return PUID;
    }

    public void setPUID(byte[] pUID) {
        PUID = pUID;
    }

    public byte[] getClientID() {
        return ClientID;
    }

    public void setClientID(byte[] clientID) {
        ClientID = clientID;
    }

    public byte getChannel() {
        return channel;
    }

    public void setChannel(byte channel) {
        this.channel = channel;
    }

    public byte getAnswer() {
        return answer;
    }

    public void setAnswer(byte answer) {
        this.answer = answer;
    }
}
