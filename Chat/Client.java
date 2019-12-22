package Chat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Client extends KeyAdapter implements ActionListener {
	public final static String HOST_NAME = "localhost";
	public final static int PORT = 9000;
	private String username = null;
	private List<String> users;
	 
	public JFrame frame;
	public JPanel conversationPanel, listPanel, container, wrapperInput;
	public JTextArea messages, friends;
	public JTextField message;
	public String name;
	public JButton send;
	public DataOutputStream os;
	public DataInputStream is;
	private JLabel chatLabel, headerLabel;
	
	public Client(String username, String ip, int PORT) {
		createUI();
		message.addKeyListener(this);
		try {
			System.out.println(ip + " " + PORT);
			Socket socket = new Socket(HOST_NAME, PORT);
			is = new DataInputStream(socket.getInputStream());
			os = new DataOutputStream(socket.getOutputStream());
			os.writeUTF(username);
			Thread readMessage = new Thread(new Runnable() {
				@Override
				public void run() {
					while (true) {
						try {
							String msg = is.readUTF();
							if (msg.contains("vừa tham gia vào phòng")) updateListFriend();
							else if (msg.contains("DISCONNECTED")) {
								updateListFriend();
								if (msg.split(":")[1].equals(username)) {
									socket.close();
									System.exit(0);
								}
							}
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
	
	private void updateListFriend () throws IOException {
		users = new ArrayList<String>(Arrays.asList(is.readUTF().split("\n")));
		if (this.username == null) this.username = users.get(users.size() - 1);
		String results = "";
		for (String name : users) {
			if (name.equals(this.username)) results += "(Tôi) " + this.username + "\n";
			else results += name + "\n";
		}
		friends.setText(results);
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
	@Override
    public void keyPressed(KeyEvent event) {
		char ch = event.getKeyChar();
		if (ch == '\n' ) {
			try {
				os.writeUTF(message.getText());
				message.setText("");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    }
}
