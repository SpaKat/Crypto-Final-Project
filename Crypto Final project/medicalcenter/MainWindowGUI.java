package medicalcenter;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class MainWindowGUI extends BorderPane{

	private AccountGUI accountGUI;

	public MainWindowGUI() {
		logout();
	}

	public void successfulLogin(Account account) {
		accountGUI = new AccountGUI(account,this);
		setCenter(accountGUI);
	}

	public void logout() {
		setCenter(new ScrollPane( new LoginGUI(this) ));		
	}

	public void close() {
		if(accountGUI != null) {
			accountGUI.close();
		}
	}

}
