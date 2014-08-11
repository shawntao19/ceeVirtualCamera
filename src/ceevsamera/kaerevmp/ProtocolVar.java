package ceevsamera.kaerevmp;

/**
 * @author 基地 杨宸
 *
 */
import ceevsamera.ConvertTools;

public class ProtocolVar {

    /**
     * 传输字节数组的最大长度
     */
    public static final int PACKET_MAX_LENGTH = 2048;

    /**
     * 数据包包头长度 单位：字节
     */
    public static final int PACKET_HEAD_LENGTH = 6;

    public static final byte ALARM_ON = 0;    //输入报警设置的开关
    public static final byte ALARM_OFF = 1;

    public static final byte[] END_FLAGBYTEARR = {(byte) 0xff, (byte) 0xff};
    public static final int PIC_ENDFLAG = ConvertTools.byteArrToInt_littleEnd(END_FLAGBYTEARR);

    public static final byte ALARMEVENT_START = (byte) 0X00;
    public static final byte ALARMEVENT_END = (byte) 0X01;

    public class PacketType {

        public static final byte PACKET_HEAD = (byte) 0XFF;       //消息包头标识符
        public static final byte SCRETKEY_PRO = (byte) 0XD0;      //申请密钥
        public static final byte LOGIN_PRO = 0X44;         //登录
        public static final byte HEARTBEAT_PRO = 0X03;         //心跳
        public static final byte ALARMSET_PRO = 0X40;         //设置某一服务器输入警报
        public static final byte CAPTURE_PRO = 0X0C;         //视频请求
        public static final byte ALARMTIME_PRO = 0X6C;         //布放时间
        public static final byte ALARMEVENT_PRO = 0X0F;         //报警事件上传
        public static final byte UPLOADPIC_PRO = (byte) 0XFA;         //图片上传上传
        public static final byte NO_NOTLOGIN = (byte) 0X06;         //没有登录
        public static final byte USER_NOPURVIEW = (byte) 0X07;         //用户没有权限 
        public static final byte SET_VIDEOCODEPARA = (byte) 0X3B;         //设置某一视频通道的视频编码参数
        public static final byte GET_VIDEOCODEPARA = (byte) 0X3E;         //获取某一视频通道的视频编码参数
        public static final byte SET_VIDEOTITLEPARA = (byte) 0X42;         //设置某一视频通道的标题
        public static final byte GET_VIDEOTITLEPARA = (byte) 0X6F;         //获取某一视频通道的标题
        public static final byte SET_DEVICE_DATE = (byte) 0X4C;         //设置编码器日期
        public static final byte GET_DEVICE_DATE = (byte) 0X62;         //获取编码器日期
        public static final byte SET_DEVICE_TIME = (byte) 0X4D;         //设置编码器时间
        public static final byte GET_DEVICE_TIME = (byte) 0X63;         //获取编码器时间
        public static final byte UPLOADVIDEO = (byte) 0X39;         //视频流传输
    }

    public class RESULT_TYPE {

        public static final byte ACK = 0X0D;       //成功
        public static final byte NAK = 0X05;       //失败
    }

    public class IMAGE_TYPE {

        public static final byte D1 = 0X00;
        public static final byte CIF = 0X02;
        public static final byte P720 = 0X0A;
    }
}
