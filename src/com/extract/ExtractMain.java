package com.extract;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class ExtractMain extends JFrame {

    private JFileChooser rootDirCs;
    private JFileChooser targetDirCs;
    private JFileChooser fileListCs;
    private JButton rootDirBtn;
    private JButton targetDirBtn;
    private JButton fileListBtn;
    private JButton extractBtn;
    private JTextArea jta;
    private JScrollPane jsp;

    
    public ExtractMain() {
        super("파일 추출기");
        init();
        eventInit();
    }

    private void init() {
        String defaultJfcPath = "C:\\Users\\" + System.getenv("USERNAME") + "\\Desktop";
        String rootJfcPath = "D:\\";

        rootDirBtn = new JButton("소스루트");
        targetDirBtn = new JButton("추출경로");
        fileListBtn = new JButton("파일목록");

        rootDirCs = new JFileChooser();
        rootDirCs.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        rootDirCs.setCurrentDirectory(new File(rootJfcPath)); // 기본경로지정

        targetDirCs = new JFileChooser();
        targetDirCs.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        targetDirCs.setCurrentDirectory(new File(defaultJfcPath)); // 기본경로지정

        fileListCs = new JFileChooser();
        fileListCs.setMultiSelectionEnabled(false); // 다중 선택 불가
        fileListCs.setCurrentDirectory(new File(defaultJfcPath)); // 기본경로지정

        JPanel panel1 = new JPanel();
        panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));
        panel1.setBorder(new EmptyBorder(10, 5, 5, 10));
        panel1.add(rootDirBtn);
        panel1.add(targetDirBtn);
        panel1.add(fileListBtn);

        extractBtn = new JButton("추출하기");

        JPanel panel2 = new JPanel();
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.X_AXIS));
        panel2.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        panel2.add(extractBtn);

        jta = new JTextArea();
        jsp = new JScrollPane(jta);
        jsp.setPreferredSize(new Dimension(0, 200));

        JPanel panel3 = new JPanel();
        panel3.setLayout(new BoxLayout(panel3, BoxLayout.X_AXIS));
        panel3.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        panel3.add(jsp);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(panel1);
        panel.add(panel2);
        panel.add(panel3);

        this.add(panel);
        this.setSize(600, 400);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void eventInit() {
        rootDirBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(rootDirCs.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    jta.append("루트경로 : " + rootDirCs.getSelectedFile().getPath() + "\n");
                }
            }
        });

        targetDirBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(targetDirCs.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    jta.append("추출경로 : " + targetDirCs.getSelectedFile().getPath() + "\n");
                }
            }
        });

        fileListBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(fileListCs.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    jta.append("파일목록 : " + fileListCs.getSelectedFile().getPath() + "\n");
                }
            }
        });

        extractBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(rootDirCs.getSelectedFile() == null) {
                    JOptionPane.showMessageDialog(null, "루트경로를 선택해주세요.");
                    return;
                }
                if(targetDirCs.getSelectedFile() == null) {
                    JOptionPane.showMessageDialog(null, "추출경로를 선택해주세요.");
                    return;
                }
                if(fileListCs.getSelectedFile() == null) {
                    JOptionPane.showMessageDialog(null, "파일목록을 선택해주세요.");
                    return;
                }

                BufferedReader reader = null;
                FileInputStream fis = null;
                FileOutputStream fos = null;
                try {
                    reader = new BufferedReader(new FileReader(fileListCs.getSelectedFile()));

                    int count = 1;
                    String line;
                    while((line = reader.readLine()) != null) {
                        String rootPath = rootDirCs.getSelectedFile().getPath();
                        String targetPath = targetDirCs.getSelectedFile().getPath();

                        try {
                            File oriFile = new File(rootPath + File.separator + line);
                            File copyFile = new File(targetPath + File.separator + line);
                            File copyFileDir = new File(targetPath + File.separator + line.substring(0, line.lastIndexOf("\\")));

                            if(!copyFileDir.exists()) {
                                copyFileDir.mkdirs();
                            }

                            fis = new FileInputStream(oriFile);
                            fos = new FileOutputStream(copyFile);

                            int fileByte = 0;
                            while((fileByte = fis.read()) != -1) {
                                fos.write(fileByte);
                            }

                            jta.append((count++) + ". " + line + "\n");
                        } catch(Exception exception) {
                            jta.append((count++) + ". " + line + " 오류 발생!!\n");
                        } finally {
                            if(fis != null) {
                                fis.close();
                            }
                            if(fos != null) {
                                fos.close();
                            }
                        }
                    }
                    reader.close();

                    JOptionPane.showMessageDialog(null, "파일추출을 완료하였습니다.");
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                    JOptionPane.showMessageDialog(null, "오류가 발생하였습니다.");
                } finally {
                    if(reader != null) {
                        try { reader.close(); } catch (IOException ioException) { ioException.printStackTrace(); }
                    }
                    if(fis != null) {
                        try { fis.close(); } catch (IOException ioException) { ioException.printStackTrace(); }
                    }
                    if(fos != null) {
                        try { fos.close(); } catch (IOException ioException) { ioException.printStackTrace(); }
                    }
                }
            }
        });

    }


    public static void main(String[] args) {
        new ExtractMain();
    }

}
