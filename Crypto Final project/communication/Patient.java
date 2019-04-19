package communication;

import java.io.Serializable;
import java.util.ArrayList;

public class Patient implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1516475018681018984L;

	private int[] doctorID;
	private String[] medicalData;
	
	
	public Patient(int[] doctorID,String[] medicalData) {

		this.doctorID = doctorID;
		this.medicalData = medicalData;
	
	}
	public int[] getDoctorID() {
		return doctorID;
	}
	public String[] getMedicalData() {
		return medicalData;
	}
	
	
}
