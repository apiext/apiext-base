package com.apiext.base.encrypt.util;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

/**
 * @author mrzhaowy
 * @create 2020-05-19 14:55
 **/
public class Md5Util {

    /**
     * md5签名常用工具, map中的key字典排序
     * 拼接成字符串key1=...&key2=...&Key=key, 进行Md5签名
     * @param paramMap
     * @param key
     * @return
     */
    public static String sign(Map<String, Object> paramMap, String key) {
        StringBuilder preSignSb = new StringBuilder();
        TreeSet<String> treeSet = new TreeSet<>(String::compareTo);
        treeSet.addAll(paramMap.keySet());
        Iterator<String> treeSetIterator = treeSet.iterator();
        System.out.println(treeSet.toString());
        while (treeSetIterator.hasNext()) {
            String paramMapKey = treeSetIterator.next();
            preSignSb.append(paramMapKey + "=" + paramMap.get(paramMapKey) + "&");
        }
        preSignSb.append("Key=" + key);
        return DigestUtils.md5Hex(preSignSb.toString());
    }
}
