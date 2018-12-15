package view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainWindowController implements Initializable,Observer{
	static String IP;
	static int PORT;
	@FXML
	PipeDisplayer pipeDisplayer;
	char[][] pipeData= {
			{'s','L','j',' ','L'},
			{'L','F','-','-',' '},
			{'7','j','|','L','7'},
			{'F','-','|','L','-'},
			{' ','j','|','L','g'},
	};

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		pipeDisplayer.setMazeData(pipeData);
	 pipeDisplayer.addEventFilter(MouseEvent.MOUSE_CLICKED, (e)->pipeDisplayer.requestFocus());
		  EventHandler<MouseEvent> circleOnMouseEventHandler = new EventHandler<MouseEvent>() {
			  
			@Override
			public void handle(MouseEvent event) {
			double x=event.getSceneX();
			double y=event.getSceneY();
			pipeDisplayer.click(x,y);
			}
		};
		 pipeDisplayer.setOnMouseClicked(circleOnMouseEventHandler);
	}

	public void start() {
		System.out.println("Start.");
	}

	public void stop() {
		System.out.println("Stop.");
	}

	public void exit() {
		System.out.println("Exiting..");
		System.exit(0);
	}

	public void openFile() {
		System.out.println("Open File.");
		FileChooser fc = new FileChooser();
		fc.setTitle("Open Pipe File");
		fc.setInitialDirectory(new File("./resources"));

		FileChooser.ExtensionFilter txtExtensionFilter = new FileChooser.ExtensionFilter("Text Files", "*.txt");
		fc.getExtensionFilters().add(txtExtensionFilter);
		fc.setSelectedExtensionFilter(txtExtensionFilter);
		File choosen = fc.showOpenDialog(null);

		if (choosen != null) {
			System.out.println(choosen.getName());

			List<char[]> lines = new ArrayList<char[]>();
			BufferedReader reader;
			try {
				reader = new BufferedReader(new FileReader(choosen));

				String line;
				while ((line = reader.readLine()) != null) {
					lines.add(line.toCharArray());
				}
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			char[][] charArray = lines.toArray(new char[lines.size()][]);
			pipeDisplayer.setMazeData(charArray);
		}
	}

	public void saveFile() {
		System.out.println("Saving into File.");
		FileChooser fc = new FileChooser();
		fc.setTitle("Choose location To Save File");
		FileChooser.ExtensionFilter txtExtensionFilter = new FileChooser.ExtensionFilter("Text Files", "*.txt");
		fc.getExtensionFilters().add(txtExtensionFilter);
		fc.setSelectedExtensionFilter(txtExtensionFilter);

		File selectedFile = null;
		selectedFile = fc.showSaveDialog(null);

		if (selectedFile == null) {
			return;
		}

		PrintWriter outFile = null;
		try {
			outFile = new PrintWriter(selectedFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int i = 0; i < pipeData.length; i++) {
			for (int j = 0; j < pipeData[i].length; j++) {
				outFile.print(pipeData[i][j]);
			}
			outFile.println();
		}

		outFile.close();
	}
	public void edit() {
		try {
			System.out.println("you get to the edit func\n");
	        FXMLLoader fxmlLoader = new FXMLLoader();
	        fxmlLoader.setLocation(getClass().getResource("/view/MainWindow.fxml"));
	        System.out.println("you get to the edit func\n");
	        /* 
	         * if "fx:controller" is not set in fxml
	         * fxmlLoader.setController(NewWindowController);
	         */
	        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
	        System.out.println("you get to the edit func\n");
	        Stage stage = new Stage();
	        stage.setTitle("EDIT_IP_AND_PORT");
	        stage.setScene(scene);
	        stage.show();
	    } catch (IOException e) {
	        Logger logger = Logger.getLogger(getClass().getName());
	        logger.log(Level.SEVERE, "Failed to create new Window.", e);
	    }
	
	}


	@Override
	public void update(java.util.Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}



}
