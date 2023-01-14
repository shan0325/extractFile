package com.extract.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProcessBulderUtil {

    public String exec(String[] cmd) {
        String result = "";
        Process process = null;
        BufferedReader reader = null;
        try {
            process = new ProcessBuilder(cmd).start();
            reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "MS949"));
            String line = null;
            StringBuffer sb = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            result = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    /*public static void main(String[] args) throws IOException {
        String[] cmd = new String[] {"cmd", "/c", "dir"};
        ProcessBulderUtil processBulderUtil = new ProcessBulderUtil();
        String exec = processBulderUtil.exec(cmd);
        System.out.println("exec = " + exec);
    }*/
}
