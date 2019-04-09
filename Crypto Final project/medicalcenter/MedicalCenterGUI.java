package medicalcenter;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MedicalCenterGUI extends Application{

	@Override
	public void start(Stage primaryStage)  {
		Scene scene = new Scene(new MainWindowGUI(),450,450);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Medical Center GUI ");
		primaryStage.show();

	}

	public static void main(String[] args) {launch(args);}

}
