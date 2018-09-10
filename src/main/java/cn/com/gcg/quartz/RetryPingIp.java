package cn.com.gcg.quartz;

import cn.com.gcg.dao.LogRepository;
import cn.com.gcg.model.BussinessLog;
import cn.com.gcg.telbox.Dial;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Jack on 2018-8-22.
 */
public class RetryPingIp implements Runnable{

    private String phone;

    private Integer sleepTime;

    private String ip;

    private String areaCode;

    private String ipName;

    private LogRepository logRepository;

    public RetryPingIp(String phone, String areaCode, String ipName, LogRepository logRepository, int sleepTime, String ip){
        this.phone = phone;
        this.sleepTime = sleepTime;
        this.ip = ip;
        this.areaCode = areaCode;
        this.ipName = ipName;
        this.logRepository = logRepository;
    }


    @Override
    public void run() {
        if(sleepTime == null || sleepTime < 0){
            sleepTime = 1000 * 60;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println("首次Ping失败等待一分钟后重试-----------------------" + ip);
            Thread.sleep(sleepTime);
            if(!IpQuartz.ping(ip)){
                System.out.println("第二次Ping失败直接拨号-----------------------" + ip);
                Dial.dial(phone,"\\static\\wav\\ipwarn.wav");
                String content =sdf.format(new Date())+":"+ ipName +":"+ ip +" 与主机网络不通";

                BussinessLog log = new BussinessLog();
                log.setAreacode(areaCode);
                log.setContent(content);
                log.setCreatetime(new Date());
                log.setType(3);//Jack 3为拨号记录
                logRepository.save(log);
            }
            System.out.println("等待一分钟后Ping正常-----------------------" + ip);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
