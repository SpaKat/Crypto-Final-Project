package medicalcenter;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class MainWindowGUI extends ScrollPane{

	private LoginGUI login = new LoginGUI();
	private AccountGUI account = new AccountGUI();
	
	public MainWindowGUI() {
		setContent(login);
	}
	
}
