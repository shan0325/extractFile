package com.extract;

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
                line = replacePathSeparator(line);
                String oriPath = sourceRootReplacePath(line);
                String destPath = "ROOT/" + targetReplacePath(line);
                //System.out.println("oriPath : " + oriPath + " ==> destPath : " + destPath);

                File oriFile = new File(sourceRootPath + "/" + oriPath);
                File destFile = new File(targetPath + "/" + destPath);

                makeDirs(destFile);
                copyFile(oriFile, destFile);

                jta.append((count++) + ". " + line + " ==> " + destPath + "\n");
                outputFileList.append(destPath + "\n");
            } catch(Exception e) {
                e.printStackTrace();
                jta.append((count++) + ". " + line + " 오류 발생!!\n");
            }
        }
        jta.append("추출을 완료하였습니다.\n");
        outputFile(targetPath, outputFileList);
    }

    public void outputFile(String targetPath, StringBuffer outputFileList) {
        String outputFileName = "extract_" + System.currentTimeMillis() + ".txt";
        File outputFile = new File(targetPath + "/" + outputFileName);

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(outputFile));
            writer.write(outputFileList.toString());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(writer != null) {
                try { writer.close(); } catch (IOException e) { e.printStackTrace(); }
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
    
    public String replacePathSeparator(String path) {
        return path.replaceAll("\\\\", "/");
    }

    public String sourceRootReplacePath(String path) {
        String ext = getExt(path);
        if("java".equals(ext)) {
            return path.replaceAll("src/main/java", "target/classes")
                        .replaceAll(".java", ".class");
        }
        return path;
    }

    public String targetReplacePath(String path) {
        String ext = getExt(path);
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
