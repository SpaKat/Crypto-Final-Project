package communication;

import java.io.Serializable;

public class Patient implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1516475018681018984L;

	private int[] doctorID;
	private String[] medicalData;
	private int id;
	
	public Patient(int[] doctorID,String[] medicalData, int id) {

		this.doctorID = doctorID;
		this.medicalData = medicalData;
		this.id = id;
	
	}
	public int[] getDoctorID() {
		return doctorID;
	}
	public String[] getMedicalData() {
		return medicalData;
	}
	public int getId() {
		return id;
	}
	
}
