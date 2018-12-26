package viewmodel;

import java.util.Observable;
import java.util.Observer;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.PipeGameModel;


public class pipeGameViewModel extends Observable implements Observer {
	
	PipeGameModel model;
	int numOfRows;
	int numOfCols;
	public StringProperty[][] mazeState; 
	
	public pipeGameViewModel(PipeGameModel m,int numOfRows,int numOfCols) {
		this.model = m;
		this.numOfRows = numOfRows;
		this.numOfCols = numOfCols;
		this.mazeState = new StringProperty[numOfRows][numOfCols];
		for (int i = 0 ; i < numOfRows ; ++i)
			for (int j = 0 ; j < numOfCols ; ++j) {
				this.mazeState[i][j] = new SimpleStringProperty(model.getItemState(i, j).get()); 
				this.mazeState[i][j].bindBidirectional(model.getItemState(i,j));
			}
		
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
	public void newMaze(char[][] charArray, int numOfRows,int numOfCols) {
		// TODO Auto-generated method stub
		this.model = new PipeGameModel(charArray, numOfRows, numOfCols); 
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
}