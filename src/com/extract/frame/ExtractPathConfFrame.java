package com.extract.frame;

import com.extract.ExtractMain;
import com.extract.util.FileUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExtractPathConfFrame extends JFrame {

    private GridBagLayout gb;
    private GridBagConstraints gbc;

    private JTextField afterTf;
    private JButton pathButton;
    private JFileChooser pathCs;
    private JButton registerBtn;

    private ExtractMain extractMain;

    public ExtractPathConfFrame(ExtractMain extractMain) {
        super("추출 기본 디렉토리 설정");

        this.extractMain = extractMain;
        init();
        eventInit();
        setJsonData();
    }

    private void init() {
        gb = new GridBagLayout();
        this.setLayout(gb);

        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        JLabel pathLabel = new JLabel("경로");
        pathLabel.setHorizontalAlignment(JLabel.CENTER);
        gbAdd(pathLabel, 0, 0, 3, 1);

        JLabel tempLabel = new JLabel("선택");
        tempLabel.setHorizontalAlignment(JLabel.CENTER);
        gbAdd(tempLabel, 3, 0, 1, 1);

        afterTf = new JTextField(20);
        pathButton = new JButton("경로선택");
        pathButton.setPreferredSize(new Dimension(5, 10));

        gbAdd(afterTf, 0, 1, 3, 1);
        gbAdd(pathButton, 3, 1, 1, 1);

        registerBtn = new JButton("등록");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(registerBtn);
        gbAdd(buttonPanel, 0, 11, 5, 1);

        String defaultJfcPath = System.getProperty("user.home") + "\\Desktop";

        pathCs = new JFileChooser();
        pathCs.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        pathCs.setCurrentDirectory(new File(defaultJfcPath));

        this.setSize(400, 150);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    private void eventInit() {
        // 경로 선택 버튼 클릭 시
        pathButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = pathCs.showOpenDialog(null);
                if(result == JFileChooser.APPROVE_OPTION) {
                    afterTf.setText(pathCs.getSelectedFile().getPath());
                }
            }
        });

        // 저장 버튼 클릭 시
        registerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Map<String, Object> jsonObj = FileUtil.getJsonObjByConfigJsonFile();

                jsonObj.put("extractPath", afterTf.getText().trim());
                FileUtil.makeConfigJsonFile(jsonObj);

                // 부모창 추출 경로 셋팅
                ExtractPathConfFrame.this.extractMain.setTargetDirCsCurrentDirectory();
                dispose();
            }
        });
    }

    public void setJsonData() {
        Map<String, Object> jsonObj = FileUtil.getJsonObjByConfigJsonFile();
        String extractPath = (String) jsonObj.get("extractPath");
        if(extractPath == null) return;

        afterTf.setText(extractPath);
    }

    //그리드백레이아웃에 붙이는 메소드
    private void gbAdd(JComponent c, int x, int y, int w, int h){
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = w;
        gbc.gridheight = h;
        //gb.setConstraints(c, gbc);
        gbc.insets = new Insets(2, 2, 2, 2);
        add(c, gbc);
    }//gbAdd
}
