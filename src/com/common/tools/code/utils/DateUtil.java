package com.common.tools.code.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.StringTokenizer;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;
/**
 * 
 * @author lx
 *
 */
public class DateUtil {
	private static Logger logger = LoggerFactory.getLogger(DateUtil.class);
	
	/**
	 * 当前系统时间，所有现在时间必须使用此时间
	 * @return
	 */
	public static Date now(){
		logger.debug("进入方法:now,参数无");
		logger.debug("结束方法:now,参数无");
		return new Date();
	}

	
	/**
	 * 增加天
	 * @param yyyyMMdd
	 * @param num
	 * @return
	 * @throws Exception
	 * @author LvXin
	 */
	public static String addDate(String yyyyMMdd,int num) throws Exception{
		Date oldDate = fromYyyyMMdd(yyyyMMdd);
		Date newDate = addDays(oldDate, num);
		String r = getYyyyMMdd(newDate);
		
		return r;
	}

	public static String getYyyyMMdd(){
		logger.debug("进入方法:getYyyyMMdd,参数无");
		logger.debug("结束方法:getYyyyMMdd,参数无");
		return getYyyyMMdd(now());
	}
	public static String getYyyyMMdd(Date date){
		logger.debug("进入方法:getYyyyMMdd,参数,date:" + date);
		logger.debug("结束方法:getYyyyMMdd,参数,date:" + date);
		return new SimpleDateFormat("yyyyMMdd").format(date);
	}
	public static String getYyyy_MM_dd(){
		logger.debug("进入方法:getYyyy_MM_dd,参数无");
		logger.debug("结束方法:getYyyy_MM_dd,参数无");
		return getYyyy_MM_dd(now());
	}
	public static String getYyyy_MM_dd(Date date){
		logger.debug("进入方法:getYyyy_MM_dd,参数,date:" + date);
		logger.debug("结束方法:getYyyy_MM_dd,参数,date:" + date);
		return new SimpleDateFormat("yyyy-MM-dd").format(date);
	}
	public static String getHHmmss(){
		logger.debug("进入方法:getHHmmss,参数无");
		logger.debug("结束方法:getHHmmss,参数无");
		return getHHmmss(now());
	}
	public static String getHHmmss(Date date){
		logger.debug("进入方法:getHHmmss,参数,date:" + date);
		logger.debug("结束方法:getHHmmss,参数,date:" + date);
		return new SimpleDateFormat("HHmmss").format(date);
	}
	
	public static String getYyyyMMddHHmmss(){
		logger.debug("进入方法:getYyyyMMddHHmmss,参数无");
		logger.debug("结束方法:getYyyyMMddHHmmss,参数无");
		return getYyyyMMddHHmmss(now());
	}
	public static String getYyyyMMddHHmmss(Date date){
		logger.debug("进入方法:getYyyyMMddHHmmss,参数,date:" + date);
		logger.debug("结束方法:getYyyyMMddHHmmss,参数,date:" + date);
		return new SimpleDateFormat("yyyyMMddHHmmss").format(date);
	}
	/**
	 * 格式当前时间
	 * yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getYyyyMMdddHHmmssSSSS(){
		logger.debug("进入方法:getYyyyMMdddHHmmssSSSS,参数无");
		logger.debug("结束方法:getYyyyMMdddHHmmssSSSS,参数无");
		return getYyyyMMddHHmmssSSSS(now());
	}
	/**
	 * 格式某时间
	 * yyyy-MM-dd HH:mm:ss
	 * @param date 要格式的时间
	 * @return
	 */
	public static String getYyyy_MM_dd_HH_mm_ss(Date date){
		logger.debug("进入方法:getYyyy_MM_dd_HH_mm_ss,参数,date:" + date);
		logger.debug("结束方法:getYyyy_MM_dd_HH_mm_ss,参数,date:" + date);
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	}
	/**
	 * 格式某时间
	 * yyyy-MM-dd HH:mm:ss
	 * @param date 要格式的时间
	 * @return
	 */
	public static String getYyyy_MM_dd_HH_mm_ss(){
		logger.debug("进入方法:getYyyy_MM_dd_HH_mm_ss,参数无");
		logger.debug("结束方法:getYyyy_MM_dd_HH_mm_ss,参数无");
		return getYyyy_MM_dd_HH_mm_ss(now());
	}
	
	public static String getYyyyMMddHHmmssSSSS(){
		logger.debug("进入方法:getYyyyMMddHHmmssSSSS,参数无");
		logger.debug("结束方法:getYyyyMMddHHmmssSSSS,参数无");
		return getYyyyMMddHHmmssSSSS(now());
	}
	public static String getYyyyMMddHHmmssSSSS(Date date){
		logger.debug("进入方法:getYyyyMMddHHmmssSSSS,参数,date:" + date);
		logger.debug("结束方法:getYyyyMMddHHmmssSSSS,参数,date:" + date);
		return new SimpleDateFormat("yyyyMMddHHmmssSSSS").format(date);
	}
	
	public static Date fromYyyyMMdd(String yyyyMMdd) throws ParseException{
		logger.debug("进入方法:fromYyyyMMdd,参数,yyyyMMdd:" + yyyyMMdd);
		logger.debug("结束方法:fromYyyyMMdd,参数,yyyyMMdd:" + yyyyMMdd);
		return new SimpleDateFormat("yyyyMMdd").parse(yyyyMMdd);
	}
	
	public static String toYyyyMMdd(String otherFormat){
		logger.debug("进入方法:toYyyyMMdd,参数,otherFormat:" + otherFormat);
		if (StringUtils.isEmpty(otherFormat)) {
			logger.debug("结束方法:toYyyyMMdd,参数,otherFormat:" + otherFormat);
			return "";
		}

		logger.debug("结束方法:toYyyyMMdd,参数,otherFormat:" + otherFormat);
		String str = otherFormat.replaceAll("-", "").replaceAll("/", "").replaceAll(" ", "").replaceAll(":", "");
		if (str.length() <= 8) {
			return str;
		}
		
		return str.substring(0,8);
	}
	
	/**
	 * yyyyMMdd to yyyy-MM-dd
	 * @param yyyyMMdd
	 * @return
	 */
	public static String yyyyMMddToYyyy_MM_dd(String yyyyMMdd){
		logger.debug("进入方法:yyyyMMddToYyyy_MM_dd,参数,yyyyMMdd:" + yyyyMMdd);
		if (yyyyMMdd == null) {
			return "";
		}

		char[] arr = yyyyMMdd.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arr.length; i++) {
			sb.append(arr[i]);
			if (i == 3 || i == 5) {
				sb.append("-");
			}
		}

		logger.debug("结束方法:yyyyMMddToYyyy_MM_dd,参数,yyyyMMdd:" + yyyyMMdd);
		return sb.toString();
	}
	
	/**
	 * yyyyMMddHHmmss to yyyy-MM-dd HH:mm:ss
	 * @param yyyyMMddHHmmss
	 * @return
	 */
	public static String yyyyMMddHHmmssToYyyy_MM_dd_HH_mm_ss(String yyyyMMddHHmmss){
		logger.debug("进入方法:yyyyMMddHHmmssToYyyy_MM_dd_HH_mm_ss,参数,yyyyMMddHHmmss:" + yyyyMMddHHmmss);
		if (yyyyMMddHHmmss == null) {
			logger.debug("结束方法:yyyyMMddHHmmssToYyyy_MM_dd_HH_mm_ss,参数,yyyyMMddHHmmss:" + yyyyMMddHHmmss);
			return "";
		}

		char[] arr = yyyyMMddHHmmss.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arr.length; i++) {
			sb.append(arr[i]);
			if (i == 3 || i == 5) {
				sb.append("-");
			}else if (i == 7) {
				sb.append(" ");
			}else if(i == 9 || i == 11){
				sb.append(":");
			}
		}

		logger.debug("结束方法:yyyyMMddHHmmssToYyyy_MM_dd_HH_mm_ss,参数,yyyyMMddHHmmss:" + yyyyMMddHHmmss);
		return sb.toString();
	}
	
	/**
	 * HHmmss to  HH:mm:ss
	 * @param yyyyMMddHHmmss
	 * @return
	 */
	public static String HHmmssToHH_mm_ss(String HHmmss){
		logger.debug("进入方法:HHmmssToYyyy_MM_dd_HH_mm_ss,参数,HHmmss:" + HHmmss);
		if (HHmmss == null) {
			logger.debug("结束方法:yyyyMMddHHmmssToYyyy_MM_dd_HH_mm_ss,参数,HHmmss:" + HHmmss);
			return "";
		}

		char[] arr = HHmmss.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arr.length; i++) {
			sb.append(arr[i]);
			if (i == 1 || i == 3) {
				sb.append(":");
			}
		}

		logger.debug("结束方法:HHmmssToYyyy_MM_dd_HH_mm_ss,参数,HHmmss:" + HHmmss);
		return sb.toString();
	}

	/**
	 * 检查begin是否小于end
	 * @param begin yyyyMMdd
	 * @param end yyyyMMdd
	 * @param beginName 开始时间名称，如：生效时间
	 * @param endName 结束时间名称，如：失效时间
	 * @throws Exception 
	 */
	public static void checkAfter(String begin,String end,String beginName,String endName) throws Exception {
		logger.debug("进入方法:checkAfter,参数,begin:" + begin + ",end:" + end + ",beginName:" + beginName + ",endName:" + endName);
		checkYyyyMMdd(begin, beginName);
		checkYyyyMMdd(end, endName);
		
		Date valueDate = null;
		try {
			valueDate = DateUtil.fromYyyyMMdd(begin);
		} catch (Exception e) {
			throw new Exception(beginName + "格式不正确：" + begin);
		}
		
		Date dueDate = null;
		try {
			dueDate = DateUtil.fromYyyyMMdd(end);
		} catch (Exception e) {
			throw new Exception(endName + "格式不正确：" + end);
		}
		
		if (!dueDate.after(valueDate)) {
			throw new Exception(endName + "要大于" + beginName + "," + beginName +":" + begin + "," + endName + ":" + end);
		}
		logger.debug("结束方法:checkAfter,参数,begin:" + begin + ",end:" + end + ",beginName:" + beginName + ",endName:" + endName);
	}
	
	public static void checkYyyyMMdd(String yyyyMMdd,String dateName) throws Exception{
		logger.debug("进入方法:checkYyyyMMdd,参数,yyyyMMdd:" + yyyyMMdd + ",bb:" + dateName);
		if (yyyyMMdd == null) {
			throw new Exception(dateName + "不能为空");
		}
		
		StringUtils.checkPattern(yyyyMMdd,dateName,"\\d{8}");

		logger.debug("结束方法:checkYyyyMMdd,参数,yyyyMMdd:" + yyyyMMdd + ",bb:" + dateName);
	}
	
	/**
	 * 两个日期之间相差的天数,yyyyMMdd
	 * @param begin
	 * @param end
	 * @return
	 * @throws Exception 
	 */
	public static int getDateDif(String begin,String end) throws Exception{
		logger.debug("进入方法:getDateDif,参数,begin:" + begin + ",bb:" + end);
		checkYyyyMMdd(begin, "日期");
		checkYyyyMMdd(end, "日期");
		SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
		Date b = f.parse(begin);
		Date e = f.parse(end);

		logger.debug("结束方法:getDateDif,参数,begin:" + begin + ",bb:" + end);
		return getDateDif(b, e);
		
	}
	
	
	
	/**
	 * 两个日期之间相差的天数
	 * @param b
	 * @param e
	 * @return
	 */
	public static int getDateDif(Date begin, Date end) {
		logger.debug("进入方法:getDateDif,参数,begin:" + begin + ",end:" + end);
		Calendar bc = Calendar.getInstance();
		bc.setTime(begin);
		Calendar ec = Calendar.getInstance();
		ec.setTime(end);

		int dif = (int)((ec.getTimeInMillis() - bc.getTimeInMillis())/1000/60/60/24);

		logger.debug("结束方法:getDateDif,参数,begin:" + begin + ",end:" + end);
		return dif;
	}
	
	/**
	 * 输入时间与当前时间的差异,以天为单位
	 * @param date 
	 * @return
	 */
	public static long getTimeDifDate(Date date){
		logger.debug("进入方法:getTimeDifDate,参数,date:" + date);
		long dateBegin = getDateBegin(date).getTime();
		long nowBegin = getDateBegin(new Date()).getTime();

		logger.debug("结束方法:getTimeDifDate,参数,date:" + date);
		return dateBegin - nowBegin;
	}
	
	/**
	 * 获得当天的开始
	 * @param date
	 * @return
	 */
	public static Date getDateBegin(Date date){
		logger.debug("进入方法:getDateBegin,参数,date:" + date);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		logger.debug("结束方法:getDateBegin,参数,date:" + date);
		return c.getTime();
	}

	
	/**
	 * 获得一个随机日期 from 2000 to now
	 * @return
	 * @throws ParseException
	 * @author LvXin
	 */
	public static String getRadomYyyyMMddHHmmss() throws ParseException{
		return getRadomYyyyMMdd() + getRadomHHmmss();
	}
	
	/**
	 * 获得一个随机日期 from 2000 to now
	 * @return
	 * @throws ParseException
	 * @author LvXin
	 */
	public static String getRadomYyyyMMdd() throws ParseException{
		return getRadomYyyyMMdd("2000",getYyyyMMdd().substring(0,4));
	}

	
	/**
	 * 获得一个随机日期 from 2000 to now
	 * @return
	 * @throws ParseException
	 * @author LvXin
	 */
	public static String getRadomYyyyMMdd(String beginYyyy,String endYyyy) throws ParseException{
		String yyyy = getRadomNumber(beginYyyy, endYyyy, 4);
		String MM = getRadomNumber("1", "12", 2);
		String dd = getRadomNumber("1", "28", 2);
		
		return yyyy + MM + dd;
	}
	
	/**
	 * 获得一个随机时间 
	 * @return
	 * @throws ParseException
	 * @author LvXin
	 */
	public static String getRadomHHmmss() throws ParseException{
		String hour = getRadomNumber("0", "23", 2);
		String m = getRadomNumber("0", "59", 2);
		String s = getRadomNumber("0", "59", 2);
		
		return hour + m + s;
	}
	/**
	 * 获取一个随机日期
	 * @param begin 开始 闭
	 * @param end 结束 闭
	 * @param length 几位字符串
	 * @return
	 * @throws ParseException
	 * @author LvXin
	 */
	public static String getRadomNumber(String begin,String end,int length) throws ParseException{
		Random r = new Random();
		int b = Integer.parseInt(begin);
		int e = Integer.parseInt(end);
		int dif = r.nextInt(e - b + 1);
		
		return AppUtils.numberToString(b + dif, length);
	}
	
	/**
	 * 获取指定格式的系统当前时间
	 * @param dateFormat
	 * @return
	 */
	public static String getFormatDate(String dateFormat) {
		return new SimpleDateFormat(dateFormat).format(new Date());
	}
	/**
	 * 接受YYYY-MM-DD的日期字符串参数,返回两个日期相差的天数
	 * 
	 * @param start
	 * @param end
	 * @return
	 * @throws ParseException
	 */
	public static long getDistDates(String start, String end)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = sdf.parse(start);
		Date endDate = sdf.parse(end);
		return getDistDates(startDate, endDate);
	}

	/**
	 * 返回两个日期相差的天数
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws ParseException
	 */
	public static long getDistDates(Date startDate, Date endDate) {
		int result = 0;
		if (startDate != null && endDate != null) {
			long timeFrom = startDate.getTime();
			long timeTo = endDate.getTime();
			result = (int) ((timeTo - timeFrom) / (1000 * 60 * 60 * 24));
		}
		return result;
	}

	/**
	 * 将String类型的日期转换成Date类型
	 * 
	 * @param date
	 * @param parse
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDate(String date, String parse)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(parse);
		return sdf.parse(date);
	}

	/**
	 * 将String类型的日期转换成Long类型
	 * <p>
	 * Title: getDate
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param date
	 * @param parse
	 * @return
	 * @throws ParseException
	 */
	public static long parseLongDate(String date, String parse)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(parse);
		return sdf.parse(date).getTime();
	}

	/**
	 * 将String类型的日期转换成Date类型，参数个数为yyyy-MM-dd
	 * 
	 * @param date
	 * @param parse
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDate(String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.parse(date);
	}

	/**
	 * 将Date类型的日期转换成String类型
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 * @throws ParseException
	 */
	public static String format(Date date, String pattern)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	public static String format(Timestamp date, String pattern)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	/**
	 * 将Date类型的日期转换成String类型，格式为yyyy-MM-dd
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 * @throws ParseException
	 */
	public static String format(Date date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}

	/**
	 * 系统当天时间
	 * 
	 * @return
	 */
	public static String getCurrentDate() {
		String curDate = null;
		try {
			curDate = format(new Date(), "yyyy-MM-dd");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return curDate;
	}

	/**
	 * 增加天
	 * 
	 * @param date
	 * @return
	 */
	public static Date addDays(Date date, int num) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, num);
		return c.getTime();
	}

	/**
	 * 增加月
	 * 
	 * @param date
	 * @return
	 */
	public static Date addMonths(Date date, int num) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, num);
		return c.getTime();
	}

	/**
	 * 增加年
	 * 
	 * @param date
	 * @return
	 */
	public static Date addYears(Date date, int num) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.YEAR, num);
		return c.getTime();
	}

	/**
	 * 增加时
	 * 
	 * @param date
	 * @return
	 */
	public static Date addHours(Date date, int num) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.HOUR, num);
		return c.getTime();
	}

	/**
	 * 增加秒
	 * 
	 * @param date
	 * @return
	 */
	public static Date addSeconds(Date date, int num) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.SECOND, num);
		return c.getTime();
	}

	public static String getChinese(String date) {
		String cdate = null;
		if (date != null) {
			String oldPatten = "yyyy-MM-dd", newPatten = "yy年MM月dd日";
			if (date.length() >= 16) {
				oldPatten = "yyyy-MM-dd HH:mm";
				newPatten = "yy年M月d日H时m分";
			}
			SimpleDateFormat sdf1 = new SimpleDateFormat(oldPatten);
			SimpleDateFormat sdf2 = new SimpleDateFormat(newPatten);
			try {
				cdate = sdf2.format(sdf1.parse(date));
				cdate = cdate.replace("日0时0分", "日");
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return cdate;
	}

	/**
	 * 获取HH:mm:ss时分秒的Long型
	 * <p>
	 * Title: getHMS
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param time
	 * @return
	 */
	public static long getHMS(String time) {
		if (time != null && time.length() > 0) {
			StringTokenizer token = new StringTokenizer(time, ":");

			int hourTime = Integer.parseInt(token.nextToken()) * 3600;
			int minute = Integer.parseInt(token.nextToken()) * 60;
			int second = Integer.parseInt(token.nextToken());

			return hourTime + minute + second;
		}
		return 0l;
	}

	/**
	 * 根据Long型的时分秒格式化成HH:mm:ss
	 * <p>
	 * Title: getHMS
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param time
	 * @return
	 */
	public static String getHMS(Long time) {
		String timeStr = "";
		if (time != null) {
			long hour = time / 3600;
			if (hour < 10)
				timeStr += "0" + hour + ":";
			else {
				timeStr += hour + ":";
			}
			long minute = (time - hour * 3600) / 60;
			if (minute < 10)
				timeStr += "0" + minute + ":";
			else {
				timeStr += minute + ":";
			}
			long second = time - hour * 3600 - minute * 60;
			if (second < 10)
				timeStr += "0" + second;
			else {
				timeStr += second;
			}
		} else {
			timeStr = "00:00:00";
		}
		return timeStr;
	}

	public static String getCnHMS(Long time) {
		String timeStr = "";
		if (time != null) {
			long hour = time / 3600;
			if (hour > 0) {
				timeStr += hour + "时";
			}
			long minute = (time - hour * 3600) / 60;
			if (minute > 0) {
				timeStr += minute + "分";
			}
			long second = time - hour * 3600 - minute * 60;
			timeStr += second + "秒";
		} else {
			timeStr = "0秒";
		}
		return timeStr;
	}
	/**
	 * 得到系统日期
	 * @param c 连接符号 
	 * 默认yyyyMMdd
	 * '-' yyyy-MM-dd
	 * '/' yyyy/MM/dd
	 * .yyyy.MM.dd
	 * t yyyy-MM-dd HH:mm:ss
	 * s yyyy-MM-dd HH:mm:ss
	 * @throws Exception
	 * @return 返回格式化以后的String日期
	 */
	public static String getSystemDate(char c) throws Exception {
		Date dt = new Date();
		SimpleDateFormat ft = null;
		switch (c) {
		case 'd':
			ft = new SimpleDateFormat("yyyyMMdd");
			break;
		case '-':
			ft = new SimpleDateFormat("yyyy-MM-dd");
			break;
		case '/':
			ft = new SimpleDateFormat("yyyy/MM/dd");
			break;
		case '.':
			ft = new SimpleDateFormat("yyyy.MM.dd");
			break;
		case 't':
			ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			break;
		case 'm':
			ft = new SimpleDateFormat("yyyyMMddHHmm");
			break;
		case 's':
			ft = new SimpleDateFormat("yyyyMMddHHmmss");
			break;
		case 'h':
			ft = new SimpleDateFormat("HHmmss");
			break;
		default:
			ft = new SimpleDateFormat("yyyyMMdd");
			break;
		}
		return ft.format(dt);
	}
	/**
	 * 系统当天时间
	 * 
	 * @return
	 */
	public static long getCurDate() {
		return new Date().getTime();
	}

	public static void main(String[] args) {
		try {
			/*System.out.println(format(new Date(1345080600000l + 1686000),
					"yyyy-MM-dd HH:mm:ss"));*/
		   System.out.println(DateUtil.getSystemDate('s'));
		   System.out.println(DateUtil.getSystemDate('d'));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
