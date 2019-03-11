package top.sinch.kingmail.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

/**
 * 自定义元数据对象处理器
 * 用于自动填充字段
 *
 * @author yoking-wi
 * @since 2019年3月5日 19:58:20
 */
@Component
public class CustomMetaObjectHandler implements MetaObjectHandler {
    private static Logger logger = LoggerFactory.getLogger(CustomMetaObjectHandler.class);

    @Override
    public void insertFill(MetaObject metaObject) {
        logger.info("start insert fill ....");
        Date insertTime = new Date();
        this.setInsertFieldValByName("createTime", insertTime, metaObject);
        this.setInsertFieldValByName("updateTime", insertTime, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        logger.info("start update fill ....");
        this.setUpdateFieldValByName("updateTime", new Date(), metaObject);
    }
}
