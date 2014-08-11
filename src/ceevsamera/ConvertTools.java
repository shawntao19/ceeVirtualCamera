package ceevsamera;

/**
 * 字节流转换工具类
 *
 * @author 基地 杨宸
 *
 *
 */
public class ConvertTools {

    /**
     * long型转换为4字节字节数组
     *
     * @param value long型
     * @return byte[] 4字节数组
     */
    public static byte[] longToByteArr(long value) {
        byte[] result = new byte[4];
        result[3] = (byte) (value & 0xff);
        result[2] = (byte) (value >> 8 & 0xff);
        result[1] = (byte) (value >> 16 & 0xff);
        result[0] = (byte) (value >> 24 & 0xff);
        return result;
    }

    /**
     * long整型转换为 小端书序 字节数组
     *
     * @param value
     * @return
     */
    public static byte[] longToByteArr_LittleEnd_(long value) {
        byte[] result = new byte[4];
        result[0] = (byte) (value & 0xff);
        result[1] = (byte) (value >> 8 & 0xff);
        result[2] = (byte) (value >> 16 & 0xff);
        result[3] = (byte) (value >> 24 & 0xff);
        return result;
    }
    /*
     * 将小端字节数组转化为short型
     * */

    public static short byteArrToShort_littleEnd(byte[] byteArr) {
        if (byteArr.length != 2) {
            return 0;
        }

        short result = 0;
        result = (short) (result | byteArr[0]);
        result = (short) (result | (byteArr[1] << 8));
        return result;
    }

    /*
     * 将小端2字节数组转化为int型
     * */
    public static int byteArrToInt_littleEnd(byte[] byteArr) {
        if (byteArr.length != 2) {
            return 0;
        }
        int result = 0;
        result = (result | ((int) byteArr[0]) & 0x000000ff);
        result = (result | ((int) byteArr[1] & 0x0000ffff) << 8);
        return result;
    }

    //int型转换为4字节   字节数组
    public static byte[] intToByteArr(int value) {
        byte[] result = new byte[4];
        result[3] = (byte) (value & 0xff);
        result[2] = (byte) (value >> 8 & 0xff);
        result[1] = (byte) (value >> 16 & 0xff);
        result[0] = (byte) (value >> 24 & 0xff);
        return result;
    }

    //int型转换为2字节   字节数组
    public static byte[] intTo2ByteArr(int value) {
        byte[] result = new byte[2];
        result[1] = (byte) (value & 0xff);
        result[0] = (byte) (value >> 8 & 0xff);
        return result;
    }
    
        //int型转换为2字节   字节数组
    public static byte[] intTo2ByteArr_littleEnd(int value) {
        byte[] result = new byte[2];
        result[0] = (byte) (value & 0xff);
        result[1] = (byte) (value >> 8 & 0xff);
        return result;
    }

    /**
     * byte数组转化为int型
     *
     * @param byteArr 长度为4的字节数组
     * @return
     */
    public static int byteArrToInt(byte[] byteArr) {
        if (byteArr.length != 4) {
            return 0;
        }
        int result = 0;
        result = result | ((int) byteArr[3] & 0x000000ff);
        result = result | ((((int) byteArr[2]) << 8) & 0x0000ffff);
        result = result | ((((int) byteArr[1]) << 16) & 0x00ffffff);
        result = result | (((int) byteArr[0]) << 24);
        return result;
    }

    /**
     * byte数组转化为int型
     *
     * @param byteArr 字节数组
     * @param index 转换成Int型的字节数组的起始位置
     * @return
     */
    public static int byteArrToInt(byte[] byteArr, int index) {
        if ((index + 3) >= byteArr.length) {
            return 0;
        }
        byte[] buff = new byte[4];
        for (int i = index; i <= index + 3; i++) {
            buff[i - index] = byteArr[i];
        }

        return byteArrToInt(buff);
    }

    /**
     * byte数组转化为long 型 避免int溢出
     *
     * @param byteArr 字节数组
     * @param index 转换成Int型的字节数组的起始位置
     * @return
     */
    public static long byteArrToLong(byte[] byteArr, int index) {
        if ((index + 3) >= byteArr.length) {
            return 0;
        }
        byte[] buff = new byte[4];

        for (int i = 0; i < 4; i++) {
            buff[i] = byteArr[i + index];
        }

        long result = 0;
        result = result | ((int) buff[3] & 0x000000ff);
        result = result | ((((int) buff[2]) << 8) & 0x0000ffff);
        result = result | ((((int) buff[1]) << 16) & 0x00ffffff);
        result = result | (((int) buff[0]) << 24);
        return result;
    }

    /**
     * byte数组转化为long 型 小端书序
     *
     * @param byteArr 字节数组
     * @param index 转换成Int型的字节数组的起始位置
     * @return
     */
    public static long byteArrToLong_littleEnd(byte[] byteArr, int index) {
        if ((index + 3) >= byteArr.length) {
            return 0;
        }
        byte[] buff = new byte[4];
        for (int i = 0; i < 4; i++) {
            buff[3 - i] = byteArr[i + index];
        }
        return byteArrToLong(buff, 0);
    }

    //short型转换为2字节   字节数组
    public static byte[] shortToByteArr(short value) {
        byte[] result = new byte[2];
        result[1] = (byte) (value & 0xff);
        result[0] = (byte) (value >> 8 & 0xff);
        return result;
    }

    /**
     * 字节数组拷贝函数
     *
     * @param sourceArr
     * @param desArr
     * @param srcStart 源字节数组拷贝开始坐标
     * @param desStart 目标字节数组拷贝开始坐标
     * @param len
     * @return 0-拷贝成功 1-拷贝错误
     */
    public static int copyByteArr(byte[] sourceArr, byte[] desArr, int srcStart, int desStart, int len) {
        if (sourceArr != null && desArr != null) {
            if ((srcStart + len) <= sourceArr.length && (desStart + len) <= desArr.length) {
                for (int srci = srcStart, desi = desStart, count = 0; count < len; count++) {
                    desArr[desi + count] = sourceArr[srci + count];
                }
                return 0;
            }
        }
        return 1;
    }

    /**
     * 将字节数组转化成 字符串
     *
     * @param arr
     * @return
     */
    public static String byteArrToStr(byte[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            sb = sb.append((char) (arr[i]));
        }
        return sb.toString();
    }

    /**
     * 将设备标识码 16字节数组转换为终端表示的字符串表示形式
     *
     * @param devSeqByteArr
     * @return 终端表示的字符串表示形式 ,如："00:90：b0:1a:d4:7d"
     */
    public static String devSeqByteArr2Str(byte[] devSeqByteArr) {
        if (devSeqByteArr == null || devSeqByteArr.length != 16) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < devSeqByteArr.length; i++) {
                if (devSeqByteArr[i] != 0) {
                    sb.append((char) devSeqByteArr[i]);
                    if (i % 2 == 1) {
                        sb.append(":");
                    }
                }
            }
            sb.deleteCharAt(sb.length() - 1);
            return sb.toString();
        }
    }

    /**
     * 将设备标识码 终端表示的字符串表示形式转换为16字节数组
     *
     * @param devSeqStr 如："00:90：b0:1a:d4:7d"
     * @return
     */
    public static byte[] devSeqStr2ByteArr(String devSeqStr) {
        byte[] buff = new byte[16];
        int index = 0;
        char chBuff;
        for (int i = 0; i < devSeqStr.length(); i++) {
            chBuff = devSeqStr.charAt(i);
            if (chBuff >= '0' && chBuff <= '9' || chBuff >= 'a' && chBuff <= 'z') {
                buff[index] = (byte) chBuff;
                index++;
                continue;
            }
            if (chBuff >= 'A' && chBuff <= 'Z') {
                buff[index] = (byte) (chBuff + 32);
                index++;
                continue;
            }
        }
        return buff;
    }

    /**
     * 将字节数组的16进制IP地址转换为标准形式字符串 如："192.168.1.20"
     */
    public static String ipByteArr2Str(byte[] ipArr) {
        if (ipArr.length != 4) {
            return null;
        } else {
            StringBuilder ipStr = new StringBuilder();
            int temp;
            for (int i = 0; i < 4; i++) {
                temp = ipArr[i];
                if (temp < 0) {
                    temp += 256;
                }
                ipStr.append(String.valueOf(temp));
                if (i < 3) {
                    ipStr.append(".");
                }
            }
            return ipStr.toString();
        }
    }

    private ConvertTools() {
    }

}
