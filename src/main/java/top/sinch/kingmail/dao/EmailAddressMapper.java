package top.sinch.kingmail.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import top.sinch.kingmail.domain.EmailAddress;

/**
 * 邮箱地址 数据访问层
 * @author sincH
 * @since 2019年3月6日 09:32:52
 */
@Repository
public interface EmailAddressMapper extends BaseMapper<EmailAddress> {
    @Select("SELECT id,address,create_time,update_time FROM biz_email_address WHERE address = #{address}")
    EmailAddress getByAddress(String address);

    @Select("SELECT * FROM biz_email_address AS bea JOIN(SELECT ROUND(RAND() * ((SELECT MAX(id) FROM biz_email_address)-(SELECT MIN(id) FROM biz_email_address))+(SELECT MIN(id) FROM biz_email_address)) AS id) AS bea2 WHERE bea.id>=bea2.id ORDER BY bea.id LIMIT 1 ")
    EmailAddress getRandomlyAndFastly();

    @Select("SELECT * FROM biz_email_address ORDER BY RAND() LIMIT 1")
    EmailAddress getRandomly();

    @Select("SELECT COUNT(*) FROM biz_email_address")
    int count();
}
