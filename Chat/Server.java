package Chat;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	public final static int SERVER_PORT = 9000;
	public static void main(String[] args) throws IOException {
		
		//Open Port
		ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
		System.out.println("Server is running on port 9000");
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		while(true) {
			//Accept socket connect to
			Socket socket = serverSocket.accept();
			if (socket.isConnected()) {
				ConnectionThread connection = new ConnectionThread(socket);
				connection.start();
			}
		}
	}
}
