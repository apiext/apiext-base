package com.apiext.base.encrypt.util.test;

import com.apiext.base.encrypt.util.PasswordUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author mrzhaowy
 * @create 2020-05-19 16:46
 **/
public class PasswordUtilTest {

    @Test
    public void testGetMd5Password(){
        while(true){
            String encryptPassword = PasswordUtil.getEncryptPassword("abcdefghijklmn");
            Assert.assertTrue(PasswordUtil.verify("abcdefghijklmn", encryptPassword));
        }
    }

}
