package cn.com.gcg.quartz;

import cn.com.gcg.dao.IpConfigRepository;
import cn.com.gcg.dao.LogRepository;
import cn.com.gcg.dao.MobileRepository;
import cn.com.gcg.model.BussinessLog;
import cn.com.gcg.model.IpConfig;
import cn.com.gcg.model.MobileConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author wzs
 * Ip网络验证定时器
 */
@Service
public class IpQuartz {
    @Autowired
    private IpConfigRepository ipConfigRepository;

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private MobileRepository mobileRepository;


//    @Scheduled(cron="0/10 * *  * * ? ")
//    public void test(){
//       // System.out.println(ipConfigRepository.findByEnabled(1));
//    }

    /**
     * 定时去ping IP地址 根据返回结果判断指定IP地址与主机网络是否通畅
     * 当发生不网络不通畅的时候，会进行拨号通知当前IP绑定的地区下的所有电话号码
     */
    @Scheduled(cron="0 0/5 *  * * ? ")
    public void ipPing(){


        //查询所有启动的ip地址
        List<IpConfig> ips = ipConfigRepository.findByEnabled(1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(IpConfig ip:ips){
           boolean res= ping(ip.getIpAddr());
           String content ="";
           Integer type =1;
           if(res){
               ip.setLastOnlinetime(sdf.format(new Date()));
               ip.setStatus(1);
               content=sdf.format(new Date())+":"+ip.getIpName()+":"+ip.getIpAddr()+" 与主机网络正常";

           }else {
               //ip.setLastOnlinetime(sdf.format(new Date()));
               ip.setStatus(0);
               content =sdf.format(new Date())+":"+ip.getIpName()+":"+ip.getIpAddr()+" 与主机网络不通";
               type=2;
           }
           ipConfigRepository.save(ip);
            BussinessLog log = new BussinessLog();
            log.setAreacode(ip.getAreacode());
            log.setContent(content);
            log.setCreatetime(new Date());
            log.setType(type);
            logRepository.save(log);
            //当type ==2 时 实现打电话通知
            //Jack 修改为 延迟1分钟重新ping 如果不通再拨打
            if(2==type){
                //查询当前IP下的所有电话号码

                List<MobileConfig> mobiles = mobileRepository.findByAreacodeAndEnabled(ip.getAreacode(),1);

                if(mobiles!=null && mobiles.size()>0){
                    for(MobileConfig mobile:mobiles){
                        //Dial.dial(mobile.getPhone());
                        //Dial.dial(mobile.getPhone(),"\\static\\wav\\ipwarn.wav");

                        RetryPingIp retryPingIp = new RetryPingIp(mobile.getPhone(),ip.getAreacode(),ip.getIpName(),logRepository,60000,ip.getIpAddr());
                        Thread thread = new Thread(retryPingIp);
                        thread.start();


                        //Jack 同一拨打电话 插入到电话任务表中
                        /*PhoneTaskConfig phoneTaskConfig = new PhoneTaskConfig();
                        phoneTaskConfig.setAreaCode(ip.getAreacode());
                        phoneTaskConfig.setCreateTime(new Date());
                        phoneTaskConfig.setDataSource(0);
                        phoneTaskConfig.setPhoneNumber(mobile.getPhone());
                        phoneTaskConfig.setVoiceFile("\\static\\wav\\ipwarn.wav");
                        phoneTaskRepository.save(phoneTaskConfig);*/
                    }
                }

            }

        }

    }

    public static boolean ping(String ipAddress) {
        BufferedReader in = null;
        Runtime r = Runtime.getRuntime();
        String pingCommand = "ping " + ipAddress ;
        try {   // 执行命令并获取输出
            //System.out.println(pingCommand);
            Process p = r.exec(pingCommand);
            if (p == null) {
                return false;
            }
            in = new BufferedReader(new InputStreamReader(p.getInputStream()));   // 逐行检查输出,计算类似出现=23ms TTL=62字样的次数
            int connectedCount = 0;
            String line = null;
            Boolean res = false;
            int i=1;
            while ((line = in.readLine()) != null) {
                //System.out.println(line);
                if(i>10){
                    break;
                }
                //connectedCount += getCheckResult(line);
                if(line.contains("ttl") || line.contains("TTL")){
                    res= true;
                    break;
                }
                i++;
            }   // 如果出现类似=23ms TTL=62这样的字样,出现的次数=测试次数则返回真
            //System.out.println(connectedCount);
            return res;
        } catch (Exception ex) {
            ex.printStackTrace();   // 出现异常则返回假
            return false;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        int trueNum = 0;
        int falseNum = 0;

        long l = System.currentTimeMillis();
        long ll;
        ll = l;
        while (true){
            if(ping("192.168.0.1")){
                trueNum = trueNum + 1;
            }else{
                falseNum = falseNum + 1;
            }
            l = System.currentTimeMillis();
            long d = l - ll;

            if (d > 10000) {
                System.out.println("倒计时结束！");
                break;
            }
        }

        System.out.println(trueNum + "-------");
        System.out.println(falseNum + "=======");


    }
}
