package com.ptu.zxk.util;

import org.apache.commons.lang.time.DateFormatUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatUtil {
    /**
     * ����ָ�����ڸ�ʽ���ַ���
     * @param pattern
     * @param date
     * @return
     */
    public static String getDate(String pattern, Date date){
        SimpleDateFormat sdf= new SimpleDateFormat(pattern);
        return sdf.format(date);
    }
}
