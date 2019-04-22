package communication;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
import crypto.Crypto;
import crypto.RSACrypto;

public class Client extends Thread{

	private RSACrypto rsa;
	private Socket s;
	private int port = 12345;
	private Patient patient;
	private int c=0;
	
	public Client(String ip, File medkey, ArrayList<String> medData, int patientId) throws Exception {
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
					medicalData[c] += sp[i]+"::";
				}
				//System.out.println(doctorID[c]);
				//System.out.println(medicalData[c]);
				c++;
			}catch (Exception e) {
				// TODO: handle exception
			}
		});
		patient = new Patient(doctorID,medicalData,patientId);
		this.start();
	}

	public Client(File medkey, ArrayList<String> medData, int patientId) throws Exception  {
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
					medicalData[c] += sp[i]+"::";
				}
				//System.out.println(doctorID[c]);
				//System.out.println(medicalData[c]);
				c++;
			}catch (Exception e) {
				// TODO: handle exception
			}
		});
		patient = new Patient(doctorID,medicalData,patientId);
	}

	@Override
	public void run() {
		try {
			Crypto c = new Crypto();
			String skey = c.getSaltString();
			AESCrypto aes = new AESCrypto(skey);
			ByteArrayOutputStream oba = aes.obectToByte(patient);
			byte[] message =aes.encrypt(new String(oba.toByteArray()));
			byte[] key = rsa.encrypt(skey,  rsa.getPublicKey());
			Message m = new Message(message,key,aes.getIV());
			ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
			oos.writeObject(m);
			s.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void write(File out) throws Exception {
		Crypto c = new Crypto();
		String skey = c.getSaltString();
		AESCrypto aes = new AESCrypto(skey);
		ByteArrayOutputStream oba = aes.obectToByte(patient);
		byte[] message =aes.encrypt(new String(oba.toByteArray()));
		byte[] key = rsa.encrypt(skey,  rsa.getPublicKey());
		Message m = new Message(message,key,aes.getIV());
		File file = new File(out.getAbsolutePath()+"/exportData.light");
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
		oos.writeObject(m);
		oos.close();
	}	
}
