package org.qum.iotdataprocessingsystem.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 密码加密
 */
public class PasswordUtil {

    private static final String SALT = "qum@13928_ABC"; // 加盐处理

    /**
     * 使用SHA-256加密密码
     *
     * @param password 需要加密的密码
     * @return 加密后的密码字符串
     */
    public static String hashPassword(String password) {
        try {
            // 创建SHA-256的MessageDigest实例
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // 可选：在密码前添加盐值来增强加密强度
            password = SALT + password;

            // 对输入的密码进行加密
            byte[] encodedHash = digest.digest(password.getBytes());

            // 将加密后的字节数组转换为16进制字符串
            return bytesToHex(encodedHash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    /**
     * 将字节数组转换为16进制字符串
     *
     * @param hash 字节数组
     * @return 16进制字符串
     */
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        String password = SALT + "123";
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(password.getBytes());
        System.out.println(bytesToHex(encodedHash));
    }
}

