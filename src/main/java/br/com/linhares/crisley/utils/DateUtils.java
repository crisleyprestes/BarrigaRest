package br.com.linhares.crisley.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static String getDataDiferencaDias(Integer qtdDias){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, qtdDias);

        return getDataFormatada(calendar.getTime());
    }

    public static String getDataFormatada (Date data){
        DateFormat format = new SimpleDateFormat("dd/MM/YYYY");

        return format.format(data);
    }
}
