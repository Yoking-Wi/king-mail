package top.sinch.kingmail.domain.bo;

import lombok.Data;
import top.sinch.kingmail.domain.Email;
import top.sinch.kingmail.domain.EmailAddress;

/**
 * 邮件 业务对象
 * 封装 多个DO对象及其业务逻辑
 * 而 DTO 仅封装多个DO对象 用于数据传输
 *
 * @author yoking-wi
 * @since 2019年3月6日 11:29:40
 */
@Data
public class EmailBO {
    /**
     * 邮件
     */
    private Email email;

    /**
     * 邮箱地址
     */
    private EmailAddress emailAddress;

    public EmailBO() {
    }

    public EmailBO(Email email, EmailAddress emailAddress) {
        this.email = email;
        this.emailAddress = emailAddress;
    }

    /**
     * 获取EmailBO对象；此对象拥有Email和EmailAddress的实例
     * 节省新建EmailBO对象时的麻烦：
     * Email email = new Email();
     * EmailAddress emailAddress = new EmailAddress();
     */
    public static EmailBO getInstanceWithEmailAndAddress() {
        EmailBO emailBO = new EmailBO();
        emailBO.email = new Email();
        emailBO.emailAddress = new EmailAddress();
        return emailBO;
    }
}
