package medicalcenter;

public class Account {
	
	private String id;
	
	public Account(String id) {
		setId(id);
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}

}
