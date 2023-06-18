package zad1.shapes.concrete;

import java.util.List;
import java.util.Stack;

import zad1.renderers.Renderer;
import zad1.shapes.AbstractGraphicalObject;
import zad1.shapes.GraphicalObject;
import zad1.utils.GeometryUtil;
import zad1.utils.Point;
import zad1.utils.Rectangle;

public class Oval extends AbstractGraphicalObject{
	
	public Oval() {
		this(new Point[] {new Point(0,10), new Point(10,0)});
	}
	
	public Oval(Point[] hotPoints) {
		super(hotPoints);
	}
	
	@Override
	public String getShapeName() {
		return "Oval";
	}
	
	@Override
	public GraphicalObject duplicate() {
		Point[] newHotPoints = new Point[this.getNumberOfHotPoints()];
		for(int i = 0; i<this.getNumberOfHotPoints(); i++) {
			newHotPoints[i] = this.getHotPoint(i);
		}
		return new Oval(newHotPoints);
	}
	
	// treba naci centar tako da se uzme presjecište pravca koji
	// prolazi kroz donji hotpoint te vodoravnog pravca koji prolazi kroz desni hotpoint
	@Override
	public Rectangle getBoundingBox() {
		Point bottomHotPoint = this.getHotPoint(0);
		Point rightHotPoint = this.getHotPoint(1);
		
		Point center = getCenter();

		double width = Math.abs(bottomHotPoint.getX() - rightHotPoint.getX());
		double height = Math.abs(bottomHotPoint.getY() - rightHotPoint.getY());

		int gornjiLijeviX = (int) (center.getX() - width);
		int gornjiLijeviY = (int) (center.getY() - height);
		
		return new Rectangle(gornjiLijeviX, gornjiLijeviY, (int)width*2, (int)height*2);
	}
	
	// gleda udaljenost od centra do tocke
	@Override
	public double selectionDistance(Point mousePoint) {
		// ako je tocka unutar elipse, vrati 0
		Point center = getCenter();
		int h = center.getX();
		int k = center.getY();
		int a = Math.abs(this.getHotPoint(0).getY() - k);
		int b = Math.abs(this.getHotPoint(1).getX() - h);
		// formula za elipsu je (x-h)^2/a^2 + (y-k)^2/b^2 = 1
		// uvrstimo tocku
		double udaljenost = ((mousePoint.getX() - h)*(mousePoint.getX() - h)/(a*a)) + ((mousePoint.getY() - k)*(mousePoint.getY() - k)/(b*b));
		if(Math.abs(udaljenost) <= 1) {
			return 0;
		}
		// ako je tocka izvan elipse, vrati udaljenost od centra do tocke
		return GeometryUtil.distanceFromPoint(center, mousePoint);
	}

	@Override
	public void render(Renderer r){
		// potrebno naci par tocki koje su na elipsi
		Point center = getCenter();
		int h = center.getX();
		int k = center.getY();
		int a = Math.abs(this.getHotPoint(0).getY() - k);
		int b = Math.abs(this.getHotPoint(1).getX() - h);
		// formula za elipsu je (x-h)^2/a^2 + (y-k)^2/b^2 = 1
		// uvrstimo svaki x izmedju lijevog i desnog kraja
		int numberOfPoints = (int)b * 2;
		Point[] points = new Point[numberOfPoints * 2];
		Point[] pointsGornji = new Point[numberOfPoints];
		Point[] pointsDonji = new Point[numberOfPoints];
		
		int index = 0;
		for(int i = (int) (h-b); i<h+b; i++) {
			int x = i;
			float y_kSquared = -(a*a*(x-h)*(x-h)/(b*b))+ a*a;
			float y_kPositive = (float) Math.sqrt(y_kSquared);
			float y_kNegative = -y_kPositive;
			int yPositive = (int) y_kPositive + k;
			int yNegative = (int) y_kNegative + k;
			pointsGornji[index] = new Point((int)x, yPositive);
			pointsDonji[index] = new Point((int)x, yNegative);
			index++;
		}
		for(int i = 0; i<numberOfPoints; i++) {
			points[i] = pointsGornji[i];
			points[numberOfPoints + i] = pointsDonji[numberOfPoints - i - 1];
		}
		r.fillPolygon(points);
	}
	
	public Point getCenter() {
		Point bottomHotPoint = this.getHotPoint(0);
		Point rightHotPoint = this.getHotPoint(1);
		return new Point((bottomHotPoint.getX()), rightHotPoint.getY());
	}
	
	@Override
	public String getShapeID() {
		return "@OVAL";
	}

	@Override
	public void save(List<String> rows) {
		rows.add(getShapeID() + " " + getHotPoint(1).getX() + " " + getHotPoint(1).getY() + " " + getHotPoint(0).getX() + " " + getHotPoint(0).getY());
	}
	
	@Override
	public void load(Stack<GraphicalObject> stack, String data) {
		String[] parts = data.split(" ");
		Point[] hotPoints = new Point[2];
		hotPoints[1] = new Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
		hotPoints[0] = new Point(Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
		stack.push(new Oval(hotPoints));
	}
}
