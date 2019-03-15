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
import top.sinch.kingmail.service.EmailAddressService;
import top.sinch.kingmail.service.EmailQuartzJobService;
import top.sinch.kingmail.service.EmailService;
import top.sinch.kingmail.tool.ResponseData;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 邮件控制层
 *
 * @author yoking-wi
 * @since 2019.03.04
 */
@CrossOrigin
@RequestMapping("/api/email")
@RestController
public class EmailController {
    private static Logger logger = LoggerFactory.getLogger(EmailController.class);
    @Autowired
    EmailService emailService;
    @Autowired
    EmailAddressService emailAddressService;
    @Autowired
    EmailQuartzJobService emailQuartzJobService;


    @ApiOperation(value = "发送邮件")
    @PostMapping("")
    public String send(@RequestBody @Valid EmailVO emailVO) {
        logger.info("发送邮件");
        // 若用户有输入邮箱地址
        if (emailVO.getAddress() != null && !emailVO.getAddress().trim().isEmpty()) {
            EmailAddress address = new EmailAddress();
            address.setAddress(emailVO.getAddress());
            emailAddressService.saveEmailAddress(address);
        }
        // 随机获取一个邮箱地址
        EmailAddress emailAddress = emailAddressService.getRandomly();
        // VO转DTO
        Email email = new Email(emailVO.getSubject(), emailVO.getContent(), emailVO.getType(), emailVO.getSendTime());
//        EmailAddress emailAddress = new EmailAddress(emailVO.getAddress());
        EmailDTO emailDTO = new EmailDTO(email, emailAddress);
        emailService.send(emailDTO);
        return new Gson().toJson(new ResponseData(Integer.toString(HttpStatus.OK.value()), HttpStatus.OK.getReasonPhrase(), ""));
    }

    @ApiOperation(value = "定时发送邮件")
    @PostMapping("/schedule")
    public String sendWithSchedule(@RequestBody @Valid EmailVO emailVO) {
        logger.info("定时发送邮件");
        // 若用户有输入邮箱地址
        if (emailVO.getAddress() != null && !emailVO.getAddress().trim().isEmpty()) {
            EmailAddress address = new EmailAddress();
            address.setAddress(emailVO.getAddress());
            //邮箱地址入库
            emailAddressService.saveEmailAddress(address);
        }
        // 随机获取一个邮箱地址
        EmailAddress emailAddress = emailAddressService.getRandomly();
        // VO转DTO
        Email email = new Email(emailVO.getSubject(), emailVO.getContent(), emailVO.getType(), emailVO.getSendTime());
//        EmailAddress emailAddress = new EmailAddress(emailVO.getAddress());
        EmailDTO emailDTO = new EmailDTO(email, emailAddress);
        emailService.sendWithSchedule(emailDTO);
        return new Gson().toJson(new ResponseData(Integer.toString(HttpStatus.OK.value()), HttpStatus.OK.getReasonPhrase(), ""));
    }

    @ApiOperation(value = "发送邮件给未来的自己")
    @PostMapping("/schedule/myself")
    public String sendToMyselfWithSchedule(@RequestBody @Valid EmailVO emailVO) {
        logger.info("发送邮件给未来的自己");
        EmailAddress address = new EmailAddress();
        address.setAddress(emailVO.getAddress());
        //邮箱地址入库
        emailAddressService.saveEmailAddress(address);
        // VO转DTO
        Email email = new Email(emailVO.getSubject(), emailVO.getContent(), emailVO.getType(), emailVO.getSendTime());
//        EmailAddress emailAddress = new EmailAddress(emailVO.getAddress());
        EmailDTO emailDTO = new EmailDTO(email, address);
        emailService.sendWithSchedule(emailDTO);
        return new Gson().toJson(new ResponseData(Integer.toString(HttpStatus.OK.value()), HttpStatus.OK.getReasonPhrase(), ""));
    }

    @ApiOperation(value = "获取所有定时任务状态")
    @GetMapping("/schedule/triggers-state")
    public String listEmailQuartzTriggersState() {
        logger.info("获取所有定时任务状态");
        Map emailQuartzJobStateMap = emailQuartzJobService.listEmailQuartzTriggersState();
        return new Gson().toJson(new ResponseData(Integer.toString(HttpStatus.OK.value()), HttpStatus.OK.getReasonPhrase(), emailQuartzJobStateMap));
    }

    @ApiOperation(value = "获取所有已完成任务的数量")
    @GetMapping("/schedule/completed-job-number")
    public String countCompletedEmailQuartzJob() {
        logger.info("获取所有已完成任务的数量");
        int completedJobNumber = emailQuartzJobService.countCompletedEmailQuartzJob();
        return new Gson().toJson(new ResponseData(Integer.toString(HttpStatus.OK.value()), HttpStatus.OK.getReasonPhrase(), completedJobNumber));
    }
}
