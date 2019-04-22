package communication;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.NoSuchPaddingException;

import crypto.AESCrypto;
import crypto.RSACrypto;
import medicalcenter.UserFile;

public class ServerClient extends Thread{

	private Socket socket;
	public ServerClient(Socket accept) {
		socket = accept;
		start();
	}

	@Override
	public void run() {
		try {
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			UserFile uf = new UserFile();
			Patient p = uf.extractPatient(ois);
			uf.processPatient(p);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	
}
