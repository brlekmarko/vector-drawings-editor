package zad1.shapes.concrete;

import java.util.List;
import java.util.Stack;

import zad1.renderers.Renderer;
import zad1.shapes.AbstractGraphicalObject;
import zad1.shapes.GraphicalObject;
import zad1.utils.GeometryUtil;
import zad1.utils.Point;
import zad1.utils.Rectangle;

public class LineSegment extends AbstractGraphicalObject{
	
	public LineSegment() {
		super(new Point[] {new Point(0,0), new Point(10,0)});
	}
	
	public LineSegment(Point startPoint, Point endPoint) {
		super(new Point[] {startPoint, endPoint});
	}
	
	@Override
	public String getShapeName() {
		return "Linija";
	}
	
	@Override
	public GraphicalObject duplicate() {
		return new LineSegment(getHotPoint(0), getHotPoint(1));
	}
	
	@Override
	public Rectangle getBoundingBox() {
		Point startPoint = getHotPoint(0);
		Point endPoint = getHotPoint(1);

		int x = startPoint.getX() < endPoint.getX() ? startPoint.getX() : endPoint.getX(); // lijevi rub
		int y = startPoint.getY() < endPoint.getY() ? startPoint.getY() : endPoint.getY(); // gornji rub

		int width = Math.abs(startPoint.getX() - endPoint.getX()); // sirina
		int height = Math.abs(startPoint.getY() - endPoint.getY()); // visina
		return new Rectangle(x, y, width, height);
	}
	
	@Override
	public double selectionDistance(Point mousePoint) {
		return GeometryUtil.distanceFromLineSegment(getHotPoint(0), getHotPoint(1), mousePoint);
	}

	@Override
	public void render(Renderer r){
		r.drawLine(getHotPoint(0), getHotPoint(1));
	}
	
	@Override
	public String getShapeID() {
		return "@LINE";
	}
	
	@Override
	public void save(List<String> rows) {
		rows.add(getShapeID() + " " + getHotPoint(0).getX() + " " + getHotPoint(0).getY() + " " + getHotPoint(1).getX() + " " + getHotPoint(1).getY());
	}

	@Override
	public void load(Stack<GraphicalObject> stack, String data) {
		String[] parts = data.split(" ");
		Point startPoint = new Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
		Point endPoint = new Point(Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
		stack.push(new LineSegment(startPoint, endPoint));
	}

}
