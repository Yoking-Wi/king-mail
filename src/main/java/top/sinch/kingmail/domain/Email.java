package top.sinch.kingmail.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.sinch.kingmail.domain.base.BaseBean;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * 邮件实体类
 *
 * @author yoking-wi
 * @since 2019年3月11日 13:14:28
 */
@Data
@TableName(value = "biz_email")
public class Email extends BaseBean {
    public Email() {
    }

    public Email(String subject, String content, String type, Date sendTime) {
        this.subject = subject;
        this.content = content;
        this.type = type;
        this.sendTime = sendTime;
    }

    /**
     * 邮件主题
     */
    @TableField(exist = false)
    private String subject;

    /**
     * 邮件内容
     */
    @NotBlank
    @TableField(exist = false)
    private String content;

    /**
     * 邮件类型
     * 可设置 0为文本 1为html等
     * 本项目中仅有html邮件
     */
    @NotBlank
    @Pattern(regexp = "html|simple",message = "type必须为html或simple")
    @TableField(exist = false)
    private String type;

    /**
     * 邮件发送时间
     */
    @ApiModelProperty(required = true, example = "2019-03-05 12:24:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date sendTime;
}
