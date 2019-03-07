package top.sinch.kingmail.service;

import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import top.sinch.kingmail.dao.EmailAddressMapper;
import top.sinch.kingmail.dao.EmailMapper;
import top.sinch.kingmail.domain.Email;
import top.sinch.kingmail.domain.EmailAddress;
import top.sinch.kingmail.domain.EmailQuartzJob;
import top.sinch.kingmail.domain.dto.EmailDTO;

import java.util.*;

/**
 * 邮件服务层
 *
 * @author sincH
 * @since 2019.03.04
 */
@Service
public class EmailService {
    private static Logger logger = LoggerFactory.getLogger(EmailService.class);
    @Autowired
    JavaMailSender mailSender;
    @Autowired
    Scheduler scheduler;
    @Autowired
    EmailMapper emailMapper;
    @Autowired
    EmailAddressMapper emailAddressMapper;
    @Value("${spring.mail.username}")
    String username;

    /**
     * 发送文本邮件
     *
     * @param emailDTO 邮件数据传输对象
     */
    public void send(EmailDTO emailDTO) {
        // DTO 转 DO
        Email email = emailDTO.getEmail();
        EmailAddress emailAddress = emailDTO.getEmailAddress();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(this.username);
        message.setTo(emailAddress.getAddress());
        // email有主题且不为空字符串时
        if (email.getSubject() != null && (!email.getSubject().trim().isEmpty())) {
            message.setSubject(email.getSubject());
        } else {
            message.setSubject("国王邮件");
        }
        message.setText(email.getContent());
        // 发送邮件
        mailSender.send(message);

        //存入数据库
        emailMapper.insert(email);
        //数据库中没有该邮箱地址时
        if(null==emailAddressMapper.getByAddress(emailAddress.getAddress())){
            emailAddressMapper.insert(emailAddress);
        }
    }

    /**
     * 定时发送文本邮件
     *
     * @param emailDTO 邮件数据传输对象
     */
    public void sendWithSchedule(EmailDTO emailDTO) {
        //TODO 加入区域时间

        // DTO 转 DO
        Email email = emailDTO.getEmail();
        EmailAddress emailAddress = emailDTO.getEmailAddress();

        // 创建定时任务实例
        JobDetail jobDetail = this.buildJobDetail(email,emailAddress);
        // 创建定时任务触发器
        Trigger trigger = this.buildTrigger(jobDetail, email.getSendTime());
        try {
            //定时任务
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException ex) {
            logger.error("邮件定时失败");
            ex.printStackTrace();
        }
        logger.info("邮件将在 " + email.getSendTime() + " 发送");
    }

    /**
     * 获取所有定时任务状的状态
     *
     * @return
     */
    public Map listEmailQuartzJobStates() {
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
     * 随机获取一个邮箱地址
     * @return
     */
    public EmailAddress getRandomly(){
        //当数据量大于100行时
        if(emailAddressMapper.count()>=100){
            emailAddressMapper.getRandomlyAndFastly();
        }
        return emailAddressMapper.getRandomly();
    }

    /**
     * 保存邮箱地址
     * @param emailAddress
     */
    public void saveEmailAddress(EmailAddress emailAddress){
        //数据库中没有该邮箱地址时
        if(null==emailAddressMapper.getByAddress(emailAddress.getAddress())){
            emailAddressMapper.insert(emailAddress);
        }
    }

    /**
     * 创建邮件的定时任务实例
     *
     * @param email 邮件实体类
     * @return
     */
    private JobDetail buildJobDetail(Email email,EmailAddress emailAddress) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("address", emailAddress.getAddress());
        jobDataMap.put("subject", email.getSubject());
        jobDataMap.put("content", email.getContent());
        jobDataMap.put("sendTime",email.getSendTime());
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
    private Trigger buildTrigger(JobDetail jobDetail, Date sendTime) {
        return TriggerBuilder.newTrigger().forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), "email-quartz-triggers")
                .withDescription("send-email trigger")
                .startAt(sendTime)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                .build();
    }
}
