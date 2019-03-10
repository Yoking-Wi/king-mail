package top.sinch.kingmail.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import java.util.Date;

/**
 * 邮件 视图对象
 * 封装前端页面需要的字段
 *
 * @author sincH
 * @since 2019年3月6日 10:29:32
 */
@Data
public class EmailVO {
    /**
     * 邮箱地址(本项目中指接收者的邮箱地址)
     */
    @Email
    private String address;

    /**
     * 邮件主题
     */
    private String subject;

    /**
     * 邮件内容
     */
    private String content;

    /**
     * 邮件类型
     * 可设置0为文本 1为html等
     * 本项目中仅有html邮件
     */
    private String type;

    /**
     * 邮件发送时间
     */
    @ApiModelProperty(required = true, example = "2019-03-05 12:24:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date sendTime;
}
