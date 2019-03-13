package top.sinch.kingmail.domain;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import top.sinch.kingmail.domain.bo.EmailBO;
import top.sinch.kingmail.domain.dto.EmailDTO;
import top.sinch.kingmail.service.EmailService;

import java.util.Date;

/**
 * Email定时任务
 *
 * @author yoking-wi
 * @since 2019年3月5日 19:51:59
 */
@Component
public class EmailQuartzJob extends QuartzJobBean {
    @Autowired
    EmailService emailService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
        String subject = jobDataMap.getString("subject");
        String content = jobDataMap.getString("content");
        String address = jobDataMap.getString("address");
        String type = jobDataMap.getString("type");
        Date sendTime = (Date) jobDataMap.get("sendTime");

        EmailBO emailBO = EmailBO.getInstanceWithEmailAndAddress();
        emailBO.getEmail().setSubject(subject);
        emailBO.getEmail().setContent(content);
        emailBO.getEmail().setType(type);
        emailBO.getEmail().setSendTime(sendTime);
        emailBO.getEmailAddress().setAddress(address);
        // BO 转 DTO
        EmailDTO emailDTO = new EmailDTO(emailBO);
        // 发送邮件
        emailService.send(emailDTO);
    }
}
