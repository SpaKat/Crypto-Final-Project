package cup;

import java.io.File;
import java.util.ArrayList;

import communication.Client;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class CupGUI extends Application{

	private ArrayList<String> medData = new ArrayList<String>();
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		VBox vbox = new VBox(10);
		vbox.setAlignment(Pos.CENTER);
		
		VBox md = new VBox();
		md.setAlignment(Pos.BASELINE_LEFT);
		
		TextField enterMedicalType = makeText("Enter Medical Type", vbox);
		TextField enterMedicalValue = makeText("Enter Medical Value", vbox);
		TextField enterMedicalTime = makeText("Enter Medical Time", vbox);
		TextField enterDoctorId = makeText("Enter DoctorID", vbox);
		TextField enterPatientId = makeText("Enter PatientID", vbox);

		
		
		Button entermedicalData = new Button("enter");
		entermedicalData.setOnAction(e-> {
			medData.add(enterDoctorId.getText()+"::"+enterMedicalType.getText() +"::"+ enterMedicalValue.getText() + "::"+ enterMedicalTime.getText() );
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
				int id = Integer.parseInt(enterPatientId.getText());
				new Client("127.0.0.1",medkey,medData,id);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		vbox.getChildren().addAll(entermedicalData,send);
		HBox hbox = new HBox(20);
		
		ScrollPane mdScroll = new ScrollPane(md);
		mdScroll.setPrefWidth(500);
		
		hbox.getChildren().addAll(vbox,mdScroll);
		hbox.setAlignment(Pos.CENTER);
		primaryStage.setTitle("simulation of cup gui");
		primaryStage.setScene(new Scene(hbox));
		primaryStage.show();
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
