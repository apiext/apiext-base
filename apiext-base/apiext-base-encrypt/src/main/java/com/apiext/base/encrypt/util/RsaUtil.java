package com.apiext.base.encrypt.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

/**
 * RSA安全编码组件
 *
 * @author zhaoweiyong
 * @create 2020-5-19
 */
public abstract class RsaUtil {

    /**
     * 非对称加密密钥算法
     */
    public static final String KEY_ALGORITHM = "RSA";

    /**
     * 公钥
     */
    private static final String PUBLIC_KEY = "RSAPublicKey";

    /**
     * 私钥
     */
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * 数字签名
     * 签名/验证算法
     */
    public static final String SHA1_WITH_RSA_ALGORITHM = "SHA1withRSA";
    public static final String SHA256_WITH_RSA_ALGORITHM = "SHA256WithRSA";


    /**
     * RSA密钥长度 默认1024位， 密钥长度必须是64的倍数， 范围在512至65536位之间。
     */
    private static final int KEY_SIZE = 2048;

    /**
     * 从文件中输入流中加载密钥字符串
     *
     * @param path openssl生成的密钥路径
     * @throws Exception 加载密钥时产生的异常
     */
    public static String loadKeyByFile(String path) throws Exception {
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String readLine = null;
            StringBuilder sb = new StringBuilder();
            while ((readLine = br.readLine()) != null) {
                if (readLine.charAt(0) == '-') {
                    continue;
                } else {
                    sb.append(readLine);
                    sb.append('\r');
                }
            }
            br.close();
            return sb.toString();
        } catch (IOException e) {
            throw new Exception("密钥数据流读取错误");
        } catch (NullPointerException e) {
            throw new Exception("密钥输入流为空");
        }
    }

    /**
     * 加载私钥和公钥（pkcs8）
     *
     * @param privateKeyStr
     * @param publicKeyStr
     * @return
     * @throws Exception
     * @title: loadPublicKeyAndPrivateKey
     * @Author: zhaoweiyong
     * @Date: 2020年5月18日下午4:24:11
     */
    public static Map<String, Object> loadPublicKeyAndPrivateKey(String privateKeyStr, String publicKeyStr)
            throws Exception {
        // 封装密钥
        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        // RSAPublicKey
        keyMap.put(PUBLIC_KEY, loadPublicKeyByStr(publicKeyStr));
        // RSAPrivateKey
        keyMap.put(PRIVATE_KEY, loadPrivateKeyByStr(privateKeyStr));
        return keyMap;
    }

    /**
     * 从字符串中加载公钥（pkcs8）
     *
     * @param publicKeyStr 公钥数据字符串
     * @throws Exception 加载公钥时产生的异常
     */
    private static RSAPublicKey loadPublicKeyByStr(String publicKeyStr) throws Exception {
        try {
            byte[] buffer = Base64.decodeBase64(publicKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("公钥非法");
        } catch (NullPointerException e) {
            throw new Exception("公钥数据为空");
        }
    }

    /**
     * 从字符串加载私钥（pkcs8）
     *
     * @param privateKeyStr
     * @return
     * @throws Exception
     * @title: loadPrivateKeyByStr
     * @Author: zhaoweiyong
     * @Date: 2020年5月18日下午4:12:00
     */
    private static RSAPrivateKey loadPrivateKeyByStr(String privateKeyStr) throws Exception {
        try {
            byte[] buffer = Base64.decodeBase64(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("私钥非法");
        } catch (NullPointerException e) {
            throw new Exception("私钥数据为空");
        }
    }

    /**
     * 私钥解密
     *
     * @param data 待解密数据
     * @param key  私钥
     * @return byte[] 解密数据
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] data, byte[] key) throws Exception {
        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        // 生成私钥
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // 对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    public static byte[] decryptByPrivateKey(String data, byte[] key) throws Exception {
        return decryptByPrivateKey(Base64.decodeBase64(data), key);
    }

    public static byte[] decryptByPrivateKey(byte[] data, String key) throws Exception {
        return decryptByPrivateKey(data, Base64.decodeBase64(key));
    }

    public static byte[] decryptByPrivateKey(String data, String key) throws Exception {
        return decryptByPrivateKey(Base64.decodeBase64(data), Base64.decodeBase64(key));
    }

    public static String decryptToStrByPrivateKey(byte[] data, byte[] key) throws Exception {
        return new String(decryptByPrivateKey(data, key), Charset.forName("UTF-8"));
    }

    public static String decryptToStrByPrivateKey(String data, byte[] key) throws Exception {
        return new String(decryptByPrivateKey(data, key), Charset.forName("UTF-8"));
    }

    public static String decryptToStrByPrivateKey(byte[] data, String key) throws Exception {
        return new String(decryptByPrivateKey(data, key), Charset.forName("UTF-8"));
    }

    public static String decryptToStrByPrivateKey(String data, String key) throws Exception {
        return new String(decryptByPrivateKey(data, key), Charset.forName("UTF-8"));
    }

    /**
     * 公钥解密
     *
     * @param data 待解密数据
     * @param key  公钥
     * @return byte[] 解密数据
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] data, byte[] key) throws Exception {
        // 取得公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        // 生成公钥
        PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);
        // 对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    public static byte[] decryptByPublicKey(String data, byte[] key) throws Exception {
        return decryptByPublicKey(Base64.decodeBase64(data), key);
    }

    public static byte[] decryptByPublicKey(byte[] data, String key) throws Exception {
        return decryptByPublicKey(data, Base64.decodeBase64(key));
    }

    public static byte[] decryptByPublicKey(String data, String key) throws Exception {
        return decryptByPublicKey(Base64.decodeBase64(data), Base64.decodeBase64(key));
    }

    public static String decryptToStrByPublicKey(String data, String key) throws Exception {
        return new String(decryptByPublicKey(data, key), Charset.forName("UTF-8"));
    }

    public static String decryptToStrByPublicKey(String data, byte[] key) throws Exception {
        return new String(decryptByPublicKey(data, key), Charset.forName("UTF-8"));
    }

    public static String decryptToStrByPublicKey(byte[] data, String key) throws Exception {
        return new String(decryptByPublicKey(data, key), Charset.forName("UTF-8"));
    }

    public static String decryptToStrByPublicKey(byte[] data, byte[] key) throws Exception {
        return new String(decryptByPublicKey(data, key), Charset.forName("UTF-8"));
    }

    // 以下方法是公钥加密相关

    /**
     * 公钥加密
     *
     * @param data 待加密数据
     * @param key  公钥
     * @return byte[] 加密数据
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, byte[] key) throws Exception {
        // 取得公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    public static byte[] encryptByPublicKey(String data, String key) throws Exception {
        return encryptByPublicKey(data.getBytes(Charset.forName("UTF-8")), Base64.decodeBase64(key));
    }

    public static byte[] encryptByPublicKey(String data, byte[] key) throws Exception {
        return encryptByPublicKey(data.getBytes(Charset.forName("UTF-8")), key);
    }

    public static byte[] encryptByPublicKey(byte[] data, String key) throws Exception {
        return encryptByPublicKey(data, Base64.decodeBase64(key));
    }

    public static String encryptToStrByPublicKey(byte[] data, byte[] key) throws Exception {
        return Base64.encodeBase64String(encryptByPublicKey(data, key));
    }

    public static String encryptToStrByPublicKey(String data, byte[] key) throws Exception {
        return Base64.encodeBase64String(encryptByPublicKey(data, key));
    }

    public static String encryptToStrByPublicKey(byte[] data, String key) throws Exception {
        return Base64.encodeBase64String(encryptByPublicKey(data, key));
    }

    public static String encryptToStrByPublicKey(String data, String key) throws Exception {
        return Base64.encodeBase64String(encryptByPublicKey(data, key));
    }

    // 以下方法是私钥加密相关

    /**
     * 私钥加密, 原文是字节数组, 私钥是字节数组， 原文字节数组是 str.getBytes("UTF-8")取得的,
     * 私钥的字节数组可能通过Base64.encode(keyBytesArray)变成密钥字符串
     *
     * @param data 待加密数据
     * @param key  私钥
     * @return byte[] 加密数据
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, byte[] key) throws Exception {
        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        // 生成私钥
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    public static byte[] encryptByPrivateKey(String data, byte[] key) throws Exception {
        return encryptByPrivateKey(data.getBytes(Charset.forName("UTF-8")), key);
    }

    public static byte[] encryptByPrivateKey(String data, String key) throws Exception {
        return encryptByPrivateKey(data.getBytes(Charset.forName("UTF-8")), Base64.decodeBase64(key));
    }

    public static byte[] encryptByPrivateKey(byte[] data, String key) throws Exception {
        return encryptByPrivateKey(data, Base64.decodeBase64(key));
    }

    public static String encryptToStrByPrivateKey(byte[] data, byte[] key) throws Exception {
        return Base64.encodeBase64String(encryptByPrivateKey(data, key));
    }

    public static String encryptToStrByPrivateKey(byte[] data, String key) throws Exception {
        return Base64.encodeBase64String(encryptByPrivateKey(data, key));
    }

    public static String encryptToStrByPrivateKey(String data, byte[] key) throws Exception {
        return Base64.encodeBase64String(encryptByPrivateKey(data, key));
    }

    public static String encryptToStrByPrivateKey(String data, String key) throws Exception {
        return Base64.encodeBase64String(encryptByPrivateKey(data, key));
    }


    // pkcs1 转 pkcs8
    public static byte[] convertPrivateKeyPkcs1ToPkcs8(byte[] key) throws Exception {
        //PKCSObjectIdentifiers.pkcs8ShroudedKeyBag
        AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(PKCSObjectIdentifiers.pkcs8ShroudedKeyBag);
        ASN1Encodable asn1Object = ASN1ObjectIdentifier.fromByteArray(key);
        PrivateKeyInfo privKeyInfo = new PrivateKeyInfo(algorithmIdentifier, asn1Object);
        byte[] pkcs8Bytes = privKeyInfo.getEncoded();
        return pkcs8Bytes;
    }

    public static String convertPrivateKeyPkcs1ToPkcs8(String key) throws Exception {
        return Base64.encodeBase64String(convertPrivateKeyPkcs1ToPkcs8(Base64.decodeBase64(key)));
    }

    public static byte[] convertPublicKeyPkcs1ToPkcs8(byte[] key) throws Exception {
        //PKCSObjectIdentifiers.pkcs8ShroudedKeyBag
        AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(PKCSObjectIdentifiers.pkcs8ShroudedKeyBag);
        ASN1Encodable asn1Object = ASN1ObjectIdentifier.fromByteArray(key);
        SubjectPublicKeyInfo publicKeyInfo = new SubjectPublicKeyInfo(algorithmIdentifier, asn1Object);
        byte[] pkcs8Bytes = publicKeyInfo.getEncoded();
        return pkcs8Bytes;
    }

    public static String convertPublicKeyPkcs1ToPkcs8(String key) throws Exception {
        return Base64.encodeBase64String(convertPublicKeyPkcs1ToPkcs8(Base64.decodeBase64(key)));
    }

    // 该方法之后的方法帮我们能够写入类似openssl的pem
    // 我们需要测试一下是否有问题,
    // openssl rsautl -encrypt -in 1.pre.txt -inkey public.pkcs8.txt -pubin -out
    // hello.en
    // openssl rsautl -decrypt -in hello.en -inkey private.pkcs8.txt -out hello.de
    // 我们公钥私钥都是pkcs8是没有问题的
    // 我们的目的是让私钥pkcs1，公钥pkcs1试验下
    // 所以pkcs8转pkcs1的方法排上用场了
    // 测试上面的命令公钥是必须pkcs8，私钥不做要求

    /**
     * 私钥 pkcs8转pkcs1 （如果其他语言需要）
     *
     * @param key
     * @return
     * @throws Exception
     * @title: convertPrivateKeyPkcs8ToPkcs1
     * @Author: zhaoweiyong
     * @Date: 2020年5月18日下午4:50:23
     */
    public static String convertPrivateKeyPkcs8ToPkcs1(byte[] key) throws Exception {
        return Base64.encodeBase64String(convertPrivateKeyPkcs8ToPkcs1BytesArray(key));
    }

    public static String convertPrivateKeyPkcs8ToPkcs1(String key) throws Exception {
        return convertPrivateKeyPkcs8ToPkcs1(Base64.decodeBase64(key));
    }

    public static byte[] convertPrivateKeyPkcs8ToPkcs1BytesArray(byte[] key) throws Exception {
        PrivateKeyInfo pkInfo = PrivateKeyInfo.getInstance(key);
        ASN1Encodable encodable = pkInfo.parsePrivateKey();
        ASN1Primitive primitive = encodable.toASN1Primitive();
        byte[] privateKeyPKCS1 = primitive.getEncoded();
        return privateKeyPKCS1;
    }

    /**
     * 公钥 pkcs8转pkcs1 （如果其他语言需要）
     *
     * @param key
     * @return
     * @throws Exception
     * @title: convertPublicKeyPkcs8ToPkcs1
     * @Author: zhaoweiyong
     * @Date: 2020年5月18日下午4:50:23
     */
    public static String convertPublicKeyPkcs8ToPkcs1(byte[] key) throws Exception {
        return Base64.encodeBase64String(convertPublicKeyPkcs8ToPkcs1BytesArray(key));
    }

    public static String convertPublicKeyPkcs8ToPkcs1(String key) throws Exception {
        return convertPublicKeyPkcs8ToPkcs1(Base64.decodeBase64(key));
    }

    public static byte[] convertPublicKeyPkcs8ToPkcs1BytesArray(byte[] key) throws Exception {
        SubjectPublicKeyInfo spkInfo = SubjectPublicKeyInfo.getInstance(key);
        ASN1Primitive primitive = spkInfo.parsePublicKey();
        byte[] publicKeyPKCS1 = primitive.getEncoded();
        return publicKeyPKCS1;
    }

    // 该方法之后的方法已经帮我们取得了pkcs8的公钥和私钥，我们可能需要写入到文件，像openssl一样
    // 供其他语言开发者像使用openssl生成一样过度

    /**
     * 公钥pkcs8到pem
     *
     * @param key
     * @param writer
     * @throws Exception
     * @title: writePublicPkcs8ToPem
     * @Author: zhaoweiyong
     * @Date: 2020年5月18日下午5:28:44
     */
    public static void writePublicPkcs8ToPem(byte[] key, Writer writer) throws Exception {
        writeToPem(key, "PUBLIC KEY", writer);
    }

    public static void writePublicPkcs8ToPem(String key, Writer writer) throws Exception {
        writeToPem(Base64.decodeBase64(key), "PUBLIC KEY", writer);
    }

    /**
     * 公钥pkcs1到pem
     *
     * @param key
     * @param writer
     * @throws Exception
     * @title: writePublicPkcs1ToPem
     * @Author: zhaoweiyong
     * @Date: 2020年5月18日下午5:29:10
     */
    public static void writePublicPkcs1ToPem(byte[] key, Writer writer) throws Exception {
        writeToPem(key, "RSA PUBLIC KEY", writer);
    }

    public static void writePublicPkcs1ToPem(String key, Writer writer) throws Exception {
        writeToPem(Base64.decodeBase64(key), "RSA PUBLIC KEY", writer);
    }

    /**
     * 私钥pkcs8到pem
     *
     * @param key
     * @param writer
     * @throws Exception
     * @title: writePrivatePkcs8ToPem
     * @Author: zhaoweiyong
     * @Date: 2020年5月18日下午5:29:20
     */
    public static void writePrivatePkcs8ToPem(byte[] key, Writer writer) throws Exception {
        writeToPem(key, "PRIVATE KEY", writer);
    }

    public static void writePrivatePkcs8ToPem(String key, Writer writer) throws Exception {
        writeToPem(Base64.decodeBase64(key), "PRIVATE KEY", writer);
    }

    /**
     * 私钥pkcs1到pem
     *
     * @param key
     * @param writer
     * @throws Exception
     * @title: writePrivatePkcs1ToPem
     * @Author: zhaoweiyong
     * @Date: 2020年5月18日下午5:29:37
     */
    public static void writePrivatePkcs1ToPem(byte[] key, Writer writer) throws Exception {
        writeToPem(key, "RSA PRIVATE KEY", writer);
    }

    public static void writePrivatePkcs1ToPem(String key, Writer writer) throws Exception {
        writeToPem(Base64.decodeBase64(key), "RSA PRIVATE KEY", writer);
    }

    /**
     * 写入pem公用方法
     *
     * @param key
     * @param header
     * @param writer
     * @throws Exception
     * @title: writeToPem
     * @Author: zhaoweiyong
     * @Date: 2020年5月18日下午5:29:59
     */
    public static void writeToPem(byte[] key, String header, Writer writer) throws Exception {
        PemObject pemObject = new PemObject(header, key);
        PemWriter pemWriter = new PemWriter(writer);
        pemWriter.writeObject(pemObject);
        pemWriter.close();
    }

    // 将initKey、getPrivateKey、getPublicKey合并，减少外部调用次数
    // 外部人员不清楚调用逻辑
    // 因为许多地方都用的字节数组的形式，所以需要写两个方法

    /**
     * 返回一个长度为2的数组arr，arr[0] = '私钥字符串', arr[1]=公钥字符串，即字节码Base64之后
     *
     * @return
     * @throws Exception
     * @title: getPrivateKeyAndPublicKey
     * @Author: zhaoweiyong
     * @Date: 2020年5月18日下午4:59:49
     */
    public static String[] getPrivateKeyAndPublicKey() throws Exception {
        Map<String, Object> keyObjectMap = initKey();
        String privateKeyStr = Base64.encodeBase64String(getPrivateKey(keyObjectMap));
        String publicKeyStr = Base64.encodeBase64String(getPublicKey(keyObjectMap));
        String[] keyArr = new String[2];
        keyArr[0] = privateKeyStr;
        keyArr[1] = publicKeyStr;
        return keyArr;
    }

    /**
     * 返回一个长度为2的数组arr，arr[0] = '私钥字符串字节码', arr[1]=公钥字符串，即Base64之前
     *
     * @return
     * @throws Exception
     * @title: getPrivateKeyAndPublicKeyBytesArray
     * @Author: zhaoweiyong
     * @Date: 2020年5月18日下午5:09:58
     */
    public static Object[] getPrivateKeyAndPublicKeyBytesArray() throws Exception {
        Map<String, Object> keyObjectMap = initKey();
        byte[] privateKeyArray = getPrivateKey(keyObjectMap);
        byte[] publicKeyArray = getPublicKey(keyObjectMap);
        Object[] keyArr = new Object[2];
        keyArr[0] = privateKeyArray;
        keyArr[1] = publicKeyArray;
        return keyArr;
    }

    /**
     * 初始化密钥
     *
     * @return Map 密钥Map
     * @throws Exception
     */
    public static Map<String, Object> initKey() throws Exception {
        // 实例化密钥对生成器
        // RSA
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        // 初始化密钥对生成器
        // 能够加密密文的最大长度
        keyPairGen.initialize(KEY_SIZE);
        // 生成密钥对
        KeyPair keyPair = keyPairGen.generateKeyPair();
        // 公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        // 私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        // 封装密钥
        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        // RSAPublicKey
        keyMap.put(PUBLIC_KEY, publicKey);
        // RSAPrivateKey
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    /**
     * 取得私钥
     *
     * @param keyMap 密钥Map
     * @return byte[] 私钥
     * @throws Exception
     */
    public static byte[] getPrivateKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return key.getEncoded();
    }

    /**
     * 取得公钥
     *
     * @param keyMap 密钥Map
     * @return byte[] 公钥
     * @throws Exception
     */
    public static byte[] getPublicKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return key.getEncoded();
    }


    // 私钥签名算法

    /**
     * 签名
     *
     * @param data       待签名数据
     * @param privateKey 私钥
     * @return byte[] 数字签名
     * @throws Exception
     */
    public static byte[] sign(byte[] data, byte[] privateKey) throws Exception {
        // 转换私钥材料
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
        // 实例化密钥工厂
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        // 取私钥匙对象
        PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // 实例化Signature
        Signature signature = Signature.getInstance(SHA256_WITH_RSA_ALGORITHM);
        // 初始化Signature
        signature.initSign(priKey);
        // 更新
        signature.update(data);
        // 签名
        return signature.sign();
    }

    public static String sign(String data, String privateKey) throws Exception {
        return Base64.encodeBase64String(sign(data.getBytes(Charset.forName("UTF-8")), Base64.decodeBase64(privateKey)));
    }

    /**
     * 校验
     *
     * @param data      待校验数据
     * @param publicKey 公钥
     * @param sign      数字签名
     * @return boolean 校验成功返回true 失败返回false
     * @throws Exception
     */
    public static boolean verify(byte[] data, byte[] publicKey, byte[] sign)
            throws Exception {
        // 转换公钥材料
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
        // 实例化密钥工厂
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        // 生成公钥
        PublicKey pubKey = keyFactory.generatePublic(keySpec);
        // 实例化Signature
        Signature signature = Signature.getInstance(SHA256_WITH_RSA_ALGORITHM);
        // 初始化Signature
        signature.initVerify(pubKey);
        // 更新
        signature.update(data);
        // 验证
        return signature.verify(sign);
    }

    public static boolean verify(String data, String publicKey, String sign) throws Exception {
        return verify(data.getBytes(Charset.forName("UTF-8")), Base64.decodeBase64(publicKey), Base64.decodeBase64(sign));
    }
}
