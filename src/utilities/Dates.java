/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author Craig Lohrman
 */
public class Dates {
    
    /**
     * Gets the current date, formats it to the layout needed, then returns it as a Calendar.
     * @return The current date as a Calendar type.
     * @throws ParseException 
     */
    public Calendar currentDateFinder() throws ParseException{
        DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        
        LocalDateTime currentDateLDT = LocalDateTime.now();
        String currentDate = currentDateLDT.format(formatDate);
        
        Date newDate = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH).parse(currentDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(newDate);
        
        return calendar;
    }
    
    public Calendar convertDateToCalendar(LocalDate sentDate) throws ParseException{
        DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        
        String currentDate = sentDate.format(formatDate);
        
        Date newDate = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH).parse(currentDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(newDate);
        
        return calendar;
    }
    
    /**
     * Gets the current date, formats it to the layout needed, subtracts 30 days, then returns it as a Calendar.
     * @return The current date as a Calendar type.
     * @throws ParseException 
     */
    public Calendar minus30DaysDateFinder() throws ParseException{
        DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        
        LocalDateTime currentDateLDT = LocalDateTime.now();
        String currentDate = currentDateLDT.format(formatDate);
        
        Date newDate = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH).parse(currentDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(newDate);
        calendar.add(Calendar.DAY_OF_MONTH, -30);
        return calendar;
    }
    
    /**
     * Gets the current date, formats it to the layout needed, subtracts 90 days, then returns it as a Calendar.
     * @return The current date as a Calendar type.
     * @throws ParseException 
     */
    public Calendar minus90DaysDateFinder() throws ParseException{
        DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        
        LocalDateTime currentDateLDT = LocalDateTime.now();
        String currentDate = currentDateLDT.format(formatDate);
        
        Date newDate = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH).parse(currentDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(newDate);
        calendar.add(Calendar.DAY_OF_MONTH, -90);
        return calendar;
    }
}
