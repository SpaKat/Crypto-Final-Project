package onlyfortesting;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Testingcypto {

	public static void main(String[] args) {
		testEncryptDecrypt();
		testmanuelKey();
		testRSAkeys();
		testObjectEncrypt();
	}

	private static void testObjectEncrypt() {

		try {
			Tester s = new Tester("TEST la  object <3");
			KeyPair kp = generateKeyPair();

			PublicKey publicKey = publicKey(kp); 
			PrivateKey privateKey = privateKey(kp);


			//save keys ---- needs hash and/or encryption to protect
			Base64.Encoder encoder = Base64.getEncoder();

			writePublicKey(publicKey, encoder);

			writePrivateKey(privateKey, encoder);

			Signature sign = Signature.getInstance("SHA256withRSA");
			sign.initSign(privateKey);


			// Encrypt cipher

			Cipher encryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding ");
			encryptCipher.init(Cipher.ENCRYPT_MODE, privateKey);

			// object to byte array
			ByteArrayOutputStream os = obectToByte(s);
			//oos.close();
			// Encrypt
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, encryptCipher);
			cipherOutputStream.write(os.toByteArray());
			cipherOutputStream.flush();
			cipherOutputStream.close();
			byte[] encryptedBytes = outputStream.toByteArray();

			// Decrypt cipher
			Cipher decryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding ");
			//IvParameterSpec ivParameterSpec = new IvParameterSpec(encryptCipher.getIV());
			decryptCipher.init(Cipher.DECRYPT_MODE, publicKey);

			// Decrypt
			ByteArrayOutputStream newout = new ByteArrayOutputStream();
			ByteArrayInputStream inStream = new ByteArrayInputStream(encryptedBytes);
			CipherInputStream cipherInputStream = new CipherInputStream(inStream, decryptCipher);
			byte[] buf = new byte[1024];// some number
			int bytesRead;
			while ((bytesRead = cipherInputStream.read(buf)) >= 0) {
				newout.write(buf, 0, bytesRead);
			}
			cipherInputStream.close();

			// read object
			Object o = byteToObject(newout);
			
			
			System.out.println("Reading object: " + o.toString());
		}catch (Exception e) {
			e.printStackTrace();
		}


	}

	private static void testRSAkeys() {
		try {
			String s = "tester my tester how are thoughs eyes <3";
			KeyPair kp = generateKeyPair();
	
			PublicKey publicKey = publicKey(kp); 
			PrivateKey privateKey = privateKey(kp);
	
	
			//save keys ---- needs hash and/or encryption to protect
			Base64.Encoder encoder = Base64.getEncoder();
			Signature sign = Signature.getInstance("SHA256withRSA");
			sign.initSign(privateKey);
	
			writePublicKey(publicKey, encoder);
			writePrivateKey(privateKey, encoder);
	
			// Encrypt cipher
			Cipher encryptCipher = encryptCipher(privateKey,"RSA/ECB/PKCS1Padding");
	
			// Encrypt
			byte[] encryptedBytes = encryptString(s, encryptCipher);
	
			// Decrypt cipher
			Cipher decryptCipher = decryptCipher(publicKey,"RSA/ECB/PKCS1Padding");
	
			ByteArrayOutputStream outputStream = decryptString(encryptedBytes, decryptCipher);
	
			System.out.println("Result: " + new String(outputStream.toByteArray()));
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void testmanuelKey() {
		try {
			String s = "Testing this hard coded key shit <3";
			String password = "Yosh my key my dudes";
			MessageDigest sha = MessageDigest.getInstance("SHA-256");
			byte[] key = sha.digest(password.getBytes("UTF-8"));
	
	
			Key aeskey = new SecretKeySpec(key, "AES");
	
			Cipher encryptCipher = encryptCipher(aeskey,"AES/CBC/PKCS5Padding");
	
			ByteArrayOutputStream outputStream;
			byte[] encryptedBytes = encryptString(s, encryptCipher);
	
			Cipher decryptCipher = decryptCipher(aeskey, encryptCipher.getIV(),"AES/CBC/PKCS5Padding");
	
			outputStream = decryptString(encryptedBytes, decryptCipher);
	
			System.out.println("Result: " + new String(outputStream.toByteArray()));
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void testEncryptDecrypt() {
		try {
	
			String s = "Testing this shit <3";
	
			SecretKey aesKey = generateKey();
	
			Cipher encryptCipher = encryptCipher(aesKey,"AES/CBC/PKCS5Padding");
	
			ByteArrayOutputStream outputStream;
			byte[] encryptedBytes = encryptString(s, encryptCipher);
	
			Cipher decryptCipher = decryptCipher(aesKey, encryptCipher.getIV(),"AES/CBC/PKCS5Padding");
	
			outputStream = decryptString(encryptedBytes, decryptCipher);
	
			System.out.println("Result: " + new String(outputStream.toByteArray()));
	
		} 
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static Object byteToObject(ByteArrayOutputStream newout) throws IOException, ClassNotFoundException {
		ByteArrayInputStream inobject = new ByteArrayInputStream(newout.toByteArray());
		ObjectInputStream findobject = new ObjectInputStream(inobject);
		Object o = findobject.readObject();
		return o;
	}

	private static ByteArrayOutputStream obectToByte(Object obj) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(os);
		oos.writeObject(obj);
		oos.flush();
		return os;
	}

	private static void writePrivateKey(PrivateKey privateKey, Base64.Encoder encoder) throws IOException {
		FileWriter prtout = new FileWriter("private.key");
		prtout.write(encoder.encodeToString(privateKey.getEncoded()));
		prtout.flush();
		prtout.close();
	}

	private static void writePublicKey(PublicKey publicKey, Base64.Encoder encoder) throws IOException {
		FileWriter pubout = new FileWriter("public.key");
		pubout.write(encoder.encodeToString(publicKey.getEncoded()));
		pubout.flush();
		pubout.close();
	}

	private static PrivateKey privateKey(KeyPair kp) {
		PrivateKey privateKey = kp.getPrivate();
		return privateKey;
	}

	private static PublicKey publicKey(KeyPair kp) {
		// get keys
		PublicKey publicKey = kp.getPublic();
		return publicKey;
	}

	private static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
		// gen pairs
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
		kpg.initialize(2048);
		KeyPair kp = kpg.generateKeyPair();
		return kp;
	}

	public static Cipher decryptCipher(Key aesKey,String type) throws NoSuchAlgorithmException,
	NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
		// Decrypt cipher
		Cipher decryptCipher = Cipher.getInstance(type);
			decryptCipher.init(Cipher.DECRYPT_MODE, aesKey);
		return decryptCipher;
	}
	public static Cipher decryptCipher(Key aesKey, byte[] iv,String type) throws NoSuchAlgorithmException,
	NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
		// Decrypt cipher
		Cipher decryptCipher = Cipher.getInstance(type);
		
			IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
			decryptCipher.init(Cipher.DECRYPT_MODE, aesKey, ivParameterSpec);
		
		return decryptCipher;
	}
	// RSA/ECB/PKCS1Padding
	// AES/CBC/PKCS5Padding
	private static Cipher encryptCipher(Key aesKey, String type)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
		// Encrypt cipher
		Cipher encryptCipher = Cipher.getInstance(type);
		encryptCipher.init(Cipher.ENCRYPT_MODE, aesKey);
		return encryptCipher;
	}
	private static SecretKey generateKey() throws NoSuchAlgorithmException {
		// Generate key
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(256);
		SecretKey aesKey = kgen.generateKey();
		return aesKey;
	}

	private static ByteArrayOutputStream decryptString(byte[] encryptedBytes, Cipher decryptCipher) throws IOException {
		ByteArrayOutputStream outputStream;
		// Decrypt
		outputStream = new ByteArrayOutputStream();
		ByteArrayInputStream inStream = new ByteArrayInputStream(encryptedBytes);
		CipherInputStream cipherInputStream = new CipherInputStream(inStream, decryptCipher);
		byte[] buf = new byte[1024];// some number
		int bytesRead;
		while ((bytesRead = cipherInputStream.read(buf)) >= 0) {
			outputStream.write(buf, 0, bytesRead);
		}
		cipherInputStream.close();
		return outputStream;
	}

	private static byte[] encryptString(String string, Cipher encryptCipher) throws IOException {
		// Encrypt
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, encryptCipher);
		cipherOutputStream.write(string.getBytes());
		cipherOutputStream.flush();
		cipherOutputStream.close();
		byte[] encryptedBytes = outputStream.toByteArray();
		return encryptedBytes;
	}
}
