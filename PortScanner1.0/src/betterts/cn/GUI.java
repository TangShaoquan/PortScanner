package betterts.cn;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class GUI{
    public JFrame frame;
    public JButton btnScan;
    public JButton btnStop;
    public Label ipLabel;
    public Label portLabel;
    public JTextArea resultText ;
    public TextField ipStart;
    public TextField ipEnd;
    public TextField portStart;
    public TextField portEnd;
    public JScrollPane result;

    public GUI() {
        //初始化
        this.frame = new JFrame("PortScanner");
        this.btnScan = new JButton("Start");
        this.btnStop = new JButton("Stop");
        this.ipLabel = new Label(  "IP     :");
        this.portLabel = new Label("Port  :");
        this.resultText = new JTextArea("使用方法：\n" +
                "\t1. IP处输入要扫描的起始IP和结束IP（默认127.0.0.1）\n" +
                "\t2. Port处输入要扫描的起始端口和结束端口（默认0~65535）\n" +
                "\t3. 点击Start按钮即可开始扫描。点击Stop即可结束扫描");
        this.ipStart = new TextField("127.0.0.1");
        this.portStart = new TextField("0");
        this.ipEnd = new TextField("127.0.0.1");
        this.portEnd = new TextField("65535");
        result = new JScrollPane(this.resultText);//滚动

        JLabel ipSplit = new JLabel("——");
        JLabel portSplit = new JLabel("——");

        //主窗口参数
            //固定窗口大小600 * 400
        this.frame.setSize(600,400);
        this.frame.setResizable(false);
            //可见
        this.frame.setVisible(true);
        this.frame.setLocation(500,150);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //布局
        this.frame.setLayout(null);
        this.ipLabel.setBounds(70,30,50,19);
        this.portLabel.setBounds(70,60,50,19);
        this.ipStart.setBounds(120,30,100,19);
        this.portStart.setBounds(120,60,50,19);
        ipSplit.setBounds(225,30,25,19);
        portSplit.setBounds(175,60,25,19);
        this.ipEnd.setBounds(255,30,100,19);
        this.portEnd.setBounds(210,60,50,19);
        this.btnScan.setBounds(400,30,100,20);
        this.btnStop.setBounds(400,60,100,20);
        result.setBounds(5,100,575,260);

        //UI
        this.btnScan.setOpaque(false);
        this.btnStop.setOpaque(false);
        this.btnScan.setBackground(new Color(0,0,0,200));
        this.btnStop.setBackground(new Color(0,0,0,200));
        this.btnScan.setMargin(new Insets(0,0,0,0));
        this.btnStop.setMargin(new Insets(0,0,0,0));
        this.btnScan.setBorderPainted(true);//不绘制边框
        this.btnStop.setBorderPainted(true);//不绘制边框

        //添加组件
        this.frame.add(this.ipLabel);
        this.frame.add(this.portLabel);
        this.frame.add(this.btnScan);
        this.frame.add(this.btnStop);
        this.frame.add(this.ipStart);
        this.frame.add(ipSplit);
        this.frame.add(portSplit);
        this.frame.add(this.ipEnd);
        this.frame.add(this.portEnd);
        this.frame.add(this.portStart);
        this.frame.add(result);

        //调用事件监听
        this.guiEvent();
    }

    public void guiEvent() {

        //开始按钮
        this.btnScan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                resultText.setText("");
                String ip_start = ipStart.getText();
                String ip_end = ipEnd.getText();
                int port_start = Integer.parseInt(portStart.getText());
                int port_end = Integer.parseInt(portEnd.getText());

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            PortScanner portScanner = new PortScanner(ip_start,ip_end,port_start,port_end);
                            List<Future<PortScanner.ScanResult>> futures = portScanner.Scanner();
                            StringBuilder res = new StringBuilder(" 扫描结果：\n");
                            for (Future<PortScanner.ScanResult> f: futures) {
                                res.append(f.get().toString() + "\n");
                                System.out.println(res.toString());
                                resultText.setText(res.toString());
                            }
                        } catch (Exception exception){
                            exception.printStackTrace();
                        }
                    }
                });
            }
        });

        //结束按钮
        this.btnStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }
}
