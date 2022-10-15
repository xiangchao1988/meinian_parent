package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.service.MemberService;
import com.atguigu.service.ReportService;
import com.atguigu.service.SetmealService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/report")//对应实体类的名字，首字母小写
public class ReportController {
    @Reference
    MemberService memberService;
    @Reference
    SetmealService setmealService;
    @Reference
    ReportService reportService;


    @RequestMapping("/exportBusinessReport")
    public void exportBusinessReport(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {//下载文件，不需要返回结果
        try {

        //1、拿到数据
        Map result = reportService.getBusinessReportData();
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

        //2、获取导入的模板文件

        //获得Excel模板文件绝对路径
        //file.separator这个代表系统目录中的间隔符，说白了就是斜线。因为linux中路径是左斜线，windows里是右斜线，为了满足跨平台都可以，可以使用File.separator表示斜线
        String filePath = request.getSession().getServletContext().getRealPath("template") +
                File.separator + "report_template.xlsx";


        //3、定义一个工作簿,往工作簿里写数据
            Workbook workbook = new XSSFWorkbook(new File(filePath));

        //4、写数据

            Sheet sheet = workbook.getSheetAt(0);//确定写入sheet1中

            Row row = sheet.getRow(2);
            row.getCell(5).setCellValue(reportDate);//日期

            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
            row.getCell(7).setCellValue(totalMember);//总会员数

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日出游数

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周出游数

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月出游数

            int rowNum = 12;
            for(Map map : hotSetmeal){//热门套餐
                String name = (String) map.get("name");
                Long setmeal_count = (Long) map.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) map.get("proportion");
                row = sheet.getRow(rowNum ++);
                row.getCell(4).setCellValue(name);//套餐名称
                row.getCell(5).setCellValue(setmeal_count);//预约数量
                row.getCell(6).setCellValue(proportion.doubleValue());//占比
            }

            //5、输出文件，以流形式文件下载，另存为操作
            ServletOutputStream out = response.getOutputStream();

            // 下载的数据类型（excel类型）
            response.setContentType("application/vnd.ms-excel");
            // 设置下载形式(通过附件的形式下载)
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");//设置下载文件的格式、位置，自定义默认的名字report.xlsx

            workbook.write(out);//写给了浏览器，就是文件下载

            //6、关闭
            out.flush();//刷新流，防止数据太大，刷新下
            out.close();
            workbook.close();


        } catch (Exception e) {
            e.printStackTrace();
            //跳转到错误页面,做请求转发
            request.getRequestDispatcher("/pages/error/downloadError.html").forward(request,response);
        }


    }

    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData(){

        try {
            Map map=reportService.getBusinessReportData();
            return new Result(true,MessageConstant.GET_BUSINESS_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport(){

        try {
            List<String> setmealNames = new ArrayList<>();//只要setmealCount集合获得就能赋值得到这个名称的集合
            List<Map> setmealCount = setmealService.getSetmealReport();

            for (Map map : setmealCount) {//获得setmealCount集合后便利，将name值赋值到setmealNames集合中
               String setmealName = (String)map.get("name");
               setmealNames.add(setmealName);
            }

            Map map=new HashMap();
            map.put("setmealNames",setmealNames);//看笔记1.3的小结，需要的数据是这两个集合,所以在上面再准备两个List集合
            map.put("setmealCount",setmealCount);

            return new Result(true,MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,map);//代码可以倒着写，这里写要返回的内容，然后看怎么实现
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }


    @RequestMapping("/getMemberReport")
    public Result getMemberReport(){

        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM");//因为要的是yyy=MM样式的字符串格式,而得到的是date,所以new一个负责格式的对象出来使用
        try {

            List<String> months = new ArrayList();
            List<Integer> memberCount= null;

            Calendar calendar = Calendar.getInstance();//实例一个日历对象出来
            calendar.add(Calendar.MONTH,-12);//当前往前的12个月添加到对象中

            for(int i=0;i<12;i++){//理解不了为什么这么弄，但大概的意思就是做个循环，将calendar对象里的月份装到months集合中
                //第一个参数是月份 2018-7
                //第二个参数是月份+1个月
                calendar.add(Calendar.MONTH,1);
                Date time = calendar.getTime();//这里是date格式，所以要格式化为字符格式
                String month = sdf.format(time);
                months.add(month);
            }

            memberCount=memberService.findMemberCountByMonth(months);//将月份集合传进去，调用远程服务查出每个月对应的会员数



            Map map = new HashMap();
            map.put("months",months);
            map.put("memberCount",memberCount);

            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }
}
