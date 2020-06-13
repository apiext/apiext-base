package com.apiext.base.common.util;

import java.util.Random;

import org.apache.commons.lang3.StringUtils;

/**
 * 字符串工具类
 *
 * @author mrzhaowy
 * @create 2020-05-19 16:32
 **/
public class StringUtil {

    private static final String DOT = ".";

    /**
     * 字符串随机码源
     */
    public static final String[] LETTER_SOURCE = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n",
            "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};

    /**
     * 字符串随机码源
     */
    public static final String[] NUMBER_LETTER_SOURCE = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n",
            "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

    /**
     * 数字随机码源
     */
    public static final String[] NUMBER_SOURCE = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};


    /**
     * 获取指定长度的随机数
     *
     * @param num
     * @param source
     * @return
     */
    public static String getRandomStr(int num, String[] source) {
        int len = source.length;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < num; i++) {
            int randomIndex = new Random().nextInt(len);
            sb.append(source[randomIndex]);
        }
        return sb.toString();
    }

    /**
     * in操作， 类似db的in
     * @param param
     * @param params
     * @return
     */
    public static boolean in(String param, String ... params) {
        for(String item : params){
            if(param.equals(item)){
                return true;
            }
        }
        return false;
    }

    /**
     * in操作，类似db的in, 忽略大小写
     * @param param
     * @param params
     * @return
     */
    public static boolean inIgnoreCase(String param, String ... params) {
        for(String item : params){
            if(param.equalsIgnoreCase(item)){
                return true;
            }
        }
        return false;
    }

    /**
     * 拼接字符串，忽略空字符串
     *
     * @param params
     * @return
     */
    public static String concat(String... params) {
        StringBuilder sb = new StringBuilder();
        for (String param : params) {
            if (StringUtils.isNotEmpty(param)) {
                sb.append(param);
            }
        }
        return sb.toString();
    }

    /**
     * 使用连接符拼接字符串
     *
     * @param result
     * @param separator
     * @param params
     */
    public static void concatWithSeparator(StringBuilder result, String separator, String... params) {
        for (String item : params) {
            if (StringUtils.isNotEmpty(item)) {
                boolean addSeparatorFlag = result.length() > 0;
                if (addSeparatorFlag) {
                    result.append(separator);
                }
                result.append(item);
            }
        }
    }


    /**
     * 删除字符串数字结尾多余的0和.,例如4.2600->4.26
     * 4.00-> 4
     *
     * @param param
     * @return
     */
    public static String rvZeroAndDot(String param) {
        if (param.indexOf(DOT) > 0) {
            // 去掉多余的0
            param = param.replaceAll("0+?$", "");
            // 如最后一位是.则去掉
            param = param.replaceAll("[.]$", "");
        }
        return param;
    }


    /**
     * 格式化字符串到固定长度
     *
     * @param param
     * @param fillChar
     * @param len
     * @return
     */
    public static String formatFill(String param, String fillChar, int len) {
        if (param.length() < len) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < len - param.length(); i++) {
                sb.append(fillChar);
            }
            sb.append(param);
            return sb.toString();
        }
        return param;
    }

    /**
     * 去掉中间和两边的空格
     * @param param
     * @return
     */
    public static String trimAll(String param){
        return param.replaceAll("\\s", "");
    }

}
