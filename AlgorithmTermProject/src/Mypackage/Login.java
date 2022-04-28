package Mypackage;

import java.awt.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.Set;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

public class Login {

  //
  static String ip = null; // txt���Ͽ���, ip�о��.
  static int portnum = 0; // port number
  String serverAddress; // ���� �ּ�
  Scanner in; // ������ �޾Ƶ��� �Ű�ü
  PrintWriter out; // �������� ���� �Ű�ü
  String curid; // ���� �α��� id
  static String res = "";
  static int count;

  // login gui variables
  JFrame frame = new JFrame("login form"); // ó�� �α���â
  JLabel lbl, la1, la2, la3, emp;
  JTextField id;
  JPasswordField passwd;
  JPanel emptyPanel, idPanel, paPanel, loginPanel;
  JButton b1, b2;
  JTextArea content;
  String curemail;
  int curnumber;
  int curgrade;
  String curname;
  String logpw;
  String logid;
  int buttoncount = 0;

  // TODO:

  // ��й�ȣ ��ȣȭ �ڵ�
  public String encryptSHA256(String str) {
    String sha = "";
    try {
      MessageDigest sh = MessageDigest.getInstance("SHA-256");
      sh.update(str.getBytes());
      byte[] byteData = sh.digest();
      StringBuilder sb = new StringBuilder();
      for (byte byteDatum : byteData) {
        sb.append(
          Integer.toString((byteDatum & 0xff) + 0x100, 16).substring(1)
        );
      }
      sha = sb.toString();
    } catch (NoSuchAlgorithmException e) {
      System.out.println("��ȣȭ ����-NoSuchAlgorithmException");
      sha = null;
    }
    return sha;
  }

  public String encryptSHA256(String str) {
    String sha = "";
    try {
      MessageDigest sh = MessageDigest.getInstance("SHA-256");
      sh.update(str.getBytes());
      byte[] byteData = sh.digest();
      StringBuilder sb = new StringBuilder();
      for (byte byteDatum : byteData) {
        sb.append(
          Integer.toString((byteDatum & 0xff) + 0x100, 16).substring(1)
        );
      }
      sha = sb.toString();
    } catch (NoSuchAlgorithmException e) {
      System.out.println("��ȣȭ ����-NoSuchAlgorithmException");
      sha = null;
    }
    return sha;
  }

  // constructor
  public Login(String serverAddress) {
    this.serverAddress = serverAddress;
    // FlowLayout���
    frame.setLayout(new FlowLayout());

    // Border�� ���� ����
    EtchedBorder eborder = new EtchedBorder();
    // ���̺� ����
    lbl = new JLabel("Enter Id and Password");
    // ���̺��� ���� �����
    lbl.setBorder(eborder);
    // ���̺� �߰�
    frame.add(lbl);

    emptyPanel = new JPanel();
    emp = new JLabel("\n");
    emptyPanel.add(emp);
    frame.add(emp);

    // id�гΰ� pw �гλ���
    idPanel = new JPanel();
    paPanel = new JPanel();

    la3 = new JLabel("User ID       ");
    la2 = new JLabel("Password  ");
    // id�ؽ�Ʈ�ʵ�� pw�ؽ�Ʈ �ʵ� ����
    id = new JTextField(15);
    passwd = new JPasswordField(15);
    idPanel.add(la3);
    idPanel.add(id);
    idPanel.setBackground(Color.white);
    paPanel.add(la2);
    paPanel.add(passwd);
    paPanel.setBackground(Color.white);

    // �α��ΰ� ȸ�������� ���� �г� ����

    loginPanel = new JPanel();
    loginPanel.setBackground(Color.white);
    b1 = new JButton("Login");
    b2 = new JButton("Resister");
    b1.setBackground(Color.yellow);
    b2.setBackground(Color.yellow);

    loginPanel.add(emp);
    loginPanel.add(b1);
    loginPanel.add(b2);
    frame.add(idPanel, BorderLayout.WEST);
    frame.add(paPanel, BorderLayout.WEST);
    frame.add(loginPanel);

    b1.addActionListener(
      new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          String comment = e.getActionCommand();
          if (
            comment.contentEquals("Login")
          ) { // �α��� ��ư�� ������ ��,
            logid = id.getText().trim(); // id�� ���� ���� �ҷ���
            logpw = passwd.getText(); // �н����忡 ���� ���� �ҷ���.
            String encryptLogPW = encryptSHA256(logpw); // ��ȣȭ�� ��й�ȣ�� �ص��ؼ� �޾ƿ�
            out.println("logid" + logid + " " + encryptLogPW); // �������� ���̵�� �н����� ����
            if (
              logid.length() == 0 || logpw.length() == 0
            ) { // id�� ��й�ȣ�� �ƹ� �͵� �Է����� �ʾ��� ��, ���� �޽����� ǥ����.
              JOptionPane.showMessageDialog(
                null,
                "���̵� �Ǵ� ��й�ȣ�� �Է� �ϼž� �˴ϴ�.",
                "���̵� ����� �Է�!",
                JOptionPane.DEFAULT_OPTION
              );
              return;
            }
            frame.dispose();
          }
        }
      }
    );
    b2.addActionListener(
      new ActionListener() { // ȸ�������� ������ ��, ȸ������ â���� �̵������ش�.
        @Override
        public void actionPerformed(ActionEvent e) {
          new Resister();
        }
      }
    );

    // 3�� 20�� ������ �ؽ�Ʈ�����
    // content = new JTextArea(3,20);
    // JScrollPane s= new JScrollPane(content);
    // add(s);
    frame.setSize(400, 200);
    frame.getContentPane().setBackground(Color.white);
    frame.setLocationRelativeTo(null);
    frame.setVisible(false);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  public class Resister { // ȸ�������� �� ���� â

    JFrame sub = new JFrame("Resister");
    JLabel lbl, namelbl, gradelbl, emaillbl, idlbl, pwlbl, stnumlbl;
    JTextField nameField, gradeField, emailField, idField, stnumField;
    JPasswordField passwd;
    JPanel namePanel, gradePanel, emailPanel, idPanel, paPanel, stnumPanel;
    JButton resister_btn;
    JTextArea content;

    public Resister() {
      sub.setLayout(new FlowLayout());

      EtchedBorder eborder = new EtchedBorder();

      lbl = new JLabel("Enter user information");
      // ���̺��� ���� �����
      lbl.setBorder(eborder);
      sub.add(lbl);

      idPanel = new JPanel();
      paPanel = new JPanel();
      stnumPanel = new JPanel();
      emailPanel = new JPanel();
      namePanel = new JPanel();
      gradePanel = new JPanel();

      idlbl = new JLabel("Userid:          ");
      idField = new JTextField(10);
      idPanel.add(idlbl);
      idPanel.add(idField);
      idPanel.setBackground(Color.white);
      sub.add(idPanel);

      pwlbl = new JLabel("Password:         ");
      passwd = new JPasswordField(10);
      paPanel.add(pwlbl);
      paPanel.add(passwd);
      paPanel.setBackground(Color.white);
      sub.add(paPanel);

      emaillbl = new JLabel("email:         ");
      emailField = new JTextField(10);
      emailPanel.add(emaillbl);
      emailPanel.add(emailField);
      emailPanel.setBackground(Color.white);
      sub.add(emailPanel);

      stnumlbl = new JLabel("Student number: ");
      stnumField = new JTextField(10);
      stnumPanel.add(stnumlbl);
      stnumPanel.add(stnumField);
      stnumPanel.setBackground(Color.white);
      sub.add(stnumPanel);

      namelbl = new JLabel("Student name:     ");
      nameField = new JTextField(10);
      namePanel.add(namelbl);
      namePanel.add(nameField);
      namePanel.setBackground(Color.white);
      sub.add(namePanel);

      gradelbl = new JLabel("Student grade:    ");
      gradeField = new JTextField(10);
      gradePanel.add(gradelbl);
      gradePanel.add(gradeField);
      gradePanel.setBackground(Color.white);
      sub.add(gradePanel);

      resister_btn = new JButton("Resister");
      resister_btn.setBackground(Color.yellow);
      sub.add(resister_btn);

      resister_btn.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            String comment = e.getActionCommand();
            if (
              comment.contentEquals("Resister")
            ) { // register��ư �Է½�,
              String name = nameField.getText(); // �̸�, ������ϵ��� �ҷ���
              String stnum = stnumField.getText();
              String email = emailField.getText();
              String id = idField.getText();
              String pw = passwd.getText();
              String encryptPW = encryptSHA256(pw);
              String grade = gradeField.getText();
              out.println(
                "Resister" +
                name +
                " " +
                stnum +
                " " +
                email +
                " " +
                id +
                " " +
                encryptPW +
                " " +
                grade
              ); // �������� ����.
            }
            sub.dispose();
          }
        }
      );
      sub.setSize(250, 350);
      sub.getContentPane().setBackground(Color.white);
      sub.setVisible(true);
      sub.setLocationRelativeTo(null);
      sub.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
  }

  public class main { // �α��� ������ ������ â

    JFrame sub = new JFrame("Main");
    JButton b1 = new JButton("My Info");
    JButton b2 = new JButton("Book");

    public main() {
      // TODO �ڵ� ������ ������ ����

      sub.setTitle("Main");
      sub.setSize(500, 350);
      sub.setResizable(false);
      sub.setLocation(800, 450);
      sub.setLocationRelativeTo(null);
      sub.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

      // panel
      JPanel panel = new JPanel();
      placeLoginPanel(panel);

      // add
      sub.add(panel);

      // visible
      sub.setVisible(true);
      sub.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void placeLoginPanel(JPanel panel) {
      panel.setLayout(null);
      JLabel userLabel = new JLabel("Welcome to Gachon Festival!");
      userLabel.setBounds(90, 10, 500, 25);
      userLabel.setFont(new Font("Serif", Font.BOLD, 25));
      panel.add(userLabel);

      JLabel passLabel = new JLabel(
        "At the 2021 Gachon Festival, AI��software department will hold a special event in"
      );
      passLabel.setBounds(5, 50, 500, 25);
      panel.add(passLabel);
      JLabel passLabel2 = new JLabel("department's room.");
      passLabel2.setBounds(5, 70, 500, 25);
      panel.add(passLabel2);
      JLabel passLabel21 = new JLabel(
        "You can make a reservation for tomorrow's schedule from 0 o'clock to 18 o'clock."
      );
      passLabel21.setBounds(5, 90, 500, 25);
      panel.add(passLabel21);
      JLabel passLabel3 = new JLabel(
        "However, our student council used a slightly unusual algorithm for reservations."
      );
      passLabel3.setBounds(5, 110, 500, 25);
      panel.add(passLabel3);
      JLabel passLabel31 = new JLabel(
        "Reservation method is not a first-come, first-served, but based on a greed algorithm."
      );
      passLabel31.setBounds(5, 130, 500, 25);
      panel.add(passLabel31);
      JLabel passLabel32 = new JLabel(
        "Reservations will be completed if your reservation isn't overlapped with others."
      );
      passLabel32.setBounds(5, 150, 500, 25);
      panel.add(passLabel32);
      JLabel passLabel33 = new JLabel(
        "At 18:00, the server will show the reservation table for the next day."
      );
      passLabel33.setBounds(5, 170, 500, 25);
      panel.add(passLabel33);
      JLabel passLabel34 = new JLabel("Good luck, students.");
      passLabel34.setBounds(5, 190, 500, 25);
      panel.add(passLabel34);

      JLabel passLabel4 = new JLabel("Click the button");
      passLabel4.setBounds(10, 230, 120, 25);
      panel.add(passLabel4);
      JLabel passLabel5 = new JLabel("to check your info");
      passLabel5.setBounds(5, 250, 120, 25);
      panel.add(passLabel5);
      JLabel passLabel6 = new JLabel("Click the button");
      passLabel6.setBounds(195, 230, 120, 25);
      panel.add(passLabel6);
      JLabel passLabel7 = new JLabel("to book the room");
      passLabel7.setBounds(190, 250, 120, 25);
      panel.add(passLabel7);
      JLabel passLabel8 = new JLabel("Click the button");
      passLabel8.setBounds(380, 230, 120, 25);
      panel.add(passLabel8);
      JLabel passLabel9 = new JLabel("to check schedule");
      passLabel9.setBounds(375, 250, 120, 25);
      panel.add(passLabel9);

      JButton b1 = new JButton("My Info");
      b1.setBounds(5, 280, 100, 25);
      panel.add(b1);
      b1.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            new info();
          }
        }
      );

      JButton b2 = new JButton("Book");
      b2.setBounds(190, 280, 100, 25);
      panel.add(b2);
      b2.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            new Timer();
          }
        }
      );

      JButton b3 = new JButton("Time table");
      b3.setBounds(375, 280, 100, 25);
      panel.add(b3);
      b3.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            new Timetable();
          }
        }
      );
    }
  }

  public class info extends JFrame { // ������� ���� & ����

    JButton b1 = new JButton("Modify");
    JButton b2 = new JButton("Exit");
    String studentnum, email, name, grade;

    // �����κ��� ������ �޾Ƽ� ������ �־���

    public info() {
      // TODO �ڵ� ������ ������ ����

      setTitle("info");
      setSize(250, 300);
      setResizable(false);
      setLocation(800, 450);
      setLocationRelativeTo(null);

      // panel
      JPanel panel = new JPanel();
      placeinfoPanel(panel);

      // add
      add(panel);

      // visible
      setVisible(true);
    }

    public void placeinfoPanel(JPanel panel) {
      panel.setLayout(null);
      JLabel userLabel = new JLabel("Information about \"" + curid + "\"");
      userLabel.setBounds(15, 10, 200, 30);
      panel.add(userLabel);

      JLabel passLabel = new JLabel("User ID");
      passLabel.setBounds(15, 40, 200, 30);
      panel.add(passLabel);
      JTextField idtext = new JTextField(20);
      idtext.setText(curid);
      idtext.setEditable(false);
      idtext.setBounds(100, 40, 100, 30);
      panel.add(idtext);

      JLabel passLabel2 = new JLabel("Stu_num");
      passLabel2.setBounds(15, 70, 200, 30);
      panel.add(passLabel2);
      JTextField stunum = new JTextField(20);
      stunum.setText("" + curnumber);
      stunum.setEditable(false);
      stunum.setBounds(100, 70, 100, 30);
      panel.add(stunum);

      JLabel passLabel3 = new JLabel("Email");
      passLabel3.setBounds(15, 100, 200, 30);
      panel.add(passLabel3);
      JTextField emailtext = new JTextField(20);
      emailtext.setText(curemail);
      emailtext.setBounds(100, 100, 100, 30);
      panel.add(emailtext);

      JLabel passLabel4 = new JLabel("Name");
      passLabel4.setBounds(15, 130, 200, 30);
      panel.add(passLabel4);
      JTextField nametext = new JTextField(20);
      nametext.setText(curname);
      nametext.setEditable(false);
      nametext.setBounds(100, 130, 100, 30);
      panel.add(nametext);

      JLabel passLabel5 = new JLabel("Grade");
      passLabel5.setBounds(15, 160, 200, 30);
      panel.add(passLabel5);
      JTextField gratext = new JTextField(20);
      gratext.setText("" + curgrade);
      gratext.setBounds(100, 160, 100, 30);
      panel.add(gratext);

      // modify ��ư �������� �� �������ְ� ������ �����ֱ�
      b1.setBounds(5, 220, 100, 25);
      panel.add(b1);
      b1.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            // String mod_num = stunum.getText();
            // studentnum = mod_num;
            // stunum.setText(studentnum);
            String mod_email = emailtext.getText();
            email = mod_email;
            emailtext.setText(email);
            // String mod_name = nametext.getText();
            // name = mod_name;
            // nametext.setText(name);
            String mod_grade = gratext.getText();
            grade = mod_grade;
            gratext.setText(grade);
            // System.out.println(studentnum + email + name + grade);
            // ���� ����
            out.println("Update" + ":" + curid + ":" + email + ":" + grade); // ������ ����
          }
        }
      );

      b2.setBounds(130, 220, 100, 25);
      panel.add(b2);
      b2.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            dispose();
          }
        }
      );
    }
  }

  public class Timer extends JFrame implements Runnable {

    JLabel label;
    JButton b1 = new JButton("Book");
    JButton b2 = new JButton("Exit");
    JLabel l1, l2, l3;
    JTextField text = new JTextField();
    String bookTime;

    public Timer() {
      setSize(350, 300);
      setLocationRelativeTo(null);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      String time = getCurrentTime();
      getContentPane().setLayout(null);
      label = new JLabel(time);
      label.setFont(new Font("TimesRoman", Font.ITALIC, 20));
      label.setHorizontalAlignment(JLabel.CENTER);
      label.setBounds(115, 5, 100, 50);
      add(label);

      l1 = new JLabel("Enter the reservation time");
      l1.setBounds(20, 50, 300, 20);
      l2 = new JLabel("Plz enter start time + space + end time");
      l2.setBounds(20, 80, 300, 20);
      l3 = new JLabel("Ex) 1320 1400");
      l3.setBounds(20, 110, 300, 20);
      add(l1);
      add(l2);
      add(l3);
      text.setBounds(120, 150, 100, 20);
      add(text);

      JButton b1 = new JButton("Book");
      b1.setBounds(5, 200, 100, 30);
      add(b1);
      b1.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            bookTime = text.getText();
            b1.setEnabled(false);
            // ������ bookTime �����ֱ�
            out.println("Book " + bookTime + " " + curid);
            JOptionPane.showMessageDialog(
              null,
              "You entered " + bookTime,
              "Check the reservation",
              JOptionPane.DEFAULT_OPTION
            );
            dispose();
          }
        }
      );
      JButton b2 = new JButton("Exit");
      b2.setBounds(230, 200, 100, 30);
      add(b2);
      b2.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            dispose();
          }
        }
      );
      Thread t1 = new Thread(this);
      t1.start();

      setVisible(true);
    }

    @Override
    public void run() {
      while (true) {
        try {
          Thread.sleep(1000);
          String time = getCurrentTime();
          label.setText(time);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }

    public String getCurrentTime() {
      Calendar c = Calendar.getInstance();
      int hour = c.get(Calendar.HOUR_OF_DAY);
      int min = c.get(Calendar.MINUTE);
      int sec = c.get(Calendar.SECOND);

      String time = hour + ":" + min + ":" + sec;
      return time;
    }
  }

  public class Timetable extends JFrame {

    String bookperson[] = { "-", "-", "-", "-", "-", "-", "-", "-" };
    String header[] = { "-", "-", "-", "-", "-", "-", "-", "-" };
    String contents[][] = new String[1][8];

    String cont[] = new String[30];

    public Timetable() {
      cont = res.split(" ");

      for (int i = 0; i < 8; i++) {
        contents[0][i] = bookperson[i];
      }

      setTitle("Time table");
      setSize(600, 110);
      setLocation(800, 450);
      setLocationRelativeTo(null);

      // panel
      JTable table = new JTable(contents, header) {
        public boolean isCellEditable(int i, int c) {
          return false;
        }
      };
      for (int i = 0; i < 8; i++) {
        table.getColumnModel().getColumn(i).setPreferredWidth(40);
      }
      DefaultTableCellRenderer renderer1 = (DefaultTableCellRenderer) table
        .getTableHeader()
        .getDefaultRenderer();
      renderer1.setHorizontalAlignment(SwingConstants.CENTER);
      table.getTableHeader().setDefaultRenderer(renderer1);
      table.getTableHeader().setReorderingAllowed(false);

      DefaultTableCellRenderer renderer2 = new DefaultTableCellRenderer();
      renderer2.setHorizontalAlignment(SwingConstants.CENTER);
      TableColumnModel model = table.getColumnModel();
      for (int i = 0; i < model.getColumnCount(); i++) {
        model.getColumn(i).setCellRenderer(renderer2);
      }
      for (int i = 0; i < count; i++) {
        bookperson[i] = cont[2 + 2 * i];
        header[i] = cont[1 + 2 * i];
      }
      for (int i = 0; i < count; i++) {
        table.setValueAt(bookperson[i], 0, i);
        table
          .getTableHeader()
          .getColumnModel()
          .getColumn(i)
          .setHeaderValue(header[i]);

        table.getTableHeader().repaint();
      }

      JScrollPane scrollpane = new JScrollPane(table);

      // add
      add(scrollpane);

      // visible
      setVisible(true);
    }
  }

  // TODO:
  public void run() {
    // TODO Auto-generated method stub
    try {
      Socket socket = new Socket(serverAddress, portnum); // �Է¹��� ip�ּҿ� portnumber�� ���� ����.
      in = new Scanner(socket.getInputStream()); // ���Ͽ��� �о���� ����
      out = new PrintWriter(socket.getOutputStream(), true); // ���Ͽ� ���� ����
      String[] userinfo = null; // ����� ���������ϴ� ����
      while (true) {
        String line = in.nextLine();
        System.out.println(line);

        if (
          line.contains("access")
        ) { // �α����� ������ ������ �˻��ϰ�, ���ε̴ٴ� �޽����� ���� ���
          userinfo = line.split(":");
          JOptionPane.showMessageDialog(
            null,
            "�α��� ����",
            "�α��� Ȯ��!",
            JOptionPane.DEFAULT_OPTION
          ); // ������ �α���
          new main();
          // ǥ��
          // TODO:
          curid = userinfo[1];
          curnumber = Integer.parseInt(userinfo[2]);
          curemail = userinfo[3];
          curname = userinfo[4];
          curgrade = Integer.parseInt(userinfo[5]);
          // ����α����� ����� �̸��� ����
        }
        // ���̵� Ʋ���� ��
        else if (line.contains("id invalid")) {
          JOptionPane.showMessageDialog(
            null,
            "���̵� Ʋ��!",
            "�α��� Ȯ��!",
            JOptionPane.DEFAULT_OPTION
          ); // �α��� ����
          // ��,
          // âǥ��
          return;
        }
        // ��й�ȣ�� Ʋ���� ��
        else if (line.contains("pw invalid")) {
          JOptionPane.showMessageDialog(
            null,
            "��й�ȣ Ʋ��!",
            "�α��� Ȯ��!",
            JOptionPane.DEFAULT_OPTION
          ); // �α���
          // ���� ��,
          // âǥ��
          return;
        } else if (
          line.contains("Welcome")
        ) { // ȸ�����Կ� �������� ��
          JOptionPane.showMessageDialog(
            null,
            "ȸ�����Լ���",
            "ȯ���մϴ�!",
            JOptionPane.DEFAULT_OPTION
          ); // ȸ������ ����
          // ��, // âǥ��
        } else if (line.contains("UC")) {
          JOptionPane.showMessageDialog(
            null,
            "���� ����",
            "���� ����!",
            JOptionPane.DEFAULT_OPTION
          );
        } else if (line.contains("UF")) {
          JOptionPane.showMessageDialog(
            null,
            "���� ����",
            "���� ����!",
            JOptionPane.DEFAULT_OPTION
          );
        } else if (line.contains("result")) {
          res = line;
          count = in.nextInt();
        } else if (
          line.contains("Late")
        ) { // ������ �����Ǿ��� ��,
          JOptionPane.showMessageDialog(
            null,
            "���� ����",
            "���� ������ �����Ǿ����ϴ�.",
            JOptionPane.DEFAULT_OPTION
          );
        }
      }
    } catch (Exception e) {
      System.out.println(e);
    } finally {
      frame.dispose();
    }
  }// test commit

  public static void server(String fileName) { // input.txt���Ͽ��� ip�ּҿ� port number�� �ҷ����� �Լ�.
    Scanner inputStream = null;
    try {
      inputStream = new Scanner(new File(fileName)); // input������ �о��.
    } catch (
      FileNotFoundException e
    ) { // ������ ���� ���, �ڵ�����
      ip = "localhost";
      portnum = 9999;
      e.printStackTrace();
    }

    while (
      inputStream.hasNext()
    ) { // input���Ͽ���, ip�ּҿ� portnumber�� �о��.
      ip = inputStream.next();
      portnum = inputStream.nextInt();
    }
  }

  public static void main(String args[]) {
    String fname = "input.txt";
    server(fname); // input�������� ���� �ҷ�����
    Login client = new Login(ip);
    client.frame.setVisible(true);
    client.run();
  }
}
