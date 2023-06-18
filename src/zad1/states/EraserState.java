package zad1.states;

import java.util.ArrayList;
import java.util.List;

import zad1.gui.DocumentModel;
import zad1.renderers.Renderer;
import zad1.shapes.CompositeShape;
import zad1.shapes.GraphicalObject;
import zad1.shapes.concrete.LineSegment;
import zad1.utils.Point;

public class EraserState implements State{

	List<Point> krivulja = new ArrayList<>();
	DocumentModel model;
	CompositeShape cs;

	public EraserState(DocumentModel model) {
		this.model = model;
		this.cs = new CompositeShape();
		model.addGraphicalObject(cs);
	}
	
	@Override
	public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
		// provjerava se kroz koje sve objekte prolazi krivulja
		List<GraphicalObject> objects = model.list();
		List<GraphicalObject> toRemove = new ArrayList<>();
		for(GraphicalObject go : objects){
			for(Point p : krivulja){
				if(go.selectionDistance(p) < 3 && go != cs){
					toRemove.add(go);
					break;
				}
			}
		}
		for(GraphicalObject go : toRemove) {
			model.removeGraphicalObject(go);
		}
		
		this.krivulja.clear();
		this.cs.reset();
	}

	@Override
	public void mouseDragged(Point mousePoint) {
		// crta se krivulja
		krivulja.add(mousePoint);
		if(krivulja.size()>=2)
			cs.addChild(new LineSegment(krivulja.get(krivulja.size()-2), krivulja.get(krivulja.size()-1)));
	}

	@Override
	public void keyPressed(int keyCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterDraw(Renderer r, GraphicalObject go) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterDraw(Renderer r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLeaving() {
		model.removeGraphicalObject(cs);
	}

}
