package model;

import javafx.beans.property.StringProperty;

public interface gameModel {
	public void solveGame();
	public String getSolution();
	public StringProperty getItemState(int i, int j);
	public void itemPressed(int i, int j);
}
