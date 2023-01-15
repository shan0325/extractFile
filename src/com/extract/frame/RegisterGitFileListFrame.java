package com.extract.frame;

import com.extract.components.RoundedButton;
import com.extract.util.CmdUtil;
import com.extract.util.GitUtil;
import com.extract.util.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class RegisterGitFileListFrame extends JFrame implements RegisterFileList {

    private JButton registerBtn;
    private JTextArea jta;
    private JScrollPane jsp;
    private String fileList;
    private JButton gitDiffBtn;
    private JButton resetFileListBtn;
    private ExtractMainFrame extractMainFrame;
    private GitUtil gitUtil;
    private String currentGitBranch;
    private int gitFileListCount;

    public RegisterGitFileListFrame(ExtractMainFrame extractMainFrame) {
        super("파일목록 등록");

        this.gitUtil = new GitUtil(new CmdUtil());
        this.extractMainFrame = extractMainFrame;

        gitInit();
        layoutInit();
        eventInit();
    }

    private void gitInit() {
        // 깃 설치 여부 확인
        if (!this.gitUtil.isInstalledGit()) return;

        String projectPath = this.extractMainFrame.getProjectPath();
        if (StringUtils.isEmpty(projectPath)) return;

        // 현재 브랜치 확인
        String currentBranch = this.gitUtil.getCurrentBranch(projectPath);
        if (StringUtils.isEmpty(currentBranch)) return;
        this.currentGitBranch = currentBranch;

        // 원격 master 존재 여부 확인
        if (!this.gitUtil.isExistOriginMaster(projectPath)) return;

        String fileList = this.gitUtil.getFileListByDiffOriginMaster(projectPath);
        if (StringUtils.isEmpty(fileList)) return;

        String[] split = fileList.split("\n");
        this.gitFileListCount = split.length;
    }

    public void layoutInit() {
        String projectName = this.extractMainFrame.getProjectName();

        JPanel projectInfoPanel = new JPanel();
        projectInfoPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        projectInfoPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        projectInfoPanel.add(new JLabel("<html><b>프로젝트 : " + projectName + "<b>"));

        String currentBranch = StringUtils.isEmpty(this.currentGitBranch) ? "찾을 수 없습니다." : this.currentGitBranch;
        JPanel gitInfoPanel = new JPanel();
        gitInfoPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        gitInfoPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        gitInfoPanel.add(new JLabel("<html><b>현재 브랜치 : " + currentBranch + "<b>"));

        String gitDiffBtnTitle = "변경파일 가져오기" + "(" + this.gitFileListCount + ")";
        gitDiffBtn = new RoundedButton(gitDiffBtnTitle);
        gitDiffBtn.setBackground(new Color(255, 152, 0));
        gitDiffBtn.setForeground(Color.WHITE);
        gitDiffBtn.setPreferredSize(new Dimension(150, 21));

        if (!StringUtils.isEmpty(this.currentGitBranch)) {
            gitInfoPanel.add(gitDiffBtn);
        }

        jta = new JTextArea();
        jsp = new JScrollPane(jta);

        JPanel jtaPanel = new JPanel();
        jtaPanel.setLayout(new BorderLayout());
        jtaPanel.add(jsp, BorderLayout.CENTER);

        resetFileListBtn = new RoundedButton("삭제");
        resetFileListBtn.setBackground(Color.darkGray);
        resetFileListBtn.setForeground(Color.WHITE);

        registerBtn = new RoundedButton("등록");
        registerBtn.setBackground(new Color(52, 73, 94));
        registerBtn.setForeground(Color.WHITE);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        bottomPanel.add(resetFileListBtn);
        bottomPanel.add(registerBtn);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(projectInfoPanel);
        panel.add(gitInfoPanel);
        panel.add(jtaPanel);
        panel.add(bottomPanel);

        this.add(panel);
        this.setSize(600, 450);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    private void eventInit() {
        // 닫기(X) 버튼 클릭시
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                extractMainFrame.setEnabled(true);
                setColorExtractMainFrameFileListBtn();
                super.windowClosing(e);
            }
        });

        // 등록 버튼 클릭 시
        this.registerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                extractMainFrame.setEnabled(true);
                fileList = jta.getText();
                setColorExtractMainFrameFileListBtn();
                dispose();
            }
        });

        // RESET 버튼 클릭시
        this.resetFileListBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeFileList();
                jta.requestFocus();
                setColorExtractMainFrameFileListBtn();
            }
        });

        // GIT DIFF MASTER 버튼 클릭 시
        this.gitDiffBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String jtaText = jta.getText();
                if (!StringUtils.isEmpty(jtaText)) {
                    int result = JOptionPane.showConfirmDialog(null, "파일 목록이 삭제됩니다. 계속 하시겠습니까?", "경고", JOptionPane.YES_NO_OPTION);
                    if (result != JOptionPane.YES_OPTION) return;
                }
                setGitFileList();
            }
        });
    }

    private void setGitFileList() {
        if (!this.gitUtil.isInstalledGit()) {
            JOptionPane.showMessageDialog(null, "GIT을 설치해주세요");
            return;
        }

        String projectPath = this.extractMainFrame.getProjectPath();
        if (StringUtils.isEmpty(projectPath)) {
            JOptionPane.showMessageDialog(null, "프로젝트를 선택해주세요.");
            return;
        }

        if (StringUtils.isEmpty(this.gitUtil.getCurrentBranch(projectPath))) return;

        if (!this.gitUtil.isExistOriginMaster(projectPath)) {
            JOptionPane.showMessageDialog(null, "원격에 master 브랜치가 없습니다.");
            return;
        }

        this.jta.setText(this.gitUtil.getFileListByDiffOriginMaster(projectPath));
    }

    private void setColorExtractMainFrameFileListBtn() {
        if (StringUtils.isEmpty(this.fileList)) {
            this.extractMainFrame.setOffColorFileListBtn();
        } else {
            this.extractMainFrame.setOnColorFileListBtn();
        }
    }

    @Override
    public void removeFileList() {
        this.jta.setText("");
        this.fileList = "";
    }

    @Override
    public String getFileList() {
        return this.fileList;
    }

    @Override
    public boolean isExistFileList() {
        if(StringUtils.isEmpty(this.fileList) || "".equals(this.fileList.trim())) {
            return false;
        }
        return true;
    }

    @Override
    public void showFrameInit() {
        gitInit();
        this.jta.setText(this.fileList);
        this.setVisible(true);
    }
}
