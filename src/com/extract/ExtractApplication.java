package com.extract;

import com.extract.util.FileUtil;

import javax.swing.*;

public class ExtractApplication {
    // 폰트 전체 변경
    public static void setUIFont(javax.swing.plaf.FontUIResource f) {
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, f);
            }
        }
    }

    public static void main(String[] args) {
        // 폰트 설정
        setUIFont(new javax.swing.plaf.FontUIResource("NanumGothic", java.awt.Font.PLAIN, 12));

        // 최초 환경파일 생성
        FileUtil.makeConfigJsonFile();

        new ExtractMainFrame();
        /*java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ExtractMain();
            }
        });*/
    }
}
