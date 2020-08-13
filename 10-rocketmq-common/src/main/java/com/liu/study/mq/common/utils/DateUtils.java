package com.liu.study.mq.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author lwa
 * @version 1.0.0
 * @createTime 2020/8/11 15:33
 */
public class DateUtils {

    public final static String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     *
     * @param date
     * @return
     */
    public static String parseDateToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_PATTERN);
        return sdf.format(date);
    }

}