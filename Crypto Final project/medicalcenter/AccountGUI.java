package medicalcenter;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class AccountGUI extends BorderPane {

	private Account account;
	private Doctor doctorAccount;
	private MenuBar menuBar = new MenuBar();
	public AccountGUI(Account account, MainWindowGUI mainWindowGUI) {
		this.account = account;
		setTop(menuBar);
		setBottom(new Text(account.getName() + "  " + account.getId()));
		menuBar(mainWindowGUI);
		makePage(account.getId());
	}

	private void menuBar(MainWindowGUI mainWindowGUI) {
		Menu file = new Menu("file");
		menuBar.getMenus().add(file);

		MenuItem logout = new MenuItem("Login Out");
		logout.setOnAction(e ->{
			account = null;
			mainWindowGUI.logout();
		});
		file.getItems().add(logout);
		// add account editing 
	}

	private void makePage(int id) {
		if (id == 0) {
			admin();
		}else {
			doctor();
		}
	}

	private void admin() {
		// the admin stuff
		Menu adminStuff = new Menu("Admin operations");
		// new doctor
		MenuItem newDoctor = new MenuItem("Add new Doctor");
		newDoctor.setOnAction(e ->{
			addDoctor();
		});
		// change password of given account
		MenuItem newPassword = new MenuItem("Change password of an Account");
		//TODO
		// process new messages? -> may be have the system just do it.
		MenuItem proccessMessages = new MenuItem("Process new messages from patients");
		//TODO

		adminStuff.getItems().addAll(newDoctor,newPassword,proccessMessages);
		menuBar.getMenus().add(adminStuff);
		// 
	}
	private void addDoctor() {
		VBox back = new VBox();
		back.setAlignment(Pos.CENTER);
		TextField doctorname = makeText("Enter Doctor Name ", back);
		TextField doctorpass = makeText("Enter Doctor Password ", back);
		TextField doctorid = makeText("Enter Doctor id > 1000", back);
		doctorid.textProperty().addListener( (obs,oldV,newV) ->{
			try {
				int x = Integer.parseInt(newV);
				UserFile user = new UserFile();
				if (x > 1000 && user.unique(x) ) {
					doctorid.setStyle("-fx-background-color: CHARTREUSE");
				}else {
					doctorid.setStyle("-fx-background-color: PALEVIOLETRED");
				}
			}catch (Exception e) {
				doctorid.setStyle("-fx-background-color: PALEVIOLETRED");
			}
		});
		Button enter = new Button("enter");
		back.getChildren().add(enter);
		enter.setOnAction(e->{
			try {
				int x = Integer.parseInt(doctorid.getText());
				UserFile user = new UserFile();
				if (x > 1000 && user.unique(doctorname.getText(),doctorpass.getText(),x) ) {
					doctorid.setStyle("-fx-background-color: CHARTREUSE");
					if(user.makeNewDoctorAccount(doctorname.getText(),doctorpass.getText(),x)) {
						System.out.println("BAD");
					}else {
						if(generateAccountInfo(x,doctorpass.getText())) {
							
						}
					}
				}else {
					doctorid.setStyle("-fx-background-color: PALEVIOLETRED");
				}
			}catch (Exception e1) {
				e1.printStackTrace();
				doctorid.setStyle("-fx-background-color: PALEVIOLETRED");
			}

		});
		setCenter(back);
	}

	private boolean generateAccountInfo(int id, String pass) {
		UserFile uf = new UserFile();
		boolean b = false;
		try {
			uf.generateAccountInfo(id,pass);
		} catch (Exception e) {
			b = true;
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return b;
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

	private void doctor() {
		// doctor stuff 
		// view patient medical info
		// add patient to patient list
		// transfer patient 
	}
}
