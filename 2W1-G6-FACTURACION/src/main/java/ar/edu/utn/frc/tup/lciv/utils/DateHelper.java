package ar.edu.utn.frc.tup.lciv.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {
    public Date parseDate(String dateStr) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }
    public String parseDateToString(Date date) {
        return null;
    }
}
