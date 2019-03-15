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

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;
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
    @Value("${spring.mail.nickname}")
    String nickname;

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
//            mimeMessageHelper.setFrom(this.username, MimeUtility.encodeText(this.nickname));
//            InternetAddress address = new InternetAddress(this.username, MimeUtility.encodeText(this.nickname));
            //设置发件人别名
            mimeMessageHelper.setFrom(this.username,this.nickname);
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
            logger.error("失败：邮件发送");
            ex.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            logger.error("失败：邮件发件人设置错误");
            e.printStackTrace();
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
        try {
            // 创建定时任务实例
            JobDetail jobDetail = emailQuartzJobService.buildJobDetail(email, emailAddress);
            // 创建定时任务触发器
            Trigger trigger = emailQuartzJobService.buildTrigger(jobDetail, email.getSendTime());
            //定时任务
            scheduler.scheduleJob(jobDetail, trigger);
            logger.info("邮件将在 " + email.getSendTime() + " 发送");
        } catch (SchedulerException ex) {
            logger.error("失败：邮件定时");
            ex.printStackTrace();
        }
    }
}
