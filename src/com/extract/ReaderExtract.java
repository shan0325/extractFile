package com.extract;

import com.extract.util.FileUtil;

import javax.swing.*;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ReaderExtract extends ExtractTemplate {

    @Override
    public ExtractResult extractByFileList(BufferedReader reader, String sourceRootPath, String targetPath, JTextArea jta) throws IOException {
        StringBuffer outputFileList = new StringBuffer();
        int count = 0;
        int successCount = 0;
        int failCount = 0;
        String line;
        while((line = reader.readLine()) != null) {
            line = line.trim();
            if("".equals(line)) continue;

            try {
                sourceRootPath = FileUtil.replacePathSeparator(sourceRootPath);
                String rootName = sourceRootPath.substring(sourceRootPath.lastIndexOf("/") + 1);

                line = FileUtil.replacePathSeparator(line);
                //String oriPath = sourceRootReplacePath(line);
                //String destPath = rootName + "/" + targetReplacePath(line);
                String oriPath = sourceRootReplacePathByConfigFile(line);
                String targetReplacePath = targetReplacePathByConfigFile(line);
                String destPath = targetReplacePath.indexOf("/") == 0 ? rootName + targetReplacePath : rootName + "/" + targetReplacePath;
                //System.out.println("oriPath : " + oriPath + " ==> destPath : " + destPath);

                File oriFile = new File(sourceRootPath + "/" + oriPath);
                File destFile = new File(targetPath + "/" + destPath);

                FileUtil.makeDirs(destFile);
                FileUtil.copyFile(oriFile, destFile);

                jta.append((++count) + ". " + line + " ==> " + destPath + "\n");
                outputFileList.append(destPath + "\n");
                successCount++;
            } catch(FileNotFoundException e) {
                e.printStackTrace();
                jta.append((++count) + ". [파일없음] " + FileUtil.replacePathSeparator(sourceRootPath) + "/" + sourceRootReplacePathByConfigFile(line) + "\n");
                failCount++;
            } catch(Exception e) {
                e.printStackTrace();
                jta.append((++count) + ". [오류발생] " + FileUtil.replacePathSeparator(sourceRootPath) + "/" + sourceRootReplacePathByConfigFile(line) + "\n");
                failCount++;
            }
            jta.setCaretPosition(jta.getDocument().getLength()); // 스크롤 맨 아래로
        }
        FileUtil.outputFile(targetPath, outputFileList); // 성공한 파일 목록 txt파일로 추출

        ExtractResult extractResult = new ExtractResult();
        extractResult.setCount(count);
        extractResult.setSuccessCount(successCount);
        extractResult.setFailCount(failCount);

        return extractResult;
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
            return path.replaceAll("src/main/webapp/", "ROOT/");
        } else if("java".equals(ext)) {
            return path.replace("src/main/java", "ROOT/WEB-INF/classes")
                        .replaceAll(".java", ".class");
        } else if("xml".equals(ext)) {
            return path.replaceAll("src/main/resources", "ROOT/WEB-INF/classes");
        }
        return path;
    }

    public String sourceRootReplacePathByConfigFile(String path) {
        Map<String, Object> jsonObj = FileUtil.getJsonObjByConfigJsonFile();
        List<String> inPathList = (List<String>) jsonObj.get("inPathList");
        return replacePathByConfigFile(path, inPathList);
    }

    public String targetReplacePathByConfigFile(String path) {
        Map<String, Object> jsonObj = FileUtil.getJsonObjByConfigJsonFile();
        List<String> outPathList = (List<String>) jsonObj.get("outPathList");
        return replacePathByConfigFile(path, outPathList);
    }

    public String replacePathByConfigFile(String path, List<String> pathList) {
        if(pathList != null) {
            for(int i = 0; i < pathList.size(); i++) {
                String pathStr = pathList.get(i);
                if(!"".equals(pathStr)) {
                    String[] pathStrs = pathStr.split(">");
                    if(pathStrs.length == 0) {
                        continue;
                    } else if (pathStrs.length == 1) {
                        path = path.replaceAll(pathStrs[0], "");
                    } else if (pathStrs.length == 2) {
                        path = path.replaceAll(pathStrs[0], pathStrs[1]);
                    }
                }
            }
        }
        return path;
    }

}
