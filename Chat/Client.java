package Chat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Client implements ActionListener {
	public final static String HOST_NAME = "localhost";
	public final static int PORT = 9000;
	
	public JFrame frame;
	public JPanel conversationPanel, listPanel, container, wrapperInput;
	public JTextArea messages, friends;
	public JTextField message;
	public String name;
	public JButton send;
	public DataOutputStream os;
	private JLabel chatLabel, headerLabel;
	
	public Client(String username, String ip, int PORT) {
		createUI();
		try {
			System.out.println(ip + " " + PORT);
			Socket socket = new Socket(HOST_NAME, PORT);
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			DataInputStream is = new DataInputStream(socket.getInputStream());
			os = new DataOutputStream(socket.getOutputStream());
			os.writeUTF(username);

			Thread readMessage = new Thread(new Runnable() {
				@Override
				public void run() {
					while (true) {
						try {
							String msg = is.readUTF();
							messages.append(msg + "\n");
						} catch (Exception e) {
							break;
						}
					}
				}
			});

			readMessage.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void createUI() {
		this.frame = new JFrame("Chat Room!");
		this.frame.setSize(1400, 750);
		this.frame.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setLocationRelativeTo(null);
		this.frame.setResizable(false);
		this.frame.setUndecorated(false);

		this.listPanel = new JPanel(new BorderLayout(4, 4));
		this.listPanel.setPreferredSize(new Dimension(314, 680));
		this.listPanel.setBackground(Color.WHITE);

		this.conversationPanel = new JPanel(new BorderLayout(4, 4));
		this.conversationPanel.setPreferredSize(new Dimension(952, 680));

		this.wrapperInput = new JPanel(new BorderLayout(0, 4));

		this.headerLabel = new JLabel("TIN NHẮN");
		this.headerLabel.setForeground(Color.BLACK);

		this.messages = new JTextArea();
		this.messages.setBackground(Color.WHITE);
		this.messages.setEditable(false);
		this.conversationPanel.add(messages, BorderLayout.CENTER);

		this.message = new JTextField();
		this.wrapperInput.add(message, BorderLayout.CENTER);
		this.send = new JButton("Gửi tin nhắn");
		this.send.addActionListener(this);
		this.wrapperInput.add(send, BorderLayout.EAST);
		this.conversationPanel.add(headerLabel, BorderLayout.NORTH);
		this.conversationPanel.add(wrapperInput, BorderLayout.SOUTH);

		this.chatLabel = new JLabel("ĐANG HOẠT ĐỘNG");
		this.listPanel.add(chatLabel, BorderLayout.NORTH);

		this.friends = new JTextArea();
		this.friends.setBackground(Color.WHITE);
		this.friends.setEditable(false);

		this.listPanel.add(friends, BorderLayout.CENTER);
		this.frame.add(this.listPanel);
		this.frame.add(this.conversationPanel);

		this.frame.setVisible(true);
	}
//	public static void main(String[] args) throws IOException {
//		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//		Socket socket = new Socket(HOST_NAME, PORT);
//		DataInputStream is = new DataInputStream(socket.getInputStream());
//		DataOutputStream os = new DataOutputStream(socket.getOutputStream());
//		System.out.println("Nhập tên đi bro: ");
//		os.writeUTF(reader.readLine());
//		Thread sendMessage = new Thread(new Runnable() {
//			@Override
//			public void run() {
//				while (true) {
//					try {
//						String msg = reader.readLine();
//						os.writeUTF(msg);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		});
//		
//		Thread readMessage = new Thread(new Runnable() {
//			@Override
//			public void run() {
//				while (true) {
//					try {
//						String msg = is.readUTF();
//						System.out.println(msg);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		});
//		
//		sendMessage.start();
//		readMessage.start();
//	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == send) {
			try {
				os.writeUTF(message.getText());
				message.setText("");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
	}
}
