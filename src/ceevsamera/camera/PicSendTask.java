/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ceevsamera.camera;

import ceevsamera.Config;
import ceevsamera.ConvertTools;
import ceevsamera.dataprocess.InitPicDataPacket;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Administrator
 */
public class PicSendTask extends Thread {

    private final byte[] PUID;
    private final String devSeq;
    private final int tag;

    private Socket senderSocket = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;

    private byte[] picData;
    private final int MAX_PIECE_LEN = 1240;

    public static int timeFlag = 1;

    public PicSendTask(byte[] PUID, String devSeq, int tag) throws IOException {
        this.PUID = PUID;
        this.devSeq = devSeq;
        this.tag = tag;
        senderSocket = new Socket(Config.serverIP, 22615);
        in = new DataInputStream(senderSocket.getInputStream());
        out = new DataOutputStream(senderSocket.getOutputStream());
    }

    @Override
    public void run() {
        try {
            picData = getPicData(tag, getTimeFlag());
        } catch (IOException ex) {
            Logger.getLogger(PicSendTask.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        int picLen = picData.length;
        int index = 0, piece_seq = 0, pieceLen = MAX_PIECE_LEN;
        byte[] pieceData;

        while (index < picLen) {
            if (picLen - index >= MAX_PIECE_LEN) {
                pieceLen = MAX_PIECE_LEN;
            } else {
                pieceLen = picLen - index;
                piece_seq = 0xffff;
            }
            pieceData = new byte[pieceLen];
            ConvertTools.copyByteArr(picData, pieceData, index, 0, pieceLen);
            try {
                sendPicData(piece_seq, pieceData);
            } catch (IOException ex) {
                Logger.getLogger(PicSendTask.class.getName()).log(Level.SEVERE, devSeq, ex);
                break;
            }
            index += pieceLen;
            piece_seq++;
        }

        Camera.addSendPicTask(devSeq, this);
    }

    private void sendPicData(int piece_Seq, byte[] pieceData) throws IOException {
        byte[] sendPacket = InitPicDataPacket.init(PUID, piece_Seq, pieceData).toByteArray();
        out.write(sendPacket);
        out.flush();
    }

    /**
     * 获取图片数据
     *
     * @param tag
     * @param times
     * @return
     * @throws java.io.IOException times 1 - 10
     */
    public static int aaa = 1;

    public static byte[] getPicData(int tag, int times) throws IOException {
        byte[] picByteArr;
        int nameTag = tag % 100;

        String imgFileName = "samplePicture/test/" + aaa + ".jpg";
        aaa++;
//        String imgFileName = "samplePicture/samPic - 副本 (" + nameTag + ")/" + times + ".jpg";
//        String imgFileName = "/webceet/yangchenTest/samplePicture/00000-1 - 副本 (" + nameTag + ")/" + times + ".jpg";
        Logger.getLogger(PicSendTask.class.getName()).info(imgFileName);
        File imgFile = new File(imgFileName);
        if (!imgFile.exists()) {
            return null;
        } else {
            BufferedImage bu = ImageIO.read(imgFile);
            ByteArrayOutputStream imageStream = new ByteArrayOutputStream();
            try {
                ImageIO.write(bu, "jpg", imageStream);
            } catch (IOException e) {
                Logger.getLogger(PicSendTask.class.getName()).log(Level.SEVERE, null, e);
            }
            imageStream.flush();
            picByteArr = imageStream.toByteArray();
            return picByteArr;
        }
    }

    public static int getTimeFlag() {
        if (timeFlag > 10) {
            timeFlag = 1;
        } else {
            timeFlag++;
        }
        return timeFlag;
    }
}
