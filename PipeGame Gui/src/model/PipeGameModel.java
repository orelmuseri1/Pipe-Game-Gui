package model;

import java.util.Observable;

public class PipeGameModel extends Observable implements gameModel {

	char[][] gameSolution;

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
	
}