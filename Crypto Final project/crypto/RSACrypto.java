package crypto;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSACrypto extends Crypto{

	private PrivateKey privateKey;
	private PublicKey publicKey;

	private String keyTypeRSA = "RSA";
	private String cipherTypeRSA = "RSA/ECB/PKCS1Padding";

	
	
	public RSACrypto(PrivateKey sk , PublicKey pk) {
		this.privateKey = sk;
		this.publicKey = pk;
	}

	public RSACrypto() throws NoSuchAlgorithmException {
		generateKeyPair();
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
	public void loadPubKey(String pkey) throws Exception{
		KeyFactory kf = KeyFactory.getInstance("RSA");

		Base64.Decoder decoder = Base64.getDecoder();
		byte[] pk = decoder.decode(pkey);

		X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(pk);
		publicKey = kf.generatePublic(keySpecX509);
	}
	public void loadSercetKey(String skey)  throws Exception{
		KeyFactory kf = KeyFactory.getInstance("RSA");

		Base64.Decoder decoder = Base64.getDecoder();
		byte[] sk = decoder.decode(skey);

		PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(sk);
		privateKey = kf.generatePrivate(keySpecPKCS8);

	}
}
