package com.extract.frame;

import com.extract.ExtractResult;
import com.extract.ExtractTemplate;
import com.extract.ReaderExtract;
import com.extract.util.DateUtil;
import com.extract.util.FileUtil;
import com.extract.util.StringUtils;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;
import java.util.*;

public class ExtractMainFrame extends JFrame {

    private static final String VERSION = "1.1.1";

    private JComboBox<String> rootDirCombo;
    private JFileChooser rootDirCs;
    private JFileChooser targetDirCs;
    private JButton targetDirBtn;
    private JButton fileListBtn;
    private JButton extractBtn;
    private JTextArea jta;
    private JScrollPane jsp;
    private RegisterFileList fileListFrame;
    private JMenuBar mb;
    private JMenuItem sourcePathConf;
    private JMenuItem extractPathConf;
    private JMenuItem inPathConfMenu;
    private JMenuItem outPathConfMenu;
    private JMenuItem readme;
    private JMenuItem exitMenu;

    private Map<String, String> sourcePathConfMap;

    private ExtractTemplate extractTemplate;

    public ExtractMainFrame() {
        super("모아모아");

        init();
        eventInit();

        extractTemplate = new ReaderExtract();
    }

    // 초기화
    private void init() {
        String defaultJfcPath = System.getProperty("user.home") + "\\Desktop";
        String rootJfcPath = "D:\\";

        targetDirBtn = new JButton("추출경로");
        targetDirBtn.setBackground(Color.WHITE);
        fileListBtn = new JButton("파일목록");
        fileListBtn.setBackground(Color.WHITE);

        rootDirCs = new JFileChooser();
        rootDirCs.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        try {
            // 기본경로지정 java_1.8 버그로 인한 Exception 처리
            rootDirCs.setCurrentDirectory(new File(rootJfcPath));
        } catch (Exception e) {
            rootDirCs.setCurrentDirectory(new File(defaultJfcPath));
        }

        rootDirCombo = new JComboBox<>(new String[0]);
        rootDirCombo.setBackground(Color.WHITE);
        // 소스 경로 콤보박스 셋팅
        setSourceRootDirCombo();

        targetDirCs = new JFileChooser();
        targetDirCs.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        targetDirCs.setCurrentDirectory(new File(defaultJfcPath)); // 기본경로지정
        // 추출대상경로 설정 셋팅
        setTargetDirCsCurrentDirectory();

        JPanel settingPanel = new JPanel();
        settingPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        settingPanel.setBorder(new CompoundBorder(new TitledBorder(""), BorderFactory.createEmptyBorder(10, 0, 10, 0)));
        settingPanel.add(rootDirCombo);
        settingPanel.add(targetDirBtn);
        settingPanel.add(fileListBtn);

        extractBtn = new JButton("추 출");
        extractBtn.setPreferredSize(new Dimension(120, 35));
        extractBtn.setBackground(Color.WHITE);

        JPanel extractBtnPanel = new JPanel();
        extractBtnPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        extractBtnPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        extractBtnPanel.add(extractBtn);

        jta = new JTextArea();
        jsp = new JScrollPane(jta);

        JPanel textAreaPanel = new JPanel();
        textAreaPanel.setLayout(new BorderLayout());
        textAreaPanel.add(jsp, BorderLayout.CENTER);

        JPanel versionPanel = new JPanel();
        versionPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        versionPanel.add(new JLabel("version " + VERSION));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(settingPanel);
        panel.add(extractBtnPanel);
        panel.add(textAreaPanel);
        panel.add(versionPanel);

        sourcePathConf = new JMenuItem("프로젝트경로");
        extractPathConf = new JMenuItem("추출기본경로");
        inPathConfMenu = new JMenuItem("가져오기");
        outPathConfMenu = new JMenuItem("내보내기");
        readme = new JMenuItem("README");
        exitMenu = new JMenuItem("EXIT");
        JMenu confMenu = new JMenu("설정");
        confMenu.add(sourcePathConf);
        confMenu.add(extractPathConf);
        confMenu.addSeparator();
        confMenu.add(inPathConfMenu);
        confMenu.add(outPathConfMenu);
        confMenu.addSeparator();
        confMenu.add(readme);
        confMenu.addSeparator();
        confMenu.add(exitMenu);
        mb = new JMenuBar();
        mb.add(confMenu);

        this.setJMenuBar(mb);
        this.add(panel);
        this.setSize(600, 500);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        //this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setIconImage(new ImageIcon(getClass().getResource("/icon/crocodile.png")).getImage()); // 아이콘
    }

    private void eventInit() {
        // 설정 EXIT 클릭 시
        exitMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // 설정 README 클릭 시
        readme.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ReadmeFrame();
            }
        });

        // 설정 프로젝트경로 클릭 시
        sourcePathConf.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SourcePathConfFrame(ExtractMainFrame.this);
            }
        });
        
        // 설정 추출기본경로 클릭 시
        extractPathConf.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ExtractPathConfFrame(ExtractMainFrame.this);
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

        // 프로젝트 선택 콤보박스 선택 시
        rootDirCombo.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    Object item = e.getItem(); // 선택한 아이템
                    fileListFrame = null;
                }
            }
        });

        // 추출경로 버튼 클릭 시
        targetDirBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = targetDirCs.showOpenDialog(null); // 열기용 창 오픈
                if(result == JFileChooser.APPROVE_OPTION) { // 열기를 클릭
                    targetDirCs.setCurrentDirectory(targetDirCs.getSelectedFile());
                }
            }
        });

        // 파일목록 버튼 클릭 시
        fileListBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(fileListFrame == null) {
                    //fileListFrame = new RegisterFileListFrame();
                    fileListFrame = new RegisterGitFileListFrame(ExtractMainFrame.this);
                } else {
                    fileListFrame.showFrameInit();
                }
            }
        });

        // 추출하기 버튼 클릭 시
        extractBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(rootDirCombo.getItemCount() == 1) {
                    JOptionPane.showMessageDialog(null, "프로젝트경로를 설정해주세요.");
                    return;
                } else if(rootDirCombo.getSelectedIndex() < 1) {
                    JOptionPane.showMessageDialog(null, "프로젝트를 선택해주세요.");
                    return;
                } else if(targetDirCs.getSelectedFile() == null) {
                    JOptionPane.showMessageDialog(null, "추출경로를 선택해주세요.");
                    return;
                } else if(fileListFrame == null || !fileListFrame.isExistFileList()) {
                    JOptionPane.showMessageDialog(null, "파일목록을 등록해주세요.");
                    return;
                }

                final BufferedReader reader = new BufferedReader(new StringReader(fileListFrame.getFileList()));
                final String projectPath = getProjectPath();
                final String targetPath = targetDirCs.getSelectedFile().getPath();

                jta.setText("================== EXTRACT INFORMATION ==================\n");
                jta.append("프로젝트경로 : " + projectPath + "\n");
                jta.append("추출경로 : " + targetPath + "\n");
                jta.append("파일목록 :\n");
                jta.append(fileListFrame.getFileList() + "\n");
                jta.append("==========================================================\n\n");
                jta.append(DateUtil.getCurrentDateTime("yyyy-MM-dd HH:mm:ss") + " : START EXTRACTING FILES\n");

                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        ExtractResult extract = extractTemplate.extract(projectPath, targetPath, reader, jta);
                        if(extract.getErrorMsg() != null) {
                            JOptionPane.showMessageDialog(null, extract.getErrorMsg());
                        } else {
                            String result = "추출을 완료하였습니다. [총 " + extract.getCount() + ", 성공 " + extract.getSuccessCount() + ", 실패 " + extract.getFailCount() + "]";
                            jta.append(result + "\n");
                            jta.append(DateUtil.getCurrentDateTime("yyyy-MM-dd HH:mm:ss") + " : END EXTRACTION FILE");
                            JOptionPane.showMessageDialog(null, result);
                        }
                    }
                };
                thread.start();
            }
        });
    }

    public String getProjectPath() {
        if (sourcePathConfMap == null ||  rootDirCombo == null) {
            return null;
        }
        return sourcePathConfMap.get((String) rootDirCombo.getSelectedItem());
    }

    public void setSourceRootDirCombo() {
        if(rootDirCombo == null) return;

        // 초기화
        rootDirCombo.removeAllItems();
        rootDirCombo.addItem("프로젝트 선택");

        sourcePathConfMap = FileUtil.getSourcePathConfMap();
        //rootDirCombo = new JComboBox<>(sourcePathConfMap.keySet().toArray(new String[0]));

        Set<String> sourcePathKeySet = sourcePathConfMap.keySet();
        Iterator<String> sourcePathIter = sourcePathKeySet.iterator();
        while (sourcePathIter.hasNext()) {
            rootDirCombo.addItem(sourcePathIter.next());
        }
    }

    public void setTargetDirCsCurrentDirectory() {
        Map<String, Object> jsonObj = FileUtil.getJsonObjByConfigJsonFile();
        String extractPath = (String) jsonObj.get("extractPath");
        if(StringUtils.isEmpty(extractPath)) return;

        File extractDir = new File(extractPath);
        if(!extractDir.exists() || !extractDir.isDirectory()) {
            return;
        }
        targetDirCs.setSelectedFile(extractDir);
        targetDirCs.setCurrentDirectory(extractDir);
    }
}
