package com.lyl.yukon.common.util.upms;

/**
 * 旧版jessite密码验证
 *
 * @Author ligj
 */
public class PwdUtils {

    /**
     * 生成安全的密码，生成随机的16位salt并经过1024次 sha-1 hash
     */
    private static final int HASH_INTERATIONS = 1024;
    private static final int SALT_SIZE = 8;

    /**
     * 获取加密过后的密文
     *
     * @param plainPassword 明文
     */
    public static String entryptPassword(String plainPassword) {
        String plain = Encodes.unescapeHtml(plainPassword);
        byte[] salt = Digests.generateSalt(SALT_SIZE);
        byte[] hashPassword = Digests.sha1(plain.getBytes(), salt, HASH_INTERATIONS);
        return Encodes.encodeHex(salt) + Encodes.encodeHex(hashPassword);
    }

    /**
     * 验证密码
     *
     * @param plainPassword 明文
     * @param password      密文
     */
    public static boolean validatePassword(String plainPassword, String password) {
        String plain = Encodes.unescapeHtml(plainPassword);
        byte[] salt = Encodes.decodeHex(password.substring(0, 16));
        byte[] hashPassword = Digests.sha1(plain.getBytes(), salt, HASH_INTERATIONS);
        return password.equals(Encodes.encodeHex(salt) + Encodes.encodeHex(hashPassword));
    }

}
