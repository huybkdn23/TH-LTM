package Chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Hashtable;

public class ConnectionThread extends Thread{
	private Socket client;
	private DataInputStream is;
	private DataOutputStream os;
	private String username;
	private String received;
	private static int connectionNumber;
	private static Hashtable<Socket, ConnectionThread> allMembers = new Hashtable<Socket, ConnectionThread>();
	
	
	public String getUsername() {
		return username;
	}

	public ConnectionThread(Socket client) {
		this.client = client;
		allMembers.put(client, this);
		connectionNumber = allMembers.size();
	}
	
	public void run() {
		try {
			is = new DataInputStream(client.getInputStream());
			os = new DataOutputStream(client.getOutputStream());
			username = is.readUTF();
			System.out.println("Name " + username);
			sendMessageToAllMembers(username + " vừa tham gia vào phòng.");
			while (true) {
				received = is.readUTF();
				System.out.println("received " + received);
				sendMessageToAllMembers(username + ": " + received);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void sendMessageToAllMembers(String message) {
		synchronized (allMembers) {
			for (Enumeration<?> e = allMembers.elements(); e.hasMoreElements();) {
				ConnectionThread ct = (ConnectionThread) e.nextElement();
				try {
					if (!ct.getUsername().equals(this.username)) ct.os.writeUTF(message);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
	}
}
