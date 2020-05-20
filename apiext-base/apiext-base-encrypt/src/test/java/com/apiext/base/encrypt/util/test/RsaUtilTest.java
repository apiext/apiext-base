package com.apiext.base.encrypt.util.test;

import com.apiext.base.encrypt.util.RsaUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;
import java.util.Base64;

/**
 * @author mrzhaowy
 * @create 2020-05-19 22:13
 **/
public class RsaUtilTest {

    private String privateKey;

    private String publicKey;

    @Before
    public void before() throws Exception {
        String[] keyArray = RsaUtil.getPrivateKeyAndPublicKey();
        privateKey = keyArray[0];
        publicKey = keyArray[1];
    }

    /**
     * 给其他语言使用如python\php等需要pkcs1
     * 阿里的支付宝开放平台开发助手有说明java是pkcs8
     * 其他语言pkcs1
     *
     * @throws Exception
     */
    @Test
    public void testWriteToPem() throws Exception {
        String privatePkcs1Str = RsaUtil.convertPrivateKeyPkcs8ToPkcs1(privateKey);
        String publicPkcs1Str = RsaUtil.convertPublicKeyPkcs8ToPkcs1(publicKey);
        StringWriter writer = new StringWriter();
        RsaUtil.writePrivatePkcs1ToPem(privatePkcs1Str, writer);
        System.out.println(writer.toString());
        writer = new StringWriter();
        RsaUtil.writePublicPkcs1ToPem(publicPkcs1Str, writer);
        System.out.println(writer.toString());

    }

    // python 代码参照resource里rsa_test.py
    // pkcs1的key在resoource里, 在可以通过testWriteToPem方法生成
    @Test
    public void testPkcs1ToPkcs8() throws Exception {
        String privatePkcs1Key = RsaUtil.loadKeyByFile("D:\\apiext\\apiext-base\\apiext-base-encrypt\\src\\main\\resources\\private_key.pem");
        String publicPckcs1Key = RsaUtil.loadKeyByFile("D:\\apiext\\apiext-base\\apiext-base-encrypt\\src\\main\\resources\\public_key.pem");
        String privatePkcs8Key = RsaUtil.convertPrivateKeyPkcs1ToPkcs8(privatePkcs1Key);
        String publicPkcs8Key = RsaUtil.convertPublicKeyPkcs1ToPkcs8(publicPckcs1Key);

        encryptAndDecrypt(publicPkcs8Key, privatePkcs8Key);
        // python pkcs1公钥加密的字符串
        String pythonPublicPkcs1EncoderStr = "wobK2X3cvTvuGv9fSQ++pwwFP1iGZ9LN6BFoEBEmTM2OHMdJthL3HL70G" +
                "85nEL5k6rjW345Ma4gsY+kBl4DJl2ckyPTK/L9H68YajQfvcH/sF9cYpbYRnbE9BYF7NJ+ulJ6gLjRs4oFaqdIRWp" +
                "URVID2lhljS4ytRU3f9T7lum59aakX6jqqa1BQRjcCV615XhWgrXe8zRP+RNhmYAoHV9cw/mEspeF5WhZdMJzbOkqsu" +
                "7CLFFZsVHkzZxPD4OkNZ9sHIF2/296YOMjzB7OjNiOkcu5aamxwWPv03O6tzckYOklpWCS2Uiy5tDr7mLeLzSn66W9kyx7/kwVvRQ4N2Q==";
        System.out.println(RsaUtil.decryptToStrByPrivateKey(pythonPublicPkcs1EncoderStr, privatePkcs8Key));
    }

    /**
     * 测试签名
     *
     * @throws Exception
     */
    @Test
    public void testSign() throws Exception {
        // 私钥签名
        // 公钥验签
        String data = "www.apiext.com;www.apiext.cn";
        String signStr = RsaUtil.sign(data, privateKey);
        Assert.assertTrue(RsaUtil.verify(data, publicKey, signStr));
    }

    /**
     * 测试加密
     *
     * @throws Exception
     */
    @Test
    public void testEncrypt() throws Exception {
        encryptAndDecrypt(publicKey, privateKey);
    }


    public void encryptAndDecrypt(String publicKey, String privateKey) throws Exception {
        // 一般是公钥加密
        // 私钥解密
        // 明文字节长度是由限制的,理论是2048/8 - 11 = 256 - 11 = 247
        String data = "www.apiext.com;www.apiext.cn";
        String encoderStr = RsaUtil.encryptToStrByPublicKey(data, publicKey);
        Assert.assertEquals(data, RsaUtil.decryptToStrByPrivateKey(encoderStr, privateKey));
    }


}
