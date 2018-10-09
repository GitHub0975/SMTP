import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Base64;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.List;
import java.awt.TextArea;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class mailS extends JFrame {
	final int SMTP_PORT=25;
	static String server="stumail.nutn.edu.tw";
	static String my_email_addr/*="s10459041@stumail.nutn.edu.tw"*/;
	String[] setMessage;
	Base64.Decoder decoder = Base64.getDecoder();
	static String username,password;
	final static int POP_PORT=110;
	static BufferedReader pop_in=null;
	static PrintWriter pop_out=null;
	static Socket pop=null;
	
	private JPanel contentPane;
	private JTextField textField;
	private TextArea textArea_1;
	private TextArea textArea;
	private JLabel lblSubject;
	private JLabel lblRecipient;
	private JButton btnSend;
	private JPanel panel_2;
	private JTextField textField_1;
	private static DefaultListModel dlm;
	private JList list;
	private static JPanel panel_1;
	private static JPanel panel_3;
	private JButton btnPreviousPage;
	private TextArea textArea_2;
	private JButton button;
	private JLabel lblSubjectno;
	private JLabel lblSenderno;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					pop=new Socket(server,POP_PORT);
					pop_in=new BufferedReader(new InputStreamReader(pop.getInputStream()));
					pop_out=new PrintWriter(pop.getOutputStream());
					String res=pop_in.readLine();
					System.out.println(res);
					if(!("+OK".equals(res.substring(0, 3)))){
						pop.close();
						throw new RuntimeException(res);
					}
					boolean success=false;
					do {
						username=JOptionPane.showInputDialog("請輸入帳號");
						success=getSingleLine("USER "+username);
						success=false;
						password=JOptionPane.showInputDialog("請輸入密碼");
						success=getSingleLine("PASS "+password);	
					}while(!success);
					my_email_addr=username;
					mailS frame = new mailS();
					frame.setTitle("郵件收發系統");
					panel_1.setVisible(false);
					panel_3.setVisible(false);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws IOException 
	 */
	public mailS() throws IOException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 885, 553);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(192, 192, 192));
		panel.setBounds(0, 0, 152, 512);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JButton Wbutton = new JButton("\u5BEB\u4FE1");
		Wbutton.setBackground(new Color(0, 204, 255));
		Wbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textArea_2.setText("");
				panel_2.setVisible(false);
				panel_3.setVisible(false);
				panel_1.setVisible(true);
			}
		});
		Wbutton.setBounds(24, 48, 104, 43);
		Wbutton.setFont(new Font("標楷體", Font.PLAIN, 27));
		panel.add(Wbutton);
		
		JButton Rbutton_1 = new JButton("\u6536\u4FE1\u5323");
		Rbutton_1.setBackground(new Color(0,204,255));
		Rbutton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea_2.setText("");
				panel_2.setVisible(true);
				panel_1.setVisible(false);
				panel_3.setVisible(false);
			}
		});
		Rbutton_1.setFont(new Font("標楷體", Font.PLAIN, 27));
		Rbutton_1.setBounds(8, 127, 136, 43);
		panel.add(Rbutton_1);
		
		button = new JButton("\u96E2\u958B");
		button.setBackground(new Color(0,204,255));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					update();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		button.setFont(new Font("標楷體", Font.PLAIN, 27));
		button.setBounds(24, 193, 104, 43);
		panel.add(button);
		
		panel_3 = new JPanel();
		panel_3.setBounds(136, 0, 733, 512);
		contentPane.add(panel_3);
		panel_3.setLayout(null);
		panel_3.setBackground(new Color(240, 230, 140));
		
		btnPreviousPage = new JButton("Previous Page");
		btnPreviousPage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea_2.setText("");
				lblSenderno.setText("");
				lblSubjectno.setText("");
				panel_2.setVisible(true);
				panel_3.setVisible(false);
				panel_1.setVisible(false);
			}
		});
		btnPreviousPage.setFont(new Font("Times New Roman", Font.PLAIN, 27));
		btnPreviousPage.setBounds(219, 458, 268, 43);
		panel_3.add(btnPreviousPage);
		
		lblSubjectno = new JLabel("Subject:(No)");
		lblSubjectno.setFont(new Font("Times New Roman", Font.PLAIN, 27));
		lblSubjectno.setBounds(43, 23, 606, 34);
		panel_3.add(lblSubjectno);
		
		textArea_2 = new TextArea();
		textArea_2.setBounds(27, 107, 657, 345);
		panel_3.add(textArea_2);
		
		lblSenderno = new JLabel("Sender:(No)");
		lblSenderno.setFont(new Font("Times New Roman", Font.PLAIN, 27));
		lblSenderno.setBounds(43, 61, 606, 34);
		panel_3.add(lblSenderno);
		
		panel_1 = new JPanel();
		panel_1.setBackground(new Color(240, 230, 140));
		panel_1.setBounds(152, 0, 717, 512);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		lblSubject = new JLabel("Subject:");
		lblSubject.setFont(new Font("Times New Roman", Font.PLAIN, 27));
		lblSubject.setBounds(24, 23, 103, 34);
		panel_1.add(lblSubject);
		
		lblRecipient = new JLabel("Recipient\r\n(\u591A\u4F4D\u6536\u4FE1\u8005\u4EE5\u5206\u865F\u5206\u9694):");
		lblRecipient.setFont(new Font("標楷體", Font.PLAIN, 27));
		lblRecipient.setBounds(24, 80, 457, 34);
		panel_1.add(lblRecipient);
		
		textField = new JTextField();
		textField.setBounds(131, 21, 527, 40);
		panel_1.add(textField);
		textField.setColumns(10);
		
		textArea = new TextArea();
		textArea.setBounds(24, 121, 634, 86);
		panel_1.add(textArea);
		
		JLabel lblContent = new JLabel("content:");
		lblContent.setFont(new Font("Times New Roman", Font.PLAIN, 27));
		lblContent.setBounds(24, 202, 103, 34);
		panel_1.add(lblContent);
		
		textArea_1 = new TextArea();
		textArea_1.setBounds(24, 240, 634, 226);
		panel_1.add(textArea_1);
		
		btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String subject=textField.getText();
				String receiver=textArea.getText();
				setMessage=receiver.split(";");
				mainproc(setMessage,subject);
				JOptionPane.showMessageDialog(null, "郵件已寄出", "發送成功!", JOptionPane.INFORMATION_MESSAGE);
				textArea_1.setText("");
				textArea.setText("");
				textField.setText("");
			}
		});
		btnSend.setFont(new Font("Times New Roman", Font.PLAIN, 27));
		btnSend.setBounds(439, 472, 130, 34);
		panel_1.add(btnSend);

		MakeList();
		
		panel_2 = new JPanel();
		panel_2.setBounds(152, 0, 717, 521);
		contentPane.add(panel_2);
		panel_2.setLayout(null);
		panel_2.setBackground(new Color(240, 230, 140));
		
		JButton btnReply = new JButton("reply");
		btnReply.setBackground(new Color(184, 134, 11));
		btnReply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea_1.setText("");
				int index=list.getSelectedIndex()+1;
				try {
					pop_out.print("RETR "+index+"\r\n");
					pop_out.flush();
					String res=pop_in.readLine();
					
					System.out.println(res);
					if(!("+OK".equals(res.substring(0, 3)))){
						pop.close();
						throw new RuntimeException(res);
					}
					
					boolean cont=true;
					String buf="";
					while(cont){
						buf=pop_in.readLine();
						if(buf.length()>7 && buf.substring(0,8).equalsIgnoreCase("subject:")) {
							textField.setText("RE:"+buf);
							continue;
						}
//						if(buf.length()>4 && buf.substring(0,5).equalsIgnoreCase("from:")) {
//							lblSenderno.setText(buf);
//							continue;
//						}
						if(".".equals(buf))
							break;
						System.out.println(buf);
						if(buf.startsWith("X-EsetId:")) {
							
							buf=pop_in.readLine();
							while(!(".".equals(buf))) {
								buf=pop_in.readLine();
								textArea_1.setText(textArea_1.getText()+buf+"\r\n");
								cont=false;
								if(buf.startsWith("Content-Type: text/plain;")) {
									cont=true;
									break;
								}
							}
						}
						if(buf.startsWith("Content-Type: text/plain;")){
							buf=pop_in.readLine();
							buf=pop_in.readLine();
							byte[] s=buf.getBytes();
							byte[] str=decoder.decode(buf);
							textArea_1.setText(new String(str,"UTF-8"));
							System.out.println(new String(str,"UTF-8"));
							
							continue;
						}
		
					}
					panel_2.setVisible(false);
					panel_3.setVisible(false);
					panel_1.setVisible(true);
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnReply.setFont(new Font("Times New Roman", Font.PLAIN, 27));
		btnReply.setBounds(377, 469, 129, 43);
		panel_2.add(btnReply);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.setBackground(new Color(184, 134, 11));
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index=list.getSelectedIndex()+1;
				try {
					getSingleLine("DELE "+index);
					//update();
					dlm.removeElementAt(index-1);
					//MakeList();
					list.setModel(dlm);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		btnDelete.setFont(new Font("Times New Roman", Font.PLAIN, 27));
		btnDelete.setBounds(552, 469, 129, 43);
		panel_2.add(btnDelete);
		
		textField_1 = new JTextField();
		textField_1.setBounds(24, 21, 533, 40);
		panel_2.add(textField_1);
		textField_1.setColumns(10);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.setBackground(new Color(184, 134, 11));
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int number=Integer.parseInt(textField_1.getText());
				try {
					pop_out.print("RETR "+number+"\r\n");
					pop_out.flush();
					String res=pop_in.readLine();
					
					System.out.println(res);
					if(!("+OK".equals(res.substring(0, 3)))){
						pop.close();
						throw new RuntimeException(res);
					}
					
					boolean cont=true;
					String buf="";
					while(cont){
						buf=pop_in.readLine();
						if(buf.length()>7 && buf.substring(0,8).equalsIgnoreCase("subject:")) {
							lblSubjectno.setText(buf);
							continue;
						}
						if(buf.length()>4 && buf.substring(0,5).equalsIgnoreCase("from:")) {
							lblSenderno.setText(buf);
							continue;
						}
						if(".".equals(buf))
							break;
						System.out.println(buf);
						if(buf.startsWith("X-EsetId:")) {
							
							buf=pop_in.readLine();
							while(!(".".equals(buf))) {
								buf=pop_in.readLine();
								textArea_2.setText(textArea_2.getText()+buf+"\r\n");
								cont=false;
								if(buf.startsWith("Content-Type: text/plain;")) {
									cont=true;
									break;
								}
							}
						}
						if(buf.startsWith("Content-Type: text/plain;")){
							buf=pop_in.readLine();
							buf=pop_in.readLine();
							byte[] s=buf.getBytes();
							byte[] str=decoder.decode(buf);
							textArea_2.setText(new String(str,"UTF-8"));
							System.out.println(new String(str,"UTF-8"));
							
							continue;
						}

		
					}
					panel_2.setVisible(false);
					panel_3.setVisible(true);
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnSearch.setFont(new Font("Times New Roman", Font.PLAIN, 27));
		btnSearch.setBounds(563, 20, 129, 43);
		panel_2.add(btnSearch);
		
		JButton btnContent = new JButton("content");
		btnContent.setBackground(new Color(184, 134, 11));
		btnContent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index=list.getSelectedIndex()+1;
				
				try {
					pop_out.print("RETR "+index+"\r\n");
					pop_out.flush();
					String res=pop_in.readLine();
					
					System.out.println(res);
					if(!("+OK".equals(res.substring(0, 3)))){
						pop.close();
						throw new RuntimeException(res);
					}
					
					boolean cont=true;
					String buf="";
					while(cont){
						buf=pop_in.readLine();
						if(buf.length()>7 && buf.substring(0,8).equalsIgnoreCase("subject:")) {
							lblSubjectno.setText(buf);
							continue;
						}
						if(buf.length()>4 && buf.substring(0,5).equalsIgnoreCase("from:")) {
							lblSenderno.setText(buf);
							continue;
						}
						if(".".equals(buf))
							break;
						System.out.println(buf);
						if(buf.startsWith("X-EsetId:")) {
							
							buf=pop_in.readLine();
							while(!(".".equals(buf))) {
								buf=pop_in.readLine();
								textArea_2.setText(textArea_2.getText()+buf+"\r\n");
								cont=false;
								if(buf.startsWith("Content-Type: text/plain;")) {
									cont=true;
									break;
								}
							}
						}
						if(buf.startsWith("Content-Type: text/plain;")){
							buf=pop_in.readLine();
							buf=pop_in.readLine();
							byte[] s=buf.getBytes();
							byte[] str=decoder.decode(buf);
							textArea_2.setText(new String(str,"UTF-8"));
							System.out.println(new String(str,"UTF-8"));
							
							continue;
						}

		
					}
					panel_2.setVisible(false);
					panel_3.setVisible(true);
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnContent.setFont(new Font("Times New Roman", Font.PLAIN, 27));
		btnContent.setBounds(206, 470, 129, 43);
		panel_2.add(btnContent);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 84, 705, 376);
		panel_2.add(scrollPane);
		
		
		list = new JList();
		scrollPane.setViewportView(list);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setFont(new Font("標楷體", Font.BOLD, 20));
		list.setModel(dlm);
		
		JButton btnSave = new JButton("Save");
		btnSave.setBackground(new Color(184, 134, 11));
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int index=list.getSelectedIndex()+1;
				try {
					pop_out.print("RETR "+index+"\r\n");
					pop_out.flush();
					String res=pop_in.readLine();
					
					System.out.println(res);
					if(!("+OK".equals(res.substring(0, 3)))){
						pop.close();
						throw new RuntimeException(res);
					}
					FileWriter fw = new FileWriter("saver.txt",true);
					boolean cont=true;
					boolean isSubject=false;
					String buf="";
					while(cont){
						buf=pop_in.readLine();
						if(buf.length()>7 && buf.substring(0,8).equalsIgnoreCase("subject:")) {
							textField.setText("RE:"+buf);
							//fw=new FileWriter(buf+".txt");
							isSubject=true;
							continue;
						}

						if(".".equals(buf))
							break;
						System.out.println(buf);
						if(buf.startsWith("X-EsetId:")) {
							//if(!isSubject)
								//fw=new FileWriter(buf+index+".txt");
							buf=pop_in.readLine();
							while(!(".".equals(buf))) {
								buf=pop_in.readLine();
								fw.write(buf+"\r\n");
								
								//textArea_1.setText(textArea_1.getText()+buf+"\r\n");
								cont=false;
								if(buf.startsWith("Content-Type: text/plain;")) {
									cont=true;
									break;
								}
							}
						}
						if(buf.startsWith("Content-Type: text/plain;")){
							buf=pop_in.readLine();
							buf=pop_in.readLine();
							byte[] s=buf.getBytes();
							byte[] str=decoder.decode(buf);
							//if(!isSubject)
								//fw=new FileWriter(buf+index+".txt");
							fw.write(buf+"\r\n");
							//textArea_1.setText(new String(str,"UTF-8"));
							System.out.println(new String(str,"UTF-8"));
							
							continue;
						}

		
					}
					fw.flush();
					fw.close();
					JOptionPane.showMessageDialog(null, "郵件已保存", "保存成功!", JOptionPane.INFORMATION_MESSAGE);
					
					
					
					
					
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
		btnSave.setFont(new Font("Times New Roman", Font.PLAIN, 27));
		btnSave.setBounds(55, 469, 121, 43);
		panel_2.add(btnSave);
		list.setVisible(true);
	}
	
	public void sendCommandAndResultCheck(Socket smtp,BufferedReader smtp_in,PrintWriter smtp_out,String command,int success_code)throws IOException {
		smtp_out.print(command+"\r\n");
		smtp_out.flush();
		System.out.println("send> "+command);
		resultCheck(smtp,smtp_in,smtp_out,success_code);
	}
	
	public void resultCheck(Socket smtp,BufferedReader smtp_in,PrintWriter smtp_out,int success_code) throws IOException{
		String res=smtp_in.readLine();
		System.out.println("recv> "+res);
		if(Integer.parseInt(res.substring(0,3))!=success_code) {
			smtp.close();
			throw new RuntimeException(res);
		}
	}
	
	public void send(String subject,String[] to,String msgs)throws IOException{
		Socket smtp=new Socket(server,SMTP_PORT);
		BufferedReader smtp_in=new BufferedReader(new InputStreamReader(smtp.getInputStream()));
		PrintWriter smtp_out=new PrintWriter(smtp.getOutputStream());
		resultCheck(smtp,smtp_in,smtp_out,220);
		
		String myname=InetAddress.getLocalHost().getHostName();
		sendCommandAndResultCheck(smtp,smtp_in,smtp_out,"HELO "+myname,250);
		sendCommandAndResultCheck(smtp,smtp_in,smtp_out,"MAIL FROM:"+my_email_addr,250);
		sendCommandAndResultCheck(smtp,smtp_in,smtp_out,"AUTH LOGIN", 334);
		Base64.Encoder encoder = Base64.getEncoder();
		byte[] id = encoder.encode(("s10459041@stumail.nutn.edu.tw").getBytes());
		String ID=new String(id);
		byte[] p = encoder.encode(("asdzxc231").getBytes());
		String P=new String(p);
		sendCommandAndResultCheck(smtp,smtp_in,smtp_out,ID,334);
		sendCommandAndResultCheck(smtp,smtp_in,smtp_out,P,235);
		for(int i=0;i<to.length;++i)
			sendCommandAndResultCheck(smtp,smtp_in,smtp_out,"RCPT TO:"+to[i],250);
		sendCommandAndResultCheck(smtp,smtp_in,smtp_out,"DATA",354);
		smtp_out.print("From:"+username+"\r\n");
		smtp_out.print("To:");
		for(int i=0;i<to.length-1;++i)
			smtp_out.print(to[i]+",");
		smtp_out.print(to[to.length-1]+"\r\n");
		smtp_out.print("Subject:"+subject+"\r\n");
		System.out.println("send>> "+"subject:"+subject);
		smtp_out.print("\r\n");
		smtp_out.print(msgs+"\r\n.");
		System.out.println("send> "+msgs);

		sendCommandAndResultCheck(smtp,smtp_in,smtp_out,"\r\n",250);

		smtp_out.flush();
		String res=smtp_in.readLine();
		System.out.println(res);
		//sendCommandAndResultCheck(smtp,smtp_in,smtp_out,"QUIT",221);
		smtp_out.flush();
		
		smtp.close();
	}
	

	
	public String setMsgs() {
		String buf="";
		buf=textArea_1.getText();
		return buf;
	}
	
	public void mainproc(String[] args,String subject) {
		
		Vector to_list=new Vector();
		for(int i=0;i<args.length;++i) {
				to_list.addElement(args[i]);

		}
		if(to_list.size()>0) {
			try {
				String[] to=new String[to_list.size()];
				to_list.copyInto(to);
				//setAddress();
				String msgs=setMsgs();
				send(subject,to,msgs);
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
		else {
			JOptionPane.showMessageDialog(null, "錯誤!!", "無收件人郵件位址!!", JOptionPane.ERROR_MESSAGE );
			System.out.println("無收件人");
		}
	}
	
	public static boolean getSingleLine(String command) throws IOException{
		pop_out.print(command+"\r\n");
		pop_out.flush();
		System.out.println(command);
		String res =pop_in.readLine();
		System.out.println(res);
		if(!("+OK".equals(res.substring(0, 3)))){
			return false;
			//pop.close();
			//throw new RuntimeException(res);
		}
		return true;
	}
	public static void MakeList() throws IOException {
		dlm=new DefaultListModel();
		pop_out.print("LIST"+"\r\n");
		pop_out.flush();
		String res=pop_in.readLine();
		
		System.out.println(res);
		if(!("+OK".equals(res.substring(0, 3)))){
			pop.close();
			throw new RuntimeException(res);
		}
		
		String buf="";
		while(true){
			buf=pop_in.readLine();
			if(".".equals(buf))
				break;
			System.out.println(buf);
			dlm.addElement(buf);
			
		}
	}
	
	public void update() throws IOException{
		getSingleLine("QUIT");
		pop.close();
		System.exit(0);
	}
}
