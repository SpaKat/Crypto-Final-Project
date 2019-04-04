package onlyfortesting;

import java.io.Serializable;

public class Tester implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2979735426558049341L;
	private String s;
	public Tester(String s) {
		this.s = s;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return s;
	}
}
