package view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import viewmodel.pipeGameViewModel;

public class SettingsWindowController implements Initializable{
	@FXML
	TextField ipField;
	@FXML
	TextField portField;
	pipeGameViewModel vm;
	StringProperty ip;
	IntegerProperty port;
	Stage stage;
	
	public void setStage(Stage _stage) {
		stage = _stage;
	}
	
	public void setViewModel(pipeGameViewModel _vm) {
		vm = _vm;
		ip = new SimpleStringProperty();
		vm.ip.bindBidirectional(ip);
		port = new SimpleIntegerProperty();
		vm.port.bindBidirectional(port);
		ipField.setText("127.0.0.1");
		portField.setText("6400");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
	public void dissmiss() {
		stage.close();
	}
	
	public void save(){
		if(!ipField.getText().equals(""))
			ip.set(ipField.getText());
		if(!portField.getText().equals(""))
			port.set(Integer.parseInt(portField.getText()));
		dissmiss();
	}
}
