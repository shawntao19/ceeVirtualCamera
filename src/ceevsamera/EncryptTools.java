package ceevsamera;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * 加解密 工具类
 * @author 基地  杨宸 
 *
 **/
public class EncryptTools {
	/**
	 * 8字节 MD5随机密码 生成函数
	 */
	public static byte[] getMD5Key(){
		try {
			KeyGenerator keyGenerator=KeyGenerator.getInstance("DES");
			keyGenerator.init(56);
			SecretKey key = keyGenerator.generateKey();
			return key.getEncoded();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取 认证密码 [用户名+随机密钥+密码] md5 加密得到
	 * @param userName
	 * @param key
	 * @param pwd
	 * @return
	 * @throws NoSuchAlgorithmException 
	 */
	public static byte[] getMD5Code(byte[] userName,byte[] key,byte[] pwd) throws NoSuchAlgorithmException{
		byte[] buf=new byte[24];   
		ConvertTools.copyByteArr(userName, buf, 0, 0, 8);
		ConvertTools.copyByteArr(key, buf, 0, 8, 8);
		ConvertTools.copyByteArr(pwd, buf, 0, 16, 8);
		MessageDigest md = MessageDigest.getInstance("MD5"); 
		md.update(buf);
		byte[] result_md5=md.digest();
		return result_md5;
	}
}
