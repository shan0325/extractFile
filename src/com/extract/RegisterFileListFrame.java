package com.extract;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterFileListFrame extends JFrame {

    private JButton registerBtn;
    private JTextArea jta;
    private JScrollPane jsp;
    private JTextArea parentJta;

    public RegisterFileListFrame(JTextArea parentJta) {
        super("파일목록 등록");
        init();
        eventInit();

        this.parentJta = parentJta;
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
                if(e.getSource() == registerBtn) {
                    parentJta.append("파일목록 :\n");
                    parentJta.append(jta.getText() + "\n\n");
                    dispose();
                }
            }
        });
    }

    public JTextArea getJta() {
        return jta;
    }
}
