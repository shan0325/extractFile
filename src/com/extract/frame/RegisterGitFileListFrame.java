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
    private Integer gitFileListCount;

    public RegisterGitFileListFrame(ExtractMainFrame extractMainFrame) {
        super("파일목록 등록");

        this.gitUtil = new GitUtil(new CmdUtil());
        this.extractMainFrame = extractMainFrame;
        this.extractMainFrame.setEnabled(false);

        gitInit();
        init();
        eventInit();
    }

    private void gitInit() {
        if (!this.gitUtil.isInstalledGit()) return;

        String projectPath = this.extractMainFrame.getProjectPath();
        if (StringUtils.isEmpty(projectPath)) return;

        String currentBranch = this.gitUtil.getCurrentBranch(projectPath);
        if (StringUtils.isEmpty(currentBranch)) return;
        this.currentGitBranch = currentBranch;

        if (!this.gitUtil.isExistOriginMaster(projectPath)) return;

        String fileList = this.gitUtil.getFileListByDiffOriginMaster(projectPath);
        if (StringUtils.isEmpty(fileList)) return;

        String[] split = fileList.split("\n");
        this.gitFileListCount = split.length;
    }

    public void init() {
        resetFileListBtn = new RoundedButton("RESET");
        resetFileListBtn.setBackground(Color.darkGray);
        resetFileListBtn.setForeground(Color.WHITE);

        String gitDiffBtnTitle = "변경파일";
        gitDiffBtnTitle = this.currentGitBranch != null ? this.currentGitBranch + " " + gitDiffBtnTitle : gitDiffBtnTitle;
        gitDiffBtnTitle = this.gitFileListCount != null ? gitDiffBtnTitle + "(" + this.gitFileListCount + ")" : gitDiffBtnTitle;
        gitDiffBtn = new RoundedButton(gitDiffBtnTitle);
        gitDiffBtn.setBackground(Color.darkGray);
        gitDiffBtn.setForeground(Color.WHITE);

        JPanel topPanal = new JPanel();
        topPanal.setLayout(new FlowLayout(FlowLayout.LEFT));
        topPanal.setBorder(BorderFactory.createEmptyBorder(0, -5, 0, 0));
        topPanal.add(resetFileListBtn);
        topPanal.add(gitDiffBtn);

        jta = new JTextArea();
        jsp = new JScrollPane(jta);
        jsp.setPreferredSize(new Dimension(0, 400));

        JPanel jtaPanel = new JPanel();
        jtaPanel.setLayout(new BoxLayout(jtaPanel, BoxLayout.X_AXIS));
        jtaPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jtaPanel.add(jsp);

        registerBtn = new RoundedButton("등록");
        registerBtn.setBackground(new Color(52, 73, 94));
        registerBtn.setForeground(Color.WHITE);

        JPanel bottomPanal = new JPanel();
        bottomPanal.setLayout(new BoxLayout(bottomPanal, BoxLayout.X_AXIS));
        bottomPanal.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        bottomPanal.add(registerBtn);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        panel.add(topPanal);
        panel.add(jtaPanel);
        panel.add(bottomPanal);

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
                super.windowClosing(e);
            }
        });

        // 등록 버튼 클릭 시
        this.registerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                extractMainFrame.setEnabled(true);
                fileList = jta.getText();
                dispose();
            }
        });

        // RESET 버튼 클릭시
        this.resetFileListBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeFileList();
                jta.requestFocus();
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

        this.jta.append(this.gitUtil.getFileListByDiffOriginMaster(projectPath));
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
        this.jta.setText(this.fileList);
        this.setVisible(true);
    }
}
