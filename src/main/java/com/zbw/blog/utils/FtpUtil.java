package com.zbw.blog.utils;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

/**
 * ftp工具类
 * @author a1758712122
 */
@Component
@ConfigurationProperties(prefix = "file.ftp")
public class FtpUtil {

    /**
     * ftp服务器地址
     */
    private String hostname;

    /**
     * ftp服务器端口
     */
    private int port;

    /**
     * ftp登录账号
     */
    private String username;

    /**
     * ftp登录密码
     */
    private String password;

    /**
     * ftp保存目录
     */
    private String basePath;

    public boolean uploadFile(String fileName, String filePath, InputStream input) {
        FTPClient client = createFtpClient();
        if(client == null){
            return false;
        }
        try {
            int reply = client.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                client.disconnect();
                return false;
            }
            String path = basePath + filePath;
            if (!client.changeWorkingDirectory(path)) {
                System.out.println(makeDir(client, path));
            }
            //跳转目标目录
            client.changeWorkingDirectory(path);
            client.storeFile(fileName, input);
            input.close();
            client.logout();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (client.isConnected()) {
                try {
                    client.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    private boolean makeDir(FTPClient ftp, String path) throws IOException {
        // 分割
        String[] paths = path.split("/");
        // 创建成功标识
        boolean isMakeSucess = false;
        // 遍历每一级路径
        for (String str : paths) {
            // 切换目录，根据切换是否成功判断子目录是否存在
            boolean changeSuccess = ftp.changeWorkingDirectory(str);
            // 该级路径不存在就创建并切换
            if (!changeSuccess) {
                isMakeSucess = ftp.makeDirectory(str);
                ftp.changeWorkingDirectory(str);
            }
        }
        return isMakeSucess;
    }

    /**
     * 删除文件
     * @param fileFullName 文件带路径全名
     * @return 删除成功返回true
     */
    public boolean deleteFile(String fileFullName) {
        boolean flag = false;
        String fileName = fileFullName.substring(fileFullName.lastIndexOf('/')+1);
        System.out.println(fileName);
        String filePath = fileFullName.substring(0,fileFullName.lastIndexOf('/'));
        System.out.println(filePath);
        FTPClient client = createFtpClient();
        if(client == null){
            return false;
        }
        try {
            // 切换FTP目录
            client.changeWorkingDirectory(filePath);
            client.dele(fileName);
            client.logout();
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (client.isConnected()) {
                try {
                    client.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return flag;
    }


    private FTPClient createFtpClient() {
        FTPClient client = new FTPClient();
        try {
            // 连接FTP服务器
            client.connect(hostname, port);
            // 登录
            client.login(username, password);
            client.setFileType(FTPClient.BINARY_FILE_TYPE);
            client.setControlEncoding("UTF-8");
            client.setConnectTimeout(5000);
            client.enterLocalPassiveMode();
            return client;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }
}
