package medicalcenter;

public class MedicalData {

	private int id;
	private String MedicalType;
	private String MedicalValue;
	private String time;
	
	public MedicalData(byte[] medicaldata) {
		String line = new String(medicaldata);
		System.out.println(line);
		String[] split =line.split("::");
		this.id = Integer.parseInt(split[0]);
		MedicalType = split[1];
		MedicalValue = split[2]; 
		time = split[3];
	}

	public int getId() {
		return id;
	}

	public String getMedicalType() {
		return MedicalType;
	}

	public String getMedicalValue() {
		return MedicalValue;
	}

	public String getTime() {
		return time;
	}
	@Override
	public String toString() {
		String l ="";
		l += "Medical Data Type: "+ MedicalType +"\n";
		l += "Medical Medical Value: "+ MedicalValue +"\n";
		l += "Date: "+ time ;
		return l;
	}

}
