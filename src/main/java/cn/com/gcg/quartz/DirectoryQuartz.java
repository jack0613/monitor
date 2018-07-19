package cn.com.gcg.quartz;

import cn.com.gcg.dao.DirConfigRepository;
import cn.com.gcg.dao.LogRepository;
import cn.com.gcg.dao.MobileRepository;
import cn.com.gcg.model.BussinessLog;
import cn.com.gcg.model.DirConfig;
import cn.com.gcg.model.MobileConfig;
import cn.com.gcg.telbox.Dial;
import cn.com.gcg.utils.DateLabelUtil;
import cn.com.gcg.utils.FileUtil;
import cn.com.gcg.utils.SpringContextUtils;
import jcifs.smb.SmbFile;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Jack on 2018-6-19.
 */
public class DirectoryQuartz {


    /**
     * 上下文对象实例
     */
    private static ApplicationContext applicationContext;

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Autowired
    private DirConfigRepository dirConfigRepository = (DirConfigRepository) SpringContextUtils.getBean(DirConfigRepository.class);

    @Autowired
    private LogRepository logRepository = (LogRepository) SpringContextUtils.getBean(LogRepository.class);

    @Autowired
    private MobileRepository mobileRepository = (MobileRepository) SpringContextUtils.getBean(MobileRepository.class);

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
                    //fileName = DateLabelUtil.formatdate(fileName);
                    //采用任务配置减少时间计算
                    if(dirConfig.getDelayTime() > 0 ){
                        fileName = DateLabelUtil.formatdate(fileName,new Date(new Date().getTime() - dirConfig.getDelayTime()));
                    }else{
                        fileName = DateLabelUtil.formatdate(fileName);
                    }

                    //文件是否存在
                    Integer fileExist = dirConfig.getFileExist();

                    //判断路径为 FTP、共享文件夹、还是本地磁盘
                    if (directory != null && !dirConfig.equals("")) {
                        String diskPattern = "[a-z|A-Z]{1}:[\\s\\S]*";

                        if (directory.startsWith("ftp") || directory.startsWith("FTP")) {
                            //ftp 目录处理方式

                            String [] dirArr = directory.split("\\{\\^\\}");
                            if(dirArr != null && dirArr.length >= 5){
                                //处理端口
                                String portStr = dirArr[1];
                                int port = 21;
                                if(Pattern.matches(pattern,portStr)){
                                    port = Integer.valueOf(portStr);
                                }
                                //处理ip
                                String ip = dirArr[0];
                                if(ip != null && !ip.equals("")){
                                    ip = ip.replace("ftp://","").replace("FTP://","");
                                }

                                if(FileUtil.isPing(ip)){
                                    //处理文件路径
                                    String path = dirArr[4];

                                    Map<String,Object> filesMap = new HashMap<String,Object>();
                                    try {

                                        filesMap = FileUtil.ftpExitsFile(path,ip,port,dirArr[2],dirArr[3],fileName);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    //日志
                                    BussinessLog log = new BussinessLog();
                                    log.setAreacode(dirConfig.getAreacode());
                                    log.setCreatetime(new Date());

                                    log.setType(1);
                                    //文件存在告警
                                    if(fileExist == 1 && filesMap != null && filesMap.size() > 0){
                                        //System.out.println("文件存在告警-------：" + fileName);
                                        //写入日志
                                        String content = "监测到FTP任务，文件存在告警：IP:" + ip +",路径：" + path + ",文件名：" + fileName;
                                        log.setContent(content);
                                        log.setType(2);
                                        log.setAreacode(dirConfig.getAreacode());
                                        log.setCreatetime(new Date());
                                        logRepository.save(log);
                                        //查询当前地区下的所有电话号码
                                        dail(dirConfig.getAreacode());
                                    }else if(fileExist == 0 && (filesMap == null || filesMap.size() == 0)){//文件不存在告警
                                        //System.out.println("文件不存在告警");
                                        //写入日志
                                        String content = "监测到FTP任务，文件不存在告警：IP:" + ip +",路径：" + path + ",文件名：" + fileName;
                                        log.setContent(content);
                                        log.setType(2);
                                        log.setAreacode(dirConfig.getAreacode());
                                        log.setCreatetime(new Date());
                                        logRepository.save(log);
                                        //查询当前地区下的所有电话号码
                                        dail(dirConfig.getAreacode());
                                    }else{
                                        //System.out.println("不告警：");
                                        String content = "监测到FTP任务,未发生告警：路径：" + path + ",文件名：" + fileName;
                                        log.setContent(content);
                                        log.setType(1);
                                        log.setAreacode(dirConfig.getAreacode());
                                        log.setCreatetime(new Date());
                                        logRepository.save(log);
                                    }


                                }else{
                                    //日志
                                    BussinessLog log = new BussinessLog();
                                    String content = "监测到FTP任务，IP不能PING通告警：IP:" + ip ;
                                    log.setContent(content);
                                    log.setType(2);
                                    log.setAreacode(dirConfig.getAreacode());
                                    log.setCreatetime(new Date());
                                    logRepository.save(log);
                                    //查询当前地区下的所有电话号码
                                    dail(dirConfig.getAreacode());
                                }

                            }


                        }/*else if (directory.startsWith("\\\\")) {
                            //共享文件夹处理方式
                            try {
                                SmbFile smbFile = new SmbFile( directory + fileName);
                                if (smbFile == null) {
                                    System.out.println("共享文件不存在");
                                    return null;
                                }


                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }
                        }*/else if(Pattern.matches(diskPattern,directory)){
                            //本地磁盘处理方式
                            System.out.println("localDisk 调用" +" 时间：" + sdf.format(new Date()));

                            Map<String,Object> filesMap = FileUtil.shareFileExitsFile(directory,fileName);
                            //日志
                            BussinessLog log = new BussinessLog();
                            log.setAreacode(dirConfig.getAreacode());
                            log.setCreatetime(new Date());
                            log.setType(1);
                            //文件存在告警
                            if(fileExist == 1 && filesMap != null && filesMap.size() > 0){
                                //System.out.println("文件存在告警-------：" + fileName);
                                //写入日志
                                String content = "监测到本地磁盘任务，文件存在告警：路径：" + directory + ",文件名：" + fileName;
                                log.setContent(content);
                                log.setType(2);
                                log.setAreacode(dirConfig.getAreacode());
                                log.setCreatetime(new Date());
                                logRepository.save(log);
                                //查询当前地区下的所有电话号码
                                dail(dirConfig.getAreacode());
                            }else if(fileExist == 0 && (filesMap == null || filesMap.size() == 0)){//文件不存在告警
                                //System.out.println("文件不存在告警");
                                //写入日志
                                String content = "监测到本地磁盘任务，文件不存在告警：路径：" + directory + ",文件名：" + fileName;
                                log.setContent(content);
                                log.setType(2);
                                log.setAreacode(dirConfig.getAreacode());
                                log.setCreatetime(new Date());
                                logRepository.save(log);
                                //查询当前地区下的所有电话号码
                                dail(dirConfig.getAreacode());
                            }else{
                                //System.out.println("不告警：");
                                String content = "监测到本地磁盘任务，未发生告警：路径：" + directory + ",文件名：" + fileName;
                                log.setContent(content);
                                log.setType(1);
                                log.setAreacode(dirConfig.getAreacode());
                                log.setCreatetime(new Date());
                                logRepository.save(log);
                            }

                        }
                    }

                }


            }
        }
    }


    private void dail(String areacode){
        try{
            //查询当前地区下的所有电话号码
            List<MobileConfig> mobiles = mobileRepository.findByAreacodeAndEnabled(areacode,1);

            if(mobiles!=null && mobiles.size()>0){
                for(MobileConfig mobile:mobiles){
                    //Dial.dial(mobile.getPhone());
                    Dial.dial(mobile.getPhone(),"\\static\\wav\\filewarn.wav");
                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }



}
