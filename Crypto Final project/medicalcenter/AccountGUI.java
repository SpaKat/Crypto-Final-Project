package medicalcenter;

import java.io.File;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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
		//TODO
		// load patient info 
		MenuItem loadPatientInfo = new MenuItem("Load Patient");
		loadPatientInfo.setOnAction(e ->{
			FileChooser filechooser = new FileChooser();
			filechooser.setTitle("load patient info");
			File patientFile = filechooser.showOpenDialog(new Stage());
			if (patientFile!=null) {
				
			}
		});
		file.getItems().add(loadPatientInfo);
		
		
		MenuItem exportAdminPubkey = new MenuItem("export server public key");
		exportAdminPubkey.setOnAction(e ->{
			UserFile u = new UserFile();
			
			DirectoryChooser filechooser = new DirectoryChooser();
			filechooser.setTitle("select export folder");
			File exportKey = filechooser.showDialog(new Stage());
			if (exportKey!=null) {
				if (exportKey.isDirectory()) {
					u.exportAdminPubKey(exportKey);
				}
			}
		});
		file.getItems().add(exportAdminPubkey);

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
		// new paitent
		MenuItem newPatient = new MenuItem("Add new Patient");
		newPatient.setOnAction(e ->{
			addAccount();
		});
		// change password of given account
		MenuItem newPassword = new MenuItem("Change password of an Account");
		//TODO
		// process new messages? -> may be have the system just do it. Threading yea
		MenuItem proccessMessages = new MenuItem("Process new messages from patients");
		//TODO
		// start/end server
		MenuItem start = new MenuItem("Start Server");
		MenuItem end = new MenuItem("End Server");
		//TODO

		adminStuff.getItems().addAll(newDoctor,newPatient,newPassword,proccessMessages);
		menuBar.getMenus().add(adminStuff);
		// 
	}
	private void addAccount() {
		VBox back = new VBox();
		back.setAlignment(Pos.CENTER);
		back.setSpacing(20);
		TextField patientname = makeText("Enter Patient Name ", back);
		TextField patientDoctorid = makeText("Enter Doctor id(s) \n(add :: between each it ) ",back);
		
		ComboBox<String> namePLUSid = new ComboBox<String>();
		UserFile user = new UserFile();
		user.loadDoctors(namePLUSid);
		
		namePLUSid.setOnAction(e ->{
			 	patientDoctorid.setText(patientDoctorid.getText() + "::"+namePLUSid.getValue().split("::")[1].trim());
		});
		back.getChildren().remove(patientDoctorid);
		Button enter = new Button("enter");
		back.getChildren().addAll(namePLUSid,enter);
		enter.setOnAction(e->{
			user.makeNewPatient(patientname.getText(), patientDoctorid.getText().split("::"));
		});
		//doctorid.setStyle("-fx-background-color: CHARTREUSE");

		//doctorid.setStyle("-fx-background-color: PALEVIOLETRED");
		setCenter(back);
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
		Menu doctorStuff = new Menu("Doctor operations");

		// doctor stuff 
		// view patient medical info
		MenuItem viewPatientmedicalData = new MenuItem("View Patient Medical Data");
		viewPatientmedicalData.setOnAction(e ->{
			viewPatientMedData();
		});
		// add patient to patient list
		// transfer patient 
		
		
		
		doctorStuff.getItems().addAll(viewPatientmedicalData);
		menuBar.getMenus().add(doctorStuff);
	}

	private void viewPatientMedData() {
		Pane back = new Pane();
		
		
		
		setCenter(back);
	}
}
