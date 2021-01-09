package com.extract;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public abstract class ExtractTemplate {

    public final void extract(JFileChooser rootDirCs, JFileChooser targetDirCs, JFileChooser fileListCs, JTextArea jta) {

        if(hasError(rootDirCs, targetDirCs, fileListCs)) {
            return;
        }

        BufferedReader reader = null;
        try {
            reader = getBufferedReaderByFileList(fileListCs);

            String rootPath = rootDirCs.getSelectedFile().getPath();
            String targetPath = targetDirCs.getSelectedFile().getPath();

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

    private boolean hasError(JFileChooser rootDirCs, JFileChooser targetDirCs, JFileChooser fileListCs) {
        boolean hasError = false;

        if(rootDirCs.getSelectedFile() == null) {
            JOptionPane.showMessageDialog(null, "루트경로를 선택해주세요.");
            hasError = true;
        }
        else if(targetDirCs.getSelectedFile() == null) {
            JOptionPane.showMessageDialog(null, "추출경로를 선택해주세요.");
            hasError = true;
        }
        else if(fileListCs.getSelectedFile() == null) {
            JOptionPane.showMessageDialog(null, "파일목록을 선택해주세요.");
            hasError = true;
        }
        return hasError;
    }

    private BufferedReader getBufferedReaderByFileList(JFileChooser fileListCs) throws FileNotFoundException {
        return new BufferedReader(new FileReader(fileListCs.getSelectedFile()));
    }

    public abstract void extractByFileList(BufferedReader reader, String rootPath, String targetPath, JTextArea jta) throws IOException;
}
