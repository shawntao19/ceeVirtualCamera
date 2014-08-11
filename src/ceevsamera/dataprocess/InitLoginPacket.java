package ceevsamera.dataprocess;

import ceevsamera.ConvertTools;
import ceevsamera.EncryptTools;
import ceevsamera.GlobalVar;
import ceevsamera.PrintHelper;
import ceevsamera.kaerevmp.Kaer_Login_req;
import ceevsamera.kaerevmp.Kaer_Login_resp;
import ceevsamera.kaerevmp.ProtocolVar;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 初始化 回复登录 数据包
 *
 * @author 基地 杨宸
 *
 *
 */
public class InitLoginPacket {

    public static ByteArrayOutputStream init(byte[] PUID, byte[] MD5Key, byte[] serverIP) {
        Kaer_Login_req loginReq = new Kaer_Login_req();
        //设置数据包头
        loginReq.setP_head((short) 0xff);
        loginReq.setP_type((short) ProtocolVar.PacketType.LOGIN_PRO);
        loginReq.setP_length(ProtocolVar.PACKET_HEAD_LENGTH + 78);

        loginReq.setPUID(PUID);
        loginReq.setUserName(GlobalVar.NULL8);
        loginReq.setPwd(GlobalVar.NULL8);

        loginReq.setState((byte) 0);
        loginReq.setChannelNum((byte) 0);

        try {
            loginReq.setAuthSeq(getAuthID(GlobalVar.NULL8,MD5Key,GlobalVar.NULL8));
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(InitLoginPacket.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
        loginReq.setServerIP(serverIP);

        loginReq.setServerMask(GlobalVar.NULL4);
        loginReq.setGateway(GlobalVar.NULL4);
        loginReq.setHardwareVersion(GlobalVar.NULL4);
        loginReq.setSoftwareVersion(GlobalVar.NULL4);
        loginReq.setCodeVersion(GlobalVar.NULL4);

        loginReq.setType(GlobalVar.NULL16);

        //写入字节流
        try {
            ByteArrayOutputStream bodyStream = loginReq.writeProtocolData();
            if (bodyStream == null) {
                return null;
            }
            return bodyStream;
        } catch (IOException e) {
            Logger.getLogger(InitLoginPacket.class.getName()).log(Level.INFO, null, e);
            return null;
        }
    }
    
    private static byte[] getAuthID(byte[] username,byte[] key,byte[] pwd) throws NoSuchAlgorithmException {
        return EncryptTools.getMD5Code(username, key, pwd);
    }
}
