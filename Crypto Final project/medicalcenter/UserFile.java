package medicalcenter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Random;

import crypto.AESCrypto;
import crypto.Crypto;
import crypto.RSACrypto;
import javafx.scene.control.ComboBox;

public class UserFile {

	private File users = new File("file/users.shadow");
	private File patients = new File("file/patients.shadow");

	public boolean valid(String name, String pass) {
		boolean valid = false;
		try {
			FileReader fr = new FileReader(users);
			BufferedReader br = new BufferedReader(fr);
			Crypto crypto = new Crypto();
			String line;
			while ( (line = br.readLine()) != null) {
				String[] split = line.split("::");
				if (name.equals(split[0])) {
					if (crypto.compareHashSHA512(split[1], pass)) {
						valid = true;
					}
				}
			}
			br.close();

		} catch (Exception e) {
			e.printStackTrace();
			valid = false;
		}
		return valid;
	}

	public Account account(String name, String pass) {
		Account doctor = null;
		try {
			FileReader fr = new FileReader(users);
			BufferedReader br = new BufferedReader(fr);
			Crypto crypto = new Crypto();
			String line;
			while ( (line = br.readLine()) != null) {
				String[] split = line.split("::");
				if (name.equals(split[0])) {
					if (crypto.compareHashSHA512(split[1], pass)) {
						doctor = setUpAccount(name,pass,Integer.parseInt( split[2]));
					}
				}
			}
			br.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return doctor;
	}

	private Account setUpAccount(String name, String pass, int id) throws Exception {
		AESCrypto aes = new AESCrypto(pass);

		File userInfo = new File("file/doctor "+id+".shadow");
		FileReader fr = new FileReader(userInfo);
		BufferedReader br = new BufferedReader(fr);


		String[] split = new String[3];

		split[0] =  br.readLine();
		split[1] =  br.readLine();
		split[2] =  br.readLine();

		br.close();

		KeyFactory kf = KeyFactory.getInstance("RSA");

		Base64.Decoder decoder = Base64.getDecoder();

		byte[] IV  = decoder.decode(split[0]); 
		byte[] csk = decoder.decode(split[1]); 
		byte[] pk = decoder.decode(split[2]);

		AESCrypto aesSecond = new AESCrypto(pass);
		byte[] sk = aesSecond.decrypt(csk,IV);
		sk = decoder.decode(sk);

		PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(sk);
		PrivateKey privKey = kf.generatePrivate(keySpecPKCS8);

		X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(pk);
		PublicKey pubKey = kf.generatePublic(keySpecX509);

		RSACrypto rsa = new RSACrypto(privKey, pubKey);

		return new Account(name,aes,rsa,id);
	}

	public boolean unique(int x) {
		boolean b = true;
		try {
			FileReader fr = new FileReader(users);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while ( (line = br.readLine()) != null) {
				String[] split = line.split("::");
				try {
					int y = Integer.parseInt(split[2]);				
					if (x == y ) {
						b = false;
					}
				}catch (Exception e) {
					// TODO: handle exception
				}
			}
			br.close();

		} catch (Exception e) { 
			e.printStackTrace();
		}
		return b;
	}

	public boolean makeNewDoctorAccount(String name, String pass, int x) {
		boolean b = false;
		Crypto crypto = new Crypto();
		try {
			pass = crypto.hashSHA512(pass);
			FileWriter fw = new FileWriter(users,true);
			fw.write(name+"::"+pass+"::"+x+"\n");
			fw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			b = true;
		}
		return b;
	}

	public boolean unique(String name, String pass, int x) {
		boolean b = true;
		try {
			FileReader fr = new FileReader(users);
			BufferedReader br = new BufferedReader(fr);
			Crypto crypto = new Crypto();
			String line;
			pass = crypto.hashSHA512(pass);
			while ( (line = br.readLine()) != null) {
				String[] split = line.split("::");
				try {
					int y = Integer.parseInt(split[2]);				
					if (x == y ) {
						b = false;
					}
				}catch (Exception e) {
					// TODO: handle exception
				}
				if (split[0].equals(name) || split[1].equals(pass)) {
					b = false;
				}
			}
			br.close();

		} catch (Exception e) { 
			e.printStackTrace();
		}		
		return b;
	}

	public void generateAccountInfo(int id, String pass) throws Exception {
		Crypto c = new Crypto();

		RSACrypto rsa = new RSACrypto();

		Base64.Encoder encoder = Base64.getEncoder();

		AESCrypto aesFirst = new AESCrypto(pass);

		String iv = encoder.encodeToString(aesFirst.getIV());
		String pri = encoder.encodeToString(rsa.getPrivateKey().getEncoded());
		String pub = encoder.encodeToString(rsa.getPublicKey().getEncoded());


		pri = encoder.encodeToString(	aesFirst.encrypt(pri)	);	
		File f = new File("file/doctor "+id+".shadow");
		FileWriter fw = new FileWriter(f);

		fw.write(iv+"\n");
		fw.write(pri+"\n");
		fw.write(pub + "\n");

		fw.close();

	}

	public void loadDoctors(ComboBox<String> namePLUSid) {

		try {
			FileReader fr = new FileReader(users);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while ( (line = br.readLine()) != null) {
				String[] split = line.split("::");
				try {
					if(Integer.parseInt(split[2]) >= 1000) {
						namePLUSid.getItems().add(new String(split[0]+" :: "+split[2]));
					}
				}catch (Exception e) {
					// TODO: handle exception
				}
			}
			br.close();

		} catch (Exception e) { 
			e.printStackTrace();
		}		


	}

	public void makeNewPatient(String name, String[] doctorid) {

		try {
			int patientId = unqueID();
			FileWriter fw = new FileWriter(patients,true);
			BufferedWriter bw = new BufferedWriter(fw);
			// name id doc ids
			String tmp = "";
			int start = 0;
			if ( doctorid[0].equals("") ) {
				start =1;
			}
			for (int i = start; i < doctorid.length-1; i++) {
				tmp = doctorid[i] + "::";  
			}
			tmp += doctorid[doctorid.length-1];

			bw.write(name+"::"+patientId+"::"+tmp);
			bw.newLine();

			bw.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	private int unqueID() {
		int id = 0;
		try {
			FileReader fr = new FileReader(patients);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while ( (line = br.readLine()) != null) {
				id ++;
			}
			br.close();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return id;


	}

	public void exportAdminPubKey(File exportKey) {
		try {
			File userInfo = new File("file/doctor 0.shadow");
			FileReader fr = new FileReader(userInfo);
			BufferedReader br = new BufferedReader(fr);


			String[] split = new String[3];

			split[0] =  br.readLine();
			split[1] =  br.readLine();
			split[2] =  br.readLine();

			br.close();

			File export = new File(exportKey.getPath() + "/MedicalCenterKey.key");
			FileWriter fw = new FileWriter(export);
			fw.write(split[2]);
			fw.close();
		}catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
	public void exportDoctorPubKey(int docoterId, File exportKey) {
		try {
			File userInfo = new File("file/doctor "+docoterId+".shadow");
			FileReader fr = new FileReader(userInfo);
			BufferedReader br = new BufferedReader(fr);
			String[] split = new String[3];
			split[0] =  br.readLine();
			split[1] =  br.readLine();
			split[2] =  br.readLine();

			br.close();

			File export = new File(exportKey.getPath() + "/DoctorKey.key");
			FileWriter fw = new FileWriter(export);
			fw.write(split[2]);
			fw.close();
		}catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
}
