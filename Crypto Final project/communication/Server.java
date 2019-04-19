package communication;

import java.io.IOException;
import java.net.ServerSocket;

public class Server extends Thread{

	private int port = 12345;
	private ServerSocket serverSocket;
	private boolean running = true;
	public Server() {
		setName("Server");
		start();
	}

	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(port);
			while(running) {
				new ServerClient(serverSocket.accept());
			}
			serverSocket.close();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}
	public void end() {
		running = false;
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		new Server();
	}

}
