import org.apache.commons.net.util.Base64;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Title: HashEncrypt
 * Description:
 *
 *  密码杂凑函数（有时称作消息摘要函数，杂凑函数又称散列函数或哈希函数）不一定使用到钥匙，
 *  但和许多重要的密码算法相关。它将输入资料（通常是一整份文件）输出成较短的固定长度杂凑值，
 *  这个过程是单向的，逆向操作难以完成，而且碰撞（两个不同的输入产生相同的杂凑值）发生的机率非常小。
 *
 * @author zmhzhaomenghui@gmail.com
 * @createDate 2018/6/19
 */
public class HashEncrypt {
    public static final String PLAIN_TEXT = "ASS HOLE";
    public static final String MD_ALGORITHM = "MD5";
    public static final String SHA_ALGORITHM = "SHA-512";
    public static final String MAC_ALGORITHM = "HmacSHA512";

    public static void main(String[] args) {
        System.out.println("原字符串:"+PLAIN_TEXT);
        System.out.println("MD5: " + MD5(PLAIN_TEXT.getBytes()));
        System.out.println("SHA-512: " + SHA(PLAIN_TEXT.getBytes()));
        System.out.println("HmacSHA512：" + MAC(PLAIN_TEXT.getBytes()));
    }

    /**
     * 1.消息摘要算法，MD家族，有MD2 MD4 MD5，其中MD4 JDK不支持
     *
     * @param plainText
     * @return
     */
    public static String MD5(byte[] plainText) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance(MD_ALGORITHM);
            return Base64.encodeBase64String(messageDigest.digest(plainText));
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 2.SHA Security Hash Algorithm 安全散列算法，固定长度摘要信息 SHA-1 SHA-2( SHA-224
     * SHA-256 SHA-384 SHA-512) 使用的依然是MessageDigest类，JDK不支持224
     *
     * @param plainText
     * @return
     */
    public static String SHA(byte[] plainText) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance(SHA_ALGORITHM);
            return Base64.encodeBase64String(messageDigest.digest(plainText));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 3.MAC(Message Authentication Code) 消息认证码算法，是含有密钥散列函数算法。
     * 兼容了MD和SHA的特性。
     * 加密过程三步走，与后面要介绍的对称加密和非对称加密是相似的
     * 1) 传入算法，实例化一个加密器
     * 2) 传入密钥，初始化加密器
     * 3) 调用doFinal方法进行加密
     * @param plainText
     * @return
     */
    public static String MAC(byte[] plainText) {

        try {
            byte[]    secretBytes = generatorMACSecretKey();
            SecretKey key         = restoreMACSecretKey(secretBytes);
            Mac       mac         = Mac.getInstance(MAC_ALGORITHM);
            mac.init(key);
            return Base64.encodeBase64String(mac.doFinal(plainText));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;

    }

    /**
     * MAC生成随机密钥 两步走 1.创建一个KeyGenerator 2.调用KeyGenerator.generateKey方法
     *
     * @return
     */
    public static byte[] generatorMACSecretKey() {
        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance(MAC_ALGORITHM);
            SecretKey key = keyGenerator.generateKey();
            return key.getEncoded();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 还原密钥
     *
     * @param secretBytes
     * @return
     */
    public static SecretKey restoreMACSecretKey(byte[] secretBytes) {
        SecretKey key = new SecretKeySpec(secretBytes, MAC_ALGORITHM);
        return key;
    }
}