package medicalcenter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Random;
import java.util.Base64.Decoder;

import javax.crypto.NoSuchPaddingException;

import communication.Message;
import communication.Patient;
import crypto.ABAC;
import crypto.AESCrypto;
import crypto.Crypto;
import crypto.RSACrypto;
import cup.User;
import javafx.scene.control.ComboBox;

public class UserFile {

	private File users = new File("file/users.shadow");
	private File patients = new File("file/patients.light");

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
	public Patient extractPatient(ObjectInputStream ois) throws Exception {
		Message m = (Message) ois.readObject();

		RSACrypto rsa = new RSACrypto(null,null);

		BufferedReader fr = new BufferedReader(new FileReader("file/doctor 0.shadow"));

		String line1 = fr.readLine();
		String line2 = fr.readLine();
		String line3 = fr.readLine();

		fr.close();

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
		return p;
	}

	public void processPatient(Patient p) {
		ABAC abac = new ABAC();
		try {
			FileReader fr = new FileReader(users);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while ( (line = br.readLine()) != null) {
				String[] split = line.split("::");
				try {

					int id = Integer.parseInt(split[2]);
					Doctor doc = new Doctor(id,doctorPubKey(id));
					if(abac.valid(doc, p)) {
						updatepatientFile(p,doc);
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private PublicKey doctorPubKey(int docoterId) {
		PublicKey key= null;
		try {
			File userInfo = new File("file/doctor "+docoterId+".shadow");
			FileReader fr = new FileReader(userInfo);
			BufferedReader br = new BufferedReader(fr);
			String[] split = new String[3];
			split[0] =  br.readLine();
			split[1] =  br.readLine();
			split[2] =  br.readLine();

			br.close();				
			KeyFactory kf = KeyFactory.getInstance("RSA");

			Base64.Decoder decoder = Base64.getDecoder();

			byte[] pk = decoder.decode(split[2]);

			X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(pk);
			key = kf.generatePublic(keySpecX509);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return key;
	}

	private void updatepatientFile(Patient p, Doctor doc) {
		try {
			File file = new File("file/patient "+ p.getId()+".light");
			FileWriter fw = new FileWriter(file,true);
			Crypto c = new Crypto();
			String key = c.getSaltString();
			AESCrypto aes = new AESCrypto(key);
			for (int i = 0; i < p.getMedicalData().length; i++) {
				Base64.Encoder encoder = Base64.getEncoder();
				byte[] d  = aes.encrypt(p.getId()+"::"+p.getMedicalData()[i]);
				byte[] hiddenkey = doc.getRsa().encrypt(key, doc.getRsa().getPublicKey());
				String iv = encoder.encodeToString(aes.getIV());
				String enKey = encoder.encodeToString(hiddenkey);	
				String data = encoder.encodeToString(d );
				fw.write(iv+"::"+enKey+"::"+data+"\n");
			}
			fw.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	public ArrayList<MedicalData> loadpatientData(Account doc, int patient){
		ArrayList<MedicalData> medData = new ArrayList<MedicalData>();
		try {
			File file = new File("file/patient "+ patient+".light");
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);

			String line;
			while ((line = br.readLine()) != null) {
				String[] spilt = line.split("::");

				Base64.Decoder decoder = Base64.getDecoder();
				byte[] iv = decoder.decode(spilt[0]);
				byte[] enkey = decoder.decode(spilt[1]);
				byte[] data = decoder.decode(spilt[2]);

				String key = doc.getRsa().decrypt(enkey, doc.getRsa().getPrivateKey());
				AESCrypto aes = new AESCrypto(key);

				byte[] medicaldata = aes.decrypt(data, iv);
				try {
					medData.add(new MedicalData(medicaldata));
				}catch (Exception e) {
					e.printStackTrace();
				}
			}

			br.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return medData;
	}
	public boolean CupGUI(String name, String pass) {
		boolean valid = false;
		try {
			FileReader fr = new FileReader(new File("file/CupUsers.shadow"));
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

	public User CupGUIusers(String name, String pass) {
		User a = null;
		try {
			FileReader fr = new FileReader(new File("file/CupUsers.shadow"));
			BufferedReader br = new BufferedReader(fr);
			Crypto crypto = new Crypto();
			String line;
			while ( (line = br.readLine()) != null) {
				String[] split = line.split("::");
				if (name.equals(split[0])) {
					if (crypto.compareHashSHA512(split[1], pass)) {
						AESCrypto aes = new AESCrypto(pass);
						a = new User(name,aes,split[2]);
					}
				}
			}
			br.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return a;
	}

	public void CupGUInewUser(String name, String pass, String id) {
		try {
			File file = new File("file/CupUsers.shadow");
			Crypto crypto = new Crypto();
			try {
				pass = crypto.hashSHA512(pass);
				FileWriter fw = new FileWriter(file,true);
				fw.write(name+"::"+pass+"::"+id+"\n");
				fw.close();
			} catch (Exception e) {

			}

		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ComboBox<String> avaiablePatients(int docId) {
		ComboBox<String> comboBox = new ComboBox<String>();
		Doctor doc = new Doctor(docId, null);
		ABAC abac = new ABAC();
		try {
			FileReader fr = new FileReader(patients);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while ( (line = br.readLine()) != null) {

				try {
					String[] sp = line.split("::");
					String name = sp[0];
					int id = Integer.parseInt(sp[1]);
					int[] doctorID = new int[sp.length-2];
					for (int i = 0; i < doctorID.length; i++) {
						doctorID[i] =  Integer.parseInt(sp[i+2]);
					}
					Patient p = new Patient(doctorID, null, id);
					if (abac.valid(doc, p)) {
						comboBox.getItems().add(name + "_"+ id);
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
			br.close();

		}catch (Exception e) {
			e.printStackTrace();
		}
		return comboBox;
	}

}
