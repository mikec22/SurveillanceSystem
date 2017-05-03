package com.surveillance.surveillancesystem.Tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public final class DateTools {

    public static Date StringToDate(String string) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return dateFormat.parse(string);
    }

    public static String DateToString(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }
}
