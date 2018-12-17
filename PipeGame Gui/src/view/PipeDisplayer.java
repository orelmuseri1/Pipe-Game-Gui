package view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Window;
import javafx.application.Application;
import javafx.application.HostServices;

public class PipeDisplayer extends Canvas{
	char[][] pipeData;
	private StringProperty backgroundFileName=null;
	private StringProperty startFileName=null;
	private StringProperty goalFileName=null;
	private StringProperty anglePipeFileName=null;
	private StringProperty verticalPipeFileName=null;
	private Image Lp = null, widthLine=null, highLine=null ,jp=null ,Fp=null, seven=null, background= null, sp=null,gp=null;
	
	
		public PipeDisplayer() {
		this.backgroundFileName = new SimpleStringProperty();
		this.startFileName = new SimpleStringProperty();
		this.goalFileName = new SimpleStringProperty();
		this.anglePipeFileName = new SimpleStringProperty();
		this.verticalPipeFileName = new SimpleStringProperty();
		play();
			}
	//==================================================SETTERS AND GETTERS=====================================================//

	void setMazeData(char[][] pipeData) {
		this.pipeData=pipeData;
		try {
			redraw();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getBackgroundFileName() {
		return backgroundFileName.getValue();
	}


	public void setBackgroundFileName(String backgroundFileName) {
		this.backgroundFileName.setValue( backgroundFileName);
	}


	public String getStartFileName() {
		return startFileName.getValue();
	}


	public void setStartFileName(String startFileName) {
		this.startFileName.setValue(startFileName);
	}


	public String getGoalFileName() {
		return goalFileName.getValue();
	}


	public void setGoalFileName(String goalFileName) {
		this.goalFileName.setValue(goalFileName);
	}


	public String getAnglePipeFileName() {
		return anglePipeFileName.getValue();
	}


	public void setAnglePipeFileName(String anglePipeFileName) {
		this.anglePipeFileName.setValue(anglePipeFileName);
	}


	public String getVerticalPipeFileName() {
		return verticalPipeFileName.getValue();
	}


	public void setVerticalPipeFileName(String verticalPipeFileName) {
		this.verticalPipeFileName.setValue(verticalPipeFileName);
	}
	//==================================================OVERRIDE FOR RESIZABLE=====================================================//
	@Override
	public boolean isResizable() {
	    return true;
	}

    @Override
    public double minHeight(double width)
    {
        return 64;
    }

    @Override
    public double maxHeight(double width)
    {
        return 1000;
    }

    @Override
    public double prefHeight(double width)
    {
        return minHeight(width);
    }

    @Override
    public double minWidth(double height)
    {
        return 0;
    }

    @Override
    public double maxWidth(double height)
    {
        return 10000;
    }
	
    @Override
    public void resize(double width, double height)
    {
        super.setWidth(width);
        super.setHeight(height);
        try {
			this.redraw();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    //==========================================swap part in the game on click func============================================//
    
    void click(double x,double y) {
    	double W= getWidth();
		double H = getHeight();
		double w= (W / pipeData[0].length);
		double h= (H / pipeData.length);
		y-=h/pipeData.length;
    	for(double i=0;i<pipeData.length;i++)	{
			for (double j=0;j<pipeData[(int) i].length;j++)
			{
				
				if( (y>=i*h && y<=i*h+h )&&
					(x>=j*w && x<=j*w+w )) {
					switch (pipeData[(int) i][(int) j]) {
					case 'L':
						pipeData[(int) i][(int) j]= 'F' ;
						break;
					case 'j':
						pipeData[(int) i][(int) j]='L';
						break;
					case '-':
						pipeData[(int) i][(int) j]='|';
						break;
					case '|':
						pipeData[(int) i][(int) j]='-';
						break;
					case 'F':
						pipeData[(int) i][(int) j]='7';
						break;
					case '7':
						pipeData[(int) i][(int) j]='j';
						break;
					case 's':
						pipeData[(int) i][(int) j]='s';
						break;
					case 'g':
						pipeData[(int) i][(int) j]='g';
						break;
					default:
						break;
						}
			}
		}
    	 try {
			redraw();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
    	
    }
    	
    	//==================================================load all the pictuers	=======================================================//
    void loadPictuer() {
    	try {
			SnapshotParameters params = new SnapshotParameters();
			params.setFill(Color.TRANSPARENT);

			widthLine = new Image(new FileInputStream(verticalPipeFileName.get()));
			ImageView iv = new ImageView(widthLine);
			iv.setRotate(90);
			highLine = iv.snapshot(params, null);
			Lp= new Image(new FileInputStream(anglePipeFileName.get()));
			iv = new ImageView(Lp);
			iv.setRotate(90);
			Fp = iv.snapshot(params, null);
			iv.setRotate(180);
			seven = iv.snapshot(params, null);
			iv.setRotate(270);
			jp = iv.snapshot(params, null);
			sp = new Image(new FileInputStream(startFileName.get()));
			gp = new Image(new FileInputStream(goalFileName.get()));
			background= new Image(new FileInputStream(backgroundFileName.get()));

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	}
    		//================================================paint the game==================================================//
	void redraw() throws FileNotFoundException {
		if(pipeData!=null) {
			double W= getWidth();
			double H = getHeight();
			double w= (W / pipeData[0].length);
			double h= (H / pipeData.length);
			GraphicsContext gc=getGraphicsContext2D();
			gc.clearRect(0, 0, W, H);
			loadPictuer();
			Image pipeImage;
			for(int i=0;i<pipeData.length;i++)
			{
				for (int j=0;j<pipeData[i].length;j++)
				{
					switch (pipeData[i][j]) {
					case 'L':
						pipeImage=Lp;
						gc.drawImage(Lp, j*w, i*h, w, h);
						break;
					case 'j':
						pipeImage=jp;
						gc.drawImage(jp, j*w, i*h, w, h);
						break;
					case '-':
						pipeImage=widthLine;
						gc.drawImage(widthLine, j*w, i*h, w, h);
						break;
					case '|':
						pipeImage=highLine;
						gc.drawImage(highLine, j*w, i*h, w, h);
						break;
					case 'F':
						pipeImage=Fp;
						gc.drawImage(Fp, j*w, i*h, w, h);
						break;
					case '7':
						pipeImage=seven;
						gc.drawImage(seven, j*w, i*h, w, h);
						break;
					case 's':
						pipeImage=sp;
						gc.drawImage(sp, j*w, i*h, w, h);
						break;
					case 'g':
						pipeImage=gp;
						gc.drawImage(gp, j*w, i*h, w, h);
						break;
					case ' ':
						pipeImage=background;
						break;
					default:
						pipeImage=background;
						break;
						
					}
					if(pipeImage!=null)
					gc.drawImage(pipeImage, j*w, i*h, w, h);
					
				}
			}
		}
	}
	
	void play() {
		String musicFile = "./resources/icy_tower.mp3";  
		Media sound = new Media(new File(musicFile).toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.setCycleCount(5);
			mediaPlayer.play();
		
	}
}

