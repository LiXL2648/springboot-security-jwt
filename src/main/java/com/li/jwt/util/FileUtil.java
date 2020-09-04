package com.li.jwt.util;

import com.li.jwt.config.MyConstants;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;

public class FileUtil {

    public static void printImg(String filePath, HttpServletResponse response) {
        BufferedInputStream bis = null;
        InputStream in = null;
        OutputStream out = null;
        byte[] b = null;
        int len;
        try {
            File file = new File(filePath);
            if (file.exists()) {
                in = new FileInputStream(filePath);
            } else {
                return;
            }
            bis = new BufferedInputStream(in);
            out = response.getOutputStream();
            b = new byte[1024];

            while ((len = bis.read(b)) != -1) {
                out.write(b, 0, len);
                out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String removefile(String filePath, String filePathDir) {
            File startFile = new File(filePath);
            String newFilePath = filePathDir + "\\" + startFile.getName();
            File endFile = new File(newFilePath);
            startFile.renameTo(endFile);
            return newFilePath;
    }

    public static void copyFile(String oldFilePath, String newFilePath) {
        File oldFile = new File(oldFilePath);
        File newFile = new File(newFilePath);
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            FileInputStream fis = new FileInputStream(oldFile);
            FileOutputStream fos = new FileOutputStream(newFile);
            bis = new BufferedInputStream(fis);
            bos = new BufferedOutputStream(fos);

            byte[] b = new byte[1024];
            int len;
            while ((len = bis.read(b)) != -1) {
                bos.write(b, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void deleteFile(String filePath) {
        if (filePath != null && !"".equals(filePath)) {
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    public static String getFileDir(Date date) {
        String dateDir = ObjectUtil.getYearMonth(date);
        String fileDir = MyConstants.BASE_PATH + dateDir;
        File dir = new File(fileDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return fileDir;
    }


}
