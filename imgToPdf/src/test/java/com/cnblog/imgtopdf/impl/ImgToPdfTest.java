package com.cnblog.imgtopdf.impl;

import com.cnblog.imgtopdf.util.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

@Slf4j
@SpringBootTest
class ImgToPdfTest {

    @Test
    public void imgToPdf(){
        
        String imgPath = "https://pic.cnblogs.com/avatar/2319511/20210331180835.png";
        File file = FileUtils.getFileByPath(imgPath);
        FileUtils.imgToPdfFile(file);
    }
}