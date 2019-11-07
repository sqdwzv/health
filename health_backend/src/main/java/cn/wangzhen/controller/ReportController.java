package cn.wangzhen.controller;

import cn.wangzhen.constant.MessagConstant;
import cn.wangzhen.entity.Result;
import cn.wangzhen.service.MemberService;
import cn.wangzhen.service.ReportService;
import cn.wangzhen.service.SetmealService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 统计报表
 */
@RestController
@RequestMapping("/report")
public class ReportController {
    @Reference
    private MemberService memberService;
    @Reference
    private SetmealService setmealService;
    @Reference
    private ReportService reportService;



    /*性别占比统计*/
    @RequestMapping("/getGenderReport")
    public Result getGenderReport(){
        try {
            Map<String,Object> map = new HashMap<>();
            List<Map<String,Object>> memberCount =   memberService.findMemberCount();
            map.put("memberCount",memberCount);

            List<String> memberSexs = new ArrayList<>();
            for (Map<String, Object> stringObjectMap : memberCount) {
                String memberSex = (String) stringObjectMap.get("name");
                memberSexs.add(memberSex);
            }
            map.put("memberSex",memberSexs);
            return new Result(true,"获取性别统计占比成功",map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"获取性别统计占比失败");
        }

    }

    /*套餐占比统计*/
    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport(){

        try {
            List<Map<String,Object>> setmealCount = setmealService.findSetmealCount();
            Map<String,Object> map = new HashMap<>();
            map.put("setmealCount",setmealCount);

            List<String> setmealNames = new ArrayList<>();

            for (Map<String, Object> map1 : setmealCount) {
                String name = (String) map1.get("name");
                setmealNames.add(name);
            }
            map.put("setmealNames",setmealNames);

            return new Result(true,MessagConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessagConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }


    }
    /*导出excel报表*/
    @RequestMapping("/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            //远程调用报表服务获取报表数据
            Map<String, Object> result = reportService.getBusinessReportData();
            //取出返回结果数据,准备将数据写入excel中
            String reportDate = (String) result.get("reportDate");
            Integer todayNewMember = (Integer) result.get("todayNewMember");
            Integer totalMember = (Integer) result.get("totalMember");
            Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");

            //获得excel模板绝对路径  File.separator 任意系统下的/
            String filePath = request.getSession().getServletContext().getRealPath("template")+ File.separator+"report_template.xlsx";

            //读取模板文件创建excel表格对象
            XSSFWorkbook excel = new XSSFWorkbook(new FileInputStream(new File(filePath)));

            //读取工作表
            XSSFSheet sheetAt = excel.getSheetAt(0);

            //获得第三行
            XSSFRow row = sheetAt.getRow(2);
            XSSFCell cell = row.getCell(5);
            cell.setCellValue(reportDate);//日期

            //五行六 8 列
            row = sheetAt.getRow(4);
            row.getCell(5). setCellValue(todayNewMember);//新增会员数;
            row.getCell(7) .setCellValue(totalMember);//总会员数

            row=sheetAt.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数


            row=sheetAt.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约人数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日到诊人数

            row=sheetAt.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约人数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊人数

            row=sheetAt.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约人数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月到诊人数

            //热门套餐
            int rowNum = 12;
            for (Map map : hotSetmeal) {
                String name = (String) map.get("name");
                Long setmeal_count = (Long) map.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) map.get("proportion");

                row = sheetAt.getRow(rowNum++);
                row.getCell(4).setCellValue(name);//套餐名称
                row.getCell(5).setCellValue(setmeal_count);//预约数量
                row.getCell(6).setCellValue(proportion.doubleValue());//占比
            }
                //获取输出流
                ServletOutputStream out = response.getOutputStream();
                response.setContentType("application/vnd.ms-excel");//代表excel文件类型
                response.setHeader("content-Disposition","attachment;filename=report.xlsx");//指定以附件形式下载
                excel.write(out);

                out.flush();
                out.close();
                excel.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessagConstant.GET_BUSINESS_REPORT_FAIL);
        }

    }




    /*获取运营统计数据*/
    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData(){
        Map<String,Object> map = null;
        try {
            map = reportService.getBusinessReportData();
            return new Result(true,MessagConstant.GET_BUSINESS_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessagConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }





    /*会员数量统计*/
    @RequestMapping("/getMemberReport")
    public Result getMemberReport(){
      /*  日历类*/
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,-12);//获得当前日期之前12个月的日期
        Date time = calendar.getTime();//前第12个月的时间
        List<String> months = new ArrayList<>();//存储时间

        for (int i = 0; i < 12; i++) {
            calendar.add(Calendar.MONTH,1);
            months.add(new SimpleDateFormat("yyyy,MM").format(calendar.getTime()));
        }

        Map<String,Object> map = new HashMap<>();

        map.put("months",months);

      List<Integer> memberCount=  memberService.findMemberCountByMonth(months);

        map.put("memberCount",memberCount);

        return new Result(false, MessagConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
    }
}
