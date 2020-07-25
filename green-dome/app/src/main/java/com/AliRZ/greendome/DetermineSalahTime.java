package com.AliRZ.greendome;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.Year;
import java.util.Calendar;

@RequiresApi(api = Build.VERSION_CODES.O)
public class DetermineSalahTime {
    final static float equationOfTimePeriodRadians= 6.283F;
    static double timeDifference=0;
    int zuhrTimeHour;
    int zuhrTimeMinute;
    String zuhrTimeString;
    double timeDifferenceTotal;
    double timeDifferenceHour;
    double timeDifferenceMinute;
    String requirement;

    private double getDayVariables (Calendar userCalendar, String requirement){
        final int  dayOfYear = userCalendar.get(Calendar.DAY_OF_YEAR);
        final int currentYear = Year.now().getValue();
        final int yearLength = Year.of(currentYear).length();
        final double daysPerRadian = yearLength / equationOfTimePeriodRadians;
        final double currentRadians = dayOfYear / daysPerRadian;
        final double equationOfTimeFunction = (-7.655 *(Math.sin(currentRadians))) + (9.873 * (Math.sin((2*currentRadians)+3.588)));
        final double equationOfTimeHour = equationOfTimeFunction/60;
        if (requirement.equals("dayOfYear")){
            return dayOfYear;
        }else if (requirement.equals("yearLength")){
            return yearLength;
        } else{
            return equationOfTimeHour;
        }
    }

    private double Declination (double yearLength, int dayOfYear){
        // The Declination Angle of the sun at any day can be calculated by determining the number of degrees that the earth has rotated since the last winter solstice
        // and multiplying the cosine of said value by the tilt of the Earth, at -23.45 degrees
        double earthTilt = Math.toRadians(-23.45); // Converts to radians as the Cosine function returns a Radian value
        double declinationPartB = Math.toRadians((360/yearLength) * (dayOfYear+10)); // Days of the year are converted into degrees and multiplied by the current day of the year to find the angle of the earth since the last winter solstice
        double declinationAngle = (earthTilt * Math.cos(declinationPartB));
        return declinationAngle;
    }

    private double timeDifferenceMethod (double horizonAngle, double declinationAngle, double userLatitude){
        double userLatitudeRadians = Math.toRadians(userLatitude);
        double timeDifference = (Math.acos (((-Math.sin(horizonAngle))-(Math.sin(userLatitudeRadians)*Math.sin(declinationAngle)))/ (Math.cos(userLatitudeRadians)*Math.cos(declinationAngle)))/15);
        return timeDifference;
    }

    private String zuhrTime (double userLongitude, double userTimezoneOffset, Calendar userCalendar){
        requirement = "equationOfTimeHour";
        double equationOfTimeHour = getDayVariables(userCalendar, requirement);
        double zuhrTimeTotal = (12 + userTimezoneOffset - (userLongitude / 15) - equationOfTimeHour);
        double zuhrTimeHour = Math.floor(zuhrTimeTotal);
        double zuhrTimeMinute = (zuhrTimeTotal - zuhrTimeHour) *60;
        zuhrTimeMinute = Math.ceil(zuhrTimeMinute);
        int zuhrTimeMinuteInteger = (int) Math.round(zuhrTimeMinute);
        int zuhrTimeHourInteger = (int) Math.round(zuhrTimeHour);
        String zuhrTimeConfirmation = zuhrTimeHourInteger+":"+zuhrTimeMinuteInteger;
        return zuhrTimeConfirmation;
    }

    private String timeDifference (double horizonAngle, double declinationAngle, double userLatitude) {
        timeDifference = timeDifferenceMethod(horizonAngle, declinationAngle, userLatitude);
        timeDifferenceTotal = Math.toDegrees(timeDifference);
        timeDifferenceHour = Math.floor(timeDifferenceTotal);
        timeDifferenceMinute = (timeDifferenceTotal - timeDifferenceHour) * 60;
        timeDifferenceMinute = Math.ceil (timeDifferenceMinute);
        int timeDifferenceHourInteger = (int) Math.round(timeDifferenceHour);
        int timeDifferenceMinuteInteger = (int) Math.round(timeDifferenceMinute);
        String timeDifference = timeDifferenceHourInteger+":"+timeDifferenceMinuteInteger;
        return timeDifference;
    }

    public String sunriseClock (double userLatitude, double userLongitude, int userTimeZoneOffset, Calendar userCalendar) {
        double horizonAngle = Math.toRadians(0.833333);
        requirement = "yearLength";
        int yearLength = (int) getDayVariables(userCalendar, requirement);
        requirement = "dayOfYear";
        int dayOfYear = (int) getDayVariables(userCalendar, requirement);
        double declinationAngle = Declination(yearLength, dayOfYear);

        zuhrTimeString = zuhrTime(userLongitude, userTimeZoneOffset, userCalendar);
        zuhrTimeHour = Integer.parseInt(zuhrTimeString.substring(0, zuhrTimeString.indexOf(':')));
        zuhrTimeMinute = Integer.parseInt(zuhrTimeString.substring(zuhrTimeString.indexOf(':')+1));

        String timeDifference = timeDifference(horizonAngle, declinationAngle, userLatitude);
        timeDifferenceHour = Double.parseDouble(timeDifference.substring(0, timeDifference.indexOf(':')));
        timeDifferenceMinute = Double.parseDouble(timeDifference.substring(timeDifference.indexOf(':')+1));

        int sunriseHour = (int) (zuhrTimeHour - timeDifferenceHour);
        int sunriseMin = (int) (zuhrTimeMinute - timeDifferenceMinute);
        String sunriseMinString;
        String sunriseResponse;

        if (sunriseMin < 0){
            sunriseHour = sunriseHour - 1;
            sunriseMin = sunriseMin + 60;
        }
        if (sunriseMin < 10){
            sunriseMinString = "0"+sunriseMin;
        }else{
            sunriseMinString = String.valueOf(sunriseMin);
        }
        return sunriseHour + " : " + sunriseMinString + " am";
    }

    public String sunsetClock (double userLatitude, double userLongitude, int userTimeZoneOffset, Calendar userCalendar) {
        requirement = "yearLength";
        int yearLength = (int) getDayVariables(userCalendar, requirement);
        requirement = "dayOfYear";
        int dayOfYear = (int) getDayVariables(userCalendar, requirement);
        double horizonAngle = Math.toRadians(0.833333);
        double declinationAngle = Declination(yearLength, dayOfYear);

        zuhrTimeString = zuhrTime(userLongitude, userTimeZoneOffset, userCalendar);
        zuhrTimeHour = Integer.parseInt(zuhrTimeString.substring(0, zuhrTimeString.indexOf(':')));
        zuhrTimeMinute = Integer.parseInt(zuhrTimeString.substring(zuhrTimeString.indexOf(':')+1));

        String timeDifference = timeDifference(horizonAngle, declinationAngle, userLatitude);
        timeDifferenceHour = Double.parseDouble(timeDifference.substring(0, timeDifference.indexOf(':')));
        timeDifferenceMinute = Double.parseDouble(timeDifference.substring(timeDifference.indexOf(':')+1));

        int sunsetHour = (int) (zuhrTimeHour + timeDifferenceHour);
        int sunsetMin = (int) (zuhrTimeMinute + timeDifferenceMinute);
        String sunsetMinString = "null";
        String sunsetResponse = "null";

        if (sunsetMin > 60){
            sunsetHour = sunsetHour +1;
            sunsetMin = sunsetMin - 60;
        }
        if (sunsetHour > 12){
            sunsetHour = sunsetHour - 12;
        }
        if (sunsetMin < 10){
            sunsetMinString = "0"+sunsetMin;
        }else{
            sunsetMinString = String.valueOf(sunsetMin);
        }
        return sunsetHour + " : " + sunsetMinString + " pm";
    }

    public String fajrClock (double userLatitude, double userLongitude, int userTimeZoneOffset, Calendar userCalendar) {
        requirement = "yearLength";
        int yearLength = (int) getDayVariables(userCalendar, requirement);
        requirement = "dayOfYear";
        int dayOfYear = (int) getDayVariables(userCalendar, requirement);
        double horizonAngle = Math.toRadians(16);
        double declinationAngle = Declination(yearLength, dayOfYear);
        zuhrTimeString = zuhrTime(userLongitude, userTimeZoneOffset, userCalendar);
        zuhrTimeHour = Integer.parseInt(zuhrTimeString.substring(0, zuhrTimeString.indexOf(':')));
        zuhrTimeMinute = Integer.parseInt(zuhrTimeString.substring(zuhrTimeString.indexOf(':')+1));

        String timeDifference = timeDifference(horizonAngle, declinationAngle, userLatitude);
        timeDifferenceHour = Double.parseDouble(timeDifference.substring(0, timeDifference.indexOf(':')));
        timeDifferenceMinute = Double.parseDouble(timeDifference.substring(timeDifference.indexOf(':')+1));

        int fajrHour = (int) (zuhrTimeHour - timeDifferenceHour);
        int fajrMin = (int) (zuhrTimeMinute - timeDifferenceMinute);
        String fajrMinString;
        String fajrResponse;

        if (fajrMin < 0){
            fajrHour = fajrHour - 1;
            fajrMin = fajrMin + 60;
        }
        if (fajrMin > 60){
            fajrHour = fajrHour +1;
            fajrMin = fajrMin - 60;
        }
        if ((fajrMin - 5) >= 0){
            System.out.println("The Imsak Time is : "+ fajrHour + " : " + (fajrMin - 5) + " am");
        }else{
            System.out.println("The Imsak Time is : "+ (fajrHour-1) + " : " + (fajrMin + 55) + " am");
        }

        if (fajrMin <= 9) {
            fajrMinString = "0"+fajrMin;
        }else{
            fajrMinString = String.valueOf(fajrMin);
        }
        fajrResponse = fajrHour + " : " + fajrMinString + " am";
        return fajrResponse;
    }

    public String maghribCalculator (double userLatitude, double userLongitude, int userTimeZoneOffset, Calendar userCalendar) {
        requirement = "yearLength";
        int yearLength = (int) getDayVariables(userCalendar, requirement);
        requirement = "dayOfYear";
        int dayOfYear = (int) getDayVariables(userCalendar, requirement);
        double horizonAngle = Math.toRadians(3);
        double declinationAngle = Declination(yearLength, dayOfYear);

        String timeDifference = timeDifference(horizonAngle, declinationAngle, userLatitude);
        timeDifferenceHour = Double.parseDouble(timeDifference.substring(0, timeDifference.indexOf(':')));
        timeDifferenceMinute = Double.parseDouble(timeDifference.substring(timeDifference.indexOf(':')+1));

        zuhrTimeString = zuhrTime(userLongitude, userTimeZoneOffset, userCalendar);
        zuhrTimeHour = Integer.parseInt(zuhrTimeString.substring(0, zuhrTimeString.indexOf(':')));
        zuhrTimeMinute = Integer.parseInt(zuhrTimeString.substring(zuhrTimeString.indexOf(':')+1));

        int maghribHour = (int) (zuhrTimeHour + timeDifferenceHour);
        int maghribMin = (int) (zuhrTimeMinute + timeDifferenceMinute);
        String maghribMinString;
        String maghribResponse;
        if (maghribMin < 0){
            maghribHour = maghribHour - 1;
            maghribMin = maghribMin + 60;
        }
        if (maghribMin > 60){
            maghribHour = maghribHour +1;
            maghribMin = maghribMin - 60;
        }
        if (maghribHour > 12) {
            maghribHour = maghribHour - 12;
        }

        if (maghribMin <= 9){
            maghribMinString = "0"+maghribMin;
        }else{
            maghribMinString = String.valueOf(maghribMin);
        }
        maghribResponse = maghribHour + " : " + maghribMinString + " pm";
        return maghribResponse;
    }

    public String zuhrClock (double userLongitude, int userTimeZoneOffset, Calendar userCalendar){
        zuhrTimeString = zuhrTime(userLongitude, userTimeZoneOffset, userCalendar);
        zuhrTimeHour = Integer.parseInt(zuhrTimeString.substring(0, zuhrTimeString.indexOf(':')));
        zuhrTimeMinute = Integer.parseInt(zuhrTimeString.substring(zuhrTimeString.indexOf(':')+1));
        String zuhrTimeConfirmation = "am";
        String zuhrTimeMinuteString;
        if (zuhrTimeHour > 12){
            zuhrTimeHour = zuhrTimeHour-12;
            zuhrTimeConfirmation = "pm";
        }
        if (zuhrTimeMinute <=9){
            zuhrTimeMinuteString = "0"+zuhrTimeMinute;
        }else{
            zuhrTimeMinuteString = String.valueOf(zuhrTimeMinute);
        }
        String zuhr = zuhrTimeHour + " : " + zuhrTimeMinuteString + " " + zuhrTimeConfirmation;
        return zuhr;
    }
}
