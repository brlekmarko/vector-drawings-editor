package zad1.shapes;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import zad1.renderers.Renderer;
import zad1.utils.GeometryUtil;
import zad1.utils.Point;
import zad1.utils.Rectangle;

public class AbstractGraphicalObject implements GraphicalObject{
	
	private Point[] hotPoints;
	private boolean[] hotPointsSelected;
	private boolean selected;
	private List<GraphicalObjectListener> listeners = new ArrayList<>();
	
	public AbstractGraphicalObject(Point[] points) {
		this.hotPoints = points;
		
		this.hotPointsSelected = new boolean[points.length];
		for(int i=0; i<points.length; i++) {
			hotPointsSelected[i] = false;
		}
		
		this.selected = false;
	}

	@Override
	public boolean isSelected() {
		return selected;
	}

	@Override
	public void setSelected(boolean selected) {
		this.selected = selected;
		notifyListenersSelectionChanged();
	}

	@Override
	public int getNumberOfHotPoints() {
		return hotPoints.length;
	}

	@Override
	public Point getHotPoint(int index) {
		return hotPoints[index];
	}

	@Override
	public void setHotPoint(int index, Point point) {
		hotPoints[index] = point;
		notifyListenersObjectChanged();
	}

	@Override
	public boolean isHotPointSelected(int index) {
		return hotPointsSelected[index];
	}

	@Override
	public void setHotPointSelected(int index, boolean selected) {
		hotPointsSelected[index] = selected;
		notifyListenersObjectChanged();
	}

	@Override
	public double getHotPointDistance(int index, Point mousePoint) {
		Point hotPoint = hotPoints[index];
		return GeometryUtil.distanceFromPoint(hotPoint, mousePoint);
	}

	@Override
	public void translate(Point delta) {
		for(int i=0; i<hotPoints.length; i++) {
			hotPoints[i] = hotPoints[i].translate(delta);
		}
		notifyListenersObjectChanged();
	}

	@Override
	public Rectangle getBoundingBox() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double selectionDistance(Point mousePoint) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void addGraphicalObjectListener(GraphicalObjectListener l) {
		this.listeners.add(l);
	}

	@Override
	public void removeGraphicalObjectListener(GraphicalObjectListener l) {
		this.listeners.remove(l);
	}

	@Override
	public String getShapeName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GraphicalObject duplicate() {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected void notifyListenersObjectChanged() {
		for(GraphicalObjectListener l : listeners) {
			l.graphicalObjectChanged(this);
		}
	}
	
	protected void notifyListenersSelectionChanged() {
		for(GraphicalObjectListener l : listeners) {
			l.graphicalObjectSelectionChanged(this);
		}
	}

	@Override
	public void render(Renderer r) {
		// TODO Auto-generated method stub
	}

	@Override
	public String getShapeID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(List<String> rows) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void load(Stack<GraphicalObject> stack, String data) {
		// TODO Auto-generated method stub
		
	}

}
