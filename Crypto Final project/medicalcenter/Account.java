package medicalcenter;

import java.security.PrivateKey;
import java.security.PublicKey;

import crypto.AESCrypto;
import crypto.RSACrypto;

public class Account {
	
	private String id;
	private String name;
	private AESCrypto aes;
	private RSACrypto rsa;
	
	public Account(String name, AESCrypto aes, RSACrypto rsa, String id) {
		this.name = name;
		this.aes = aes;
		this.id = id;
		this.rsa = rsa;
	}
	public String getName() {
		return name;
	}
	public String getId() {
		return id;
	}
	
}
