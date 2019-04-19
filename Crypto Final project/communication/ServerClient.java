package communication;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Base64;

import crypto.AESCrypto;
import crypto.RSACrypto;

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
			Message m = (Message) ois.readObject();

			RSACrypto rsa = new RSACrypto(null,null);
			BufferedReader fr = new BufferedReader(new FileReader("file/doctor 0.shadow"));
			
			String line1 = fr.readLine();
			String line2 = fr.readLine();
			String line3 = fr.readLine();

			Base64.Decoder decoder = Base64.getDecoder();

			AESCrypto aes = new AESCrypto("toor");

			byte[] v = aes.decrypt(decoder.decode(line2), decoder.decode(line1));
			rsa.loadSercetKey(new String(v));

			String key =  rsa.decrypt(m.getKey(), rsa.getPrivateKey());
			AESCrypto messageAES = new AESCrypto(key);

			byte[] decMessage = messageAES.decrypt(m.getMessage(), m.getIv());
			ByteArrayOutputStream bnm = new ByteArrayOutputStream();
			bnm.write(decMessage);

			Patient p = (Patient) aes.byteToObject(bnm);

			for (int i = 0; i < p.getDoctorID().length; i++) {
				System.out.println(p.getDoctorID()[i]);

			}
			for (int i = 0; i < p.getMedicalData().length; i++) {
				System.out.println(p.getMedicalData()[i]);
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
