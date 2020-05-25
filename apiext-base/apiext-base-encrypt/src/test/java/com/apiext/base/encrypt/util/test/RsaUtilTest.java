package com.apiext.base.encrypt.util.test;

import com.apiext.base.encrypt.util.RsaUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;

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

        StringWriter writer = new StringWriter();
        RsaUtil.writePrivatePkcs8ToPem(privateKey, writer);
        System.out.println(writer.toString());

        writer = new StringWriter();
        RsaUtil.writePublicPkcs8ToPem(publicKey, writer);
        System.out.println(writer.toString());

        String privatePkcs1Str = RsaUtil.convertPrivateKeyPkcs8ToPkcs1(privateKey);
        String publicPkcs1Str = RsaUtil.convertPublicKeyPkcs8ToPkcs1(publicKey);
        writer = new StringWriter();
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

        StringWriter writer = new StringWriter();
        // 以下的pkcs8 php不能使用
        RsaUtil.writePrivatePkcs8ToPem(privatePkcs8Key, writer);
        System.out.println(writer.toString());

        String publicPkcs8Key = RsaUtil.convertPublicKeyPkcs1ToPkcs8(publicPckcs1Key);
        writer = new StringWriter();
        RsaUtil.writePublicPkcs8ToPem(publicPkcs8Key, writer);
        System.out.println(writer.toString());

        encryptAndDecrypt(publicPkcs8Key, privatePkcs8Key);
        // java 用pkcs8的私钥解密
        // python pkcs1公钥加密的字符串
        String pythonPublicPkcs1EncoderStr = "na29zvVmSAZ5+ax25RziYjGKX3KYfiOY0UCDPnAa+5Zd6khSaBo1YGd7G6erh/1r261lvGwpFy0w9fDkERSxEoIYCo0zrL5G2EdYn78r8rgLu+y75Q23MwNq5tSG4vLzM2mDYRHneBofqqGkW6bwsjEiLN8STKbRRUXjL0NNa0iACfqG6I1XIKbYFYAHUVV8w/3YdvNAJGBPNKHOg0JrOK+k8BAenEQWVgBCXTDth58w9n/sEFXI1zOpV7mpPbBHk/NLPxH4SpClCdsMkWBhAlSy7neTcWPwCLMzspkQb/X80itaELtWpOAGqxgknVKEi7N2FlRmF7IB/cOboQBCnw==";
        String preStr = RsaUtil.decryptToStrByPrivateKey(pythonPublicPkcs1EncoderStr, privatePkcs8Key);
        System.out.println(preStr);
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
        // String signStr = RsaUtil.sign(data, privateKey);
        String privatePkcs8Key = RsaUtil.loadKeyByFile("D:\\apiext\\apiext-base\\apiext-base-encrypt\\src\\main\\resources\\private_pkcs8_key.pem");
        String publicPkcs8Key = RsaUtil.loadKeyByFile("D:\\apiext\\apiext-base\\apiext-base-encrypt\\src\\main\\resources\\public_pkcs8_key.pem");
        String signStr = RsaUtil.sign(data, privatePkcs8Key);
        System.out.println(signStr);
        Assert.assertTrue(RsaUtil.verify(data, publicPkcs8Key, signStr));

        // 验签php
        boolean verifyResult = RsaUtil.verify("1", publicPkcs8Key, "ZHoaK9XwROjnpBBh/bOVtMf1g5QXB1Q3dohIM+pgSrUGA8M41PXv0qotMPqIj9EcZ11nmw+bb9UurVh75yOI19BDtmCqUcF5s9lFiHDNXbKVsuJMXILFsKZWeLnKg+DtLa6PpUuSQsiXZGC5Du+GJM4z4xPwVb0xsZkO07lUmzlJeQ/SghDkUgjvAl9spLWaZtt1HV4uXPHaadtq7AOkrdGKYym4X4Dh8UHFW40YFxRjzeFVz8hNwGYKsOVBmuNkt7oDIg9Uhc2Y9ow+FTONPA7dbGsKNmf0IPDPyZfbKQzw6S2lmtP3TooKdPg0FQH8LBvTCJoHpa/7JGg0eW+JPA==");
        System.out.println(verifyResult);


        // 验签python
        verifyResult = RsaUtil.verify(data, publicPkcs8Key, "KTCU/l+ouUheP4JeI8dsHTE3TJJayfzDFZbXL88PC/AqpmhzU1BwS5Or35mD6o9ig9v8MeueSROASt0vWAomwYa0uF3lqRjsMnETxtfEBabevu9enWasIb9weL4cdDfgUzsKf3hcy56Tu81ZRg72oE11H+NfYiz1LFWOEqKir1ZT8Eajj3O0eqZJG7TtA2aJFT9YZzRNaAPjJVt9wYLST4j7L1emdIrkYl0IBagm9QJ5g0tt3gxlMTytV/+kvuLiOopwCgXoBaTQ38oFygX0HICWLaB0XOytfCrzDTtNJu6g82llnhOyezR0vbMdRPNxF4zFZ4R/AW+yn62notPSig==");
        System.out.println(verifyResult);
    }

    /**
     * 测试加密
     *
     * @throws Exception
     */
    @Test
    public void testEncrypt() throws Exception {
        String privatePkcs8Key = RsaUtil.loadKeyByFile("D:\\apiext\\apiext-base\\apiext-base-encrypt\\src\\main\\resources\\private_pkcs8_key.pem");
        String publicPkcs8Key = RsaUtil.loadKeyByFile("D:\\apiext\\apiext-base\\apiext-base-encrypt\\src\\main\\resources\\public_pkcs8_key.pem");

        encryptAndDecrypt(publicPkcs8Key, privatePkcs8Key);
    }


    private void encryptAndDecrypt(String publicKey, String privateKey) throws Exception {

        StringWriter writer = new StringWriter();
        RsaUtil.writePrivatePkcs8ToPem(privateKey, writer);
        System.out.println(writer.toString());


        writer = new StringWriter();
        RsaUtil.writePublicPkcs8ToPem(publicKey, writer);
        System.out.println(writer.toString());

        // 一般是公钥加密
        // 私钥解密
        // 明文字节长度是由限制的,理论是2048/8 - 11 = 256 - 11 = 247
        String data = "www.apiext.com;www.apiext.cn";
        String encoderStr = RsaUtil.encryptToStrByPublicKey(data, publicKey);
        System.out.println(encoderStr);
        Assert.assertEquals(data, RsaUtil.decryptToStrByPrivateKey(encoderStr, privateKey));
    }


}
