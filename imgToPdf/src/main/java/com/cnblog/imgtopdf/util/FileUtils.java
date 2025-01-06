package com.cnblog.imgtopdf.util;

import com.aspose.pdf.Document;
import com.aspose.pdf.Image;
import com.aspose.pdf.Page;
import com.aspose.pdf.Rectangle;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.URL;

@Slf4j
public class FileUtils {
    
    /**
     * 根据文件路径获取文件
     * @param filePath 文件路径
     * @return File
     */
    public static File getFileByPath(String filePath) {
        log.info("------------开始获取图片文件------------");
        // 拆分路径，获取文件后缀名
        String[] split = filePath.substring(filePath.lastIndexOf("/") + 1).split("\\.");
        
        File file = null;
        URL url;
        InputStream inStream = null;
        OutputStream os = null;
        try {
            // 创建临时文件，使用路径名称和路径后缀名
            file = File.createTempFile(split[0], "." + split[1]);
            url = new URL(filePath);
            inStream = url.openStream();
            os = new FileOutputStream(file);
            
            int bytesRead;
            int byteSize = 1024 * 528;
            byte[] buffer = new byte[byteSize];
            // 写入文件
            while ((bytesRead = inStream.read(buffer, 0, byteSize)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            log.info("------------获取图片文件完成------------");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != os) {
                    os.close();
                }
                if (null != inStream) {
                    inStream.close();
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }
    
    /**
     * 图片转PDF
     * @param file 图片文件
     * @return File PDF文件
     */
    public static File imgToPdfFile(File file) {
        log.info("------------开始执行图片转PDF------------");
        // 获取classpath,还可使用临时文件夹
        String pdfPath = ClassLoader.getSystemResource("").getPath();
        String pdfName = "demo.pdf";
        try {
            String pdfFilePath = pdfPath + pdfName;
            // 创建一个新文档
            Document doc  = new Document();
            // 将页面添加到文档的页面集合
            Page page = doc.getPages().add();
            // 将图像加载到流中
            java.io.FileInputStream imageStream = new java.io.FileInputStream(file.getPath());
            // 设置边距，以便图像适合等。
            page.getPageInfo().getMargin().setBottom(0);
            page.getPageInfo().getMargin().setTop(200);
            page.getPageInfo().getMargin().setLeft(200);
            page.getPageInfo().getMargin().setRight(0);
            page.setCropBox(new Rectangle(0, 0, 595, 842));
            // 创建图像对象
            Image image1 = new Image();
            // 将图像添加到该部分的段落集合中
            page.getParagraphs().add(image1);
            // 设置图片文件流
            image1.setImageStream(imageStream);
            // 保存生成的 PDF 文件
            doc.save(pdfFilePath);
            //输出流
            File mOutputPdfFile = new File(pdfFilePath);
            // 判断临时文件否存在，如果存在可选择删除
            if (!mOutputPdfFile.exists()) {
                mOutputPdfFile.deleteOnExit();
                return null;
            }
            doc.close();
            log.info("------------图片转PDF执行完成------------");
            return mOutputPdfFile;
        } catch (IOException e) {
            log.error("图片转PDF失败：file:{}, error:{}", file.getPath(), e.getMessage());
        }
        return null;
    }
}
