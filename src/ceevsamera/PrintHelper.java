package ceevsamera;

/**
 *
 * @author 基地 杨宸
 */

public class PrintHelper {

    /**
     * 按每行16个字节 输出字节数组
     *
     * @param data 要打印的字节数组
     * @param len 要打印的字节数组的长度 当值为0时，表示打印数组字节长度
     */
    public static void printByteArr(byte[] data, int printLen) {
        int length = printLen;
        int line = 1;
        if (printLen == 0) {
            length = data.length;
        }
        for (int i = 0; i < length; i++) {
            System.out.printf("  " + "%02x", (byte) data[i]);
            if (i % 4 == 3) {
                System.out.print("  ");
            }
            if (i % 16 == 15) {
                System.out.print("|  line:" + line + "   num:" + line * 16);
                line++;
                System.out.println();
            }
        }
        System.out.println();
    }

    /**
     * 打印设备标识码
     */
    public static void printDevSeq(byte[] data) {
        for (int i = 0; i < 12; i++) {
            System.out.print((char) data[i]);
            if (i % 2 == 1&&i!=11) {
                System.out.print(":");
            }
        }
        System.out.println();
    }
    
    public static void printDevSeqNoLn(byte[] data) {
        for (int i = 0; i < 12; i++) {
            System.out.print((char) data[i]);
            if (i % 2 == 1&&i!=11) {
                System.out.print(":");
            }
        }
    }

}
