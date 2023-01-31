import javax.swing.*;

import com.sun.management.VMOption.Origin;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;

public class Mandelbrot  extends JPanel {

	public static final int WIDTH = 700;
	public static final int HEIGHT = 700;
	public static final int MAX_THREADS = 4; //will be number squared for horizontal and vertical
	private double zoomFactor = .7;
	private Coordinate center;
	private Point origin;
	private Color brotArray[][];
	
	
	

	public Mandelbrot() {
		brotArray = new Color[WIDTH][HEIGHT];
		origin = new Point(WIDTH/2, HEIGHT/2);
		center = new Coordinate(0, 0);
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
	}

	public void zoom(Point zoomPix) {
		center = pixelToCoord(zoomPix);
		zoomFactor *= 5;
		System.out.println("Zoom Level: " + zoomFactor);
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		drawBrot(g);
	}

	private void drawBrot(Graphics g) {
		long startTime = System.currentTimeMillis();
		ComputeThread[][] threads = new ComputeThread[MAX_THREADS][MAX_THREADS];
		 for (int i=0;i<MAX_THREADS;i++) {
			for(int j = 0;j<MAX_THREADS;j++) {
				threads[i][j] = new ComputeThread(i*WIDTH/MAX_THREADS,(i+1)*WIDTH/MAX_THREADS,j*HEIGHT/MAX_THREADS,(j+1)*HEIGHT/MAX_THREADS, WIDTH, HEIGHT, center, zoomFactor, origin,brotArray);
				threads[i][j].start();
			}
		}
		
		boolean stop;
        do
        {
            stop=true;
            for(int i=0;i<MAX_THREADS;i++){
            	for(int j=0;j<MAX_THREADS;j++) {
            		if (threads[i][j].isAlive()) {
                        stop=false;
                    }
            	}
            }
        }while(!stop);
		
		
		
		for(int i = 0;i<WIDTH;i++) {
			for(int j=0;j<HEIGHT;j++) {
				g.setColor(brotArray[i][j]);
				g.drawLine(i, j, i, j);
			}
		}
		
//		System.out.print("Execution Time: ");
//		System.out.println(System.currentTimeMillis() - startTime);
//		System.out.println("ms with " + MAX_THREADS*MAX_THREADS + " Threads");
	}
	
	Coordinate pixelToCoord(Point pix) {
		double x = (double)(pix.x - origin.x) / (zoomFactor*origin.x) + center.x;
		double y = (double)(pix.y - origin.y) / (zoomFactor*origin.y) + center.y;
		Coordinate temp = new Coordinate(x, y);
		return temp;
	}

	


	//    public static void main(String[] args) {
	//    	Mandelbrot test = new Mandelbrot();
	//    	Point point = new Point(400,250);
	//    	Coordinate mycoord = test.pixelToCoord(point);
	//    }



}
