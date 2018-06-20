package cn.com.gcg.config;

import cn.com.gcg.dao.DirConfigRepository;
import cn.com.gcg.model.DirConfig;
import cn.com.gcg.quartz.ScheduleTask;
import cn.com.gcg.telbox.InitializeBox;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Jack on 2018-6-13.
 */
@Component
public class AppRunner implements ApplicationRunner {

    public static final String JOBDETAIL_NAME = "job_";
    public static final String TRIGGER_NAMES = "trigger_";

    @Autowired
    private DirConfigRepository dirConfigRepository;


    @Override
    public void run(ApplicationArguments var1) throws Exception{
        //开启电话盒子
        InitializeBox.initialize();
        addTaskToQuartz();
    }

    //向quartz添加任务
    private void addTaskToQuartz()  {
        List<DirConfig> dirConfigList = dirConfigRepository.findByEnabled(1);

        for (DirConfig dirconfig : dirConfigList) {
            long dirId = dirconfig.getId();
            String monitorTime = dirconfig.getMonitorTime();

            try {
                //创建Job实例
                JobDetail jobDetail = JobBuilder.newJob(ScheduleTask.class).withIdentity(JOBDETAIL_NAME + dirId).build();

                //判断cron 是否正确表达式
                Boolean b = CronExpression.isValidExpression(monitorTime);
                if (!b) {
                    System.out.println("keyId:"
                            + " cron:" + monitorTime + " 不是cron表达式");
                    break;
                }else{
                    //创建一个trigger
                    Trigger trigger = TriggerBuilder.newTrigger().withIdentity(TRIGGER_NAMES + dirId)
                            .withSchedule(CronScheduleBuilder.cronSchedule(monitorTime)).build();

                    //创建Scheduler实例
                    SchedulerFactory schedulerFactory = new StdSchedulerFactory();
                    Scheduler scheduler = schedulerFactory.getScheduler();
                    scheduler.scheduleJob(jobDetail,trigger);
                    scheduler.start();
                }
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        }
    }


}
