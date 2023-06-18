package zad1.states;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import zad1.gui.DocumentModel;
import zad1.renderers.Renderer;
import zad1.shapes.CompositeShape;
import zad1.shapes.GraphicalObject;
import zad1.utils.Point;
import zad1.utils.Rectangle;

public class SelectShapeState implements State{

	private DocumentModel model;
	
	public SelectShapeState(DocumentModel model) {
		this.model = model;
	}
	
	@Override
	public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
		// ako je kliknuto na neki hotpoint, ne treba ništa raditi
		if(model.getSelectedObjects().size() == 1){
			// provjeri da li je kliknuto na neki hotpoint
			GraphicalObject go = model.getSelectedObjects().get(0);
			int index = model.findSelectedHotPoint(go, mousePoint);
			if(index != -1) {
				return;
			}
		}
		if(!ctrlDown) {
			model.deselectAll();
			// provjeri da li je kliknuto na neki objekt
			GraphicalObject go = model.findSelectedGraphicalObject(mousePoint);
			if(go != null) {
				go.setSelected(true);
			}
		}else {
			// provjeri da li je kliknuto na neki objekt
			GraphicalObject go = model.findSelectedGraphicalObject(mousePoint);
			if(go != null) {
				go.setSelected(!go.isSelected());
			}
		}
		
	}

	@Override
	public void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(Point mousePoint) {
		// probaj pomaknuti hotpoint
		if(model.getSelectedObjects().size() == 1) {
			GraphicalObject go = model.getSelectedObjects().get(0);
			int index = model.findSelectedHotPoint(go, mousePoint);
			if(index != -1) {
				go.setHotPoint(index, mousePoint);
			}
		}
		
	}

	@Override
	public void keyPressed(int keyCode) {
		switch(keyCode) {
		case KeyEvent.VK_UP:
			// pomakni sve selektirane objekte za 1 pixel gore
			for(GraphicalObject go : model.getSelectedObjects()) {
				go.translate(new Point(0, -1));
			}
			break;
		case KeyEvent.VK_DOWN:
			// pomakni sve selektirane objekte za 1 pixel dolje
			for(GraphicalObject go : model.getSelectedObjects()) {
				go.translate(new Point(0, 1));
			}
			break;
		case KeyEvent.VK_LEFT:
			// pomakni sve selektirane objekte za 1 pixel lijevo
			for(GraphicalObject go : model.getSelectedObjects()) {
				go.translate(new Point(-1, 0));
			}
			break;
		case KeyEvent.VK_RIGHT:
			// pomakni sve selektirane objekte za 1 pixel desno
			for(GraphicalObject go : model.getSelectedObjects()) {
				go.translate(new Point(1, 0));
			}
			break;
		case KeyEvent.VK_PLUS:
			// povećaj Z index selektiranih objekata za 1
			for(GraphicalObject go : model.getSelectedObjects()) {
				model.increaseZ(go);
			}
			break;
		case KeyEvent.VK_MINUS:
			// smanji Z index selektiranih objekata za 1
			for(GraphicalObject go : model.getSelectedObjects()) {
				model.decreaseZ(go);
			}
			break;
		case KeyEvent.VK_G:
			// grupiraj selektirane objekte
			List<GraphicalObject> selectedObjects = new ArrayList<>(model.getSelectedObjects());
			if(selectedObjects.size() > 1) {
				CompositeShape composite = new CompositeShape(selectedObjects);
				for(GraphicalObject go : selectedObjects) {
					model.removeGraphicalObject(go);
				}
				model.addGraphicalObject(composite);
				composite.setSelected(true);
			}
			break;
		case KeyEvent.VK_U:
			// razgrupiraj selektirane objekte
			selectedObjects = model.getSelectedObjects();
			if(selectedObjects.size() == 1) {
				GraphicalObject go = selectedObjects.get(0);
				if(go instanceof CompositeShape) {
					CompositeShape composite = (CompositeShape) go;
					model.deselectAll();
					model.removeGraphicalObject(composite);
					for(GraphicalObject child : composite.getChildren()) {
						model.addGraphicalObject(child);
					}
				}
			}
			break;
		default:
			break;
		}
		
	}

	@Override
	public void afterDraw(Renderer r, GraphicalObject go) {
		if(go.isSelected()) {
			// nacrtaj okvir oko selektiranog objekta
			Rectangle boundingBox = go.getBoundingBox();
			Point topLeft = new Point(boundingBox.getX(), boundingBox.getY());
			Point topRight = new Point(boundingBox.getX() + boundingBox.getWidth(), boundingBox.getY());
			Point bottomLeft = new Point(boundingBox.getX(), boundingBox.getY() + boundingBox.getHeight());
			Point bottomRight = new Point(boundingBox.getX() + boundingBox.getWidth(), boundingBox.getY() + boundingBox.getHeight());
			r.drawLine(topLeft, topRight);
			r.drawLine(topRight, bottomRight);
			r.drawLine(bottomRight, bottomLeft);
			r.drawLine(bottomLeft, topLeft);
			if(model.getSelectedObjects().size() == 1) {
				// potrebno nacrtati kvadratice oko hotpointova
				for(int i = 0; i < go.getNumberOfHotPoints(); i++) {
					Point hotPoint = go.getHotPoint(i);
					topLeft  = new Point(hotPoint.getX() - 3, hotPoint.getY() - 3);
					topRight = new Point(hotPoint.getX() + 3, hotPoint.getY() - 3);
					bottomLeft = new Point(hotPoint.getX() - 3, hotPoint.getY() + 3);
					bottomRight = new Point(hotPoint.getX() + 3, hotPoint.getY() + 3);
					r.drawLine(topLeft, topRight);
					r.drawLine(topRight, bottomRight);
					r.drawLine(bottomRight, bottomLeft);
					r.drawLine(bottomLeft, topLeft);
				}
			}
		}
	}

	@Override
	public void afterDraw(Renderer r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLeaving() {
		// odselektiraj sve objekte
		model.deselectAll();
	}

}
