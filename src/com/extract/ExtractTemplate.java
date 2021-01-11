package com.extract;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public abstract class ExtractTemplate {

    public final void extract(String rootPath, String targetPath, BufferedReader reader, JTextArea jta) {
        try {
            extractByFileList(reader, rootPath, targetPath, jta);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "파일을 찾을 수 없습니다.");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "파일을 읽을 수 없습니다. 파일을 확인해 주세요.");
        } finally {
            if(reader != null) {
                try { reader.close(); } catch (IOException e) { e.printStackTrace(); }
            }
        }
    }

    public abstract void extractByFileList(BufferedReader reader, String rootPath, String targetPath, JTextArea jta) throws IOException;
}
