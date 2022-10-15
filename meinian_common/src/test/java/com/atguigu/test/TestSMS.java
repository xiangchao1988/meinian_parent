package com.atguigu.test;

import com.atguigu.util.HttpUtils;
import org.apache.http.HttpResponse;

import java.util.HashMap;
import java.util.Map;

public class TestSMS {


    public static void main(String[] args) {
        String host = "http://dingxin.market.alicloudapi.com";//这地址是固定的
        String path = "/dx/sendSms";//固定的映射
        String method = "POST";
        String appcode = "81a6bec3b70f4fc3a75103dfe1710e55";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);//固定的格式，APPCODE后面有一个空格，会自动填入前面填的appcode

        //三个参数名称是固定的
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", "15060031805");
        querys.put("param", "code:1234");//code:开头，只支持数据和字母
        querys.put("tpl_id", "TP1711063");//测试模板id,如果需要自定义模板，需要联系客服

        Map<String, String> bodys = new HashMap<String, String>();//这里是请求体


        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            //HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            //System.out.println(response.toString());
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
