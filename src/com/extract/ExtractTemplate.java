package com.extract;

import com.extract.exception.DirectoryNotFoundException;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;

public abstract class ExtractTemplate {

    public final ExtractResult extract(String rootPath, String targetPath, BufferedReader reader, JTextArea jta) {
        ExtractResult extractResult = new ExtractResult();
        try {

            extractResult = extractByFileList(reader, rootPath, targetPath, jta);

        } catch (IOException e) {
            e.printStackTrace();
            extractResult.setErrorMsg("파일을 읽을 수 없습니다. 파일목록을 확인해 주세요.");
        } catch (DirectoryNotFoundException e) {
            e.printStackTrace();
            extractResult.setErrorMsg(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            extractResult.setErrorMsg("Error : 알수 없는 오류가 발생하였습니다. [" + e.getMessage() + "]");
        } finally {
            if(reader != null) {
                try { reader.close(); } catch (IOException e) { e.printStackTrace(); }
            }
        }
        return extractResult;
    }

    public abstract ExtractResult extractByFileList(BufferedReader reader, String rootPath, String targetPath, JTextArea jta) throws Exception;
}
