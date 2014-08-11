package ceevsamera.kaerevmp;

import ceevsamera.ConvertTools;
import ceevsamera.GlobalVar;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 卡尔 企业视频监控协议 上传传感器报警事件 指令类型 0x0f
 *
 * @author 基地 杨宸
 *
 */
public class Kaer_alarmEvent_req extends Kaer_head {

    private byte[] PUID;         //4B(小端书序) 视频服务器 ID，即设备ID 号，此处注意登录成功后要重新更新设备ID号
    private byte channel;        //1B  通道号
    private byte state;          //1B状态  0开始   1结束
    private byte[] timeStamp;    //5B时间戳 
    private byte sequence;       //1B序号

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

        outs.write(channel);
        outs.write(state);
        
        //写入登录参数
        if (timeStamp == null || timeStamp.length != 5) {
            return null;
        }
        outs.write(timeStamp);
        
        outs.write(sequence);
        return outs;
    }
    
    public byte[] getPUID() {
        return PUID;
    }

    public void setPUID(byte[] pUID) {
        PUID = pUID;
    }

    public byte getChannel() {
        return channel;
    }

    public void setChannel(byte channel) {
        this.channel = channel;
    }

    public byte getState() {
        return state;
    }

    public void setState(byte state) {
        this.state = state;
    }

    public byte[] getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(byte[] timeStamp) {
        this.timeStamp = timeStamp;
    }

    public byte getSequence() {
        return sequence;
    }

    public void setSequence(byte sequence) {
        this.sequence = sequence;
    }

}
