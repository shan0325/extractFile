package com.extract;

import com.extract.util.FileUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class ExtractMain extends JFrame {

    private static final String VERSION = "1.0.2";

    private JFileChooser rootDirCs;
    private JFileChooser targetDirCs;
    private JFileChooser fileListCs;
    private JButton rootDirBtn;
    private JButton targetDirBtn;
    private JButton fileListBtn;
    private JButton extractBtn;
    private JTextArea jta;
    private JScrollPane jsp;
    private RegisterFileListFrame fileListFrame;
    private JMenuBar mb;
    private JMenuItem inPathConfMenu;
    private JMenuItem outPathConfMenu;
    private JMenuItem exitMenu;

    private ExtractTemplate extractTemplate;

    
    public ExtractMain() {
        super("소스 추출기");
        init();
        eventInit();

        // 최초 환경파일 생성
        FileUtil.makeConfigJsonFile();

        extractTemplate = new ReaderExtract();
    }

    // 초기화
    private void init() {
        String defaultJfcPath = System.getProperty("user.home") + "\\Desktop";
        String rootJfcPath = "D:\\";

        rootDirBtn = new JButton("소스루트");
        targetDirBtn = new JButton("추출경로");
        fileListBtn = new JButton("파일목록");

        rootDirCs = new JFileChooser();
        rootDirCs.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        try {
            // 기본경로지정 java_1.8 버그로 인한 Exception 처리
            rootDirCs.setCurrentDirectory(new File(rootJfcPath));
        } catch (Exception e) {
            rootDirCs.setCurrentDirectory(new File(defaultJfcPath));
        }

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

        JPanel panel4 = new JPanel();
        panel4.setLayout(new BoxLayout(panel4, BoxLayout.X_AXIS));
        panel4.add(new JLabel("version " + VERSION));

        JLabel info = new JLabel("# 설명");
        info.setForeground(Color.RED);

        JLabel warning = new JLabel("주의!! : 자바 inner class파일은 추출 되지 않을 수 있음");
        warning.setForeground(Color.RED);

        JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayout(4, 1));
        panel5.setBorder(BorderFactory.createEtchedBorder());
        panel5.add(info);
        panel5.add(new JLabel("소스루트 : 추출대상 소스 루트 폴더 선택(ex D:\\Dev\\Workspace\\IB_WEB)"));
        panel5.add(new JLabel("추출경로 : 자신이 원하는 추출경로 폴더 선택"));
        panel5.add(new JLabel("파일목록 : Git에 커밋된 파일목록 복사 붙여넣기(ex src/main/java/.../DateUtil.java)"));
        //panel5.add(warning);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(panel5);
        panel.add(panel1);
        panel.add(panel2);
        panel.add(panel3);
        panel.add(panel4);

        inPathConfMenu = new JMenuItem("가져오기");
        outPathConfMenu = new JMenuItem("내보내기");
        exitMenu = new JMenuItem("EXIT");
        JMenu confMenu = new JMenu("설정");
        confMenu.add(inPathConfMenu);
        confMenu.add(outPathConfMenu);
        confMenu.addSeparator();
        confMenu.add(exitMenu);
        mb = new JMenuBar();
        mb.add(confMenu);

        this.setJMenuBar(mb);
        this.add(panel);
        this.setSize(600, 500);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void eventInit() {
        // 설정 EXIT 클릭 시
        exitMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // 설정 가져오기 클릭 시
        inPathConfMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new InPathConfFrame();
            }
        });

        // 설정 내보내기 클릭 시
        outPathConfMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new OutPathConfFrame();
            }
        });

        // 소스루트 버튼 클릭 시
        rootDirBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = rootDirCs.showOpenDialog(null);
                if(result == JFileChooser.APPROVE_OPTION) {
                    jta.append("소스루트경로 : " + rootDirCs.getSelectedFile().getPath() + "\n");
                } else {
                    rootDirCs.setSelectedFile(null);
                    jta.append("소스루트경로 :\n");
                }
            }
        });

        // 추출경로 버튼 클릭 시
        targetDirBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = targetDirCs.showOpenDialog(null);
                if(result == JFileChooser.APPROVE_OPTION) {
                    jta.append("추출경로 : " + targetDirCs.getSelectedFile().getPath() + "\n");
                } else {
                    targetDirCs.setSelectedFile(null);
                    jta.append("추출경로 :\n");
                }
            }
        });

        // 파일목록 버튼 클릭 시
        fileListBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /*if(fileListCs.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    jta.append("파일목록 : " + fileListCs.getSelectedFile().getPath() + "\n");
                }*/
                fileListFrame = new RegisterFileListFrame(jta);
            }
        });

        // 추출하기 버튼 클릭 시
        extractBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(rootDirCs.getSelectedFile() == null) {
                    JOptionPane.showMessageDialog(null, "소스루트경로를 선택해주세요.");
                    return;
                }
                else if(targetDirCs.getSelectedFile() == null) {
                    JOptionPane.showMessageDialog(null, "추출경로를 선택해주세요.");
                    return;
                }
                else if(fileListFrame == null) {
                    JOptionPane.showMessageDialog(null, "파일목록을 등록해주세요.");
                    return;
                }

                JTextArea fileList = fileListFrame.getJta();
                if("".equals(fileList.getText().trim())) {
                    JOptionPane.showMessageDialog(null, "파일목록을 등록해주세요.");
                    return;
                }

                final BufferedReader reader = new BufferedReader(new StringReader(fileList.getText()));
                final String rootPath = rootDirCs.getSelectedFile().getPath();
                final String targetPath = targetDirCs.getSelectedFile().getPath();

                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        ExtractResult extract = extractTemplate.extract(rootPath, targetPath, reader, jta);
                        if(extract.getErrorMsg() != null) {
                            JOptionPane.showMessageDialog(null, extract.getErrorMsg());
                        } else {
                            String result = "추출을 완료하였습니다. [총 " + extract.getCount() + ", 성공 " + extract.getSuccessCount() + ", 실패 " + extract.getFailCount() + "]";
                            jta.append(result + "\n");
                            JOptionPane.showMessageDialog(null, result);
                        }
                    }
                };
                thread.start();
            }
        });

    }

    private BufferedReader getBufferedReaderByFileList(JFileChooser fileListCs) throws FileNotFoundException {
        return new BufferedReader(new FileReader(fileListCs.getSelectedFile()));
    }

    public static void main(String[] args) {
        new ExtractMain();
        /*java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ExtractMain();
            }
        });*/
    }

}
