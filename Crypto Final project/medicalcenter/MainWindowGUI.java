package medicalcenter;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class MainWindowGUI extends BorderPane{

	public MainWindowGUI() {
		setCenter(new ScrollPane( new LoginGUI(this) ));
	}

	public void successfulLogin(Account account) {
		setCenter(new AccountGUI(account));
	}
	
}
