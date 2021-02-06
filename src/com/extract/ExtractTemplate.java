package com.extract;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public abstract class ExtractTemplate {

    public final ExtractResult extract(String rootPath, String targetPath, BufferedReader reader, JTextArea jta) {
        ExtractResult extractResult = new ExtractResult();
        try {
            extractResult = extractByFileList(reader, rootPath, targetPath, jta);
        } catch (IOException e) {
            e.printStackTrace();
            extractResult.setErrorMsg("파일을 읽을 수 없습니다. 파일을 확인해 주세요.");
        } finally {
            if(reader != null) {
                try { reader.close(); } catch (IOException e) { e.printStackTrace(); }
            }
        }
        return extractResult;
    }

    public abstract ExtractResult extractByFileList(BufferedReader reader, String rootPath, String targetPath, JTextArea jta) throws IOException;
}
