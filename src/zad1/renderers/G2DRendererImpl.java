package zad1.renderers;

import java.awt.Color;
import java.awt.Graphics2D;

import zad1.utils.Point;

public class G2DRendererImpl implements Renderer {

	
	private Graphics2D g2d;
	
	public G2DRendererImpl(Graphics2D g2d) {
		this.g2d = g2d;
	}
	
	@Override
	public void drawLine(Point s, Point e) {
		// Postavi boju na plavu
		// Nacrtaj linijski segment od S do E
		// (sve to uporabom g2d dobivenog u konstruktoru)
		g2d.setColor(Color.BLUE);
		g2d.drawLine(s.getX(), s.getY(), e.getX(), e.getY());
	}

	@Override
	public void fillPolygon(Point[] points) {
		// Postavi boju na plavu
		// Popuni poligon definiran danim točkama
		// Postavi boju na crvenu
		// Nacrtaj rub poligona definiranog danim točkama
		// (sve to uporabom g2d dobivenog u konstruktoru)

		// postoji metoda fillPolygon i drawPolygon koja prima niz x i y koordinata
		g2d.setColor(Color.BLUE);
		int[] x_koordinate = new int[points.length];
		int[] y_koordinate = new int[points.length];
		for(int i = 0; i<points.length; i++) {
			x_koordinate[i] = points[i].getX();
			y_koordinate[i] = points[i].getY();
		}
		g2d.fillPolygon(x_koordinate, y_koordinate, points.length);
		g2d.setColor(Color.RED);
		g2d.drawPolygon(x_koordinate, y_koordinate, points.length);
	}

}
