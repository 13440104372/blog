package com.zbw.blog.module.file;


import com.zbw.blog.utils.FtpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FileService {
    private FtpUtil ftpUtil;

    public String uploadFile(MultipartFile file, String path) throws IOException{
        if(path.isEmpty()){
            path = "/upload";
        }
        InputStream inputStream = file.getInputStream();
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        String fileName = UUID.randomUUID() +originalFilename.substring(originalFilename.lastIndexOf("."));
        if(ftpUtil.uploadFile(fileName,path,inputStream)){
            return ftpUtil.getBasePath()+path+"/"+fileName;
        }
        return null;
    }

    public List<String> uploadFileList(List<MultipartFile> files, String path) throws IOException{
        List<String> result = new ArrayList<>();
        for (MultipartFile file:files){
            result.add(uploadFile(file,path));
        }
        return result;
    }



    @Autowired
    public void setFtpUtil(FtpUtil ftpUtil) {
        this.ftpUtil = ftpUtil;
    }

}
