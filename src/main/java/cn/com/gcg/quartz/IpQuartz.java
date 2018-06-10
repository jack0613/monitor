package cn.com.gcg.quartz;

import cn.com.gcg.dao.IpConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import sun.plugin2.util.SystemUtil;

@Service
public class IpQuartz {
    @Autowired
    private IpConfigRepository ipConfigRepository;

    @Scheduled(cron="0/10 * *  * * ? ")
    public void test(){
        System.out.println(ipConfigRepository.findOne(1l));
    }
}
