package onlyfortesting;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;

import crypto.AESCrypto;
import crypto.RSACrypto;

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
		
		
		
		try {
			

			RSACrypto rsa = new RSACrypto();
			
			Base64.Encoder encoder = Base64.getEncoder();
			
			String pri = encoder.encodeToString(rsa.getPrivateKey().getEncoded());
			String pub = encoder.encodeToString(rsa.getPublicKey().getEncoded());
			
			AESCrypto aesFirst = new AESCrypto("toor");
			Cipher cipherfirst = aesFirst.encryptCipher(aesFirst.getKey(), aesFirst.getCipherTypeAES());
			
			
			File f = new File("t.test");
			FileWriter fw = new FileWriter(f);
			BufferedWriter bw = new BufferedWriter(fw);
			
			bw.write(encoder.encodeToString(cipherfirst.getIV()));
			bw.newLine();
			bw.write(new String(aesFirst.encryptString(pri, cipherfirst)));
			bw.newLine();
			bw.write(new String(aesFirst.encryptString(pub, cipherfirst)));
			bw.newLine();
	
			bw.close();
			
			
			
			AESCrypto aesSecond = new AESCrypto("toor");
			
			
			
			FileReader filereader = new FileReader(f);
			BufferedReader bufferRead = new BufferedReader(filereader);
			
			Base64.Decoder decoder = Base64.getDecoder();
			
			Cipher cipherSecond = aesSecond.decryptCipher(aesSecond.getKey(),decoder.decode(bufferRead.readLine()), aesSecond.getCipherTypeAES());
			
			ByteArrayOutputStream privateKey = aesSecond.decryptString(bufferRead.readLine().getBytes(), cipherSecond);
			ByteArrayOutputStream publicKey = aesSecond.decryptString(bufferRead.readLine().getBytes(), cipherSecond);
			
	        KeyFactory kf = KeyFactory.getInstance("RSA");
			
	        
			PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(decoder.decode(privateKey.toByteArray()));
	        PrivateKey privKey = kf.generatePrivate(keySpecPKCS8);

	        X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(decoder.decode(publicKey.toByteArray()));
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
		

	}


}
