package top.sinch.kingmail.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import top.sinch.kingmail.domain.Email;

/**
 * 邮件 数据访问层
 *
 * @author sincH
 * @since 2019年3月5日 20:23:24
 */
@Repository
public interface EmailMapper extends BaseMapper<Email> {
}
