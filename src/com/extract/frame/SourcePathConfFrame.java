package com.extract.frame;

import com.extract.ExtractMain;
import com.extract.util.FileUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SourcePathConfFrame extends JFrame {

    private GridBagLayout gb;
    private GridBagConstraints gbc;

    private JTextField[] beforeTf = new JTextField[10];
    private JTextField[] afterTf = new JTextField[10];
    private JButton[] pathButton = new JButton[10];
    private JFileChooser pathCs;
    private JButton registerBtn;

    private ExtractMain extractMain;

    public SourcePathConfFrame(ExtractMain extractMain) {
        super("프로젝트 경로 설정");

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

        JLabel projectNameLabel = new JLabel("프로젝트명");
        projectNameLabel.setHorizontalAlignment(JLabel.CENTER);
        gbAdd(projectNameLabel, 0, 0, 1, 1);

        JLabel pathLabel = new JLabel("경로");
        pathLabel.setHorizontalAlignment(JLabel.CENTER);
        gbAdd(pathLabel, 1, 0, 3, 1);

        JLabel tempLabel = new JLabel("선택");
        tempLabel.setHorizontalAlignment(JLabel.CENTER);
        gbAdd(tempLabel, 4, 0, 1, 1);

        for(int i = 0; i < beforeTf.length; i++) {
            beforeTf[i] = new JTextField(5);
            afterTf[i] = new JTextField(20);
            pathButton[i] = new JButton("경로선택");
            pathButton[i].setPreferredSize(new Dimension(5, 10));

            gbAdd(beforeTf[i], 0, i + 1, 1, 1);
            gbAdd(afterTf[i], 1, i + 1, 3, 1);
            gbAdd(pathButton[i], 4, i + 1, 1, 1);
        }
        registerBtn = new JButton("등록");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(registerBtn);
        gbAdd(buttonPanel, 0, 11, 5, 1);

        String defaultJfcPath = System.getProperty("user.home") + "\\Desktop";
        String rootJfcPath = "D:\\";

        pathCs = new JFileChooser();
        pathCs.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        try {
            // 기본경로지정 java_1.8 버그로 인한 Exception 처리
            pathCs.setCurrentDirectory(new File(rootJfcPath));
        } catch (Exception e) {
            pathCs.setCurrentDirectory(new File(defaultJfcPath));
        }

        this.setSize(550, 450);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    private void eventInit() {
        // 경로 선택 버튼 클릭 시
        for (int i = 0; i < pathButton.length; i++) {
            final int finalI = i;
            pathButton[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int result = pathCs.showOpenDialog(null);
                    if(result == JFileChooser.APPROVE_OPTION) {
                        afterTf[finalI].setText(pathCs.getSelectedFile().getPath());
                    }
                }
            });
        }

        // 저장 버튼 클릭 시
        registerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Map<String, Object> jsonObj = FileUtil.getJsonObjByConfigJsonFile();
                List<String> sourcePathList = new ArrayList<>();
                for(int i = 0; i < beforeTf.length; i++) {
                    if(!"".equals(beforeTf[i].getText().trim()) && "".equals(afterTf[i].getText().trim())) {
                        JOptionPane.showMessageDialog(null, "경로를 입력해주세요");
                        return;
                    } else if("".equals(beforeTf[i].getText().trim()) && !"".equals(afterTf[i].getText().trim())) {
                        JOptionPane.showMessageDialog(null, "프로젝트명을 입력해주세요");
                        return;
                    }
                    String inPath = beforeTf[i].getText().trim() + ">" + afterTf[i].getText().trim();
                    sourcePathList.add(inPath);
                }
                jsonObj.put("sourcePathList", sourcePathList);
                FileUtil.makeConfigJsonFile(jsonObj);

                // 부모창 소스 경로 콤보박스 셋팅
                SourcePathConfFrame.this.extractMain.setSourceRootDirCombo();
                dispose();
            }
        });
    }

    public void setJsonData() {
        Map<String, Object> jsonObj = FileUtil.getJsonObjByConfigJsonFile();
        java.util.List<String> sourcePathList = (java.util.List<String>) jsonObj.get("sourcePathList");
        if(sourcePathList == null) return;

        for(int i = 0; i < sourcePathList.size(); i++) {
            String pathStr = sourcePathList.get(i);
            if(pathStr != null && !"".equals(pathStr) && pathStr.length() > 1) {
                String[] pathStrs = pathStr.split(">");
                if(pathStrs.length == 2) {
                    beforeTf[i].setText(pathStrs[0]);
                    afterTf[i].setText(pathStrs[1]);
                }
            }
        }
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
