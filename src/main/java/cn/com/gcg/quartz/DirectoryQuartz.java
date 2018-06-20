package cn.com.gcg.quartz;

import cn.com.gcg.dao.DirConfigRepository;
import cn.com.gcg.model.DirConfig;
import cn.com.gcg.utils.DateLabelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by Jack on 2018-6-19.
 */
@Service
public class DirectoryQuartz {

    @Autowired
    private DirConfigRepository dirConfigRepository;


    /**
     * @author: Jack
     * @data: 2018-6-19 13:38
     * @param: id
     * @description: 根据ID查询目录信息 执行文件是否存在告警电话
     * @return：void
     */
    public void execMonitorDirectory(String jobName){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if(jobName != null && jobName.indexOf("_") > 0){
            // 获得目录id
            String dirId = jobName.split("_")[1];

            //验证id是否为纯数字
            String pattern = "[0-9]*";
            if(Pattern.matches(pattern,dirId)){
                DirConfig dirConfig = dirConfigRepository.findOne(Long.valueOf(dirId));

                if(dirConfig != null){
                    //目录路径
                    String directory = dirConfig.getFilePath();
                    //替换日期标签
                    directory = DateLabelUtil.formatdate(directory);
                    //文件名称
                    String fileName = dirConfig.getFileName();
                    //替换日期标签
                    fileName = DateLabelUtil.formatdate(fileName);
                    //文件是否存在
                    Integer fileExist = dirConfig.getFileExist();


                    //判断路径为 FTP、共享文件夹、还是本地磁盘
                    if (directory != null && !dirConfig.equals("")) {
                        String diskPattern = "[a-zA-Z]{1}:";

                        if (directory.startsWith("ftp") || directory.startsWith("FTP")) {
                            //ftp 目录处理方式
                            System.out.println("ftp 调用"+ " 时间：" + sdf.format(new Date()));





                        }else if (directory.startsWith("\\\\")) {
                            //共享文件夹处理方式
                            System.out.println("shareFile 调用" + " 时间：" + sdf.format(new Date()));
                        }else if(Pattern.matches(diskPattern,directory)){
                            //本地磁盘处理方式
                            System.out.println("localDisk 调用" +" 时间：" + sdf.format(new Date()));

                        }
                    }

                }


            }
        }
    }





}
