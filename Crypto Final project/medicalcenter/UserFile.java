package medicalcenter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import crypto.Crypto;

public class UserFile {
	
	private File users = new File("file/users.shadow");
	
	public boolean valid(String name, String pass) {
		boolean valid = false;
		try {
			FileReader fr = new FileReader(users);
			BufferedReader br = new BufferedReader(fr);
			Crypto crypto = new Crypto();
			String line;
			while ( (line = br.readLine()) != null) {
				String[] split = line.split("::");
				if (name.equals(split[0])) {
					if (crypto.compareHashSHA512(split[1], pass)) {
						valid = true;
					}
				}
			}
			br.close();
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			valid = false;
		}
		return valid;
	}

	public Account account(String name, String pass) {
		Account doctor = null;
		try {
			FileReader fr = new FileReader(users);
			BufferedReader br = new BufferedReader(fr);
			Crypto crypto = new Crypto();
			String line;
			while ( (line = br.readLine()) != null) {
				String[] split = line.split("::");
				if (name.equals(split[0])) {
					if (crypto.compareHashSHA512(split[1], pass)) {
						doctor = new Account(split[2]);
					}
				}
			}
			br.close();
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return doctor;
	}
	
	
}
