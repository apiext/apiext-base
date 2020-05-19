package com.apiext.base.encrypt.util;

import com.apiext.base.common.util.StringUtil;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * 密码加密工具
 *
 * @author mrzhaowy
 * @create 2020-05-19 16:32
 **/
public class PasswordUtil {

    /**
     * 随机盐的长度
     */
    private static final int SALT_LEN = 8;

    /**
     * Md5签名的长度，最终长度
     */
    private static final int MD5_LEN = 32 + SALT_LEN;

    /**
     * 使用密码产生签名
     * @param password
     * @return
     */
    public static String getEncryptPassword(String password) {
        String salt = StringUtil.getRandomStr(SALT_LEN, StringUtil.LETTER_SOURCE);
        return getEncryptPassword(password, salt);
    }

    /**
     * 校验密码是否正确
     * @param password
     * @param encryptPassword
     * @return
     */
    public static boolean verify(String password, String encryptPassword) {
        StringBuilder saltSb = new StringBuilder();
        for (int i = 0; i < SALT_LEN; i++) {
            saltSb.append(encryptPassword.charAt(saltIndexToPasswordIndex(i)));
        }
        return getEncryptPassword(password, saltSb.toString()).equals(encryptPassword);
    }

    /**
     * 密码和盐产生签名
     * @param password
     * @param salt
     * @return
     */
    private static String getEncryptPassword(String password, String salt) {
        String encryptPassword = DigestUtils.md5Hex(password + salt);
        StringBuilder encryptWithSaltPassword = new StringBuilder(encryptPassword);
        for (int i = 0; i < SALT_LEN; i++) {
            encryptWithSaltPassword.insert(saltIndexToPasswordIndex(i), salt.charAt(i));
        }
        return encryptWithSaltPassword.toString();
    }

    /**
     * 密码盐索引转密码索引
     * @param saltIndex
     * @return
     */
    private static int saltIndexToPasswordIndex(int saltIndex) {
        // MD5_LEN / SALT_LEN 主要是为了取间距
        // 例如40/8 则需要每隔5个插入一个
        // 例如48/16 则需要每隔3个插入一个
        return (saltIndex + 1) * (MD5_LEN / SALT_LEN) - 1;
    }
}
