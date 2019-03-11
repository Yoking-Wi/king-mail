package top.sinch.kingmail.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.sinch.kingmail.dao.EmailAddressMapper;
import top.sinch.kingmail.domain.EmailAddress;

/**
 * 邮件地址 服务层
 *
 * @author yoking-wi
 * @since 2019年3月10日 17:42:57
 */
@Service
public class EmailAddressService {
    @Autowired
    EmailAddressMapper emailAddressMapper;

    /**
     * 随机获取一个邮箱地址
     *
     * @return
     */
    public EmailAddress getRandomly() {
        //当数据量大于100行时
        if (emailAddressMapper.count() >= 100) {
            emailAddressMapper.getRandomlyAndFastly();
        }
        return emailAddressMapper.getRandomly();
    }

    /**
     * 保存邮箱地址
     *
     * @param emailAddress
     */
    public void saveEmailAddress(EmailAddress emailAddress) {
        //数据库中没有该邮箱地址时
        if (null == emailAddressMapper.getByAddress(emailAddress.getAddress())) {
            emailAddressMapper.insert(emailAddress);
        }
    }
}
