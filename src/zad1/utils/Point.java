package zad1.utils;

public class Point {

	private int x;
	private int y;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

	public Point translate(Point dp) {
		// vraća NOVU točku translatiranu za argument tj. THIS+DP...
		return new Point(x + dp.getX(), y + dp.getY());
	}
	
	public Point difference(Point p) {
		// vraća NOVU točku koja predstavlja razliku THIS-P...
		return new Point(x - p.getX(), y - p.getY());
	}
	
}