package crypto;

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
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class Crypto {

	private final String SHA_256 = "SHA-256";
	private final String UTF_8 = "UTF-8";	
	public String getSHA_256() {
		return SHA_256;
	}
	public String getUTF_8() {
		return UTF_8;
	}
	public Object byteToObject(ByteArrayOutputStream newout) throws IOException, ClassNotFoundException {
		ByteArrayInputStream inobject = new ByteArrayInputStream(newout.toByteArray());
		ObjectInputStream findobject = new ObjectInputStream(inobject);
		Object o = findobject.readObject();
		return o;
	}
	public ByteArrayOutputStream obectToByte(Object obj) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(os);
		oos.writeObject(obj);
		oos.flush();
		return os;
	}
	public Cipher decryptCipher(Key aesKey,String type) throws NoSuchAlgorithmException,
	NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
		// Decrypt cipher
		Cipher decryptCipher = Cipher.getInstance(type);
		decryptCipher.init(Cipher.DECRYPT_MODE, aesKey);
		return decryptCipher;
	}
	public Cipher decryptCipher(Key aesKey, byte[] iv,String type) throws NoSuchAlgorithmException,
	NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
		// Decrypt cipher
		Cipher decryptCipher = Cipher.getInstance(type);

		IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
		decryptCipher.init(Cipher.DECRYPT_MODE, aesKey, ivParameterSpec);

		return decryptCipher;
	}
	// RSA/ECB/PKCS1Padding
	// AES/CBC/PKCS5Padding
	public Cipher encryptCipher(Key aesKey, String type)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
		// Encrypt cipher
		Cipher encryptCipher = Cipher.getInstance(type);
		encryptCipher.init(Cipher.ENCRYPT_MODE, aesKey);
		return encryptCipher;
	}
	public ByteArrayOutputStream decryptString(byte[] encryptedBytes, Cipher decryptCipher) throws IOException {
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
	public byte[] encryptString(String string, Cipher encryptCipher) throws IOException {
		// Encrypt
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, encryptCipher);
		cipherOutputStream.write(string.getBytes());
		cipherOutputStream.flush();
		cipherOutputStream.close();
		byte[] encryptedBytes = outputStream.toByteArray();
		return encryptedBytes;
	}
	public String hashSHA512(String input) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-512");
		byte[] message = md.digest(input.getBytes());
		Base64.Encoder encoder = Base64.getEncoder();
		message = encoder.encode(message);
		return new String(message);
	}
	public boolean compareHashSHA512(String hash, String input) throws NoSuchAlgorithmException {
		boolean b = false;
		b = new String(hash).equals(new String(hashSHA512(input)));
		return b;
	}
	public void writeKey(Key key, String name) throws IOException {
		Base64.Encoder encoder = Base64.getEncoder();
		FileWriter prtout = new FileWriter(name +".key");
		prtout.write(encoder.encodeToString(key.getEncoded()));
		prtout.flush();
		prtout.close();
	}
}
