package com.mes.mesBackend.helper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Constants {
    public static final String YYMMDDHHMMSSSS = "yyMMddHHmmssSS";
    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String ASIA_SEOUL = "Asia/Seoul";
    public static final String YYMMDD = "yyMMdd";
    public static final String YYMM = "yyMM";
    public static final String LOT_DEFAULT_SEQ = String.format("%04d", 0);
    public static final String PRODUCT_ITEM_ACCOUNT = "완제품";
    public static final String MONGO_TEMPLATE = "mongoTemplate";
    public static final String NOW_YYMMDD = LocalDate.now().format(DateTimeFormatter.ofPattern(YYMMDD));
    public static final String YYYYMMDD_HHMMSS_SSS = "yyyyMMdd_HHmmss_SSS";
    public static final String DECIMAL_POINT_2 = "%.2f";
    public static final String PERCENT = "%";
}
