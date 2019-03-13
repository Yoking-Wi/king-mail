package top.sinch.kingmail.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 格式化工具
 *
 * @author yoking-wi
 * @since 2019年3月6日 12:30:03
 */
public class FormatTool {
    private static Logger logger = LoggerFactory.getLogger(FormatTool.class);

    /**
     * 日期字符串转日期对象(java.util.Date)
     *
     * @param dateStr
     */
    public static Date transformString2Date(String dateStr) {
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

    /**
     * 格式化字符串 加上双引号
     *
     * 将文本内容，诸如以下
     *  xxx
     *  yyy
     *  转变为带双引号 且 以逗号分隔
     *  "xxx",
     *  "yyy"
     */
    public static void formatStringWithDoubleQuotes(){
        List<String> strList = new ArrayList<String>();
        String inputFile = "C:\\Users\\sincH\\Desktop\\sensitive-words.txt"; //读取的文件的路径
        String outputFile = "C:\\Users\\sincH\\Desktop\\sensitive-words-new.txt"; //写入的文件的路径
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter =null;
        try {
            bufferedReader = new BufferedReader(new FileReader(inputFile));
            String lineTxt = "";
            String newStr = "";
            while((lineTxt=bufferedReader.readLine())!=null){
//                lineTxt=lineTxt.replace(",","");
                newStr = "\""+lineTxt+"\",";
                strList.add(newStr);
            }
            bufferedWriter = new BufferedWriter(new FileWriter(outputFile));
            for(int i =0;i<strList.size();i++){
                bufferedWriter.write(strList.get(i));
                if((i+1)%20==0){ // 是20的倍数时 换行输出
                    bufferedWriter.newLine();
                }
            }
            // 刷新缓存区，将缓存区内容输入文件中
            bufferedWriter.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 释放资源
                bufferedReader.close();
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
//        formatStringWithDoubleQuotes();
    }
}
