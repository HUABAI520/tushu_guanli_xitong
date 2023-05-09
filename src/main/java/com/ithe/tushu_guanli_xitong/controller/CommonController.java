package com.ithe.tushu_guanli_xitong.controller;


import com.ithe.tushu_guanli_xitong.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {
    @Value("${tu.path}")
    private String basePath;

    /*
    文件下载
    * */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){

        try {
            //输入流，通过输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));
            log.info("找到图片！");
            //输出流，通过输出流将文件写回浏览器，在浏览器展示图片
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            outputStream.close();
            fileInputStream.close();//关闭资源
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    /*
    文件上传
    * */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        //file是一个临时文件，需要转存到指定位置
        log.info(file.toString());
        String originalFilename = file.getOriginalFilename();//使用原始文件名
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String s = UUID.randomUUID().toString()+suffix;//重新生成文件名,防止名称重复
        //创建一个目录对象
        File file1 = new File(basePath);
        //判断当前目录是否存在,不存在则创建
        if(!file1.exists()){
            file1.mkdirs();
        }
        try {
            //将临时文件转存到指定位置
//            file.transferTo(new File(basePath+originalFilename));
            file.transferTo(new File(basePath+s));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(s);
    }
}
