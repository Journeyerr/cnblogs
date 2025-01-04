package com.cnblog.htmltopng.service.impl;

import com.cnblog.htmltopng.service.HtmlToPngService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class HtmlToPngServiceImplTest {

    @Autowired
    private HtmlToPngService htmlToPngService;


    @Test
    public void htmlToPngTest(){
        try {
            htmlToPngService.htmlToPng();
        }catch (Exception e) {
            log.info("PngError:{}", e.getMessage());
        }
    }
}