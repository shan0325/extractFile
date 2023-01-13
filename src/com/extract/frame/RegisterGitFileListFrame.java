package com.extract.frame;

import com.extract.util.CmdUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterGitFileListFrame extends JFrame implements RegisterFileList {

    private JButton registerBtn;
    private JTextArea jta;
    private JScrollPane jsp;
    private String projectPath;

    public RegisterGitFileListFrame(String projectPath) {
        super("파일목록 등록");
        this.projectPath = projectPath;
        init();
        eventInit();
        setGitFileList();
    }

    public void init() {
        jta = new JTextArea();
        jsp = new JScrollPane(jta);
        jsp.setPreferredSize(new Dimension(0, 200));

        JPanel panel1 = new JPanel();
        panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));
        panel1.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        panel1.add(jsp);

        registerBtn = new JButton("등록");

        JPanel panel2 = new JPanel();
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.X_AXIS));
        panel2.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        panel2.add(registerBtn);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(panel1);
        panel.add(panel2);

        this.add(panel);
        this.setSize(600, 400);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    private void eventInit() {
        registerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void setGitFileList() {
        String gitCmd = this.projectPath;
        System.out.println("gitCmd = " + gitCmd);

        CmdUtil cmd = new CmdUtil();
        String exec = cmd.exec("git -C D:\\Dev\\Workspace\\IB_WEB diff origin/master..#9825_모바일문화상품권_도입_건 --name-only");
        System.out.println("exec = " + exec);

        this.jta.append(exec);
    }

    @Override
    public String getFileList() {
        return jta.getText();
    }

    @Override
    public boolean isExistFileList() {
        if("".equals(jta.getText().trim())) {
            return false;
        }
        return true;
    }
}
