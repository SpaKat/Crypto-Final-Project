package cup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Base64;

import crypto.AESCrypto;

public class User {

	private String name;
	private AESCrypto aes;
	private int id;

	public User(String name, AESCrypto aes, String id) {
		this.name = name;
		this.aes = aes;
		this.id = Integer.parseInt(id);
	}

	public int getId() {
		return id;
	}

	public void save(ArrayList<String> medData) {
		try {
			File f = new File("file/CupUsers "+id+".shadow");
			FileWriter fw = new FileWriter(f,true);
			
			medData.forEach(e ->{
				
				try {
					byte[] d = aes.encrypt(e);
					Base64.Encoder encoder = Base64.getEncoder();
					
					fw.write(encoder.encodeToString(d)+"::"+encoder.encodeToString(aes.getIV())+"\n");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
			});
			
			fw.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public void read(ArrayList<String> medData){
		
	
		try {
			File f = new File("file/CupUsers "+id+".shadow");
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while( (line = br.readLine())!= null ) {
				String[] split = line.split("::");
				Base64.Decoder decoder = Base64.getDecoder();
				
				byte[] data = decoder.decode(split[0]);
				byte[] iv = decoder.decode(split[1]);
				byte[] val = aes.decrypt(data, iv);
				
				medData.add(new String(val));
			}
			
			br.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
