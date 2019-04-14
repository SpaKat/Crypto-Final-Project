package onlyfortesting;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import crypto.AESCrypto;
import crypto.RSACrypto;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class ABAC_TEST {

	public static void main(String[] args) {

		/*
		Enforcer e = new Enforcer("onlyfortesting/abac_model.conf"); // ,"onlyfortesting/policy_test.csv"
		TestFunction testFunction = new TestFunction();
		e.addFunction("Boi", testFunction);
		Sub s = new Sub();
		boolean b = e.enforce(s,"data");
        //Enforcer e = new Enforcer("examples/rbac_model.conf", "examples/rbac_policy.csv");
		System.out.println("Yosh: ------> " + b);
	//	e.getAllActions().forEach(System.out::println);
		Crypto c = new Crypto();
			ABAC g = new ABAC();
			Account a = new Account("S");
			a.setId("s");
			Account f = new Account("S");
			f.setId("s");
			boolean b1 = g.valid(a,f);
	        //Enforcer e = new Enforcer("examples/rbac_model.conf", "examples/rbac_policy.csv");

			System.out.println("Yosh1: ------> " + b1);
		 
		//	e.getAllActions().forEach(System.out::println);
		//Crypto c = new Crypto();
		/*
		RSACrypto rsa = new RSACrypto();
		try {
			rsa.generateKeyPair();
			String s = "kissy face";
			byte[] v = rsa.encrypt(s, rsa.getPublicKey());
			String d = rsa.decrypt(v,rsa.getPrivateKey());
			for (int i = 0; i < v.length; i++) {
				System.out.print(v[i]);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		try{
			String f = "admin";
			String t = c.hashSHA512(f);
			System.out.println(t);
			System.out.println();
			System.out.println(c.compareHashSHA512(t, "toor"));
			
			
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		*/
		
		
		/*
		try {
			

			RSACrypto rsa = new RSACrypto();
			
			//Base64.Encoder encoder = Base64.getEncoder();
			
			String pri = new String(rsa.getPrivateKey().getEncoded());
			String pub = new String(rsa.getPublicKey().getEncoded());
			
			System.out.println(rsa.getPrivateKey().getEncoded());
			System.out.println(rsa.getPublicKey().getEncoded());
			
			AESCrypto aesFirst = new AESCrypto("toor");
			Cipher cipherfirst = aesFirst.encryptCipher(aesFirst.getKey(), aesFirst.getCipherTypeAES());
			
			
			File f = new File("t.test");
			FileWriter fw = new FileWriter(f);
			
			//bw.write(encoder.encodeToString(cipherfirst.getIV())+"::");
			fw.write(new String(cipherfirst.getIV())+"::");
			System.out.println(cipherfirst.getIV().length);
			fw.flush();
			//bw.write(encoder.encodeToString(aesFirst.encryptString(pri, cipherfirst))+"::");
			fw.write(new String(aesFirst.encryptString(pri, cipherfirst))+"::");
			fw.flush();
			//bw.write(encoder.encodeToString(aesFirst.encryptString(pub, cipherfirst)));
			fw.write(new String(aesFirst.encryptString(pub, cipherfirst)));
			fw.flush();
			fw.close();
			
			
			
			AESCrypto aesSecond = new AESCrypto("toor");
			
			
			
			FileReader filereader = new FileReader(f);
			BufferedReader bufferRead = new BufferedReader(filereader);
			
		//	Base64.Decoder decoder = Base64.getDecoder();
			String[] split = bufferRead.readLine().split("::");
			Cipher cipherSecond = aesSecond.decryptCipher(aesSecond.getKey(),cipherfirst.getIV(), aesSecond.getCipherTypeAES());
			
			ByteArrayOutputStream privateKey = aesSecond.decryptString(aesFirst.encryptString(pri, cipherfirst), cipherSecond);
			ByteArrayOutputStream publicKey = aesSecond.decryptString(aesFirst.encryptString(pub, cipherfirst), cipherSecond);
			
	        KeyFactory kf = KeyFactory.getInstance("RSA");
			
	        
			PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(privateKey.toByteArray());
	        PrivateKey privKey = kf.generatePrivate(keySpecPKCS8);

	        X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(publicKey.toByteArray());
	        PublicKey pubKey = kf.generatePublic(keySpecX509);
			// name
			// private key
			// public key
	        bufferRead.close();
			
			System.out.println(rsa.getPrivateKey().equals(privKey));
			System.out.println(rsa.getPublicKey().equals(pubKey));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		try {
			RSACrypto rsa = new RSACrypto();
			
			Base64.Encoder encoder = Base64.getEncoder();
			
			
			AESCrypto aesFirst = new AESCrypto("toor");
			//Cipher cipherfirst = aesFirst.encryptCipher(aesFirst.getKey(), aesFirst.getCipherTypeAES());
			
			String iv = encoder.encodeToString(aesFirst.getIV());
			String pri = encoder.encodeToString(rsa.getPrivateKey().getEncoded());
			String pub = encoder.encodeToString(rsa.getPublicKey().getEncoded());
		
			//pri = encoder.encodeToString(	aesFirst.encryptString(pri, cipherfirst)	);	
			pri = encoder.encodeToString(	aesFirst.encrypt(pri)	);	
			File f = new File("file/root.shadow");
			FileWriter fw = new FileWriter(f);
			
			fw.write(iv+"\n");
			fw.write(pri+"\n");
			fw.write(pub);
			
			fw.close();
			
			//----------------------------------------------------------------//
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			
			//String line = br.readLine();
			String[] split = new String[3]; //line.split("::");

			split[0] = br.readLine();
			split[1] = br.readLine();
			split[2] = br.readLine();

			
			System.out.println(split[0].equals(iv));
			System.out.println(split[1].equals(pri));
			System.out.println(split[2].equals(pub));

			br.close();
			
			
			KeyFactory kf = KeyFactory.getInstance("RSA");
			
			Base64.Decoder decoder = Base64.getDecoder();
			
			byte[] IV  = decoder.decode(split[0]); 
			byte[] csk = decoder.decode(split[1]); 
			byte[] pk = decoder.decode(split[2]);
			
			AESCrypto aesSecond = new AESCrypto("toor");
			//Cipher cipherSecond = aesSecond.decryptCipher(aesSecond.getKey(),IV, aesSecond.getCipherTypeAES());
			//System.out.println( aesSecond.decryptString(aesFirst.encryptString("TEST", cipherfirst), cipherSecond) );
			//byte[] sk = aesSecond.decryptString(csk, cipherSecond).toByteArray();
			byte[] sk = aesSecond.decrypt(csk,IV);
			sk = decoder.decode(sk);
			
			PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(sk);
	        PrivateKey privKey = kf.generatePrivate(keySpecPKCS8);

	        X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(pk);
	        PublicKey pubKey = kf.generatePublic(keySpecX509);
			
	        RSACrypto newRSA = new RSACrypto(privKey, pubKey);
	        
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
	}


}
