package com.extract;

import javax.swing.*;
import java.io.*;

public class ReaderExtract extends ExtractTemplate {

    @Override
    public void extractByFileList(BufferedReader reader, String sourceRootPath, String targetPath, JTextArea jta) throws IOException {
        int count = 1;
        String line;
        while((line = reader.readLine()) != null) {
            line = line.trim();
            if("".equals(line)) continue;

            try {
                String oriPath = sourceRootReplacePath(line);
                String destPath = "ROOT" + File.separator + targetReplacePath(line);
                //System.out.println("oriPath : " + oriPath + " ==> destPath : " + destPath);

                File oriFile = new File(sourceRootPath + File.separator + oriPath);
                File destFile = new File(targetPath + File.separator + destPath);

                makeDirs(destFile);
                copyFile(oriFile, destFile);

                jta.append((count++) + ". " + line + " ==> " + destPath + "\n");
            } catch(Exception e) {
                e.printStackTrace();
                jta.append((count++) + ". " + line + " 오류 발생!!\n");
            }
        }
    }

    public void makeDirs(File file) {
        String dirPath = file.getPath();
        dirPath = dirPath.substring(0, dirPath.lastIndexOf(File.separator));

        File dirFile = new File(dirPath);
        if(!dirFile.exists()) {
            dirFile.mkdirs();
        }
    }

    public String sourceRootReplacePath(String path) {
        String ext = getExt(path);
        if("java".equals(ext)) {
            return path.replace("src" + File.separator + "main" + File.separator + "java", "target" + File.separator + "classes")
                        .replaceAll(".java", ".class");
        }
        return path;
    }

    public String targetReplacePath(String path) {
        String ext = getExt(path);
        if("jsp".equals(ext)) {
            return path.replace("src" + File.separator + "main" + File.separator + "webapp" + File.separator, "");
        } else if("java".equals(ext)) {
            return path.replace("src" + File.separator + "main" + File.separator + "java", "WEB-INF" + File.separator + "classes")
                        .replaceAll(".java", ".class");
        } else if("xml".equals(ext)) {
            return path.replace("src" + File.separator + "main" + File.separator + "resources", "WEB-INF" + File.separator + "classes");
        }
        return path;
    }

    public String getExt(String path) {
        return path.substring(path.lastIndexOf(".") + 1);
    }

    public void copyFile(File oriFile, File destFile) throws IOException {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(oriFile);
            fos = new FileOutputStream(destFile);

            int fileByte = 0;
            while((fileByte = fis.read()) != -1) {
                fos.write(fileByte);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if(fis != null) {
                fis.close();
            }
            if(fos != null) {
                fos.close();
            }
        }
    }

}
