package top.sinch.kingmail.tool;

import lombok.Data;

/**
 * 响应数据类
 * 包装后端数据返回给前端显示
 * @author sincH
 * @since 2019.03.04
 */
@Data
public class ResponseData {
    /**
     * 状态码
     */
    private String code;

    /**
     * 消息
     */
    private String msg;

    /**
     * 数据
     */
    private  Object data;

    public ResponseData(String code,String msg,Object data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
