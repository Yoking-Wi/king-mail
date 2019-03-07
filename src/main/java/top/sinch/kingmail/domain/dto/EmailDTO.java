package top.sinch.kingmail.domain.dto;

import lombok.Data;
import top.sinch.kingmail.domain.Email;
import top.sinch.kingmail.domain.EmailAddress;
import top.sinch.kingmail.domain.bo.EmailBO;

/**
 * 邮件 数据传输对象
 * 仅 封装多个DO对象
 * 在controller层 VO转DTO
 * 在service层 DTO转DO 或者 DTO转BO，BO再转DO
 * @author sincH
 * @since 2019年3月6日 10:25:36
 */
@Data
public class EmailDTO {
    public EmailDTO(){}

    // VO(属性) 转 DTO
    public EmailDTO(Email email,EmailAddress emailAddress){
        this.email = email;
        this.emailAddress = emailAddress;
    }

    // BO 转 DTO
    public EmailDTO(EmailBO emailBO){
        this.email = emailBO.getEmail();
        this.emailAddress = emailBO.getEmailAddress();
    }

    /**
     * 邮件
     */
    private Email email;

    /**
     * 邮箱地址
     */
    private EmailAddress emailAddress;
}
