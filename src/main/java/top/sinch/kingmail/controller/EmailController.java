package top.sinch.kingmail.controller;

import com.google.gson.Gson;
import io.swagger.annotations.ApiOperation;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import top.sinch.kingmail.domain.Email;
import top.sinch.kingmail.domain.EmailAddress;
import top.sinch.kingmail.domain.dto.EmailDTO;
import top.sinch.kingmail.domain.vo.EmailVO;
import top.sinch.kingmail.service.EmailService;
import top.sinch.kingmail.tool.ResponseData;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 邮件控制层
 *
 * @author sincH
 * @since 2019.03.04
 */
@CrossOrigin
@RequestMapping("/api/email")
@RestController
public class EmailController {
    private static Logger logger = LoggerFactory.getLogger(EmailController.class);
    @Autowired
    EmailService emailService;

    @ApiOperation(value = "发送文本邮件")
    @PostMapping("")
    public String send(@RequestBody @Valid EmailVO emailVO) {
        logger.info("发送文本邮件");
        // 若用户有输入邮箱地址
        if (emailVO.getAddress() != null && !emailVO.getAddress().trim().isEmpty()) {
            EmailAddress address = new EmailAddress();
            address.setAddress(emailVO.getAddress());
            emailService.saveEmailAddress(address);
        }
        // 随机获取一个邮箱地址
        EmailAddress emailAddress = emailService.getRandomly();
        // VO转DTO
        Email email = new Email(emailVO.getSubject(), emailVO.getContent(), emailVO.getSendTime());
//        EmailAddress emailAddress = new EmailAddress(emailVO.getAddress());
        EmailDTO emailDTO = new EmailDTO(email, emailAddress);
        emailService.send(emailDTO);
        return new Gson().toJson(new ResponseData(Integer.toString(HttpStatus.OK.value()), HttpStatus.OK.getReasonPhrase(), ""));
    }

    @ApiOperation(value = "定时发送文本邮件")
    @PostMapping("/schedule")
    public String sendWithSchedule(@RequestBody @Valid EmailVO emailVO) {
        logger.info("定时发送文本邮件");
        // 若用户有输入邮箱地址
        if (emailVO.getAddress() != null && !emailVO.getAddress().trim().isEmpty()) {
            EmailAddress address = new EmailAddress();
            address.setAddress(emailVO.getAddress());
            emailService.saveEmailAddress(address);
        }
        // 随机获取一个邮箱地址
        EmailAddress emailAddress = emailService.getRandomly();
        // VO转DTO
        Email email = new Email(emailVO.getSubject(), emailVO.getContent(), emailVO.getSendTime());
//        EmailAddress emailAddress = new EmailAddress(emailVO.getAddress());
        EmailDTO emailDTO = new EmailDTO(email, emailAddress);

        if (!email.getSendTime().before(new Date())) {
            emailService.sendWithSchedule(emailDTO);
            return new Gson().toJson(new ResponseData(Integer.toString(HttpStatus.OK.value()), HttpStatus.OK.getReasonPhrase(), ""));
        }
        return new Gson().toJson(new ResponseData(Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), ""));
    }

    @ApiOperation(value = "获取所有定时任务的状态")
    @GetMapping("/schedule/states")
    public String listEmailQuartzJobStates() {
        logger.info("获取所有定时任务的状态");
        Map emailQuartzJobStateMap = emailService.listEmailQuartzJobStates();
        return new Gson().toJson(new ResponseData(Integer.toString(HttpStatus.OK.value()), HttpStatus.OK.getReasonPhrase(), emailQuartzJobStateMap));
    }
}
