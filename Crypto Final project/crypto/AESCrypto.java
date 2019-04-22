package crypto;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class AESCrypto extends Crypto{
	

	private Key key;
	private final String keyTypeAES = "AES";
	private final String cipherTypeAES = "AES/CBC/PKCS5Padding";
	private Cipher cipherfirst;
	public AESCrypto() throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException {
		generateKey();
		cipherfirst = encryptCipher(getKey(), getCipherTypeAES());
	}
	
	public AESCrypto(String pass) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException, NoSuchPaddingException {
		generateKey(pass);
		cipherfirst = encryptCipher(getKey(), getCipherTypeAES());
	}
	
	public AESCrypto(byte[] bytes) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
		key = new SecretKeySpec(bytes,keyTypeAES);
		cipherfirst = encryptCipher(getKey(), getCipherTypeAES());
	}

	private void generateKey() throws NoSuchAlgorithmException {
		// Generate key
		KeyGenerator kgen = KeyGenerator.getInstance(keyTypeAES);
		kgen.init(256);
		key = kgen.generateKey();
	}
	
	private void generateKey(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		// Generate key
		MessageDigest sha = MessageDigest.getInstance(getSHA_256());
		byte[] eatkey = sha.digest(password.getBytes(getUTF_8()));
		key = new SecretKeySpec(eatkey, keyTypeAES);
	}
	public Key getKey() {
		return key;
	}
	public String getCipherTypeAES() {
		return cipherTypeAES;
	}
	public String getKeyTypeAES() {
		return keyTypeAES;
	}

	public byte[] decrypt(byte[] cText, byte[] iV) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IOException {
		Cipher cipherSecond = decryptCipher(getKey(),iV, getCipherTypeAES());
		return decryptString(cText, cipherSecond).toByteArray();
	}

	public byte[] encrypt(String message) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IOException {
		return encryptString(message, cipherfirst);
	}

	public byte[] getIV() {
		return cipherfirst.getIV();
	}
	
}
