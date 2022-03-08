package com.extract;

import com.extract.exception.DirectoryNotFoundException;
import com.extract.util.FileUtil;

import javax.swing.*;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ReaderExtract extends ExtractTemplate {

    @Override
    public ExtractResult extractByFileList(BufferedReader reader, String sourceRootPath, String targetPath, JTextArea jta) throws Exception {
        Map<String, Object> jsonConfigMap = FileUtil.getJsonObjByConfigJsonFile(); // 설정 파일 가져오기
        StringBuffer outputFileList = new StringBuffer();
        int count = 0;
        int successCount = 0;
        int failCount = 0;
        String rootName = "";
        String inSourcePath = "";
        String outSourcePath = "";

        sourceRootPath = FileUtil.replacePathSeparator(sourceRootPath);
        rootName = sourceRootPath.substring(sourceRootPath.lastIndexOf("/") + 1);
        rootName = FileUtil.getCheckDuplicateDirName(targetPath, rootName); //디렉토리 중복 제거

        String line;
        while((line = reader.readLine()) != null) {
            line = line.trim();
            if("".equals(line)) continue;

            try {
                line = FileUtil.replacePathSeparator(line);
                inSourcePath = replacePathByConfigFile(line, (List<String>) jsonConfigMap.get("inPathList"));
                outSourcePath = replacePathByConfigFile(line, (List<String>) jsonConfigMap.get("outPathList"));
                String destPath = outSourcePath.indexOf("/") == 0 ? rootName + outSourcePath : rootName + "/" + outSourcePath;
                //System.out.println("inSourcePath : " + inSourcePath + " ==> destPath : " + destPath);

                File oriFile = new File(sourceRootPath + "/" + inSourcePath);
                File destFile = new File(targetPath + "/" + destPath);

                if(!oriFile.isFile() || !oriFile.exists()) {
                    jta.append((++count) + ". [파일없음] " + FileUtil.replacePathSeparator(sourceRootPath) + "/" + inSourcePath + "\n");
                    failCount++;
                    continue;
                }
                FileUtil.makeDirs(destFile);
                FileUtil.copyFile(oriFile, destFile);

                jta.append((++count) + ". " + line + " ==> " + destPath + "\n");
                outputFileList.append(destPath + "\n");
                successCount++;

                // class파일인경우 inner class 추출
                String oriFileName = oriFile.getName();
                if("class".equals(FileUtil.getExt(oriFileName).toLowerCase())) {
                    final String classFileName = oriFileName.substring(0, oriFileName.lastIndexOf("."));
                    File[] innerClassFiles = oriFile.getParentFile().listFiles(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String name) {
                            return name.matches(classFileName + "\\$.+\\.class");
                        }
                    });
                    for (int i = 0; i < innerClassFiles.length; i++) {
                        String classDestPath = destPath.substring(0, destPath.lastIndexOf("/") + 1) + innerClassFiles[i].getName();
                        File classDestFile = new File(targetPath + "/" + classDestPath);
                        FileUtil.copyFile(innerClassFiles[i], classDestFile);

                        jta.append((++count) + ". " + line + " ==> " + classDestPath + "\n");
                        outputFileList.append(classDestPath + "\n");
                        successCount++;
                    }
                }
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
