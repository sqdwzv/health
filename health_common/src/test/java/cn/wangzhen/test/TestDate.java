package cn.wangzhen.test;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TestDate {
    @Test
    public void test1() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        Date date = new Date();
        String format = simpleDateFormat.format(date);
        Calendar instance = Calendar.getInstance();//当前时间
        instance.setTime(date);
        int i = instance.get(Calendar.DAY_OF_MONTH);
        System.out.println(i);
        System.out.println(format);
    }
}
