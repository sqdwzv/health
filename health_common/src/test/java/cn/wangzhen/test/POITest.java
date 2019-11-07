package cn.wangzhen.test;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class POITest {
  /*  //遍历工作表过去数据
    @Test
    public void test2() throws Exception {
        //创建工作簿
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook("D:\\poi.xlsx");
        //获取工作表可以根据顺序获取(0开始)也可以根据名称获取
        XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
        //遍历工作表获得行对象
        for (Row row : sheet) {
            //获得单元格对象
            for (Cell cell : row) {
                //获得单元格中的值
                String stringCellValue = cell.getStringCellValue();
                System.out.println(stringCellValue);
            }
        }
        xssfWorkbook.close();
    }

    //获取工作表最后一个行号，从而根据行号获得行对象
    @Test
    public void test() throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook("d:\\poi.xlsx");
        XSSFSheet sheetAt = workbook.getSheetAt(0);
        //获取当前工作表最后一行的行号，行号从0开始
        int lastRowNum = sheetAt.getLastRowNum();
        for (int i = 0; i <= lastRowNum; i++) {
            XSSFRow row = sheetAt.getRow(i);
            short lastCellNum = row.getLastCellNum();
            for (int j = 0; j < lastCellNum; j++) {
                String stringCellValue = row.getCell(j).getStringCellValue();
                System.out.println(stringCellValue);
            }
        }
    }

    //向excel中写入数据
    @Test
    public void test3() throws IOException {
        //创建一个空的excel文件
        XSSFWorkbook sheets = new XSSFWorkbook();
        //创建表
        XSSFSheet sheet = sheets.createSheet("传智健康");
        //创建行
        XSSFRow row = sheet.createRow(0);
        //创建单元格
        row.createCell(0).setCellValue("编号");
        row.createCell(1).setCellValue("名称");
        row.createCell(2).setCellValue("年龄");
        XSSFRow row1 = sheet.createRow(1);
        row1.createCell(0).setCellValue("1");
        row1.createCell(1).setCellValue("小明");
        row1.createCell(2).setCellValue("10");
        XSSFRow row2 = sheet.createRow(2);
        row2.createCell(0).setCellValue("2");
        row2.createCell(1).setCellValue("小王");
        row2.createCell(2).setCellValue("20");
        //通过输出流将workbook对象下载到磁盘
        FileOutputStream fileOutputStream = new FileOutputStream("d:\\itcast.xlsx");
        sheets.write(fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
        sheets.close();
    }*/
}
