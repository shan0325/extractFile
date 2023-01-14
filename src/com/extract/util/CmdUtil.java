package com.extract.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class CmdUtil {

    public String exec(String cmd) {
        String result = "";
        Process process = null;
        BufferedReader reader = null;
        try {
            process = Runtime.getRuntime().exec("cmd /c " + cmd);
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
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

    /*public static void main(String[] args) {
        CmdUtil cmdUtil = new CmdUtil();
        String result = cmdUtil.exec("git status");
        System.out.println(result);
    }*/
}
