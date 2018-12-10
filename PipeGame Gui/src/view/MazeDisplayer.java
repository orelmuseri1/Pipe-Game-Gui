package view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class MazeDisplayer extends Canvas{
	char[][] mazeData;
	
	void setMazeData(char[][] mazeData) {
		this.mazeData=mazeData;
		redraw();
	}

	
	void redraw() {
		if(mazeData!=null) {
			double W= getWidth();
			double H = getHeight();
			double w= (W / mazeData[0].length);
			double h= (H / mazeData.length);
			GraphicsContext gc=getGraphicsContext2D();
			Image Lp = null, widthLine=null, highLine=null ,jp=null ,Fp=null, seven=null, nothing= null;
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
			
			
			for(int i=0;i<mazeData.length;i++)
			{
				for (int j=0;j<mazeData[i].length;j++)
				{
				//gc.fillRect(j*w, i*h, w, h);
					if(mazeData[i][j]== 'L') 		{gc.drawImage(Lp, j*w, i*h, w, h);}
					else if (mazeData[i][j]== 'j')  {gc.drawImage(jp, j*w, i*h, w, h);}
					else if (mazeData[i][j]== '-')  {gc.drawImage(widthLine, j*w, i*h, w, h);}
					else if (mazeData[i][j]== '|')  {gc.drawImage(highLine, j*w, i*h, w, h);}
					else if (mazeData[i][j]== 'F')   {gc.drawImage(Fp, j*w, i*h, w, h);}
					else if (mazeData[i][j]== '7')  {gc.drawImage(seven, j*w, i*h, w, h);}
					else {gc.drawImage(nothing, j*w, i*h, w, h);}
				}
			}
		}
	}
}
