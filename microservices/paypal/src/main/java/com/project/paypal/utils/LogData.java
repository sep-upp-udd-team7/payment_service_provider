package com.project.paypal.utils;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class LogData {

    private String message;
    private String date;

    public LogData(String message) {
        this.message = message;
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("CET"));
        String formattedDate = sdf.format(date);
        this.date = formattedDate;
    }

    @Override
    public String toString() {
        return message + "|" + date;
    }
}
