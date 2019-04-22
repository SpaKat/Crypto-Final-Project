package medicalcenter;

import java.security.PublicKey;

import crypto.RSACrypto;

public class Doctor {
	
	private int id;
	private RSACrypto rsa;
	
	public Doctor(int id, PublicKey pk) {
		this.id = id;
		rsa = new RSACrypto(null, pk);
	}
	
	public int getId() {
		return id;
	}
	public RSACrypto getRsa() {
		return rsa;
	}
	
}
