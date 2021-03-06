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
        Map<String, Object> jsonConfigMap = FileUtil.getJsonObjByConfigJsonFile(); // 설정 파일 가져오기
        StringBuffer outputFileList = new StringBuffer();
        int count = 0;
        int successCount = 0;
        int failCount = 0;
        String rootName = "";
        String inSourcePath = "";
        String outSourcePath = "";

        String line;
        while((line = reader.readLine()) != null) {
            line = line.trim();
            if("".equals(line)) continue;

            try {
                sourceRootPath = FileUtil.replacePathSeparator(sourceRootPath);
                rootName = sourceRootPath.substring(sourceRootPath.lastIndexOf("/") + 1);

                line = FileUtil.replacePathSeparator(line);
                inSourcePath = replacePathByConfigFile(line, (List<String>) jsonConfigMap.get("inPathList"));
                outSourcePath = replacePathByConfigFile(line, (List<String>) jsonConfigMap.get("outPathList"));
                String destPath = outSourcePath.indexOf("/") == 0 ? rootName + outSourcePath : rootName + "/" + outSourcePath;
                //System.out.println("inSourcePath : " + inSourcePath + " ==> destPath : " + destPath);

                File oriFile = new File(sourceRootPath + "/" + inSourcePath);
                File destFile = new File(targetPath + "/" + destPath);

                if(!oriFile.exists()) {
                    jta.append((++count) + ". [파일없음] " + FileUtil.replacePathSeparator(sourceRootPath) + "/" + inSourcePath + "\n");
                    failCount++;
                    continue;
                }

                FileUtil.makeDirs(destFile);
                FileUtil.copyFile(oriFile, destFile);

                jta.append((++count) + ". " + line + " ==> " + destPath + "\n");
                outputFileList.append(destPath + "\n");
                successCount++;
            } catch(Exception e) {
                jta.append((++count) + ". [오류발생] " + FileUtil.replacePathSeparator(sourceRootPath) + "/" + inSourcePath + "\n");
                failCount++;
            }
            jta.setCaretPosition(jta.getDocument().getLength()); // 스크롤 맨 아래로
        }
        FileUtil.outputFile(targetPath, rootName, outputFileList); // 성공한 파일 목록 txt파일로 추출

        ExtractResult extractResult = new ExtractResult();
        extractResult.setCount(count);
        extractResult.setSuccessCount(successCount);
        extractResult.setFailCount(failCount);

        return extractResult;
    }

    // 경로 치환 처리
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
