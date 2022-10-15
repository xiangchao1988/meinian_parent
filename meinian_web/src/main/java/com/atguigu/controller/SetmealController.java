package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Setmeal;
import com.atguigu.service.SetmealService;
import com.atguigu.util.QiniuUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/setmeal")//对应实体类的名字，首字母小写
public class SetmealController {
    @Reference
    SetmealService setmealService;

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult=
        setmealService.findPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize(),queryPageBean.getQueryString());
        return pageResult;

    }

    @RequestMapping("/add")
    public Result add(Integer[] travelgroupIds, @RequestBody Setmeal setmeal){
        try {
            setmealService.add(travelgroupIds,setmeal);
            return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,MessageConstant.ADD_SETMEAL_FAIL);
        }
    }

    @RequestMapping("/upload")
    public Result upload(MultipartFile imgFile){
        try {
            //1、获取原始文件名称
            String originalFilename = imgFile.getOriginalFilename();
            //2、生成新的文件名称，防止同名文件被覆盖
            int index = originalFilename.lastIndexOf(".");//获取截取位置索引值gxt97.jpg
            String substring = originalFilename.substring(index);//.jpg
            String filename = UUID.randomUUID().toString()+substring;//拼接生成的UUID变成一个唯一的文件名


            //3、调用工具类，上传图片到七牛云
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),filename);//提供两个上传的方法，传上传路径或字节数组，因为是浏览器传的文件，所以用传字节数组方法

            //4、返回结果
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,filename);//页面要回显图片名称，所以要传回去

        } catch (IOException e) {
            e.printStackTrace();
            return new Result(true,MessageConstant.PIC_UPLOAD_FAIL);
        }
    }
}
