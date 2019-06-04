/**
 * 文件名称:          		DateUtils.java
 * 版权所有@ 2019-2020 	无锡爱超信息技术有限公司
 * 编译器:           		JDK1.8
 */

package com.ljj.tcc.core.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * 新旧日期类转换
 * 
 * Version 1.0.0
 * 
 * @author liangjinjing
 * 
 * Date 2019-06-04 16:44
 * 
 */
public class DateUtils {

    /**
     * LocalDateTime 转换 Date
     * 
     * @param ldt
     * @return
     */
    public static Date localDateTimeToDate(LocalDateTime ldt) {
        return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 
     * @param date
     * @return
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * 
     * @param date
     * @return
     */
    public static LocalDateTime dateToLocalDateTimeBySqlDate(java.sql.Date sqlDate) {
        Date date = new Date(sqlDate.getTime());
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }
    
    
    /**
     * 
     * @param date
     * @return
     */
    public static LocalDate dateToLocalDate(Date date) {
        return dateToLocalDateTime(date).toLocalDate();
    }
    
    /**
     * 
     * @param date
     * @return
     */
    public static LocalTime dateToLocalTime(Date date) {
        return dateToLocalDateTime(date).toLocalTime();
    }
    
}
