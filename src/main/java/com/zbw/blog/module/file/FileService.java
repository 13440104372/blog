package com.zbw.blog.module.file;


import com.zbw.blog.utils.FtpUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author 17587
 */
@Service
public class FileService {
    private FtpUtil ftpUtil;

    public String uploadFile(MultipartFile file, String path) throws IOException {
        if (null == path || path.isEmpty()) {
            path = "/upload";
        }
        InputStream inputStream = file.getInputStream();
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        String fileName = UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
        if (ftpUtil.uploadFile(fileName, path, inputStream)) {
            return ftpUtil.getBasePath() + path + "/" + fileName;
        }
        return null;
    }

    public List<String> uploadFileList(List<MultipartFile> files, String path) throws IOException {
        List<String> result = new ArrayList<>();
        for (MultipartFile file : files) {
            result.add(uploadFile(file, path));
        }
        return result;
    }

    public boolean deleteFile(String fileFullName) {
        return ftpUtil.deleteFile(fileFullName);
    }


    @Autowired
    public void setFtpUtil(FtpUtil ftpUtil) {
        this.ftpUtil = ftpUtil;
    }

}
