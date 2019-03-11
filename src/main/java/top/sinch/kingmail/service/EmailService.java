package top.sinch.kingmail.service;

import org.quartz.*;
import org.quartz.core.QuartzScheduler;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import top.sinch.kingmail.dao.EmailAddressMapper;
import top.sinch.kingmail.dao.EmailMapper;
import top.sinch.kingmail.domain.Email;
import top.sinch.kingmail.domain.EmailAddress;
import top.sinch.kingmail.domain.EmailQuartzJob;
import top.sinch.kingmail.domain.dto.EmailDTO;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.*;

/**
 * 邮件服务层
 *
 * @author yoking-wi
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
    EmailAddressService emailAddressService;
    @Autowired
    EmailQuartzJobService emailQuartzJobService;
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;
    @Value("${spring.mail.username}")
    String username;

    /**
     * 发送邮件
     *
     * @param emailDTO 邮件数据传输对象
     */
    public void send(EmailDTO emailDTO) {
        // DTO 转 DO
        Email email = emailDTO.getEmail();
        EmailAddress emailAddress = emailDTO.getEmailAddress();

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message);

        try {
            mimeMessageHelper.setFrom(this.username);
            mimeMessageHelper.setTo(emailAddress.getAddress());
            // email有主题且不为空字符串时
            if (email.getSubject() != null && (!email.getSubject().trim().isEmpty())) {
                mimeMessageHelper.setSubject(email.getSubject());
            } else {
                mimeMessageHelper.setSubject("一封书信");
            }
            // 判断邮件类型
            if ("html".equals(email.getType())) {
                mimeMessageHelper.setText(email.getContent(), true);
            }
            if ("simple".equals(email.getType())) {
                mimeMessageHelper.setText(email.getContent());
            }
            // 发送邮件
            mailSender.send(message);

            //存入数据库
            emailMapper.insert(email);
            emailAddressService.saveEmailAddress(emailAddress);

            // websocket 推送消息 已完成的任务数量
            simpMessagingTemplate.convertAndSend("/topic/completed-job-number", emailQuartzJobService.countCompletedEmailQuartzJob());
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 定时发送邮件
     *
     * @param emailDTO 邮件数据传输对象
     */
    public void sendWithSchedule(EmailDTO emailDTO) {
        //TODO 加入区域时间

        // DTO 转 DO
        Email email = emailDTO.getEmail();
        EmailAddress emailAddress = emailDTO.getEmailAddress();

        // 创建定时任务实例
        JobDetail jobDetail = emailQuartzJobService.buildJobDetail(email, emailAddress);
        // 创建定时任务触发器
        Trigger trigger = emailQuartzJobService.buildTrigger(jobDetail, email.getSendTime());
        try {
            //定时任务
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException ex) {
            logger.error("邮件定时失败");
            ex.printStackTrace();
        }
        logger.info("邮件将在 " + email.getSendTime() + " 发送");
    }
}
