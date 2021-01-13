package com.extract;

import com.extract.util.FileUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

public class InPathConfFrame extends JFrame {

    private JTextField[] beforeTf = new JTextField[10];
    private JTextField[] afterTf = new JTextField[10];
    private JButton registerBtn;

    public InPathConfFrame() {
        super("소스 가져오기 경로 치환 설정");
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
        List<String> inPathList = (List<String>) jsonObj.get("inPathList");
        if(inPathList != null) {
            for(int i = 0; i < inPathList.size(); i++) {
                String pathStr = inPathList.get(i);
                if(!"".equals(pathStr)) {
                    String[] pathStrs = pathStr.split(">");
                    if(pathStrs.length > 1) {
                        beforeTf[i].setText(pathStrs[0]);
                        afterTf[i].setText(pathStrs[1]);
                    } else {
                        beforeTf[i].setText(pathStrs[0]);
                    }
                }
            }
        }
    }

    private void eventInit() {
        registerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Map<String, Object> jsonObjByJsonFile = FileUtil.getJsonObjByConfigJsonFile();
                System.out.println(jsonObjByJsonFile);

                List<String> test = (List<String>) jsonObjByJsonFile.get("outPathList");
                System.out.println(test);
                JOptionPane.showMessageDialog(null, test);

            }
        });
    }
}
