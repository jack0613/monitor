package cn.com.gcg.utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Jack on 2018-6-19.
 */
public class FileUtil {


    /**
     * 检查ftp连接是否成功
     *
     */


    public static boolean isPing(String ip) {
        boolean status = false;
        if (ip != null) {
            try {
                status = InetAddress.getByName(ip).isReachable(3000);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return status;
    }



}
