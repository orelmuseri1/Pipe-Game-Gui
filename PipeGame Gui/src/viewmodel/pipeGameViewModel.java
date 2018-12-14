package viewmodel;

import java.util.Observable;
import java.util.Observer;

import model.gameModel;


public class pipeGameViewModel extends Observable implements Observer {
	
	gameModel m;
	
	public pipeGameViewModel(gameModel m) {
		this.m = m;
	}

	@Override
	public void update(Observable observable, Object obj) {
		// TODO Auto-generated method stub
	}
}