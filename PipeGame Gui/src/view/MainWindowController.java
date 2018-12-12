package view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class MainWindowController implements Initializable{
	@FXML
	MazeDisplayer mazeDisplayer;
	char[][] mazeData= {
			{'7','L','j',' ','L'},
			{' ','F','-','s',' '},
			{'g','j','|','L','7'},
			{'F','-','|','L','-'},
			{' ','j','|','L','-'},
	};

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	mazeDisplayer.setMazeData(mazeData);
	}
	
}
