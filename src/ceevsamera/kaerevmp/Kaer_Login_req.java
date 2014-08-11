package ceevsamera.kaerevmp;

import ceevsamera.ConvertTools;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 卡尔 企业视频监控协议 登录
 *
 * @author 基地 杨宸
 *
 */
public class Kaer_Login_req extends Kaer_head {

    private byte[] PUID;         //4B(小端书序) 视频服务器 ID，即设备ID 号，此处注意登录成功后要重新更新设备ID号
    private byte[] userName;  //用户名   8B
    private byte[] pwd;       //口令      8B
    private byte state;       //状态  0 登录，1 注销  1B
    private byte channelNum;  //音视频通道数  1B
    private byte[] authSeq;   //认证码  用户名+随机密钥+密码md5 加密得到   16B
    private byte[] serverIP;  //视频服务器ip 4B
    private byte[] serverMask;  //视频服务器mask 4B
    private byte[] gateway;  //视频服务器gateway 4B
    private byte[] hardwareVersion;   //硬件版本 4B
    private byte[] softwareVersion;   //软件版本 4B
    private byte[] codeVersion;       //编码版本 4B 
    private byte[] type;              //型号（字符串） 16B  

    /**
     * 将回复登录信息加入数据流中
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

        //写入userName
        if (userName == null || userName.length != 8) {
            return null;
        }
        outs.write(userName);

        //写入pwd
        if (pwd == null || pwd.length != 8) {
            return null;
        }
        outs.write(pwd);

        outs.write(state);
        outs.write(channelNum);
        //写入authSeq
        if (authSeq == null || authSeq.length != 16) {
            return null;
        }
        outs.write(authSeq);
        //写入serverIP
        if (serverIP == null || serverIP.length != 4) {
            return null;
        }
        outs.write(serverIP);
        //写入serverMask
        if (serverMask == null || serverMask.length != 4) {
            return null;
        }
        outs.write(serverMask);
        //写入gateway
        if (gateway == null || gateway.length != 4) {
            return null;
        }
        outs.write(gateway);
        //写入hardwareVersion
        if (hardwareVersion == null || hardwareVersion.length != 4) {
            return null;
        }
        outs.write(hardwareVersion);
        //写入softwareVersion
        if (softwareVersion == null || softwareVersion.length != 4) {
            return null;
        }
        outs.write(softwareVersion);
        //写入codeVersion
        if (codeVersion == null || codeVersion.length != 4) {
            return null;
        }
        outs.write(codeVersion);
        //写入type
        if (type == null || type.length != 16) {
            return null;
        }
        outs.write(type);
        return outs;
    }

    public byte[] getPUID() {
        return PUID;
    }

    public void setPUID(byte[] pUID) {
        PUID = pUID;
    }

    public byte[] getUserName() {
        return userName;
    }

    public void setUserName(byte[] userName) {
        this.userName = userName;
    }

    public byte[] getPwd() {
        return pwd;
    }

    public void setPwd(byte[] pwd) {
        this.pwd = pwd;
    }

    public byte getState() {
        return state;
    }

    public void setState(byte state) {
        this.state = state;
    }

    public byte getChannelNum() {
        return channelNum;
    }

    public void setChannelNum(byte channelNum) {
        this.channelNum = channelNum;
    }

    public byte[] getAuthSeq() {
        return authSeq;
    }

    public void setAuthSeq(byte[] authSeq) {
        this.authSeq = authSeq;
    }

    public byte[] getServerIP() {
        return serverIP;
    }

    public void setServerIP(byte[] serverIP) {
        this.serverIP = serverIP;
    }

    public byte[] getServerMask() {
        return serverMask;
    }

    public void setServerMask(byte[] serverMask) {
        this.serverMask = serverMask;
    }

    public byte[] getGateway() {
        return gateway;
    }

    public void setGateway(byte[] gateway) {
        this.gateway = gateway;
    }

    public byte[] getHardwareVersion() {
        return hardwareVersion;
    }

    public void setHardwareVersion(byte[] hardwareVersion) {
        this.hardwareVersion = hardwareVersion;
    }

    public byte[] getSoftwareVersion() {
        return softwareVersion;
    }

    public void setSoftwareVersion(byte[] softwareVersion) {
        this.softwareVersion = softwareVersion;
    }

    public byte[] getCodeVersion() {
        return codeVersion;
    }

    public void setCodeVersion(byte[] codeVersion) {
        this.codeVersion = codeVersion;
    }

    public byte[] getType() {
        return type;
    }

    public void setType(byte[] type) {
        this.type = type;
    }
}
