package com.zbw.blog.module.file;

import com.zbw.blog.AppResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/ftp")
public class FileController {

    private FileService service;

    @PostMapping("/uploadFile")
    public AppResponse<String> uploadFile(@RequestParam("file") MultipartFile file,
                                          @RequestParam("path") String path) throws IOException {
        if (file.isEmpty()) {
            return AppResponse.onError(-1, "文件不能为空");
        }
        String result = service.uploadFile(file, path);
        if (result == null) {
            return AppResponse.onError(-1, "上传失败");
        }
        return AppResponse.onSuccess(result);
    }

    @PostMapping("/uploadFileList")
    public AppResponse<List<String>> uploadFileList(HttpServletRequest request,
                                               @RequestParam("path") String path) throws IOException {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        List<String> result = service.uploadFileList(files, path);
        if (result.size() == 0) {
            return AppResponse.onError(-1, "上传失败");
        }
        return AppResponse.onSuccess(result);
    }



    @Autowired
    public void setService(FileService service) {
        this.service = service;
    }
}


