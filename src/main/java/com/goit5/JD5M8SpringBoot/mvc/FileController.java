package com.goit5.JD5M8SpringBoot.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@RequestMapping("/file")
@Controller
public class FileController {
    @PostMapping("/upload")
    public ModelAndView postReceiveFile(@RequestParam(name = "file") MultipartFile file) throws IOException {
        ModelAndView result = new ModelAndView("upload-file");

//        int fileSize = file.getBytes().length;
//        result.addObject("fileSize", fileSize);

        byte[] bytes = file.getBytes();
        int fileSize = bytes.length;
        result.addObject("fileSize", bytes.length);
        result.addObject("fileContent", new String(bytes));

        return result;
    }
}