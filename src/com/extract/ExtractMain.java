package com.extract;

import com.extract.util.FileUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class ExtractMain extends JFrame {

    private static final String VERSION = "1.0.0";

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
        super("파일 추출기");
        init();
        eventInit();

        // 최초 환경파일 생성
        FileUtil.makeConfigJsonFile();

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

        JPanel panel4 = new JPanel();
        panel4.setLayout(new BoxLayout(panel4, BoxLayout.X_AXIS));
        panel4.add(new JLabel("version " + VERSION));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
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
        this.setSize(600, 400);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void eventInit() {
        exitMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        inPathConfMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new InPathConfFrame();
            }
        });

        outPathConfMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new OutPathConfFrame();
            }
        });

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
                /*if(fileListCs.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    jta.append("파일목록 : " + fileListCs.getSelectedFile().getPath() + "\n");
                }*/
                fileListFrame = new RegisterFileListFrame(jta);
            }
        });

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

                BufferedReader reader = new BufferedReader(new StringReader(fileList.getText()));

                String rootPath = rootDirCs.getSelectedFile().getPath();
                String targetPath = targetDirCs.getSelectedFile().getPath();

                extractTemplate.extract(rootPath, targetPath, reader, jta);
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
