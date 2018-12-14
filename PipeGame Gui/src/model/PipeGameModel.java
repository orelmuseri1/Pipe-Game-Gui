package model;

import java.util.Observable;

public class PipeGameModel extends Observable implements gameModel {

	int n; // Size of the board.
	int numOfMovedPlayed; // Number of moved played by a given player.
	char[][] pipeBoardState; // Current state of the board.
	char[][] gameSolution; // The game end solution given by the server.

	public PipeGameModel(int boardSize,char[][] board) {
		// TODO Auto-generated constructor stub
		this.n= boardSize;
		this.numOfMovedPlayed = 0;
		for (int i = 0 ; i < n ; ++i)
			for (int j =0 ; j < n ; ++j) {
				this.gameSolution[i][j] = board[i][j];
			}
		this.gameSolution = getSolutionFromServer(this.pipeBoardState);
	}
	
	public PipeGameModel(int boardSize,char[][] board,int numberOfmovesAlreadyPlayed) {
		// TODO Auto-generated constructor stub
		this.n= boardSize;
		this.numOfMovedPlayed = numberOfmovesAlreadyPlayed;
		for (int i = 0 ; i < n ; ++i)
			for (int j =0 ; j < n ; ++j) {
				this.gameSolution[i][j] = board[i][j];
			}
		this.gameSolution = getSolutionFromServer(this.pipeBoardState);
	}
	/*
	 * getSolutionFromServer will you send the pipeBoardState init 
	 * board and will receive back the solution.
	 */
	private char[][] getSolutionFromServer(char[][] boardToSendTotheServer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void solveGame(char[][] gameData) {
		// TODO Auto-generated method stub

		setChanged(); 
		notifyObservers();
	}

	@Override
	public char[][] getSolution() {
		return this.gameSolution;
	}
	
	/*
	 * isSolution responsible on return true or false is the 
	 * saved state of the pipe game is correct.
	 * It will run char char and compare Solution
	 * to saved state.
	 */

	public boolean isSolution() {
		for (int i = 0 ; i < n ; ++i)
			for (int j =0 ; j < n ; ++j) {
				if (pipeBoardState[i][j] != gameSolution[i][j])
					return false;
			}
		return true;
	}
}