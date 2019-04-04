package medicalcenter;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class MainWindowGUI extends ScrollPane{

	
	private AccountGUI accountGUI = new AccountGUI();
	
	public MainWindowGUI() {
		setContent(new LoginGUI(this));
	}

	public void successfulLogin(Account account) {
		accountGUI.setAccount(account);
		setContent(accountGUI);
	}
	
}
