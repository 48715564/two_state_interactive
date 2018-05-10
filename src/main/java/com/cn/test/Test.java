package com.cn.test;

import com.xiaoleilu.hutool.cache.Cache;
import com.xiaoleilu.hutool.cache.CacheUtil;
import com.xiaoleilu.hutool.io.resource.ClassPathResource;
import com.xiaoleilu.hutool.json.JSONObject;
import com.xiaoleilu.hutool.util.CharsetUtil;

import java.io.*;

/**
 * Created by bozhou on 2018/1/18.
 */
public class Test {
    /**
     * 执行外部程序,并获取标准输出
     */
    public static String excuteCmd_multiThread(String[] cmd, String encoding, String path) {
        BufferedReader bReader = null;
        InputStreamReader sReader = null;
        try {
            Process p = Runtime.getRuntime().exec(cmd, null, new File(path));
            /*为"错误输出流"单独开一个线程读取之,否则会造成标准输出流的阻塞*/
            Thread t = new Thread(new InputStreamRunnable(p.getErrorStream(), "ErrorStream"));
            t.start();
            /*"标准输出流"就在当前方法中读取*/
            BufferedInputStream bis = new BufferedInputStream(p.getInputStream());

            if (encoding != null && encoding.length() != 0) {
                sReader = new InputStreamReader(bis, encoding);//设置编码方式
            } else {
                sReader = new InputStreamReader(bis, "GBK");
            }
            bReader = new BufferedReader(sReader);

            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = bReader.readLine()) != null) {
                sb.append(line);
                sb.append("/n");
            }

            bReader.close();
            p.destroy();
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        } finally {
        }
    }


    public static void main(String[] args) {
        String [] cmd={"cmd","/C","yo" ,"jhipster", "--skip-git","--skip-install" ,"--f"};
        String path = "D:\\app\\app";
//        ClassPathResource
        excuteCmd_multiThread(cmd, CharsetUtil.systemCharset().name(),path);
    }
}
