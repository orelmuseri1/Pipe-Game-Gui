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

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.PipeGameModel;
import viewmodel.pipeGameViewModel;

public class MainWindowController implements Initializable,Observer{
	private int theme = 1;
	int numOfRows;
	int numOfCols;
    pipeGameViewModel vm;
	public StringProperty[][] pipeData;
	IntegerProperty time; // Time pass since game start
	@FXML
	PipeDisplayer pipeDisplayer;
	
	public void setViewModel(pipeGameViewModel viewModel) {
		this.vm = new pipeGameViewModel(viewModel);
		this.pipeData = new StringProperty[viewModel.getNumOfRows()][viewModel.getNumOfCols()];
		for (int i = 0 ; i < vm.getNumOfRows() ; ++i) {
			for (int j = 0 ; j < vm.getNumOfCols() ; ++j) {
				this.pipeData[i][j] = new SimpleStringProperty(vm.mazeState[i][j].get());
				vm.mazeState[i][j].bindBidirectional(pipeData[i][j]);
			}
		}
		this.time = new SimpleIntegerProperty();
		this.time.bind(this.vm.time);
	}
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		char[][] pipeData = {
				{'s','J','L'},
				{'|','L','g'}
		};
		
		this.numOfRows = 2;
		this.numOfCols= 3;
		PipeGameModel pgm = new PipeGameModel(pipeData,numOfRows,numOfCols);
		this.setViewModel(new pipeGameViewModel(pgm, numOfRows,numOfCols));
		pgm.addObserver(this.vm);
		vm.addObserver(this);
		pipeDisplayer.setMazeData(this.pipeData,numOfRows,numOfCols);
		pipeDisplayer.addEventFilter(MouseEvent.MOUSE_CLICKED, (e)->pipeDisplayer.requestFocus());
		EventHandler<MouseEvent> circleOnMouseEventHandler = new EventHandler<MouseEvent>() {	  
			@Override
			public void handle(MouseEvent event) {
				double x=event.getSceneX();
				double y=event.getSceneY();
				MainWindowController.this.click(x, y);
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
			vm.newMaze(charArray,lines.size(),lines.get(0).length);
			pipeDisplayer.setMazeData(this.pipeData,this.numOfRows,this.numOfCols);
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

		for (int i = 0; i < vm.getNumOfRows(); i++) {
			for (int j = 0; j < vm.getNumOfCols(); j++) {
				outFile.print(vm.mazeState[i][j].get().charAt(0));
			}
			outFile.println();
		}

		outFile.close();
	}
	
	public void solveGame() {
		try {
			this.vm.solveGame();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public void theme1() {
		if (this.theme != 1) {
			this.theme = 1;
			//this.pipeDisplayer = new PipeDisplayer(1);
		}
	};

	public void theme2() {
		if (this.theme != 2) {
			this.theme = 2;
			//this.pipeDisplayer = new PipeDisplayer(2);
	}};
	
	public void status() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Game Status");
		alert.setHeaderText(null);
		alert.setContentText("Time:  " + this.time.get() + " Seconds");

		alert.showAndWait();
	}
	
	public void edit() {
		try {
	        FXMLLoader fxmlLoader = new FXMLLoader();
	        fxmlLoader.setLocation(getClass().getResource("/view/MainWindow.fxml"));
	        /* 
	         * if "fx:controller" is not set in fxml
	         * fxmlLoader.setController(NewWindowController);
	         */ 
	        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
	        Stage stage = new Stage();
	        stage.setTitle("EDIT_IP_AND_PORT");
	        stage.setScene(scene);
	        stage.show();
	    } catch (IOException e) {
	        Logger logger = Logger.getLogger(getClass().getName());
	        logger.log(Level.SEVERE, "Failed to create new Window.", e);
	    }
	
	}

    void click(double x,double y) {
    	double W= this.pipeDisplayer.getWidth();
		double H = this.pipeDisplayer.getHeight();
		double w= (W / pipeData[0].length);
		double h= (H / pipeData.length);
		y-=h/pipeData.length;
    	for(double i=0;i<pipeData.length;i++)	{
			for (double j=0;j<pipeData[(int) i].length;j++)
			{
				if( (y>=i*h && y<=i*h+h ) && (x>=j*w && x<=j*w+w )) {
					switch (pipeData[(int) i][(int) j].get().charAt(0)) {
					case 'L':
						pipeData[(int) i][(int) j].set(Character.toString('F'));
						break;
					case 'J':
						pipeData[(int) i][(int) j].set(Character.toString('L'));
						break;
					case '-':
						pipeData[(int) i][(int) j].set(Character.toString('|'));
						break;
					case '|':
						pipeData[(int) i][(int) j].set(Character.toString('-'));
						break;
					case 'F':
						pipeData[(int) i][(int) j].set(Character.toString('7'));
						break;
					case '7':
						pipeData[(int) i][(int) j].set(Character.toString('J'));
						break;
					case 's':
						pipeData[(int) i][(int) j].set(Character.toString('s'));
						break;
					case 'g':
						pipeData[(int) i][(int) j].set(Character.toString('g'));
						break;
					default:
						break;
						}
					}
				}
			pipeDisplayer.setMazeData(this.pipeData,this.numOfRows,this.numOfCols);
			}
    	
    		// Create popUp if the cilck gave the solution
    		if(this.vm.isSolution()) {
    			Alert alert = new Alert(AlertType.INFORMATION);
    			alert.setTitle("You win!!!!!");
    			alert.setHeaderText(null);
    			alert.setContentText("Great job friend!\nWe knew we can count on you!");

    			alert.showAndWait();	
    		}
    	}

	@Override
	public void update(java.util.Observable o, Object arg) {
		// TODO Auto-generated method stub
		pipeDisplayer.setMazeData(this.pipeData,this.numOfRows,this.numOfCols);
	}



}
