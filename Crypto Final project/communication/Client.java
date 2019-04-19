package communication;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Random;

import javax.crypto.spec.SecretKeySpec;

import crypto.AESCrypto;
import crypto.RSACrypto;

public class Client extends Thread{

	private RSACrypto rsa;
	private Socket s;
	private int port = 12345;
	private Patient patient;
	private int c=0;
	public Client(String ip, File medkey, ArrayList<String> medData) throws Exception {
		s= new Socket(ip, port);
		rsa = new RSACrypto(null, null);
		BufferedReader fr = new BufferedReader(new FileReader(medkey));
		String line = fr.readLine();
		rsa.loadPubKey(line);
		fr.close();

		String[] medicalData = new String[medData.size()];
		int[] doctorID = new int[medData.size()];
		medData.forEach(data ->{
			try {
				String[] sp = data.split("::");
				doctorID[c] = Integer.parseInt(sp[0]);
				medicalData[c] = "";
				for (int i = 1; i < sp.length; i++) {
					medicalData[c] += sp[i];
				}
				//System.out.println(doctorID[c]);
				//System.out.println(medicalData[c]);
				c++;
			}catch (Exception e) {
				// TODO: handle exception
			}
		});
		patient = new Patient(doctorID , medicalData );
		this.start();
	}

	@Override
	public void run() {
		
		try {
			String skey = getSaltString();
			AESCrypto aes = new AESCrypto(skey);
			ByteArrayOutputStream oba = aes.obectToByte(patient);
			byte[] message =aes.encrypt(new String(oba.toByteArray()));
			byte[] key = rsa.encrypt(skey,  rsa.getPublicKey());
			Message m = new Message(message,key,aes.getIV());
			ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
			oos.writeObject(m);
			s.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) {
		try {
			ServerSocket ss = new ServerSocket(12345);
			Socket s = ss.accept();
			
				System.out.println(s.getInputStream().available());
				ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
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
				
			/*	String sd = rsa.decrypt(v, rsa.getPrivateKey());
				
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				
				baos.write(sd.getBytes());
				
				
				Patient p = (Patient) rsa.byteToObject(baos);
				
*/
			ss.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 100) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }
}
