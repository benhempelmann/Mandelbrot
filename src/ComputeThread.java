import java.awt.Color;
import java.awt.Point;

public class ComputeThread extends Thread{

	private int xStart,xEnd, yStart, yEnd;
	public static final int MAX_ITER = 1500;
	public static final double INFINITY = 10e8;
	private Coordinate center;
	private Color brotArray[][];
	private Point origin;
	private int WIDTH, HEIGHT;
	private double zoomFactor;
	
	public ComputeThread(int xStart, int xEnd, int yStart, int yEnd, int WIDTH, int HEIGHT, Coordinate center, double zoomFactor, Point origin, Color brotArray[][]) {
		this.xStart = xStart;
		this.xEnd = xEnd;
		this.yStart = yStart;
		this.yEnd = yEnd;
		this.origin = origin;
		this.center = center;
		this.brotArray = brotArray;
		center = new Coordinate(0,0);
		this.zoomFactor = zoomFactor;
	}
	
	@Override
	public void run() {
		calcSection(xStart,xEnd,yStart,yEnd);
	}
	public void calcSection(int xStart, int xEnd, int yStart, int yEnd) {
		Point checkPoint = new Point();
		for(int i=xStart; i<xEnd;i++) {
			for(int j = yStart; j< yEnd;j++) {
				checkPoint.x = i;
				checkPoint.y = j;
				brotArray[i][j] = (checkPixel(checkPoint));
			}
		}
	}

	private Color checkPixel(Point pix) {
		//x: -2 -> 0.5
		//y: -1 -> 1
		//center: 400, 250 -> 0,0
		//z_1 = z^2 + c
		Coordinate c = pixelToCoord(pix);
		Coordinate z = new Coordinate(0,0);

		for (int i=0;i<MAX_ITER;i++) {
			z = mandelEqn(z,c);
			if (Math.abs(z.x) > INFINITY || Math.abs(z.y) > INFINITY ) {
				return Color.white;
			}
		}   	
		return Color.black;
	}

	private Coordinate mandelEqn(Coordinate z, Coordinate c) {
		double real = z.x * z.x + z.y * z.y * -1;
		double imaginary = 2 * z.x * z.y;
		real += c.x;
		imaginary += c.y;
		Coordinate newCoord = new Coordinate(real, imaginary);
		return newCoord;
	}

	Coordinate pixelToCoord(Point pix) {
		double x = (double)(pix.x - origin.x) / (zoomFactor*origin.x) + center.x;
		double y = (double)(pix.y - origin.y) / (zoomFactor*origin.y) + center.y;
		Coordinate temp = new Coordinate(x, y);
		return temp;
	}


}
