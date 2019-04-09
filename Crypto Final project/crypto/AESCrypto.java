package crypto;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

public class AESCrypto extends Crypto{
	

	private Key key;
	
	
	private final String keyTypeAES = "AES";
	private final String cipherTypeAES = "AES/CBC/PKCS5Padding";
	
	public AESCrypto() throws NoSuchAlgorithmException {
		generateKey();
	}
	
	public AESCrypto(String pass) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		generateKey(pass);
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
	
}
