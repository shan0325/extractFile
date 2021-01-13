package com.extract;

import com.extract.util.FileUtil;

import javax.swing.*;
import java.io.*;
import java.util.UUID;

public class ReaderExtract extends ExtractTemplate {

    @Override
    public void extractByFileList(BufferedReader reader, String sourceRootPath, String targetPath, JTextArea jta) throws IOException {
        StringBuffer outputFileList = new StringBuffer();
        int count = 1;
        String line;
        while((line = reader.readLine()) != null) {
            line = line.trim();
            if("".equals(line)) continue;

            try {
                line = FileUtil.replacePathSeparator(line);
                String oriPath = sourceRootReplacePath(line);
                String destPath = "ROOT/" + targetReplacePath(line);
                //System.out.println("oriPath : " + oriPath + " ==> destPath : " + destPath);

                File oriFile = new File(sourceRootPath + "/" + oriPath);
                File destFile = new File(targetPath + "/" + destPath);

                FileUtil.makeDirs(destFile);
                FileUtil.copyFile(oriFile, destFile);

                jta.append((count++) + ". " + line + " ==> " + destPath + "\n");
                outputFileList.append(destPath + "\n");
            } catch(Exception e) {
                e.printStackTrace();
                jta.append((count++) + ". " + line + " 오류 발생!!\n");
            }
        }
        jta.append("추출을 완료하였습니다.\n");
        FileUtil.outputFile(targetPath, outputFileList);
    }

    public String sourceRootReplacePath(String path) {
        String ext = FileUtil.getExt(path);
        if("java".equals(ext)) {
            return path.replaceAll("src/main/java", "target/classes")
                        .replaceAll(".java", ".class");
        }
        return path;
    }

    public String targetReplacePath(String path) {
        String ext = FileUtil.getExt(path);
        if("jsp".equals(ext)) {
            return path.replaceAll("src/main/webapp/", "");
        } else if("java".equals(ext)) {
            return path.replace("src/main/java", "WEB-INF/classes")
                        .replaceAll(".java", ".class");
        } else if("xml".equals(ext)) {
            return path.replaceAll("src/main/resources", "WEB-INF/classes");
        }
        return path;
    }

}
