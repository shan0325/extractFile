package com.extract.frame;

import javax.swing.*;
import java.awt.*;

public class ReadmeFrame extends JFrame {

    public ReadmeFrame() throws HeadlessException {
        super("README");
        init();
        eventInit();
    }

    public void init() {

        JLabel info = new JLabel("# 설명");
        info.setForeground(Color.RED);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 1));
//        panel.setBorder(BorderFactory.createEtchedBorder());
        panel.add(new JLabel(""));
        panel.add(info);
        panel.add(new JLabel(""));
        panel.add(new JLabel("프로젝트 선택 : 소스를 추출할 프로젝트 선택(설정에서 프로젝트 셋팅 필요)"));
        panel.add(new JLabel(""));
        panel.add(new JLabel("추출경로 : 소스를 다운 받을 디렉토리 선택"));
        panel.add(new JLabel(""));
        panel.add(new JLabel("파일목록 : 추출할 소스 파일 목록 등록(ex src/main/java/.../DateUtil.java)"));

        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.add(panel);
        this.setSize(550, 300);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    public void eventInit() {

    }
}
