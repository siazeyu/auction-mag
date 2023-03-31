package com.example.auctionmag.controller;

import com.example.auctionmag.tool.FileUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/file")
public class FileController {

    @GetMapping("/{fileName}")
    public byte[]getFile(@PathVariable String fileName){
        return FileUtil.getFileBytes(fileName);
    }

}
