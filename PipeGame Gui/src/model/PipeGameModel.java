package model;

import java.util.Observable;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PipeGameModel extends Observable implements gameModel {

	int numOfRows;
	int numOfCols;
	int countMoves; // Number of moves made
	StringProperty[][] PipeBoardState; // Current state of the board.
	StringProperty[][] gameSolution; // The game end solution given by the server.

	public PipeGameModel(char[][] board ,int numOfRows,int numOfCols) {
		// TODO Auto-generated constructor stub
		this.numOfRows = numOfRows;
		this.numOfCols = numOfCols;
		this.PipeBoardState = new StringProperty[numOfRows][numOfCols];
		for (int i = 0 ; i < numOfRows; ++i)
			for (int j = 0 ; j < numOfCols; ++j) {
				this.PipeBoardState[i][j] = new SimpleStringProperty(Character.toString(board[i][j]));
			}
		//this.gameSolution = getSolutionFromServer(this.PipeBoardState);
	}

	/*
	 * getSolutionFromServer will you send the pipeBoardState  
	 * board and will receive back the solution.
	 */
	private char[][] getSolutionFromServer(char[][] boardToSendTotheServer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void solveGame() {
		// made for test only solution should come form server
		/*
		 * char[][] solution = {
		 
				{'s','7'},
				{'|','g'}
		};
		*/
		
		char[][] solution ={
				{'s','L','j',' ','L'},
				{'L','7','-','-',' '},
				{'7','L','-','7','7'},
				{'F','-','|','L','7'},
				{' ','j','|','L','g'},
				};
			 
		for (int i = 0 ; i < numOfRows; ++i)
			for (int j = 0 ; j < numOfCols; ++j)
				if (PipeBoardState[i][j].get().charAt(0) != solution[i][j]) {
					this.itemPressed(i, j);
					--j;
				}
		System.out.println("Game Solved");	
	}

	@Override
	public StringProperty[][] getSolution() {
		return this.gameSolution;
	}
	
	/*
	 * isSolution responsible on return true or false is the 
	 * saved state of the pipe game is correct.
	 * It will run char char and compare Solution
	 * to saved state.
	 */

	public boolean isSolution() {
		for (int i = 0 ; i < numOfRows ; ++i)
			for (int j =0 ; j < numOfCols ; ++j) {
				if (PipeBoardState[i][j] != gameSolution[i][j])
					return false;
			}
		return true;
	}

	@Override
	public void itemPressed(int i , int j) {
		try {
			switch (this.PipeBoardState[i][j].get().charAt(0)){
			case 'L':
				this.PipeBoardState[i][j].set(Character.toString('F'));
				break;
			case 'j':
				this.PipeBoardState[i][j].set(Character.toString('L'));
				break;
			case '-':
				this.PipeBoardState[i][j].set(Character.toString('|'));
				break;
			case '|':
				this.PipeBoardState[i][j].set(Character.toString('-'));
				break;
			case 'F':
				this.PipeBoardState[i][j].set(Character.toString('7'));
				break;
			case '7':
				this.PipeBoardState[i][j].set(Character.toString('j'));
				break;
			case 's':
				this.PipeBoardState[i][j].set(Character.toString('s'));
				break;
			case 'g':
				this.PipeBoardState[i][j].set(Character.toString('g'));
				break;
			default:
				break;
			}
			++countMoves;
			// This next to lines will notify the viewModel that will next change the view
			setChanged();
			notifyObservers();
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public StringProperty getItemState(int i, int j) {
		// TODO Auto-generated method stub
		return PipeBoardState[i][j];
	}

	
}