package medicalcenter;

import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;

public class MainMenuGUI extends BorderPane{

	private MainWindowGUI mainWindow = new MainWindowGUI();
	private MenuBar mainMenuBar = new MenuBar();
	
	public MainMenuGUI() {
		setTop(mainMenuBar);
		setCenter(mainWindow);
	}

}
