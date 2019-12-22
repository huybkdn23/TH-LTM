package Chat;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Login extends JFrame implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField tfUser = null;
	public Login(String title) {
		super(title);
		JPanel p1 = new JPanel();
		JPanel p2 = new JPanel();
		JPanel p3 = new JPanel();
		JPanel p4 = new JPanel();
		JLabel lbTitle = new JLabel("Phòng Chat");
		lbTitle.setFont(new Font(lbTitle.getName(), Font.PLAIN, 28));
		JLabel lbUser = new JLabel("Tên Đăng Nhập: ");
		tfUser = new JTextField(30);
		JButton btnLogin = new JButton("Login");
		JButton btnReset = new JButton("Reset");
		JButton btnExit = new JButton("Exit");
		btnLogin.addActionListener(this);
		btnReset.addActionListener(this);
		btnExit.addActionListener(this);
		p1.add(lbTitle);
		p2.add(lbUser);
		p2.add(tfUser);
		p4.add(btnLogin);
		p4.add(btnReset);
		p4.add(btnExit);
		add(p1);
		add(p2);
		add(p3);
		add(p4);
		setSize(450, 300);
		setLocationRelativeTo(null);
		setLayout(new GridLayout(5, 1));
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args) {
		new Login("ĐĂNG NHẬP");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String btn = e.getActionCommand();
		if (btn.equals("Login")) {
			String user = tfUser.getText();
			String ip = JOptionPane.showInputDialog("Nhập vào IP máy chủ", "localhost");
			int port = Integer.parseInt(JOptionPane.showInputDialog("Nhập vào port máy chủ", "9000"));
			new Client(user, ip, port);
			dispose();
		}
		if (btn.equals("Reset")) {
			tfUser.setText("");
		}
		if (btn.equals("Exit")) {
			System.exit(0);
		}
	}
}
