package viewmodel;

import java.util.Observable;
import java.util.Observer;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.PipeGameModel;


public class pipeGameViewModel extends Observable implements Observer {
	
	public PipeGameModel model;
	int numOfRows;
	int numOfCols;
	public StringProperty ip;
	public IntegerProperty port;
	public StringProperty[][] mazeState; 
	public IntegerProperty time; // Time pass since game start
	public IntegerProperty numberOfSteps; // number of steps made since game start
	
	public pipeGameViewModel(PipeGameModel m,int numOfRows,int numOfCols) {
		ip = new SimpleStringProperty();
		ip.bindBidirectional(m.ip);
		port = new SimpleIntegerProperty();
		port.bindBidirectional(m.port);
		this.model = m;
		this.numOfRows = numOfRows;
		this.numOfCols = numOfCols;
		this.mazeState = new StringProperty[numOfRows][numOfCols];
		for (int i = 0 ; i < numOfRows ; ++i)
			for (int j = 0 ; j < numOfCols ; ++j) {
				this.mazeState[i][j] = new SimpleStringProperty(model.getItemState(i, j).get()); 
				this.mazeState[i][j].bindBidirectional(model.getItemState(i,j));
			}
		this.time = new SimpleIntegerProperty();
		this.time.bind(this.model.time);
		this.numberOfSteps = new SimpleIntegerProperty();
		this.numberOfSteps.bindBidirectional(this.model.numberOfSteps);

	}

	public pipeGameViewModel(pipeGameViewModel viewModel) {
		// TODO Auto-generated constructor stub
		this.numOfRows = viewModel.numOfRows;
		this.numOfCols = viewModel.numOfCols;
		this.model = viewModel.model;
		this.mazeState = new StringProperty[numOfRows][numOfCols];
		for (int i = 0 ; i < this.numOfRows ; ++i)
			for (int j = 0 ; j < this.numOfCols ; ++j) {
				this.mazeState[i][j] = new SimpleStringProperty(viewModel.mazeState[i][j].get());
				this.mazeState[i][j].bindBidirectional(model.getItemState(i,j));
			}
		this.time = new SimpleIntegerProperty();
		this.time.bind(this.model.time);
		this.numberOfSteps = new SimpleIntegerProperty();
		this.numberOfSteps.bindBidirectional(this.model.numberOfSteps);
	}

	public void itemPressed(int i,int j) {
		model.itemPressed(i,j);
	}
	
	public int getNumOfRows() {
		return this.numOfRows;
	}
	
	public int getNumOfCols() {
		return this.numOfCols;
	}
	
	
	public void solveGame() {
		// TODO Auto-generated method stub
		model.solveGame();
	}
	
	/*
	 *  When a new maze is open from a text file,
	 *  this method will get the char array that
	 *  was extracted and will create a new model.
	 */
	public void newMaze(char[][] charArray, int numOfRows,int numOfCols, IntegerProperty givenTime, IntegerProperty givenNumberOfSteps) {
		// TODO Auto-generated method stub
		this.model = new PipeGameModel(charArray, numOfRows, numOfCols,givenTime,givenNumberOfSteps); 
		this.numOfRows = numOfRows;
		this.numOfCols = numOfCols;
		this.time.unbind();
		this.time.set(givenTime.get());
		this.numberOfSteps.unbind();
		this.numberOfSteps.set(givenNumberOfSteps.get());
		this.mazeState = new StringProperty[numOfRows][numOfCols];
		for (int i = 0 ; i < numOfRows ; ++i)
			for (int j = 0 ; j < numOfCols; ++j) {
				this.mazeState[i][j] = new SimpleStringProperty(model.getItemState(i, j).get()); 
				this.mazeState[i][j].bindBidirectional(model.getItemState(i,j));
			}
	}
	
	public void newMaze(char[][] charArray, int numOfRows,int numOfCols) {
		// TODO Auto-generated method stub
		this.model = new PipeGameModel(charArray, numOfRows, numOfCols); 
		this.numOfRows = numOfRows;
		this.numOfCols = numOfCols;
		this.mazeState = new StringProperty[numOfRows][numOfCols];
		for (int i = 0 ; i < numOfRows ; ++i)
			for (int j = 0 ; j < numOfCols; ++j) {
				this.mazeState[i][j] = new SimpleStringProperty(model.getItemState(i, j).get()); 
				this.mazeState[i][j].bindBidirectional(model.getItemState(i,j));
			}
	}
	
	/*
	 * 
	 * When update happened we will match hour board with the board
	 * showing in the data model.
	 *  
	 */
	@Override
	public void update(Observable observable, Object obj) {
		// TODO Auto-generated method stub
		setChanged();
		notifyObservers();
	}

	public boolean isSolution() {
		return this.model.isSolution();
	}	
}