package medicalcenter;

import java.security.PrivateKey;
import java.security.PublicKey;

import crypto.AESCrypto;
import crypto.RSACrypto;

public class Account {
	
	private int level;
	private String name;
	private AESCrypto aes;
	private RSACrypto rsa;
	
	public Account(String name, AESCrypto aes, RSACrypto rsa, int level) {
		this.name = name;
		this.aes = aes;
		this.level = level;
		this.rsa = rsa;
	}
	public String getName() {
		return name;
	}
	public int getId() {
		return level;
	}
	
}
