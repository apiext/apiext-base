package com.apiext.base.encrypt.util.test;

import com.apiext.base.encrypt.util.RsaUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
     * 测试签名
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
     * @throws Exception
     */
    @Test
    public void testEncrypt() throws Exception {
        // 一般是公钥加密
        // 私钥解密
        // 明文字节长度是由限制的,理论是2048/8 - 11 = 256 - 11 = 247
        String data = "www.apiext.com;www.apiext.cn";
        String encoderStr = RsaUtil.encryptToStrByPublicKey(data, publicKey);
        Assert.assertEquals(data, RsaUtil.decryptToStrByPrivateKey(encoderStr, privateKey));
    }


}
