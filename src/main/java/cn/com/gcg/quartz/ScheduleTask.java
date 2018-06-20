package cn.com.gcg.quartz;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by Jack on 2018-6-16.
 */
public class ScheduleTask implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDetail jobDetail = context.getJobDetail();
        String jobName = jobDetail.getKey().getName();

        DirectoryQuartz directoryQuartz = new DirectoryQuartz();
        //根据任务id查询目录信息 监测目录文件是否存在告警
        directoryQuartz.execMonitorDirectory(jobName);

    }
}
