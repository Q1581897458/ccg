package com.mr.service;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Component//创建类 实例化 service也是 牛逼刘六六
public class UploadService {


    public String uploadImg(MultipartFile file) throws IOException {
//1验证后缀
// 2校验是否是图片内容上校验是否是图
// 3上传到服务器（1电脑本机 2分布式服务 fastdfs）



        File imgFolder = new File("E:\\Mr\\images");
        if (imgFolder.exists()==false){
            imgFolder.mkdirs();
        }
        file.transferTo(new File(imgFolder, file.getOriginalFilename()));

        String url = "http://image.b2c.com/" + file.getOriginalFilename();

        return url;
    }


}
