package view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Window;

public class MazeDisplayer extends Canvas{
	char[][] mazeData;
	private StringProperty backgroundFileName=null;
	private StringProperty startFileName=null;
	private StringProperty goalFileName=null;
	private StringProperty anglePipeFileName=null;
	private StringProperty verticalPipeFileName=null;
	private Image Lp = null, widthLine=null, highLine=null ,jp=null ,Fp=null, seven=null, background= null, sp=null,gp=null;
	
	//==================================================SETTERS AND GETTERS=====================================================//
	public MazeDisplayer() {
		this.backgroundFileName = new SimpleStringProperty();
		this.startFileName = new SimpleStringProperty();
		this.goalFileName = new SimpleStringProperty();
		this.anglePipeFileName = new SimpleStringProperty();
		this.verticalPipeFileName = new SimpleStringProperty();
			}
	void setMazeData(char[][] mazeData) {
		this.mazeData=mazeData;
	 //   widthProperty().addListener(evt -> redraw());
     //   heightProperty().addListener(evt -> redraw());
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
    
	void redraw() throws FileNotFoundException {
		if(mazeData!=null) {
			double W= getWidth();
			double H = getHeight();
			double w= (W / mazeData[0].length);
			double h= (H / mazeData.length);
			GraphicsContext gc=getGraphicsContext2D();
			gc.clearRect(0, 0, W, H);
			
			//=======================load all the pictuers	===============================//
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
			/*
			try {
				sp = new Image(new FileInputStream("./resources/s.jpg"));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	try {
				gp = new Image(new FileInputStream("./resources/g.jpg"));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				Lp = new Image(new FileInputStream("./resources/L.jpg"));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				 widthLine = new Image(new FileInputStream("./resources/-.jpg"));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				 highLine = new Image(new FileInputStream("./resources/|.jpg"));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				 jp = new Image(new FileInputStream("./resources/j.jpg"));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				 Fp = new Image(new FileInputStream("./resources/F.jpg"));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				 seven = new Image(new FileInputStream("./resources/7.jpg"));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				 nothing = new Image(new FileInputStream("./resources/nothing.jpg"));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
			//paint the game
			Image pipeImage;
			for(int i=0;i<mazeData.length;i++)
			{
				for (int j=0;j<mazeData[i].length;j++)
				{
					switch (mazeData[i][j]) {
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
}

