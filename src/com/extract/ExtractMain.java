package com.extract;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class ExtractMain extends JFrame {

    private static final String VERSION = "1.0.0";

    private JLabel versionLabel;
    private JFileChooser rootDirCs;
    private JFileChooser targetDirCs;
    private JFileChooser fileListCs;
    private JButton rootDirBtn;
    private JButton targetDirBtn;
    private JButton fileListBtn;
    private JButton extractBtn;
    private JTextArea jta;
    private JScrollPane jsp;

    private ExtractTemplate extractTemplate;

    
    public ExtractMain() {
        super("파일 추출기");
        init();
        eventInit();

        extractTemplate = new ReaderExtract();
    }

    private void init() {
        String defaultJfcPath = System.getProperty("user.home") + "\\Desktop";
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

        versionLabel = new JLabel("version " + VERSION);

        JPanel panel4 = new JPanel();
        panel4.setLayout(new BoxLayout(panel4, BoxLayout.X_AXIS));
        panel4.add(versionLabel);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(panel1);
        panel.add(panel2);
        panel.add(panel3);
        panel.add(panel4);

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
                extractTemplate.extract(rootDirCs, targetDirCs, fileListCs, jta);
            }
        });

    }


    public static void main(String[] args) {
        new ExtractMain();
    }

}