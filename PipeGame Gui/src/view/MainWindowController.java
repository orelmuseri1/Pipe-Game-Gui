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
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import model.PipeGameModel;
import viewmodel.pipeGameViewModel;

public class MainWindowController implements Initializable,Observer{
	private int theme = 1;
	int numOfRows;
	int numOfCols;
    pipeGameViewModel vm;
	public StringProperty[][] pipeData;
	IntegerProperty time; // Time pass since game start
	public IntegerProperty numberOfSteps; // number of steps made since game start
	int port = 10000;
	String ip = "localhost";
	@FXML
	PipeDisplayer pipeDisplayer;
	
	public void setViewModel(pipeGameViewModel viewModel) {
		this.vm = new pipeGameViewModel(viewModel);
		this.numOfRows = vm.getNumOfRows();
		this.numOfCols = vm.getNumOfCols();
		this.ip = vm.ip;
		this.port = vm.port;
		this.pipeData = new StringProperty[viewModel.getNumOfRows()][viewModel.getNumOfCols()];
		for (int i = 0 ; i < vm.getNumOfRows() ; ++i) {
			for (int j = 0 ; j < vm.getNumOfCols() ; ++j) {
				this.pipeData[i][j] = new SimpleStringProperty(vm.mazeState[i][j].get());
				vm.mazeState[i][j].bindBidirectional(pipeData[i][j]);
			}
		}
		vm.model.addObserver(this.vm);
		vm.addObserver(this);
		this.time = new SimpleIntegerProperty();
		this.time.bind(this.vm.time);
		this.numberOfSteps = new SimpleIntegerProperty();
		this.numberOfSteps.bindBidirectional(this.vm.numberOfSteps);
	
	}
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		char[][] pipeData = {
				{'s','J','L'},
				{'|','L','g'}
		};
		
		this.numOfRows = 2;
		this.numOfCols= 3;
		PipeGameModel pgm = new PipeGameModel(pipeData,numOfRows,numOfCols,port,ip);
		this.setViewModel(new pipeGameViewModel(pgm, numOfRows,numOfCols,port,ip));
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
					if(line.startsWith("Time=")) {
						this.time.unbind();
						this.time.set(Integer.parseInt(line.split("=")[1]));
					}
					else if(line.startsWith("Steps=")) {
						this.numberOfSteps.unbind();
						this.numberOfSteps.set(Integer.parseInt(line.split("=")[1]));
					}
					else {
						lines.add(line.toCharArray());
					}
				}
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			char[][] charArray = lines.toArray(new char[lines.size()][]);
			/*
			 * Because there is a possibility that 
			 * a level without time or steps
			 * we will check if the time property is still 
			 * bound if so, we will address it as a new level
			 */
			if (this.time.isBound())
				vm.newMaze(charArray,lines.size(),lines.get(0).length,port,ip);
			else
				vm.newMaze(charArray,lines.size(),lines.get(0).length,port,ip,this.time,this.numberOfSteps);
			this.setViewModel(vm);
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
		outFile.println("Time=" + this.time.get());
		outFile.println("Steps=" + this.numberOfSteps.get());
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
		alert.setContentText("Time:  " + this.time.get() + " Seconds\n" +"Moves made:  " + this.numberOfSteps.get());
		alert.showAndWait();
	}
	
	public void edit() {
		// Create the custom dialog.
		Dialog<String[]> dialog = new Dialog<>();
		dialog.setTitle("Game setting");
		dialog.setHeaderText("Change port and ip as you wish, be careful");
		
		// Set the button types.
		ButtonType doneButtonType = new ButtonType("Done", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(doneButtonType, ButtonType.CANCEL);	
		
		// Create the port and ip labels and fields.
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));
	
		TextField portField = new TextField();
		portField.setPromptText("port");
		TextField ipField = new TextField();
		ipField.setPromptText("ip");
		
		grid.add(new Label("port:"), 0, 0);
		grid.add(portField, 1, 0);
		grid.add(new Label("ip:"), 0, 1);
		grid.add(ipField, 1, 1);
		dialog.getDialogPane().setContent(grid);	
		Optional<String[]> result = dialog.showAndWait();
		
		if (result.isPresent()){
			System.out.println(Integer.parseInt(portField.getText()));
			this.port = Integer.parseInt(portField.getText());
			System.out.println(ipField.getText());
			this.ip = ipField.getText();
		}
		this.vm.newMaze(this.pipeDisplayer.pipeData, numOfRows, numOfCols, port, ip, this.time, this.numberOfSteps);
		this.setViewModel(vm);
		pipeDisplayer.setMazeData(this.pipeData,this.numOfRows,this.numOfCols);
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
						this.numberOfSteps.set(this.numberOfSteps.get() + 1);
						break;
					case 'J':
						pipeData[(int) i][(int) j].set(Character.toString('L'));
						this.numberOfSteps.set(this.numberOfSteps.get() + 1);
						break;
					case '-':
						pipeData[(int) i][(int) j].set(Character.toString('|'));
						this.numberOfSteps.set(this.numberOfSteps.get() + 1);
						break;
					case '|':
						pipeData[(int) i][(int) j].set(Character.toString('-'));
						this.numberOfSteps.set(this.numberOfSteps.get() + 1);
						break;
					case 'F':
						pipeData[(int) i][(int) j].set(Character.toString('7'));
						this.numberOfSteps.set(this.numberOfSteps.get() + 1);
						break;
					case '7':
						pipeData[(int) i][(int) j].set(Character.toString('J'));
						this.numberOfSteps.set(this.numberOfSteps.get() + 1);
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
