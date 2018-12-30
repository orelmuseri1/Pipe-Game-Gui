package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PipeGameModel extends Observable implements gameModel {

	int numOfRows;
	int numOfCols;
	int countMoves; // Number of moves made
	StringProperty[][] PipeBoardState; // Current state of the board.
	StringProperty gameSolution; // The game end solution given by the server.
	public PipeGameModel(char[][] board ,int numOfRows,int numOfCols) {
		// TODO Auto-generated constructor stub
		this.numOfRows = numOfRows;
		this.numOfCols = numOfCols;
		this.PipeBoardState = new StringProperty[numOfRows][numOfCols];
		this.gameSolution=new SimpleStringProperty();
		for (int i = 0 ; i < numOfRows; ++i)
			for (int j = 0 ; j < numOfCols; ++j) {
				this.PipeBoardState[i][j] = new SimpleStringProperty(Character.toString(board[i][j]));
			}
		try {
			this.gameSolution.set(getSolutionFromServer());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * getSolutionFromServer will you send the pipeBoardState  
	 * board and will receive back the solution.
	 */
	private String getSolutionFromServer() throws UnknownHostException, IOException {
	    Socket theServer = new Socket("localhost", 10000);
        System.out.println("Connected to server");
	    BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(theServer.getInputStream()));
        PrintWriter outToServer = new PrintWriter(theServer.getOutputStream());
        String problem = "";
        for(int i=0;i < this.numOfRows;i++) {
    		for(int j=0;j<this.numOfCols;j++){
    			problem+=this.PipeBoardState[i][j].getValue();
    		}
    		if(i != this.numOfRows-1)
    			problem += "\r\n";
    	}   
        System.out.println(problem);
        outToServer.println(problem);
        outToServer.flush();
        outToServer.println("done");
        outToServer.flush();
        this.gameSolution.setValue(inFromServer.readLine());
        System.out.println(this.gameSolution.getValue());
        outToServer.println("done");
        outToServer.flush();
        inFromServer.close();
        outToServer.close();
        inFromUser.close();
        theServer.close();
        return null;     //this.gameSolution.getValue()
	}

	@Override
	public void solveGame() {
		try {
			this.gameSolution.set(getSolutionFromServer());
			System.out.println("My print");
			System.out.println(this.gameSolution.getValue());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	@Override
	public String getSolution() {
		return this.gameSolution.getValue();
	}
	
	/*
	 * isSolution responsible on return true or false is the 
	 * saved state of the pipe game is correct.
	 * It will run char char and compare Solution
	 * to saved state.
	 */

	/*public boolean isSolution() {
		for (int i = 0 ; i < numOfRows ; ++i)
			for (int j =0 ; j < numOfCols ; ++j) {
				if (PipeBoardState[i][j] != gameSolution[i][j])
					return false;
			}
		return true;
	}*/

	@Override
	public void itemPressed(int i , int j) {
		
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
				this.PipeBoardState[i][j].set(Character.toString('J'));
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
			try {
				Thread.sleep(200);
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