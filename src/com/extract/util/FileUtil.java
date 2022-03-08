package com.extract.util;

import com.extract.exception.DirectoryNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.io.*;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class FileUtil {

    public static final String CONFIG_PATH = System.getProperty("user.home") + "/AppData/Local/ExtractFile";
    public static final String CONFIG_FILE_NAME = "config.json";

    public static String replacePathSeparator(String path) {
        return path.replaceAll("\\\\", "/");
    }

    public static String getExt(String path) {
        return path.substring(path.lastIndexOf(".") + 1);
    }

    public static void makeDirs(File file) {
        String dirPath = file.getPath();
        dirPath = dirPath.substring(0, dirPath.lastIndexOf(File.separator));

        File dirFile = new File(dirPath);
        if(!dirFile.exists()) {
            dirFile.mkdirs();
        }
    }

    /**
     * 중복된 디렉토리명이 있으면 새로운 디렉토리명 만들기
     * @param dirPath
     * @param makeDirName
     * @return
     */
    public static String getCheckDuplicateDirName(String dirPath, String makeDirName) {

        File dirPathFile = new File(dirPath);
        if(!dirPathFile.exists()) {
            throw new DirectoryNotFoundException("Error : 디렉토리가 존재하지 않습니다. [" + dirPath + "]");
        }

        final String pattern = makeDirName + "(_[0-9]*)?";
        File[] dirs = dirPathFile.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory() && Pattern.matches(pattern, pathname.getName());
            }
        });

        if(dirs.length == 0) {
            return makeDirName;
        }

        String newMakeDirName = "";
        try {
            int maxNum = 0;
            for (File dir : dirs) {
                String dirName = dir.getName();
                if(dirName.equals(makeDirName)) continue;

                int num = Integer.parseInt(dirName.substring(dirName.lastIndexOf("_") + 1));
                if(maxNum < num) maxNum = num;
            }
            newMakeDirName = makeDirName + "_" + (maxNum + 1);
        } catch (Exception e) {
            newMakeDirName = makeDirName;
        }

        return newMakeDirName;
    }

    public static void copyFile(File oriFile, File destFile) throws IOException {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(oriFile));
            bos = new BufferedOutputStream(new FileOutputStream(destFile));

            int fileByte = 0;
            while((fileByte = bis.read()) != -1) {
                bos.write(fileByte);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if(bis != null) {
                bis.close();
            }
            if(bos != null) {
                bos.close();
            }
        }
    }

    public static void outputFile(String targetPath, String rootName, StringBuffer outputFileList) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String curDate = sdf.format(new Date());

        String outputFileName = rootName + "_" + curDate + ".txt";
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

    public static Map<String, Object> getJsonObjByConfigJsonFile() {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonObj = new HashMap<String, Object>();

        File jsonFile = new File(CONFIG_PATH + "/" + CONFIG_FILE_NAME);

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(jsonFile));
            StringBuffer jsonStr = new StringBuffer();
            String temp = "";
            while ((temp = br.readLine()) != null) {
                jsonStr.append(temp);
            }
            //System.out.println("jsonStr : " + jsonStr.toString());

            if(!"".equals(jsonStr.toString())) {
                jsonObj = objectMapper.readValue(jsonStr.toString(), Map.class);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "설정파일을 찾을 수 없습니다.\n" + CONFIG_PATH + "/" + CONFIG_FILE_NAME + " 파일을 확인해 주세요.");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "설정파일을 읽을 수 없습니다.\n" + CONFIG_PATH + "/" + CONFIG_FILE_NAME + " 파일을 확인해 주세요.");
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "설정파일 오류 발생!!\n" + CONFIG_PATH + "/" + CONFIG_FILE_NAME + " 파일을 확인해 주세요.");
            System.exit(0);
        } finally {
            if(br != null) {
                try { br.close(); } catch (IOException e) { e.printStackTrace(); }
            }
        }
        return jsonObj;
    }

    // 환경파일 생성
    public static void makeConfigJsonFile() {
        File jsonFile = new File(CONFIG_PATH + "/" + CONFIG_FILE_NAME);
        if(!jsonFile.exists()) {
            makeDirs(jsonFile);

            Map<String, Object> jsonObj = new HashMap<>();

            List<String> inPathList = new ArrayList<>();
            inPathList.add("src/main/java>target/classes");
            inPathList.add(".java>.class");

            List<String> outPathList = new ArrayList<>();
            outPathList.add("src/main/webapp/>ROOT/");
            outPathList.add("src/main/java>ROOT/WEB-INF/classes");
            outPathList.add(".java>.class");
            outPathList.add("src/main/resources>ROOT/WEB-INF/classes");
            outPathList.add("WebContent/>/");

            jsonObj.put("inPathList", inPathList);
            jsonObj.put("outPathList", outPathList);
            makeConfigJsonFile(jsonObj);
        }
    }

    public static void makeConfigJsonFile(Map<String, Object> jsonObj) {
        File jsonFile = new File(CONFIG_PATH + "/" + CONFIG_FILE_NAME);
        BufferedWriter bw = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonStr = objectMapper.writeValueAsString(jsonObj);

            bw = new BufferedWriter(new FileWriter(jsonFile));
            bw.write(jsonStr);
            bw.flush();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "설정파일을 생성하지못했습니다.");
            System.exit(0);
        } finally {
            if(bw != null) {
                try { bw.close(); } catch (IOException e) { e.printStackTrace(); }
            }
        }
    }

}
