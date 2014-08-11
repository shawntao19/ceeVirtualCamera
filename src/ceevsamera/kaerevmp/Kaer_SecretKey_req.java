package ceevsamera.kaerevmp;

import ceevsamera.ConvertTools;
import ceevsamera.GlobalVar;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 卡尔 企业视频监控协议 申请密钥消息
 *
 * @author 基地 杨宸
 *
 */
public class Kaer_SecretKey_req extends Kaer_head {

    private byte[] PUID;         //4 字节(小端书序) 视频服务器 ID，即设备ID 号，此处注意登录成功后要重新更新设备ID号
    private byte[] devSeq;     //设备表示码  16B
    private byte[] manufacId={0,0,0,0};  //厂商ID    (4B)

    public ByteArrayOutputStream writeToByteArr() throws IOException {
        ByteArrayOutputStream outs = new ByteArrayOutputStream();
        outs.write((byte) p_head);
        outs.write((byte) p_type);
        outs.write(ConvertTools.longToByteArr_LittleEnd_(p_length));
        //写入登录参数
        if (PUID == null || PUID.length != 4) {
            return null;
        }
        outs.write(PUID);
        if (devSeq == null || devSeq.length != 16) {
            return null;
        }
        outs.write(devSeq);
        if (manufacId == null || manufacId.length != 4) {
            return null;
        }
        outs.write(manufacId);
        return outs;
    }

    public byte[] getDevSeq() {
        return devSeq;
    }

    public void setDevSeq(byte[] devSeq) {
        this.devSeq = devSeq;
    }

    public byte[] getPUID() {
        return PUID;
    }

    public void setPUID(byte[] pUID) {
        PUID = pUID;
    }

    public byte[] getManufacId() {
        return manufacId;
    }

    public void setManufacId(byte[] manufacId) {
        this.manufacId = manufacId;
    }

}
