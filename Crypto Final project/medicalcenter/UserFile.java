package medicalcenter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import crypto.AESCrypto;
import crypto.Crypto;
import crypto.RSACrypto;

public class UserFile {

	private File users = new File("file/users.shadow");

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
						doctor = setUpAccount(name,pass,split[2]);
					}
				}
			}
			br.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return doctor;
	}

	private Account setUpAccount(String name, String pass, String id) throws Exception {
		AESCrypto aes = new AESCrypto(pass);
		
		File userInfo = new File("file/"+name+".shadow");
		
		FileReader filereader = new FileReader(userInfo);
		BufferedReader bufferRead = new BufferedReader(filereader);
		
		Base64.Decoder decoder = Base64.getDecoder();
		byte[] privateKey = decoder.decode(bufferRead.readLine());
		byte[] publicKey = decoder.decode(bufferRead.readLine());;
		
        KeyFactory kf = KeyFactory.getInstance("RSA");
		
		PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(privateKey);
        PrivateKey privKey = kf.generatePrivate(keySpecPKCS8);

        X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(publicKey);
        PublicKey pubKey = kf.generatePublic(keySpecX509);

		
		RSACrypto rsa = new RSACrypto(privKey, pubKey);
		
		bufferRead.close();
		
		return new Account(name,aes,rsa,id);
	}


}
