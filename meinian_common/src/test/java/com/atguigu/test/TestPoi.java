package com.atguigu.test;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.IOException;

public class TestPoi {
    @Test
    public void readExcl() throws IOException {
        //1、创建工作簿
        XSSFWorkbook workbook = new XSSFWorkbook("D:\\guigu.xlsx");
        //2、设置读取哪个sheet,可以根据顺序或sheet名称
        XSSFSheet sheet = workbook.getSheetAt(0);
        //3、读取行内容
        for (Row row : sheet) {
            //4、读取单元格的值
            for (Cell cell : row) {
                String value = cell.getStringCellValue();
                System.out.println("value:"+value);
            }
        }
        workbook.close();
    }
}
