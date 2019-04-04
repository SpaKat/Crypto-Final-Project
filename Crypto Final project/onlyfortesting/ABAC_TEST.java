package onlyfortesting;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;

import crypto.Crypto;
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
			Doctor a = new Doctor();
			a.setId("s");
			Patient f = new Patient();
			f.setId("s");
			boolean b1 = g.valid(a,f);
	        //Enforcer e = new Enforcer("examples/rbac_model.conf", "examples/rbac_policy.csv");

			System.out.println("Yosh1: ------> " + b1);
		 */
		//	e.getAllActions().forEach(System.out::println);
		Crypto c = new Crypto();
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
		*/
		
		try{
			String f = "toor";
			String t = c.hashSHA512(f);
			System.out.println(t);
			System.out.println();
			System.out.println(c.compareHashSHA512(t, "toor"));
			
			
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		

	}


}
