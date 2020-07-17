package com.liu.study.mq.kafka.common;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @desc
 * @author Liuweian
 * @version 1.0.0
 * @createTime 2020/4/23 15:19
 */
public class MessageUtils {

    public static String getMessage(String producerName) {
        String dateString = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss").format(new Date());
        String result = "【生产者】：" + producerName + "；时间：" + dateString;
        return  result;
    }

}
