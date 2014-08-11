package ceevsamera.kaerevmp;

import ceevsamera.ConvertTools;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 卡尔 企业视频监控协议 抓拍图像视频流传输格式
 *
 * @author 基地 杨宸
 *
 */
public class Kaer_pictureData_req extends Kaer_head {

    private byte[] PUID;   //4B  视频服务器ID
    private byte videoOutput_channel;  //1B视频输出通道
    private int frame_seq;  //  2B 帧序号   
    private int piece_seq;  //2B 片序号
    private byte[] timeStamp_s;  //4B  时间戳(s)
    private byte timeStamp_ms;  //1B  时间戳(ms)
    private byte img_size;      //1B  图像大小
    private byte[] img_length;      //4B  长度

    private byte[] data;    //图片数据

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
        outs.write(videoOutput_channel);
        outs.write(ConvertTools.intTo2ByteArr_littleEnd(frame_seq));
        outs.write(ConvertTools.intTo2ByteArr_littleEnd(piece_seq));
        outs.write(timeStamp_s);
        outs.write(timeStamp_ms);
        outs.write(img_size);
        outs.write(img_length);
       outs.write(data);
        return outs;
    }

    public byte[] getPUID() {
        return PUID;
    }

    public void setPUID(byte[] pUID) {
        PUID = pUID;
    }

    public byte getVideoOutput_channel() {
        return videoOutput_channel;
    }

    public void setVideoOutput_channel(byte videoOutput_channel) {
        this.videoOutput_channel = videoOutput_channel;
    }

    public byte getTimeStamp_ms() {
        return timeStamp_ms;
    }

    public void setTimeStamp_ms(byte timeStamp_ms) {
        this.timeStamp_ms = timeStamp_ms;
    }

    public byte getImg_size() {
        return img_size;
    }

    public void setImg_size(byte img_size) {
        this.img_size = img_size;
    }

    public byte[] getImg_length() {
        return img_length;
    }

    public void setImg_length(byte[] img_length) {
        this.img_length = img_length;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getFrame_seq() {
        return frame_seq;
    }

    public void setFrame_seq(int frame_seq) {
        this.frame_seq = frame_seq;
    }

    public int getPiece_seq() {
        return piece_seq;
    }

    public void setPiece_seq(int piece_seq) {
        this.piece_seq = piece_seq;
    }

    public byte[] getTimeStamp_s() {
        return timeStamp_s;
    }

    public void setTimeStamp_s(byte[] timeStamp_s) {
        this.timeStamp_s = timeStamp_s;
    }
}
