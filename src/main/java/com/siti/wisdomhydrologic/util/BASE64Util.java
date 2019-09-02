package com.siti.wisdomhydrologic.util;

import sun.misc.BASE64Decoder;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by zyh on 2017/11/8.
 * BASE64 加密
 */
public final class BASE64Util {

    /**
     * 采用BASE64算法对字符串进行加密
     *
     * @param string 原字符串
     * @return 加密后的字符串
     */
    public static String encryptString(String string) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(string.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) i += 256;
                if (i < 16) buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();    //   32位加密
            //return buf.toString().substring(8, 24);  // 16位的加密
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

    }
    /**
     * 字符串解密，采用BASE64的算法
     *
     * @param encoder 需要解密的字符串
     * @return 解密后的字符串
     */
    public static final String decode(String encoder) {
        if (encoder == null || "".equals(encoder))
            return null;
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] buf = decoder.decodeBuffer(encoder);
            return new String(buf);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
