package com.extract.frame;

import com.extract.util.FileUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OutPathConfFrame extends JFrame {

    private JTextField[] beforeTf = new JTextField[10];
    private JTextField[] afterTf = new JTextField[10];
    private JButton registerBtn;

    public OutPathConfFrame() {
        super("소스 내보내기 경로 치환 설정");
        init();
        eventInit();
        setJsonData();
    }

    private void init() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        for(int i = 0; i < beforeTf.length; i++) {
            beforeTf[i] = new JTextField();
            afterTf[i] = new JTextField();

            gbc.weightx = 0.5;
            gbc.gridx = 0;
            gbc.gridy = i;
            panel.add(beforeTf[i], gbc);

            JLabel arrow = new JLabel(">");
            arrow.setHorizontalAlignment(JLabel.CENTER);
            gbc.weightx = 0.1;
            gbc.gridx = 1;
            gbc.gridy = i;
            panel.add(arrow, gbc);

            gbc.weightx = 0.5;
            gbc.gridx = 2;
            gbc.gridy = i;
            panel.add(afterTf[i], gbc);
        }
        registerBtn = new JButton("등록");
        JPanel panel2 = new JPanel();
        panel2.setLayout(new FlowLayout());
        panel2.add(registerBtn);

        this.setLayout(new BorderLayout());
        this.add(panel, BorderLayout.CENTER);
        this.add(panel2, BorderLayout.SOUTH);
        this.setSize(500, 300);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    public void setJsonData() {
        Map<String, Object> jsonObj = FileUtil.getJsonObjByConfigJsonFile();
        List<String> outPathList = (List<String>) jsonObj.get("outPathList");
        if(outPathList == null) return;

        for(int i = 0; i < outPathList.size(); i++) {
            String pathStr = outPathList.get(i);
            if(!"".equals(pathStr)) {
                String[] pathStrs = pathStr.split(">");
                if(pathStrs.length == 0) {
                    continue;
                } else if(pathStrs.length == 1) {
                    beforeTf[i].setText(pathStrs[0]);
                } else if(pathStrs.length == 2) {
                    beforeTf[i].setText(pathStrs[0]);
                    afterTf[i].setText(pathStrs[1]);
                }
            }
        }
    }

    private void eventInit() {
        registerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Map<String, Object> jsonObj = FileUtil.getJsonObjByConfigJsonFile();
                List<String> outPathList = new ArrayList<>();
                for(int i = 0; i < beforeTf.length; i++) {
                    if(!"".equals(beforeTf[i].getText().trim()) || !"".equals(afterTf[i].getText().trim())) {
                        String inPath = beforeTf[i].getText().trim() + ">" + afterTf[i].getText().trim();
                        outPathList.add(inPath);
                    }
                }
                jsonObj.put("outPathList", outPathList);
                FileUtil.makeConfigJsonFile(jsonObj);
                dispose();
            }
        });
    }
}
