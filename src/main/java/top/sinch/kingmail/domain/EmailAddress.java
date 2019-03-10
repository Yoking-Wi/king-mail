package top.sinch.kingmail.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import top.sinch.kingmail.domain.base.BaseBean;

import javax.validation.constraints.Email;

/**
 * 邮箱地址实体类
 *
 * @author sincH
 * @since 2019.03.05
 */
@Data
@TableName(value = "biz_email_address")
public class EmailAddress extends BaseBean {
    public EmailAddress() {
    }

    public EmailAddress(String address) {
        this.address = address;
    }

    /**
     * 邮箱地址(本项目中指接收者的邮箱地址)
     */
    @Email
    String address;
}
