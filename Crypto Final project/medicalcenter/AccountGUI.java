package medicalcenter;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import communication.Patient;
import communication.Server;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class AccountGUI extends BorderPane {

	private Account account;
	private MenuBar menuBar = new MenuBar();
	private Server server;
	public AccountGUI(Account account, MainWindowGUI mainWindowGUI) {
		this.account = account;
		setTop(menuBar);
		setBottom(new Text("Name and Id ---------> "+account.getName() + "  " + account.getId()));
		menuBar(mainWindowGUI);
		makePage(account.getId());
	}
	private void menuBar(MainWindowGUI mainWindowGUI) {
		Menu file = new Menu("file");
		menuBar.getMenus().add(file);

		MenuItem logout = new MenuItem("Login Out");
		logout.setOnAction(e ->{
			account = null;
			try {
				server.end();
			}catch (Exception e1) {
				//e1.printStackTrace();
			}
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
				loadPatient(patientFile);
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
	private void loadPatient(File patientFile) {
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(patientFile));
			UserFile uf = new UserFile();
			Patient p = uf.extractPatient(ois);
			uf.processPatient(p);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
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
		newPassword.setOnAction(e->{
			newPass();
		});
		//TODO
		// start/end server
		MenuItem start = new MenuItem("Start Server");
		start.setOnAction(e ->{
			try {
				server.end();
			}catch (Exception e1) {
				//e1.printStackTrace();
			}
			server = new Server();
			System.out.println("start server");

		});
		MenuItem end = new MenuItem("End Server");
		end.setOnAction(e ->{
			server.end();
			System.out.println("end Sever");
		});
		adminStuff.getItems().addAll(newDoctor,newPatient,newPassword,start,end);
		menuBar.getMenus().add(adminStuff);
		// 
	}
	private void newPass() {
		VBox back = new VBox();
		back.setAlignment(Pos.CENTER);
		back.setSpacing(20);
		TextField newpass = makeText("Enter new Password ", back);
		
		ComboBox<String> namePLUSid = new ComboBox<String>();
		UserFile user = new UserFile();
		user.loadDoctors(namePLUSid);

		
		Button enter = new Button("enter");
		back.getChildren().addAll(namePLUSid,enter);
		enter.setOnAction(e->{
			try {
				String[] split = namePLUSid.getValue().split("::");
				for (int i = 0; i < split.length; i++) {
					System.out.println(split[i]);
				}
				user.changePass(split[0].trim(), Integer.parseInt(split[1].trim()), newpass.getText());

			}catch (Exception e1) {
				e1.printStackTrace();
				// TODO: handle exception
			}
		});
		//doctorid.setStyle("-fx-background-color: CHARTREUSE");

		//doctorid.setStyle("-fx-background-color: PALEVIOLETRED");
		setCenter(back);
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

		//export public key
		MenuItem exportPubkey = new MenuItem("Export Public Key");
		exportPubkey.setOnAction(e ->{
			UserFile u = new UserFile();
			DirectoryChooser filechooser = new DirectoryChooser();
			filechooser.setTitle("select export folder");
			File exportKey = filechooser.showDialog(new Stage());
			if (exportKey!=null) {
				if (exportKey.isDirectory()) {
					u.exportDoctorPubKey(account.getId(),exportKey);
				}
			}
		});


		doctorStuff.getItems().addAll(viewPatientmedicalData,exportPubkey);
		menuBar.getMenus().add(doctorStuff);
	}

	private void viewPatientMedData() {
		SplitPane sp = new SplitPane();
		VBox left = new VBox();
		FlowPane Right = new FlowPane();
		UserFile u = new UserFile();
		ComboBox<String> patients = u.avaiablePatients(account.getId());
		patients.setOnAction(e ->{
			Right.getChildren().clear();
			ArrayList<MedicalData> data = u.loadpatientData(account, Integer.parseInt(patients.getValue().split("_")[1]));
			data.forEach(md ->{
				Label l = new Label();
				l.setText(md.toString());
				l.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(5),BorderStroke.THIN)));
				Right.getChildren().add(l);
			});
		});
		left.getChildren().add(patients);
		ScrollPane scp = new ScrollPane(Right);
		Right.prefWidthProperty().bind(scp.widthProperty().subtract(3));
		sp.getItems().addAll(left,scp);
		setCenter(sp);
	}
	public void close() {
		try {
			server.end();
		}catch (Exception e1) {
			//e1.printStackTrace();
		}
	}
}
