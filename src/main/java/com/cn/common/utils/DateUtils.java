package com.cn.common.utils;

import com.xiaoleilu.hutool.date.DateUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
public class DateUtils {

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
      
    /** 
     * 将Date类转换为XMLGregorianCalendar 
     * @param date 
     * @return  
     */  
    public static XMLGregorianCalendar dateToXmlDate(Date date){  
            Calendar cal = Calendar.getInstance();  
            cal.setTime(date);  
            DatatypeFactory dtf = null;  
             try {  
                dtf = DatatypeFactory.newInstance();  
            } catch (DatatypeConfigurationException e) {  
            }  
            XMLGregorianCalendar dateType = dtf.newXMLGregorianCalendar();  
            dateType.setYear(cal.get(Calendar.YEAR));  
            //由于Calendar.MONTH取值范围为0~11,需要加1  
            dateType.setMonth(cal.get(Calendar.MONTH)+1);  
            dateType.setDay(cal.get(Calendar.DAY_OF_MONTH));  
            dateType.setHour(cal.get(Calendar.HOUR_OF_DAY));
            dateType.setMinute(cal.get(Calendar.MINUTE));  
            dateType.setSecond(cal.get(Calendar.SECOND));  
            return dateType;  
        }   
  
    /** 
     * 将XMLGregorianCalendar转换为Date 
     * @param cal 
     * @return  
     */  
    public static Date xmlDate2Date(XMLGregorianCalendar cal){  
        return cal.toGregorianCalendar().getTime();  
    }

    /**
     * 获得两个时间间的所有时间
     */
    public static List<String> getAllDateStr(String startDateStr,String endDateStr,long count){
        List<String> dateList = new ArrayList<>();
        Date startDate = DateUtil.parse(startDateStr,"yyyy-MM-dd HH:mm:ss");
        Date endDate = DateUtil.parse(endDateStr,"yyyy-MM-dd HH:mm:ss");
        //计算间隔时间
        Integer interval = Math.toIntExact((endDate.getTime()+20000 - startDate.getTime()) / (1000 * count));
        Date tempDate = startDate;
        dateList.add(DateUtil.format(startDate,"yyyy-MM-dd HH:mm:ss"));
        for(int i = 1;i<=count;i++) {
            tempDate = DateUtil.offsetSecond(tempDate, interval);
            if(tempDate.getTime()<=endDate.getTime()){
                dateList.add(DateUtil.format(tempDate,"yyyy-MM-dd HH:mm:ss"));
            }
        }
        return dateList;
    }

    public static String getTime(long time){
        return simpleDateFormat.format(new Date(time));
    }
    public static void main(String[] args) {
        System.out.println(getTime(1537933230000L));
    }
}