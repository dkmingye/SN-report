package com.sparnord.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.Locale;

import com.mega.modeling.api.MegaObject;
import com.mega.modeling.api.MegaRoot;
import com.mega.toolkit.errmngt.ErrorLogFormater;

public class LDCDateUtilities {

  private static final String MEGA_DATE_FORMAT = "yyyy/MM/dd";

  public static Date convertDate(final MegaRoot root, final String sDate, final String format) {
    Date date = null;
    SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.ROOT);
    try {
      date = formatter.parse(sDate);
    } catch (Exception e) {
      final ErrorLogFormater err = new ErrorLogFormater();
      err.openSession(root);
      err.addSessionInfo("Component", "(Java) New Analysis Engine: ReportContent: getDate");
      err.logMessage("Date parsing error.");
      err.closeSession();
    }
    return date;
  }

  public static Date resetTime(final MegaRoot root, final Date date) {
    DateFormat formatter = new SimpleDateFormat(LDCDateUtilities.MEGA_DATE_FORMAT);
    String stringDate = formatter.format(date);
    Date dateWithNoTime = LDCDateUtilities.getParsedDate(root, LDCDateUtilities.MEGA_DATE_FORMAT, stringDate);
    return dateWithNoTime;
  }

  public static int diffMonth(final Date startDate, final Date endDate) {

    Calendar startCalendar = new GregorianCalendar();
    startCalendar.setTime(startDate);
    startCalendar.set(Calendar.DAY_OF_MONTH, 1);
    Calendar endCalendar = new GregorianCalendar();
    endCalendar.setTime(endDate);
    endCalendar.set(Calendar.DAY_OF_MONTH, endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));

    int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
    int diffMonth = ((diffYear * 12) + endCalendar.get(Calendar.MONTH)) - startCalendar.get(Calendar.MONTH);
    return diffMonth;
  }

  public static Date initDateToStartMonth(final Date oDate) {
    Calendar startCalendar = new GregorianCalendar();
    startCalendar.setTime(oDate);
    startCalendar.set(Calendar.DAY_OF_MONTH, 1);
    return startCalendar.getTime();
  }

  public static Date initDateToEndMonth(final Date oDate) {
    Calendar endCalendar = new GregorianCalendar();
    endCalendar.setTime(oDate);
    endCalendar.set(Calendar.DAY_OF_MONTH, endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
    return endCalendar.getTime();
  }

  /**
   * @param date
   * @return a date without a time -useful for comparing two date
   */
  public static Date resetBeginDateTime(final Date date) {
    if (date != null) {
      Calendar cal = Calendar.getInstance();
      cal.setTime(date);
      cal.set(Calendar.HOUR_OF_DAY, 0);
      cal.set(Calendar.MINUTE, 0);
      cal.set(Calendar.SECOND, 0);
      cal.set(Calendar.MILLISECOND, 0);
      return cal.getTime();
    }
    return null;
  }

  /**
   * @param date
   * @return a date without a time -useful for comparing two date
   */
  public static Date resetEndDateTime(final Date date) {
    if (date != null) {
      Calendar cal = Calendar.getInstance();
      cal.setTime(date);
      cal.set(Calendar.HOUR_OF_DAY, 23);
      cal.set(Calendar.MINUTE, 59);
      cal.set(Calendar.SECOND, 59);
      cal.set(Calendar.MILLISECOND, 59);
      return cal.getTime();
    }
    return null;
  }

  /**
   * @param date Date
   * @param root MegaRoot
   * @param format String
   * @return a date from a string format
   */
  public static Date getParsedDate(final MegaRoot root, final String format, final String date) {
    Date dateNode = null;
    try {

      SimpleDateFormat formatMega = new SimpleDateFormat(format);
      dateNode = formatMega.parse(date);

    } catch (Exception e) {
      ErrorLogFormater err = new ErrorLogFormater();
      err.openSession(root);
      err.logError(e);

    }
    return dateNode;

  }

  /**
   * @return current date
   */
  public static Date getCurrentDate() {
    Calendar cal = Calendar.getInstance();
    return LDCDateUtilities.resetEndDateTime(cal.getTime());

  }

  public static Date getDate(final String str) {
    Date date = null;
    SimpleDateFormat formatMega = new SimpleDateFormat("yyyy/MM/dd");
    try {
      date = formatMega.parse(str);
    } catch (java.text.ParseException e) {

    }
    return date;
  }

  public static Date getDate(final MegaRoot megaRoot, final String sDate) {
    Date date = null;
    SimpleDateFormat formatMega = new SimpleDateFormat("yyyy/MM/dd");
    try {
      date = formatMega.parse(sDate);
    } catch (java.text.ParseException e) {
      final ErrorLogFormater err = new ErrorLogFormater();
      err.openSession(megaRoot);
      err.addSessionInfo("Component", "(Java) New Analysis Engine: ReportContent: ContexualizedReportOnRisksPerProcess: getDate");
      err.logMessage("Date parsing error.");
      err.closeSession();
    }
    return date;
  }

  /**
   * @param megaObject megaObject
   * @param metaAttribute megaField for a mettaAttribute Date
   * @return get a date from mega
   */
  public static Date getDateFromMega(final MegaObject megaObject, final String metaAttribute) {

    Object megaDate = megaObject.getProp(metaAttribute, "internal");

    if (megaDate != null) {
      return (Date) megaDate;
    }

    return null;
  }

  /**
   * @param mgObjectDate
   * @param firstDateRange
   * @param secondDateRange
   * @return If the given date is in dates Range
   */
  public static boolean isInDatesRange(final Date mgObjectDate, final Date firstDateRange, final Date secondDateRange) {
    if ((mgObjectDate != null) && (mgObjectDate.equals(firstDateRange) || (mgObjectDate.before(secondDateRange) && mgObjectDate.after(firstDateRange)) || mgObjectDate.equals(secondDateRange))) {
      return true;
    }
    return false;
  }

  /**
   * @return Current year
   */
  public static int getCurrentYear() {

    return Calendar.getInstance().get(Calendar.YEAR);

  }

  public static Date getFirstDateOfTheYear() {

    return LDCDateUtilities.resetBeginDateTime(LDCDateUtilities.getDate(LDCDateUtilities.getCurrentYear() + "/01/01"));

  }

  /**
   * choose the amount of day/month/year to add/subtract
   * @param date : date given
   * @param type : possible values : GregorianCalendar.YEAR =
   *          year;GregorianCalendar.MONTH = month;GregorianCalendar.DAY = day
   * @param number : amount to add/subtract
   * @return Date
   */
  public static Date addTimeAmount(final Date date, final Integer type, final Integer number) {

    GregorianCalendar calStr1 = new GregorianCalendar();
    calStr1.setTime(date);
    calStr1.add(type, number);
    return calStr1.getTime();

  }

  public static Date getTheFirstDayOfTheMonth(final Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.set(Calendar.DAY_OF_MONTH, 1);
    return LDCDateUtilities.resetBeginDateTime(calendar.getTime());
  }

  /**
   * @param firstDate First date to compare
   * @param secondDate Second date to compare
   * @return If month and year are equal
   */
  public static boolean checkIfMonthAndYearAreEqual(final Date firstDate, final Date secondDate) {
    boolean isEqual = false;

    if ((firstDate != null) && (secondDate != null)) {
      Calendar firstCal = Calendar.getInstance();
      firstCal.setTime(firstDate);
      Calendar secondCal = Calendar.getInstance();
      secondCal.setTime(secondDate);
      isEqual = (firstCal.get(Calendar.YEAR) == secondCal.get(Calendar.YEAR)) && (firstCal.get(Calendar.MONTH) == secondCal.get(Calendar.MONTH));
    }

    return isEqual;
  }

  /**
   * @param date
   * @return Month of the given date
   */
  public static int getMonth(final Date date) {

    GregorianCalendar calendar = new GregorianCalendar();
    calendar.setTime(date);
    return calendar.get(Calendar.MONTH) + 1;
  }

  /**
   * @param date
   * @return Year of the given date
   */
  public static int getYear(final Date date) {

    GregorianCalendar calendar = new GregorianCalendar();
    calendar.setTime(date);
    return calendar.get(Calendar.YEAR);
  }

  /**
   * @return
   */
  public static LinkedHashMap<String, String> getIncidentsbyDateMap() {
    LinkedHashMap<String, String> incidentsByMonths = new LinkedHashMap<String, String>();
    Date currentDate = LDCDateUtilities.resetEndDateTime(new Date());
    //initialize the map by the eleven past month
    for (int i = 11; i >= 0; i--) {
      Date _date = LDCDateUtilities.addTimeAmount(currentDate, Calendar.MONTH, -i);
      Integer month = LDCDateUtilities.getMonth(_date);
      Integer year = LDCDateUtilities.getYear(_date);
      incidentsByMonths.put(month + "-" + year, "");
    }
    return incidentsByMonths;
  }

  /**
   * @param incident
   * @param incidentsByMonths
   * @param macro
   * @param megaCurrency
   * @param userCurrency
   */
  public static void orderElementsByMonths(final MegaObject incident, final LinkedHashMap<String, String> incidentsByMonths) {
    Date incidentCreationDate = LDCDateUtilities.getDateFromMega(incident, LDCConstants.MA_DECLARATION_DATE);
    if (incidentCreationDate != null) {
      Integer month = LDCDateUtilities.getMonth(incidentCreationDate);
      Integer year = LDCDateUtilities.getYear(incidentCreationDate);
      String key = month + "-" + year;
      if (incidentsByMonths.get(key) != null) {
        String incidentsIdsAndNetLossAttribute = incidentsByMonths.get(key);

        double netLossAmmount = Double.parseDouble(incident.getProp(LDCConstants.MA_NET_LOSS_LOCAL, "Internal").toString());

        if (incidentsIdsAndNetLossAttribute.equals("")) {
          incidentsIdsAndNetLossAttribute = incident.megaUnnamedField() + ":" + netLossAmmount;
        } else {
          incidentsIdsAndNetLossAttribute = incidentsIdsAndNetLossAttribute + "," + incident.megaUnnamedField() + ":" + netLossAmmount;
        }
        incidentsByMonths.put(key, incidentsIdsAndNetLossAttribute);
      }
    }
  }

}

