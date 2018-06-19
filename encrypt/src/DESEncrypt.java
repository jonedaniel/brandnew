import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;


/**
 * Title: DESEncrypt
 * Description:
 *
 *  数据加密算法（Data Encryption Algorithm，DEA）是一种对称加密算法，很可能是使用最广泛的密钥系统，
 *  特别是在保护金融数据的安全中，最初开发的DEA是嵌入硬件中的。通常，自动取款机（Automated Teller Machine，ATM）都使用DEA。
 *  它出自IBM的研究工作，IBM也曾对它拥有几年的专利权，但是在1983年已到期后，处于公有范围中，允许在特定条件下可以免除专利使用费而使用。
 *  DES 使用一个 56 位的密钥以及附加的 8 位奇偶校验位，产生最大 64 位的分组大小。
 *  这是一个迭代的分组密码，使用称为 Feistel 的技术，其中将加密的文本块分成两半。
 *  使用子密钥对其中一半应用循环功能，然后将输出与另一半进行“异或”运算；接着交换这两半，这一过程会继续下去，但最后一个循环不交换。
 *  DES 使用 16 个循环，使用异或，置换，代换，移位操作四种基本运算。
 *
 * @author zmhzhaomenghui@gmail.com
 * @createDate 2018/6/19
 */
public class DESEncrypt {

    /**
     * 测试方法
     * @param args
     */
    public static void main(String[] args) {
        String key = DESEncrypt.init();
        System.out.println("DES密钥:\n" + key);

        String word = "123";


        String encWord = encryptDES(word, key);

        System.out.println(word + "\n加密后：\n" + encWord);
        System.out.println(word + "\n解密后：\n" + decryptDES(encWord, key));
    }

    /**
     * 定义加密方式
     */
    private final static String KEY_DES = "DES";
    private final static String KEY_AES = "AES";    // 测试

    /**
     * 全局数组
     */
    private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    /**
     * 初始化密钥
     * @return
     */
    public static String init() {
        return init(null);
    }

    /**
     * 初始化密钥
     * @param seed 初始化参数
     * @return
     */
    public static String init(String seed) {
        SecureRandom secure = null;
        String       str    = "";
        try {
            if (null != secure) {
                // 带参数的初始化
                secure = new SecureRandom(decryptBase64(seed));
            } else {
                // 不带参数的初始化
                secure = new SecureRandom();
            }

            KeyGenerator generator = KeyGenerator.getInstance(KEY_DES);
            generator.init(secure);

            SecretKey key = generator.generateKey();
            str = encryptBase64(key.getEncoded());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 转换密钥
     * @param key 密钥的字节数组
     * @return
     */
    private static Key byteToKey(byte[] key) {
        SecretKey secretKey = null;
        try {
            DESKeySpec       dks     = new DESKeySpec(key);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_DES);
            secretKey = factory.generateSecret(dks);

            // 当使用其他对称加密算法时，如AES、Blowfish等算法时，用下述代码替换上述三行代码
//            secretKey = new SecretKeySpec(key, KEY_DES);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return secretKey;
    }

    /**
     * DES 解密
     * @param data 需要解密的字符串
     * @param key 密钥
     * @return
     */
    public static String decryptDES(String data, String key) {
        // 验证传入的字符串
        if (data==null||data.equals("")) {
            return "";
        }
        // 调用解密方法完成解密
        byte[] bytes = decryptDES(hexString2Bytes(data), key);
        // 将得到的字节数组变成字符串返回
        return new String(bytes);
    }

    /**
     * DES 解密
     * @param data 需要解密的字节数组
     * @param key 密钥
     * @return
     */
    public static byte[] decryptDES(byte[] data, String key) {
        byte[] bytes = null;
        try {
            Key    k      = byteToKey(decryptBase64(key));
            Cipher cipher = Cipher.getInstance(KEY_DES);
            cipher.init(Cipher.DECRYPT_MODE, k);
            bytes = cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }

    /**
     * DES 加密
     * @param data 需要加密的字符串
     * @param key 密钥
     * @return
     */
    public static String encryptDES(String data, String key) {
        // 验证传入的字符串
        if (data==null||data.equals("")) {
            return "";
        }
        // 调用加密方法完成加密
        byte[] bytes = encryptDES(data.getBytes(), key);
        // 将得到的字节数组变成字符串返回
        return byteArrayToHexString(bytes);
    }

    /**
     * DES 加密
     * @param data 需要加密的字节数组
     * @param key 密钥
     * @return
     */
    public static byte[] encryptDES(byte[] data, String key) {
        byte[] bytes = null;
        try {
            Key    k      = byteToKey(decryptBase64(key));
            Cipher cipher = Cipher.getInstance(KEY_DES);
            cipher.init(Cipher.ENCRYPT_MODE, k);
            bytes = cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }


    /**
     * BASE64 解密
     * @param key 需要解密的字符串
     * @return 字节数组
     * @throws Exception
     */
    public static byte[] decryptBase64(String key) throws Exception {
        return (new BASE64Decoder()).decodeBuffer(key);
    }

    /**
     * BASE64 加密
     * @param key 需要加密的字节数组
     * @return 字符串
     * @throws Exception
     */
    public static String encryptBase64(byte[] key) throws Exception {
        return (new BASE64Encoder()).encodeBuffer(key);
    }

    /**
     * 将一个字节转化成十六进制形式的字符串
     * @param b 字节数组
     * @return 字符串
     */
    private static String byteToHexString(byte b) {
        int ret = b;
        //System.out.println("ret = " + ret);
        if (ret < 0) {
            ret += 256;
        }
        int m = ret / 16;
        int n = ret % 16;
        return hexDigits[m] + hexDigits[n];
    }

    /**
     * 转换字节数组为十六进制字符串
     * @param bytes 字节数组
     * @return 十六进制字符串
     */
    private static String byteArrayToHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(byteToHexString(bytes[i]));
        }
        return sb.toString();
    }


    /**
     * 转换十六进制字符串为字节数组
     * @param hexstr 十六进制字符串
     * @return
     */
    public static byte[] hexString2Bytes(String hexstr) {
        byte[] b = new byte[hexstr.length() / 2];
        int j = 0;
        for (int i = 0; i < b.length; i++) {
            char c0 = hexstr.charAt(j++);
            char c1 = hexstr.charAt(j++);
            b[i] = (byte) ((parse(c0) << 4) | parse(c1));
        }
        return b;
    }

    /**
     * 转换字符类型数据为整型数据
     * @param c 字符
     * @return
     */
    private static int parse(char c) {
        if (c >= 'a')
            return (c - 'a' + 10) & 0x0f;
        if (c >= 'A')
            return (c - 'A' + 10) & 0x0f;
        return (c - '0') & 0x0f;
    }

}
