package top.sinch.kingmail.domain.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.util.Date;

/**
 * 基础实体类
 *
 * @author sincH
 * @since 2019年3月5日 19:52:56
 */
public class BaseBean {
    /**
     * 非业务主键id
     */
    @TableId(value = "id", type = IdType.UUID)
    String id;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    Date updateTime;
}
