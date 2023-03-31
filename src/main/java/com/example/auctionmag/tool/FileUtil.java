package com.example.auctionmag.tool;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class FileUtil {

    public static void save(MultipartFile file) {
        File temp = new File("F:\\uploads");
        if (!temp.exists()) {
            temp.mkdirs();
        }

        File localFile = new File("F:\\uploads\\" + file.getOriginalFilename());
        try {
            file.transferTo(localFile); //把上传的文件保存至本地
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] getFileBytes(String fileName){
        File localFile = new File("F:\\uploads\\" + fileName);
        if (!localFile.exists() || localFile.isDirectory()){
            return new byte[0];
        }
        // 将文件写入输入流
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(localFile);
            InputStream fis = new BufferedInputStream(fileInputStream);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            return buffer;
        } catch (IOException e) {
            e.printStackTrace();
        }
       return null;
    }
}
