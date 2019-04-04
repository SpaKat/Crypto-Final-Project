package crypto;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

public class RSACrypto extends Crypto{

	private PrivateKey privateKey;
	private PublicKey publicKey;
	
	private String keyTypeRSA = "RSA";
	private String cipherTypeRSA = "RSA/ECB/PKCS1Padding";

	public RSACrypto() {
		// TODO Auto-generated constructor stub
	}

	public void generateKeyPair() throws NoSuchAlgorithmException {
		KeyPairGenerator kpg = KeyPairGenerator.getInstance(keyTypeRSA);
		kpg.initialize(2048);
		KeyPair kp = kpg.generateKeyPair();
		privateKey = kp.getPrivate();
		publicKey = kp.getPublic();
	}	
	
	public PrivateKey getPrivateKey() {
		return privateKey;
	}
	
	public PublicKey getPublicKey() {
		return publicKey;
	}
	public String getCipherTypeRSA() {
		return cipherTypeRSA;
	}
	public String getKeyTypeRSA() {
		return keyTypeRSA;
	}

	public byte[] encrypt(String string,Key key)throws Exception {
			return encryptString(string, encryptCipher(key, cipherTypeRSA));
	}

	public String decrypt(byte[] enbytes, Key key) throws Exception {
		return decryptString(enbytes, decryptCipher(key, cipherTypeRSA)).toString();
	}
}
