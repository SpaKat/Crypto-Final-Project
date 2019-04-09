package medicalcenter;

import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

public class AccountGUI extends BorderPane {
	
	private Account account;

	public AccountGUI(Account account) {
		this.account = account;
		setTop(new Text(account.getName() + "  " + account.getId()));
		
		
	}

}
