package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;

public class PipeGameModel extends Observable implements gameModel {

	// Properties
	int numOfRows;
	int numOfCols;
	public IntegerProperty numberOfSteps; // number of steps made since game start
	public IntegerProperty time; // Time pass since game start
	StringProperty[][] PipeBoardState; // Current state of the board.
	StringProperty gameSolution; // The game end solution given by the server.
	int[] sPos,gPos;
	Timer timeCounter  = new Timer();
	TimerTask task = new TimerTask() {
		public void run(){
			time.set(time.get() + 1);
		}
	};

	// CTOR
	public PipeGameModel(char[][] board ,int numOfRows,int numOfCols) {
		this.numOfRows = numOfRows;
		this.numOfCols = numOfCols;
		this.PipeBoardState = new StringProperty[numOfRows][numOfCols];
		this.gameSolution=new SimpleStringProperty();
		this.gameSolution.set("");
		
		// Copy board
		for (int i = 0 ; i < numOfRows; ++i)
			for (int j = 0 ; j < numOfCols; ++j) {
				this.PipeBoardState[i][j] = new SimpleStringProperty(Character.toString(board[i][j]));
			}
		
		// Search for the s and g, place them in int array, first num is row second is col.
		this.sPos = new int[2];
		this.gPos = new int[2];
		for (int i = 0 ; i < numOfRows; ++i)
			for (int j = 0 ; j < numOfCols; ++j) {
				if (this.PipeBoardState[i][j].get().charAt(0) == 's') {
					sPos[0] = i;
					sPos[1] = j;
				}
				if (this.PipeBoardState[i][j].get().charAt(0) == 'g') {
					gPos[0] = i;
					gPos[1] = j;
				}
			}
		
		// Set timer
		this.time = new SimpleIntegerProperty();
		this.numberOfSteps = new SimpleIntegerProperty(0);
		timeCounter.scheduleAtFixedRate(task, 1000, 1000);
	}
	
	// Methods

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
        outToServer.println(problem);
        outToServer.flush();
        outToServer.println("done");
        outToServer.flush();
        String solution = inFromServer.readLine();
        outToServer.println("done");
        outToServer.flush();
        inFromServer.close();
        outToServer.close();
        inFromUser.close();
        theServer.close();
        return solution;     //this.gameSolution.getValue()
	}

	@Override
	public void solveGame() {
		   Task<Void> task = new Task<Void>() {
			   protected Void call() {
		try {
			gameSolution.set(getSolutionFromServer());
			String[] solution = (gameSolution.getValue()).split(",");
			for (int i = 0 ; i < solution.length; i += 3) {
				for (int j = 0 ; j < Integer.parseInt(solution[i+2]); ++j) {
					int rowToPress = Integer.parseInt(solution[i]);
					int colToPress = Integer.parseInt(solution[i+1]);
					Platform.runLater(() -> itemPressed(rowToPress, colToPress));
					Thread.sleep(500);
					}
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null; 
		   }
		   };
	        new Thread(task).start();
	}

	
	@Override
	public String getSolution() {
		return this.gameSolution.getValue();
	}
	

	public boolean isSolution() {
		
		/*
		 *  This function will call a recursive function to 
		 *  any move available from the s position on the current 
		 *  board state
		 */
		
		for (int i = 0; i < 4 ; i++) {
			if(validMove(sPos[0],sPos[1],i))
				return true;
		}
		return false;
	}
	
	// lets try valid enters
	private boolean validMove(int BrickRow,int BrickCol,int enterNode) {
		
		/*
		 * Will check if the move that was made in our search for
		 * the g is even valid.
		 * if so the pathToGoal recursive func will be called.
		 */
		
		switch(enterNode) {
		case 0:
			// checks if there's an above brick
			if (BrickRow-1 >= 0 && validTouch(this.PipeBoardState[BrickRow][BrickCol].get().charAt(0),this.PipeBoardState[BrickRow-1][BrickCol].get().charAt(0),2))
				return pathToGoal(BrickRow-1,BrickCol,2);
			break;
		case 1:
			// check if there's an right brick
			if (BrickCol+1 <= numOfCols - 1 && validTouch(this.PipeBoardState[BrickRow][BrickCol].get().charAt(0),this.PipeBoardState[BrickRow][BrickCol+1].get().charAt(0),3))
				return pathToGoal(BrickRow,BrickCol+1,3);	
			break;
		case 2:
			// check if there's an down brick
			if (BrickRow+1 <= numOfRows - 1 && validTouch(this.PipeBoardState[BrickRow][BrickCol].get().charAt(0),this.PipeBoardState[BrickRow+1][BrickCol].get().charAt(0),0))
				return pathToGoal(BrickRow+1,BrickCol,0);
			break;
		case 3:
			// check if there's an left brick
			if (BrickCol-1 >= 0 && validTouch(this.PipeBoardState[BrickRow][BrickCol].get().charAt(0),this.PipeBoardState[BrickRow][BrickCol-1].get().charAt(0),1))
				return pathToGoal(BrickRow,BrickCol-1,1);	
			break;
		}
		return false;
	}

	private boolean validTouch(char c, char d,int enterNodeTod) {
		if (c == ' ' || d == ' ')
			return false;
		
		if (c == '|') {
			switch(enterNodeTod) {
				case(2):
					if (d == '|' || d == '7' || d == 'F' || d == 's' || d == 'g')
						return true;
					break;
				case(0):
					if (d == '|' || d == 'L' || d == 'J' || d == 's' || d == 'g')
						return true;
					break;
				default:
					return false;
			}
		}
		if (c == '-') {
			switch(enterNodeTod) {
				case(3):
					if (d == '-' || d == '7' || d == 'J' || d == 's' || d == 'g')
						return true;
					break;
				case(1):
					if (d == '-' || d == 'L' || d == 'F' || d == 's' || d == 'g')
						return true;
					break;
				default:
					return false;
			}
		}
		if (c == 'L') {
			switch(enterNodeTod) {
				case(2):
					if (d == '|' || d == '7' || d == 'F' || d == 's' || d == 'g')
						return true;
					break;
				case(3):
					if (d == '-' || d == '7' || d == 'J' || d == 's' || d == 'g')
						return true;
					break;
				default:
					return false;
			}
		}
		if (c == 'J') {
			switch(enterNodeTod) {
				case(2):
					if (d == '|' || d == '7' || d == 'F' || d == 's' || d == 'g')
						return true;
					break;
				case(1):
					if (d == '-' || d == 'F' || d == 'L' || d == 's' || d == 'g')
						return true;
					break;
				default:
					return false;
			}
		}
		if (c == '7') {
			switch(enterNodeTod) {
				case(0):
					if (d == '|' || d == 'J' || d == 'L' || d == 's' || d == 'g')
						return true;
					break;
				case(1):
					if (d == '-' || d == 'F' || d == 'L' || d == 's' || d == 'g')
						return true;
					break;
				default:
					return false;
			}
		}
		if (c == 'F') {
			switch(enterNodeTod) {
				case(0):
					if (d == '|' || d == 'J' || d == 'L' || d == 's' || d == 'g')
						return true;
					break;
				case(3):
					if (d == '-' || d == 'J' || d == '7' || d == 's' || d == 'g')
						return true;
					break;
				default:
					return false;
			}
		}
		if (c == 's') {
			switch(enterNodeTod) {
			case(0):
				if (d == '|' || d == 'J' || d == 'L' || d == 'g')
					return true;
				break;
			case(1):
				if (d == '-' || d == 'F' || d == 'L' || d == 'g')
					return true;
				break;
			case(2):
					if (d == '|' || d == 'F' || d == '7' || d == 'g')
						return true;
					break;
			case(3):
					if (d == '-' || d == 'J' || d == '7' || d == 'g')
						return true;
					break;
				default:
					return false;
			}
		}
		if (c == 'g') {
			switch(enterNodeTod) {
			case(0):
				if (d == '|' || d == 'J' || d == 'L' || d == 'g')
					return true;
				break;
			case(1):
				if (d == '-' || d == 'F' || d == 'L' || d == 'g')
					return true;
				break;
			case(2):
					if (d == '|' || d == 'F' || d == '7' || d == 'g')
						return true;
					break;
			case(3):
					if (d == '-' || d == 'J' || d == '7' || d == 'g')
						return true;
					break;
				default:
					return false;
					}
		}
		return false;
	}
	
	// Helping method for isGoal()
	private boolean pathToGoal(int BrickRow, int BrickCol,int enterNode) {
		// Don't return to S
		if (BrickRow == sPos[0] && BrickCol == sPos[1])
			return false;
		// if we have reached G return true
		if (BrickRow == gPos[0] && BrickCol == gPos[1])
			return true;
		for (int i = 0; i < 4 ; i++) {
			// Checks we aren't returning back
			char go = this.PipeBoardState[BrickRow][BrickCol].get().charAt(0);
			if (i != enterNode) {
				if (PossibleExit(go,i)) {
					if(validMove(BrickRow,BrickCol,i))
						return true;
			
				}
		}
		}
		return false;
	}
	
	private boolean PossibleExit(char go, int enterNode) {
		if (go == 's' || go == 'g')
			return true;
		 switch(go) {
			case '|':
				if(enterNode==2)
						return true;
				if(enterNode==0)
						return true;
				break;
			case 'L':
				if(enterNode==0)
						return true;
				if(enterNode==1)
						return true;
				break;
			case 'F':
				if(enterNode==1)
						return true;
				if(enterNode==2)
						return true;
				break;
			case '7':
				if(enterNode==3)
						return true;
				if(enterNode==2)
						return true;
				break;
			case 'J':
				if(enterNode==0)
						return true;
				if(enterNode==3)
						return true;
				break;
			case '-':
				if(enterNode==3)
						return true;
				if(enterNode==1)
						return true;
				break;
					
			}
		return false;
	}
	
	@Override
	public void itemPressed(int i , int j) {
		
			switch (this.PipeBoardState[i][j].get().charAt(0)){
			case 'L':
				this.PipeBoardState[i][j].set(Character.toString('F'));
				break;
			case 'J':
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
			this.numberOfSteps.set(this.numberOfSteps.get() + 1);
			// This next to lines will notify the viewModel that will next change the view
			setChanged();
			notifyObservers();
	}

	@Override
	public StringProperty getItemState(int i, int j) {
		return PipeBoardState[i][j];
	}

	
}