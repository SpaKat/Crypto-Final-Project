package onlyfortesting;

public class Sub {

	String[] yosh =new String[10];
	public Sub() {
		// TODO Auto-generated constructor stub
		for (int i = 0; i < yosh.length; i++) {
			yosh[i] = "data"+i;
		}
		yosh[7] = "data";
	}
	public String[] getYosh() {
		return yosh;
	}
	
}
