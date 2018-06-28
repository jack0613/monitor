package cn.com.gcg.utils;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

/**
 * Created by Jack on 2018-6-19.
 */
public class FileUtil {


    /**
     * 检查ftp连接是否成功
     *
     */
    public static boolean content(FTPClient ftpClient,String hostname,int port,String usename,String password){
        boolean flag = false;
        try {
            int reply;
            ftpClient.setConnectTimeout(30*1000);
            //判读IP地址是否ping通
            if(isPing(hostname)){
                // 链接到ftp服务器
                ftpClient.connect(hostname, port);
                // System.out.println("连接到ftp服务器：" + hostname + " 成功..开始登录");
                // 登录.用户名 密码
                boolean b = ftpClient.login(usename, password);
                // 登录.用户名 密码
                // System.out.println("登录成功." + b);
                // 检测连接是否成功
                reply = ftpClient.getReplyCode();
                if (FTPReply.isPositiveCompletion(reply)) {
                    flag = true;
                }else {
                    ftpClient.disconnect();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * @author: Jack
     * @data: 2018-6-20 21:54
     * @param: remoteDir
     * @description: 文件是否存在
     * @return：Map
     */
    public static Map<String, Object> ftpExitsFile(String remoteDir, String hostname, int port,
            String usename, String password,String filename) throws Exception, IOException {
        int flag = 0;
        FTPClient ftpClient = null;
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            ftpClient = new FTPClient();
            ftpClient.setConnectTimeout(30*1000);
            boolean boor = content(ftpClient, hostname, port, usename, password);
            if (boor) {
                ftpClient.setControlEncoding("GBK");
                ftpClient.setRemoteVerificationEnabled(false);
                FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_NT);
                conf.setServerLanguageCode("zh");
                FTPFile[] allFiles = ftpClient.listFiles(new String(remoteDir.getBytes("GB2312"), "ISO-8859-1"));
                if (null != allFiles && allFiles.length > 0) {
                    // 循环所有路径
                    for (int i = 0; i < allFiles.length; i++) {
                        if (allFiles[i].isDirectory()) {// 如果是文件夹
                            continue;
                        }else { // 文件
                            if (allFiles[i].getName().equals(filename)) {
                                flag = 1;
                                resultMap.put("lm", allFiles[i].getTimestamp().getTimeInMillis());
                                break;
                            }
                        }
                    }
                }
            }
            resultMap.put("flag", flag);
        }catch (Exception e) {
            resultMap.put("flag", flag);
            e.printStackTrace();
        }finally {
            if (null != ftpClient)
                ftpClient.logout();
            ftpClient.disconnect();
        }
        return resultMap;
    }


    /**
     * @author: Jack
     * @data: 2018-6-20 21:53
     * @param: ip
     * @description: ip 是否接通
     * @return：boolean
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


    /**
     * @author: Jack
     * @data: 2018-6-27 21:49
     * @param: directory  fileName
     * @description: 查看本地磁盘文件是否存在
     * @return：map
     */
    public static Map<String,Object> shareFileExitsFile(String directory,String fileName){
        Map<String,Object> fileMap = new HashMap<String,Object>();
        try{
            File file = new File(directory + fileName);

            if(!file.isDirectory() && file.exists()){
                fileMap.put("isExits","true");
            }

            /*if(file.isDirectory()){
                String[] filelist = file.list();
                for (int i = 0; i < filelist.length; i++) {
                    File shareFilesName = new File(directory + "\\" + filelist[i]);
                    if (shareFilesName.isDirectory() && filelist[i].equals(fileName)) {
                        fileMap.put("lm",filelist[i]);
                    }
                }

            }else{
                return fileMap;
            }*/


        }catch(Exception e){
            e.printStackTrace();
        }

        return fileMap;
    }



}
