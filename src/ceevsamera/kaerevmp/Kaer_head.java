package ceevsamera.kaerevmp;

import ceevsamera.ConvertTools;
import ceevsamera.GlobalVar;


/**
 * 卡尔 企业视频监控协议 协议模板
 * @author 基地  杨宸 
 *
 */

public class Kaer_head {
	protected short p_head=0xff;               //协议头   1字节
	protected short p_type;                    //类型 
	protected long p_length;                  //协议长度  4字节  低位在前，高位在后，长度代表整条协议的总长度(小端书序)
	
	public int parsePacket(byte[] packet){
		if(packet==null||packet.length<ProtocolVar.PACKET_HEAD_LENGTH)
			return GlobalVar.FAILED;
		//获取包头 p_head
		this.p_head=packet[0];
		//获取 类型p_type
		this.p_type=packet[1];
		//获取 数据包长度 p_length
		this.p_length=ConvertTools.byteArrToLong_littleEnd(packet,2);

		return GlobalVar.SUCCESSED;
	}
	
	public short getP_head() {
		return p_head;
	}
	public void setP_head(short p_head) {
		this.p_head = p_head;
	}
	public short getP_type() {
		return p_type;
	}
	public void setP_type(short p_type) {
		this.p_type = p_type;
	}
	public long getP_length() {
		return p_length;
	}
	public void setP_length(long p_length) {
		this.p_length = p_length;
	}
}
