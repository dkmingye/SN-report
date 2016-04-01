package com.sparnord.heatmaps.grcu;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.mega.modeling.api.MegaObject;
import com.mega.modeling.api.MegaRoot;
import com.mega.toolkit.errmngt.ErrorLogFormater;

/**
 * Common operation of date time manipulation
 * @author ARK
 */
public final class GRCDateUtility {

  /**
   * return current date
   * @return
   */
  public static Date getCurrentDate() {
    Calendar cal = Calendar.getInstance();
    return GRCDateUtility.resetEndDateTime(cal.getTime());

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

  public static boolean isInDatesRange(final Date mgObjectDate, final Date firstDateRange, final Date secondDateRange) {
    if ((mgObjectDate != null) && (mgObjectDate.equals(firstDateRange) || (mgObjectDate.before(secondDateRange) && mgObjectDate.after(firstDateRange)) || mgObjectDate.equals(secondDateRange))) {
      return true;
    }
    return false;
  }

  public static int getCurrentYear() {
    return Calendar.getInstance().get(Calendar.YEAR);
  }

  public static Date getFirstDateOfTheYear() {
    return GRCDateUtility.resetBeginDateTime(GRCDateUtility.getDate(GRCDateUtility.getCurrentYear() + "/01/01"));

  }

  /**
   * choose the amount of day/month/year to add/subtract
   * @param date : date given
   * @param type : possible values : GregorianCalendar.YEAR =
   *          year;GregorianCalendar.MONTH = month;GregorianCalendar.DAY = day
   * @param number : amonut to add/subtract
   * @return
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
    return GRCDateUtility.resetBeginDateTime(calendar.getTime());
  }

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

  public static int getMonth(final Date d) {

    GregorianCalendar calendar = new GregorianCalendar();
    calendar.setTime(d);
    return calendar.get(Calendar.MONTH) + 1;
  }

  public static int getYear(final Date d) {

    GregorianCalendar calendar = new GregorianCalendar();
    calendar.setTime(d);
    return calendar.get(Calendar.YEAR);
  }

  /**
   * return a date without a time -useful for comparing two date
   * @param date
   * @return
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
   * return a date without a time -useful for comparing two date
   * @param date
   * @return
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
   * return current date + 30
   * @return
   */
  public static Date getCurrentDatePlus30() {
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DAY_OF_YEAR, 30);
    return GRCDateUtility.resetEndDateTime(cal.getTime());

  }

}

