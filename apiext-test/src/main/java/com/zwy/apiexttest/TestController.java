package com.zwy.apiexttest;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.web.bind.annotation.*;

/**
 * @author mrzhaowy
 * @create 2020-05-25 15:06
 **/
@RestController
public class TestController {

    @RequestMapping(value = "/test.do", method = RequestMethod.POST)
    @ResponseBody
    public String test(@RequestBody String str) throws InterruptedException {
        System.out.println(str);
        Thread.sleep(1000 * 60 * 30);
        return "5";
    }

}
