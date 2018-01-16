/**
 * 
 */
package com.aizhixin.cloud.dataanalysis.common.util;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期计算工具
 * @author zhen.pan
 *
 */
public class DateUtil {
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	public static String FORMAT_LONG = "yyyy-MM-dd HH:mm:ss";
	public static String FORMAT_SHORT = "yyyy-MM-dd";
	private static long ONE_DAY_SEC = 86400000L;

	public static String format(Date date) {
        return format(date, FORMAT_LONG);
    }
	
	 /**
     * 使用用户格式格式化日期
     *
     * @param date    日期
     * @param pattern 日期格式
     * @return
     */
    public static String format(Date date, String pattern) {
        String returnValue = "";
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            returnValue = df.format(date);
        }
        return (returnValue);
    }
	
	/**
	 * 判断是否周一
	 * @param d
	 * @return
	 */
	public static boolean isMonday(Calendar d) {
		if(Calendar.MONDAY == d.get(Calendar.DAY_OF_WEEK)) {
			return true;
		}
		return false;
	}
	/**
	 * 判断是否周日 
	 * @param d
	 * @return
	 */
	public static boolean isSunday(Calendar d) {
		if(Calendar.SUNDAY == d.get(Calendar.DAY_OF_WEEK)) {
			return true;
		}
		return false;
	}
	/**
	 * 计算任意一天所在周的周一的日期
	 * 每周的周一作为第一天
	 * @param date
	 * @return
	 */
	public static Date getMonday(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		zeroTime(c);
		int delta = 0;
		if(isSunday(c)) {//Calendar的星期是从周日开始的，对周日需要修正
			delta = 6;
		} else {
			delta = c.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;
		}
		c.add(Calendar.DATE, 0 - delta);
		return c.getTime();
	}
	/**
	 * 计算任意一天所在周的周日
	 * 每周的周一作为第一天
	 * @param date
	 * @return
	 */
	public static Date getSunday(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		zeroTime(c);
		int delta = 0;
		if(isSunday(c)) {
			return c.getTime();
		} else {
			delta = Calendar.SATURDAY  + 1 - c.get(Calendar.DAY_OF_WEEK);
		}
		c.add(Calendar.DATE, delta);
		return c.getTime();
	}
	/**
	 * 根据输入的周一计算本周输出的周日
	 * @param monday
	 * @return
	 */
	public static Date getSunDayByMonday(Date monday) {
		Calendar c = Calendar.getInstance();
		c.setTime(monday);
		if(!isMonday(c)) {
			throw new RuntimeException("Input date[" + format.format(c.getTime()) + "\tWEEK:" + c.get(Calendar.DAY_OF_WEEK) + "] is not MONDAY");
		}
		c.add(Calendar.DATE, 6);
		return c.getTime();
	}
	/**
	 * 得到传入日期的下一天
	 * @param date
	 * @return
	 */
	public static Date nextDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, 1);
		zeroTime(c);
		return c.getTime();
	}
	
	/**
	 * 前一天
	 * @param c
	 * @return
	 */
	public static Calendar getPreDate(Calendar c) {
		c.add(Calendar.DATE, -1);
		return c;
	}
	/**
	 * 获取两个日期之前的天的数量，start是开区间，end是闭区间
	 * @param start
	 * @param end
	 * @return
	 */
	public static int getDaysBetweenDate(Date start, Date end) {
		if(end.before(start)) {
			throw new RuntimeException("Input end date [" + format.format(end) + "] before start date [" + format.format(start) + "]");
		}
		Calendar cs = Calendar.getInstance();
		cs.setTime(start);
		zeroTime(cs);
		
		Calendar ce = Calendar.getInstance();
		ce.setTime(end);
		zeroTime(ce);
		
		long s = cs.getTimeInMillis();
		long e = ce.getTimeInMillis();
		return (int)((e - s)/ONE_DAY_SEC);
	}
	/**
	 * 日历时间归0时、0分、0秒、0纳秒
	 * 直接修改传入对象
	 * @param c
	 */
	public static void zeroTime(Calendar c) {
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
	}
	/**
	 * 获取下周一的日期
	 * @param date
	 * @return
	 */
	public static Date getNextMonday(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.WEEK_OF_YEAR, 1);
		return getMonday(c.getTime());
	}
	/**
	 * 返回n天后的日期
	 */
	public static Date afterNDay(Date date, int n) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, n);
		Date d2 = c.getTime();
		return d2;
	}
	/**
	 * 获取一个日期，时间部分是全是0
	 * @param d
	 * @return
	 */
	public static Date getZerotime(Date d) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		zeroTime(c);
		return c.getTime();
	}
	/**
	 * 获取两个日期之间包含的周的数量
	 * @param start
	 * @param end
	 * @return
	 */
	public static int getWeekNumbersBetweenDate(Date start, Date end) {
//		int days = getDaysBetweenDate(start, end);
		int weeks = 0;
		Calendar cs = Calendar.getInstance();
		cs.setTime(start);
		zeroTime(cs);
		
		Calendar ce = Calendar.getInstance();
		ce.setTime(end);
		zeroTime(ce);
		
		
		Date sm = getNextMonday(cs.getTime());
		System.out.println("Date:" + format.format(cs.getTime()) + "\t Monday: " + format.format(getMonday(cs.getTime())) + "\tNext Monday:" + format.format(sm));
		if(sm.before(ce.getTime())) {
			weeks += 1;
		}
		cs.setTime(sm);
//		
//		c = getPreDate(c);//
//		if(!isSunday(c)) {
//			weeks += 1;
//		}
//		int days = getDaysBetweenDate(sm, end);
//		weeks += days/7;
//		return weeks;
//		DateTime dateTime1 = new DateTime(start);
//		DateTime dateTime2 = new DateTime(end);
		
	    while (cs.before(ce)) {
	        cs.add(Calendar.WEEK_OF_YEAR, 1);
	        weeks++;
	    }
	    return weeks;

//		int weeks = Weeks.weeksBetween(dateTime1, dateTime2).getWeeks();
//		return weeks;
	}

	/**
	 * 周一到周天用1到7来表示
	 * @param date
	 * @return
	 */
	public static Integer getDayOfWeek(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek == Calendar.SUNDAY) {
			return 7;
		} else {
			return dayOfWeek - 1;
		}
	}

	public static Map<Integer, Date> getWeekAllDate(Date firstDate) {
		Map<Integer, Date> wds = new HashMap<>();
		wds.put(1, firstDate);
		Date n = nextDate(firstDate);
		wds.put(2, n);
		n = nextDate(n);
		wds.put(3, n);
		n = nextDate(n);
		wds.put(4, n);
		n = nextDate(n);
		wds.put(5, n);
		n = nextDate(n);
		wds.put(6, n);
		n = nextDate(n);
		wds.put(7, n);
		return wds;
	}

	public static String getCurrentTime(String formatStr) {
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		return format.format(new Date());
	}
	
  
    /** 
     * 获取随机日期 
     *  
     * @param beginDate 
     *            起始日期，格式为：yyyy-MM-dd 
     * @param endDate 
     *            结束日期，格式为：yyyy-MM-dd 
     * @return 
     */  
  
    public static Date randomDate(String beginDate, String endDate) {  
        try {  
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
            Date start = format.parse(beginDate);// 构造开始日期  
            Date end = format.parse(endDate);// 构造结束日期  
            // getTime()表示返回自 1970 年 1 月 1 日 00:00:00 GMT 以来此 Date 对象表示的毫秒数。  
            if (start.getTime() >= end.getTime()) {  
                return null;  
            }  
            long date = random(start.getTime(), end.getTime());  
  
            return new Date(date);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return null;  
    }  
  
    private static long random(long begin, long end) {  
        long rtn = begin + (long) (Math.random() * (end - begin));  
        // 如果返回的是开始时间和结束时间，则递归调用本函数查找随机值  
        if (rtn == begin || rtn == end) {  
            return random(begin, end);  
        }  
        return rtn;  
    }


	/**
	 * 判断时间是否在时间段内
	 *
	 * @param date           当前时间 yyyy-MM-dd HH:mm:ss
	 * @param strDateBegin  开始时间 00:00:00
	 * @param strDateEnd    结束时间 00:05:00
	 * @return
	 */
	public static boolean isInDate(Date date, String strDateBegin, String strDateEnd) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String strDate = sdf.format(date);
		// 截取当前时间时分秒
		int strDateH = Integer.parseInt(strDate.substring(11, 13));
		int strDateM = Integer.parseInt(strDate.substring(14, 16));
		int strDateS = Integer.parseInt(strDate.substring(17, 19));
		// 截取开始时间时分秒
		int strDateBeginH = Integer.parseInt(strDateBegin.substring(0, 2));
		int strDateBeginM = Integer.parseInt(strDateBegin.substring(3, 5));
		int strDateBeginS = Integer.parseInt(strDateBegin.substring(6, 8));
		// 截取结束时间时分秒
		int strDateEndH = Integer.parseInt(strDateEnd.substring(0, 2));
		int strDateEndM = Integer.parseInt(strDateEnd.substring(3, 5));
		int strDateEndS = Integer.parseInt(strDateEnd.substring(6, 8));
		if ((strDateH >= strDateBeginH && strDateH <= strDateEndH)) {
			// 当前时间小时数在开始时间和结束时间小时数之间
			if (strDateH > strDateBeginH && strDateH < strDateEndH) {
				return true;
				// 当前时间小时数等于开始时间小时数，分钟数在开始和结束之间
			} else if (strDateH == strDateBeginH && strDateM >= strDateBeginM
					&& strDateM <= strDateEndM) {
				return true;
				// 当前时间小时数等于开始时间小时数，分钟数等于开始时间分钟数，秒数在开始和结束之间
			} else if (strDateH == strDateBeginH && strDateM == strDateBeginM
					&& strDateS >= strDateBeginS && strDateS <= strDateEndS) {
				return true;
			}
			// 当前时间小时数大等于开始时间小时数，等于结束时间小时数，分钟数小等于结束时间分钟数
			else if (strDateH >= strDateBeginH && strDateH == strDateEndH
					&& strDateM <= strDateEndM) {
				return true;
				// 当前时间小时数大等于开始时间小时数，等于结束时间小时数，分钟数等于结束时间分钟数，秒数小等于结束时间秒数
			} else if (strDateH >= strDateBeginH && strDateH == strDateEndH
					&& strDateM == strDateEndM && strDateS <= strDateEndS) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

//    public static void main(String[] args) { 
//    	for (int i = 0; i < 10; i++) {
//    		Date randomDate = randomDate("2007-01-01", "2007-03-01");  
//    		System.out.println(randomDate.toString());  
//    	}
//    }  
//	public static void main(String[] args) {
//		Calendar c = Calendar.getInstance();
//		c.setTime(new Date());
//		c.set(Calendar.DATE, 2);
//		Date d = c.getTime();
//		System.out.println(format.format(d));
//		d = DateUtil.getMonday(d);
//		System.out.println("\tMonday:" + format.format(d));
//		d = DateUtil.getSunDayByMonday(d);
//		System.out.println("\tSunday:" + format.format(d));
//		System.out.println("\tNext Monday:" + format.format(nextDate(d)));
//		
//		c = Calendar.getInstance();
//		c.setTime(new Date());
//		c.set(Calendar.DATE, 7);
//		Date s = c.getTime();
//		c.setTime(new Date());
////		c.set(Calendar.MONTH, 1);
//		c.set(Calendar.DATE, 24);
//		Date e = c.getTime();
//		System.out.println(format.format(s) + "\t" + format.format(e) + "\t day numbers:" + getDaysBetweenDate(s, e) + "\t week numbers:" + getWeekNumbersBetweenDate(s, e));
//		
//		c = Calendar.getInstance();
//		c.setTime(new Date());
//		c.set(Calendar.DATE, 2);
////		c.add(Calendar.DATE, -1);
//		d = c.getTime();
//		System.out.println(format.format(d));
//		d = DateUtil.getSunday(d);
//		System.out.println("\tSunday:" + format.format(d));
//
////		d = new Date ();
//		System.out.println(getDayOfWeek(d));
//	}
}