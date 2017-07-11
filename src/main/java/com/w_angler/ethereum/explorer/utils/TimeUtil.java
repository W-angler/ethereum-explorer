package com.w_angler.ethereum.explorer.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 时间处理的工具，用于转化不同的时间API
 * Created by wangle on 17-6-8.
 */
public class TimeUtil {
    private static final DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    /**
     * Date转换为LocalDateTime
     *
     * @param date 待转换的Date
     * @return 转换后的LocalDateTime
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * Date转换为LocalDate
     *
     * @param date 待转换的Date
     * @return 转换后的LocalDate
     */
    public static LocalDate dateToLocalDate(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Date转换为LocalTime
     *
     * @param date 待转换的Date
     * @return 转换后的LocalTime
     */
    public static LocalTime dateToLocalTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).toLocalTime();
    }

    /**
     * LocalDateTime转换为Date
     *
     * @param localDateTime 待转换的LocalDateTime
     * @return 转换后的Date
     */
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * LocalDate转换为Date
     *
     * @param localDate 待转换的LocalDate
     * @return 转换后的Date
     */
    public static Date localDateToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * LocalTime转换为Date
     *
     * @param localTime 待转换的LocalTime
     * @return 转换后的Date
     */
    public static Date localTimeToDate(LocalTime localTime) {
        return Date.from(LocalDateTime.of(LocalDate.now(), localTime).atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 转换为格式为“yyyyMMddHHmmss”的字符串
     * @param localDateTime 时间
     * @return 转换后的BigInteger
     */
    public static String localDateTimeToString(LocalDateTime localDateTime){
        return localDateTime.format(dateTimeFormatter);
    }
    /**
     * 转换为格式为“yyyyMMddHHmmss”的BigInteger
     * @param timeString 时间
     * @return 转换后的BigInteger
     */
    public static LocalDateTime stringToLocalDateTime(String timeString){
        return LocalDateTime.parse(timeString,dateTimeFormatter);
    }
}
