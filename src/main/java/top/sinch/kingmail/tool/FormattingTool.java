package top.sinch.kingmail.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 格式化工具
 * @author sincH
 * @since 2019年3月6日 12:30:03
 */
public class FormattingTool {
    private static Logger logger = LoggerFactory.getLogger(FormattingTool.class);
    /**
     * 日期字符串转日期对象(java.util.Date)
     * @param dateStr
     */
    public static Date transformString2Date(String dateStr){
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sim.parse(dateStr);
        } catch (ParseException e) {
            logger.error("失败：日期字符串转日期对象(java.util.Date)");
            e.printStackTrace();
        }
        return date;
    }
}
