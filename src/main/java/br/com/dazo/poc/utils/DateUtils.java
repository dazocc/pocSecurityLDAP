package br.com.dazo.poc.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    public static String localDateTimeToString(LocalDateTime data, String formmatter){

        return  data.format(DateTimeFormatter.ofPattern(formmatter));
    }

    public static String localDateTimeToString(LocalDateTime data){
        return  localDateTimeToString(data, "yyyy-MM-dd'T'HH:mm:ss.S");
    }

}
