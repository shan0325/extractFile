package com.extract.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class CmdUtil {

    public String exec(String cmd) {
        try {
            Process process = Runtime.getRuntime().exec("cmd /c " + cmd);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "MS949"));
            String line = null;
            StringBuffer sb = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*public static void main(String[] args) {
        CmdUtil cmdUtil = new CmdUtil();
        String result = cmdUtil.exec("git status");
        System.out.println(result);
    }*/
}
