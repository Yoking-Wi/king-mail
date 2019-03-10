package top.sinch.kingmail.service;

import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.sinch.kingmail.domain.Email;
import top.sinch.kingmail.domain.EmailAddress;
import top.sinch.kingmail.domain.EmailQuartzJob;

import java.util.*;

/**
 * 邮件定时任务 服务层
 *
 * @author sincH
 * @since 2019年3月10日 17:52:31
 */
@Service
public class EmailQuartzJobService {
    @Autowired
    Scheduler scheduler;

    /**
     * 获取所有定时任务状态
     *
     * @return
     */
    public Map listEmailQuartzTriggersState() {
        Map emailQuartzJobStateMap = new HashMap();
        try {
            //获取所有的触发器组
            List<String> triggerGroupNames = scheduler.getTriggerGroupNames();
            for (String groupName : triggerGroupNames) {
                //根据groupName获取匹配条件
                GroupMatcher groupMatcher = GroupMatcher.groupEquals(groupName);
                //根据groupMatcher(匹配条件)获取所有的triggerKey
                Set<TriggerKey> triggerKeySet = scheduler.getTriggerKeys(groupMatcher);
                for (TriggerKey triggerKey : triggerKeySet) {
                    //根据triggerKey获取trigger状态
                    emailQuartzJobStateMap.put(triggerKey.getName(), scheduler.getTriggerState(triggerKey).name());
                }
            }
        } catch (SchedulerException ex) {
            ex.printStackTrace();
        }
        return emailQuartzJobStateMap;
    }

    /**
     * 获取所有已完成任务的数量
     *
     * @return
     */
   public int countCompletedEmailQuartzJob(){
       //已完成的任务数量
       int completedJobNum = 0;
       try {
           //获取所有的任务组
           List<String> jobGroupNames = scheduler.getJobGroupNames();
           for (String groupName : jobGroupNames) {
               //根据groupName获取匹配条件
               GroupMatcher groupMatcher = GroupMatcher.groupEquals(groupName);
               //根据groupMatcher(匹配条件)获取所有的jobKey
               Set<JobKey> jobKeySet = scheduler.getJobKeys(groupMatcher);
               //获取所有的任务数量
               completedJobNum = jobKeySet.size();
               for (JobKey jobKey : jobKeySet) {
                   //根据jobKey获取任务拥有的触发器
                   if(!scheduler.getTriggersOfJob(jobKey).isEmpty()){
                       // 若有触发器 说明任务未完成
                       completedJobNum--;
                   }
               }
           }
       } catch (SchedulerException ex) {
           ex.printStackTrace();
       }
       return completedJobNum;
   }

    /**
     * 创建邮件的定时任务实例
     *
     * @param email 邮件实体类
     * @return
     */
    public JobDetail buildJobDetail(Email email, EmailAddress emailAddress) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("address", emailAddress.getAddress());
        jobDataMap.put("subject", email.getSubject());
        jobDataMap.put("content", email.getContent());
        jobDataMap.put("type", email.getType());
        jobDataMap.put("sendTime", email.getSendTime());
        return JobBuilder.newJob(EmailQuartzJob.class)
                .withIdentity(UUID.randomUUID().toString(), "email-quartz-jobs")
                .withDescription("send-email job")
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    /**
     * 创建定时任务的触发器
     *
     * @param jobDetail 定时任务
     * @param sendTime  发送时间
     * @return
     */
    public Trigger buildTrigger(JobDetail jobDetail, Date sendTime) {
        return TriggerBuilder.newTrigger().forJob(jobDetail)
                .withIdentity(UUID.randomUUID().toString(), "email-quartz-triggers")
                .withDescription("send-email trigger")
                .startAt(sendTime)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                .build();
    }
}
