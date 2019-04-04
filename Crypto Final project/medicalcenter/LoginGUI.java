package medicalcenter;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class LoginGUI extends HBox {


	public LoginGUI(MainWindowGUI mainWindowGUI) {
		setAlignment(Pos.CENTER);
		Text user = new Text("Enter Username");
		TextField enUser = new TextField();
		Text password = new Text("Enter Password");
		PasswordField  enPassword = new PasswordField ();
		Button enter = new Button("Enter");
		Text error = new Text("INVALD");
		enter.setOnAction(e ->{
			UserFile users = new UserFile();
			error.setVisible(false);
			if (users.valid(enUser.getText(),enPassword.getText())) {
				Account doctor = users.account(enUser.getText(),enPassword.getText());
				if(doctor != null) {
					mainWindowGUI.successfulLogin(doctor);
				}
			}else {
				error.setVisible(true);
			}
		});
		VBox vbox = new VBox();
		vbox.setAlignment(Pos.CENTER);
		vbox.getChildren().addAll(user,enUser,password,enPassword,enter,error);
		getChildren().add(vbox);

		prefWidthProperty().bind(mainWindowGUI.widthProperty().subtract(2));
		vbox.prefHeightProperty().bind(mainWindowGUI.heightProperty().subtract(3));

		Font font = new Font(30);
		user.setFont(font );
		enUser.setFont(font);
		password.setFont(font);
		enPassword.setFont(font);
		enter.setFont(font);
		error.setFont(font);
		error.setFill(Color.RED);
		error.setVisible(false);
	}

}
