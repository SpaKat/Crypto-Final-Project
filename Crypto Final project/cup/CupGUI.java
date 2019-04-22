package cup;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import communication.Client;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import medicalcenter.Account;
import medicalcenter.UserFile;

public class CupGUI extends Application{

	private ArrayList<String> medData = new ArrayList<String>();
	private User account;
	@Override
	public void start(Stage primaryStage) throws Exception {
		HBox hbox = new HBox();
		BorderPane bp = new BorderPane(hbox);
		hbox.setAlignment(Pos.CENTER);
		Text user = new Text("Enter Username");
		TextField enUser = new TextField();
		Text password = new Text("Enter Password");
		PasswordField  enPassword = new PasswordField();
		Button enter = new Button("Enter");
		Text error = new Text("INVALD");
		enter.setOnAction(e ->{
			UserFile users = new UserFile();
			error.setVisible(false);
			if (users.CupGUI(enUser.getText(),enPassword.getText())) {		
					account = users.CupGUIusers(enUser.getText(),enPassword.getText());
					HBox mainwindow = mainWindow(primaryStage);
					bp.setCenter(mainwindow);
			}else {
				error.setVisible(true);
			}
		});
		VBox vbox = new VBox();
		vbox.setAlignment(Pos.CENTER);
		Button newUser = new Button("new user");
		newUser.setOnAction(e -> newUser());
		vbox.getChildren().addAll(user,enUser,password,enPassword,enter,error,newUser );
		hbox.getChildren().add(vbox);
		
		Font font = new Font(30);
		user.setFont(font );
		enUser.setFont(font);
		password.setFont(font);
		enPassword.setFont(font);
		enter.setFont(font);
		error.setFont(font);
		error.setFill(Color.RED);
		error.setVisible(false);
		
		primaryStage.setTitle("simulation of cup gui");
		primaryStage.setScene(new Scene(bp));
		primaryStage.show();
	}

	private void newUser() {
		
		VBox back = new VBox();
		TextField name = makeText("Enter User Name", back );
		TextField password = makeText("Enter Password", back );
		TextField id = makeText("Enter id", back );
		Button enter = new Button("Enter");
		Stage stage = new Stage();
		id.textProperty().addListener( (obs,old,newv)->{
			try {
				Integer.parseInt(newv);
			}catch (Exception e) {
				if(!newv.equals("")) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							id.setText("");
						}
					});
				}
			}
		});
		enter.setOnAction(e ->{
			UserFile u = new UserFile();
			u.CupGUInewUser(name.getText(),password.getText(),id.getText());
			stage.close();
		});
		back.getChildren().add(enter);
		
		stage.setScene(new Scene(back));
		stage.show();
	}

	public HBox mainWindow(Stage primaryStage) {
		VBox vbox = new VBox(10);
		vbox.setAlignment(Pos.CENTER);
		
		VBox md = new VBox();
		md.setAlignment(Pos.BASELINE_LEFT);
		
		TextField enterMedicalType = makeText("Enter Medical Type", vbox);
		TextField enterMedicalValue = makeText("Enter Medical Value", vbox);
		DatePicker enterMedicalTime = makeTime("Enter Medical Time", vbox);
		TextField enterDoctorId = makeText("Enter DoctorID(s) \nadd , between each ID\nexample 1111,1112", vbox);

		
		Button entermedicalData = new Button("enter");
		entermedicalData.setOnAction(e-> {
			String[] ids = enterDoctorId.getText().split(",");
			for (int i = 0; i < ids.length; i++) {
				medData.add(ids[i].trim()+"::"+enterMedicalType.getText() +"::"+ enterMedicalValue.getText() + "::"+ enterMedicalTime.getValue().toString() );
			}
			md.getChildren().clear();
			medData.forEach(d ->{
				md.getChildren().add(new Label(d));
			});
		});
		
		
		Button send = new Button("Send Data");
		send.setOnAction(e ->{
			FileChooser fc = new FileChooser();
			File medkey =	fc.showOpenDialog(primaryStage);
			try {
				int id = account.getId();
				new Client("127.0.0.1",medkey,medData,id);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		Button save = new Button("save Data");
		save.setOnAction(e ->{
			account.save(medData);
		});
		Button load = new Button("load Data");
		load.setOnAction(e ->{
			medData.clear();
			account.read(medData);
			md.getChildren().clear();
			medData.forEach(d ->{
				md.getChildren().add(new Label(d));
			});
		});
		Button export = new Button("Export Data to Medical Center");
		export.setOnAction(e ->{
			FileChooser fc = new FileChooser();
			File medkey =	fc.showOpenDialog(primaryStage);
			try {
				int id = account.getId();
				Client c = new Client(medkey,medData,id);
				DirectoryChooser dir = new DirectoryChooser();
				File out = dir.showDialog(primaryStage);
				c.write(out);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		vbox.getChildren().addAll(entermedicalData,send,save,load,export);
		HBox hbox = new HBox(20);
		
		ScrollPane mdScroll = new ScrollPane(md);
		mdScroll.setPrefWidth(500);
		
		hbox.getChildren().addAll(vbox,mdScroll);
		hbox.setAlignment(Pos.CENTER);
		hbox.setPrefWidth(800);
		primaryStage.setWidth(800);
		return hbox;
	}
	
	private DatePicker makeTime(String string, VBox back) {
		Text text = new Text(string);
		DatePicker datePicker = new DatePicker();
		datePicker.setValue(LocalDate.now());
		HBox hb = new HBox(20);
		hb.setAlignment(Pos.CENTER);
		hb.getChildren().addAll(text,datePicker);
		back.getChildren().add(hb);
		return datePicker;
	}

	private TextField makeText(String string, VBox back) {
		Text text = new Text(string);
		TextField tf = new TextField();
		HBox hb = new HBox(20);
		hb.setAlignment(Pos.CENTER);
		hb.getChildren().addAll(text,tf);
		back.getChildren().add(hb);
		return tf;
	}

	public static void main(String[] args) {
		launch(args);
	}

}
